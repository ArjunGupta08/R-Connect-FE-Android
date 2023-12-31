package rconnect.retvens.technologies.dashboard.configuration.others.holiday

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.guestsAndReservation.reservationType.GetReservationTypeDataClass
import rconnect.retvens.technologies.dashboard.configuration.others.transportationTypes.UpdateTransportationTypeDataClass
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.util.Date

class HolidaysAdapter(var list:ArrayList<GetHotelData>, val applicationContext: Context):RecyclerView.Adapter<HolidaysAdapter.NotificationHolder>() {
    lateinit var loader:Dialog
    lateinit var endDatePickerDialog: DatePickerDialog
    lateinit var startDatePickerDialog: DatePickerDialog
    var startDate:Date? = null
    var endDate:Date? = null
    var isDateSelected = false

    var mListener : OnItemUpdate ?= null
    fun setOnItemUpdateListener(listener : OnItemUpdate){
        mListener = listener
    }
    interface OnItemUpdate {
        fun onUpdate()
    }

    class NotificationHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {

        val shortCode = itemView.findViewById<TextView>(R.id.shortCode)
        val text2 = itemView.findViewById<TextView>(R.id.text2)
        val text3 = itemView.findViewById<TextView>(R.id.text3)
        val text4 = itemView.findViewById<TextView>(R.id.text4)
        val text5 = itemView.findViewById<TextView>(R.id.text5)
        val lastModified = itemView.findViewById<TextView>(R.id.text6)

        val edit = itemView.findViewById<ImageView>(R.id.edit)
        val delete = itemView.findViewById<ImageView>(R.id.delete)
        val info = itemView.findViewById<ImageView>(R.id.info)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_holidays,parent,false)
        return NotificationHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        val item = list[position]

        holder.shortCode.text = item.shortCode
        holder.text2.text = item.holidayName
        holder.text3.text = item.startDate
        holder.text4.text = item.endDate
        holder.text5.text = "${item.createdBy} ${item.createdOn}"
        holder.lastModified.text = "${item.modifiedBy} ${item.modifiedOn}"

        holder.edit.setOnClickListener {
            openCreateNewDialog(applicationContext, item.shortCode, item.holidayName, item.startDate, item.endDate, item.holidayId)
        }
        holder.delete.setOnClickListener {
            showDeleteConfirmationDialog(applicationContext){
                loader = showProgressDialog(applicationContext)
                deleteHoliday(applicationContext,item.holidayId,item)
            }
        }
    }

    private fun openCreateNewDialog(context : Context, shortCodeTxt:String , holidayName:String , from:String , to:String , holidayId : String) {
        val dialog = Dialog(context) // Use 'this' as the context, assuming this code is within an Activity
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dialog_create_holiday)
        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent) // Makes the background transparent
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        val shortCode = dialog.findViewById<TextInputEditText>(R.id.shortCodeHoliday)
        val holidayNameET = dialog.findViewById<TextInputEditText>(R.id.holidayNameHoliday)
        shortCode.setText(shortCodeTxt)
        holidayNameET.setText(holidayName)

        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        val save = dialog.findViewById<CardView>(R.id.saveBtn)


        val from_date = dialog.findViewById<TextView>(R.id.from_date)
        val to_date = dialog.findViewById<TextView>(R.id.to_date)
        startDatePickerDialog = createDatePickerDialog(applicationContext,from_date) { date->
            startDate = date
        }

        endDatePickerDialog = createDatePickerDialog(applicationContext,to_date){date->

            endDatePickerDialog.datePicker.minDate = startDate!!.time

            if (startDate!=null&&date.before(startDate)){
//                    isRightEndDate = false

                Toast.makeText(applicationContext, "End date cannot be before start date", Toast.LENGTH_SHORT).show()
//                    showToast("End date cannot be before start date")
//                    to_date.text = "--/--/----"
//                    Handler().postDelayed(Runnable {
//                        isRightEndDate = true
//                    },1000)
            }
            else{
                endDate = date
            }
        }

        endDatePickerDialog = toCreateDatePickerDialog(to_date){date->
            if (startDate!=null){
                endDatePickerDialog.datePicker.minDate = startDate!!.time
                endDate = date
            }
        }
        from_date.text = from
        to_date.text = to

        from_date.setOnClickListener {
            startDatePickerDialog.show()
            dialog.show()
        }
        to_date.setOnClickListener {
            endDatePickerDialog.show()
            dialog.show()
        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }
        save.setOnClickListener {
            loader = showProgressDialog(applicationContext)
            saveHoliday(applicationContext, dialog, shortCode.text.toString(), holidayNameET.text.toString(), from_date.text.toString(), to_date.text.toString(), holidayId)
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.END)

        dialog.show()
    }
    private fun saveHoliday(context: Context, dialog: Dialog, shortCodeTxt : String, holidayName:String, startDate:String, endDate:String, holidayId: String) {
        val create = OAuthClient<GeneralsAPI>(context).create(GeneralsAPI::class.java).updateHolidayApi(
             holidayId, UserSessionManager(context).getUserId().toString(), UpdateHolidayDataClass(shortCodeTxt, holidayName, startDate, endDate)
        )
        create.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(
                call: Call<ResponseData?>,
                response: Response<ResponseData?>
            ) {
                loader.dismiss()
                Log.d( "holiday", "${response.code()} ${response.message()}")
                mListener?.onUpdate()
                dialog.dismiss()
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                loader.dismiss()
                Log.d("error", t.localizedMessage)
            }
        })
    }

    private fun deleteHoliday(context: Context, holidayId: String,getHotelData: GetHotelData) {
        val create = OAuthClient<GeneralsAPI>(context).create(GeneralsAPI::class.java).
            deleteHolidayApi(holidayId,UserSessionManager(applicationContext).getUserId().toString(),DisplayStatusData("0"))
        create.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(
                call: Call<ResponseData?>,
                response: Response<ResponseData?>
            ) {
                if (response.isSuccessful){
                    list.remove(getHotelData)
                    notifyDataSetChanged()
                }
                loader.dismiss()
                Log.d( "holiday", "${response.code()} ${response.message()}")
                mListener?.onUpdate()
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                loader.dismiss()
                Log.d("error", t.localizedMessage)
            }
        })
    }

    fun filterList(inputString : ArrayList<GetHotelData>) {
        list = inputString
        notifyDataSetChanged()
    }

    fun showDeleteConfirmationDialog(context: Context, onDeleteConfirmed: () -> Unit) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Confirm Deletion")
        alertDialogBuilder.setMessage("Do you really want to delete this item?")

        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            // User clicked "Yes"
            onDeleteConfirmed.invoke()
        }

        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
            // User clicked "No"
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
    fun createDatePickerDialog(applicationContext: Context, textDate:TextView, onDateSetListener: (Date) -> Unit): DatePickerDialog {
        val calendar = Calendar.getInstance()
        return DatePickerDialog(
            applicationContext,
            { _, year, month, dayOfMonth ->
                // Create a Date object from the selected date
                calendar.set(year, month, dayOfMonth)
                val selectedDate = calendar.time
                // Invoke the provided listener
                onDateSetListener.invoke(selectedDate)
                // Set the selected date on the EditText
                val selectedDate2 = "$dayOfMonth/${month+1}/$year"
                textDate.setText(selectedDate2)

//                if (textDate==from_date){
//                    startDate = selectedDate
//                }
//                else{
//                    endDate = selectedDate
//                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    private fun toCreateDatePickerDialog(textDate:TextView,onDateSetListener: (Date) -> Unit): DatePickerDialog {
        val calendar = Calendar.getInstance()
        return DatePickerDialog(
            applicationContext,
            { _, year, month, dayOfMonth ->
                // Create a Date object from the selected date
                calendar.set(year, month, dayOfMonth)
                val selectedDate = calendar.time
                // Invoke the provided listener
                onDateSetListener.invoke(selectedDate)
                // Set the selected date on the EditText
                val selectedDate2 = "$dayOfMonth/${month+1}/$year"
                textDate.text = selectedDate2

//                if (textDate==from_date){
//                    startDate = selectedDate
//                }
//                else{
//                    endDate = selectedDate
//                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }
}