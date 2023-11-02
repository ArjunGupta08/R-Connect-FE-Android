package rconnect.retvens.technologies.dashboard.configuration.others.holiday

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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class HolidaysAdapter(val list:ArrayList<GetHotelData>, val applicationContext: Context):RecyclerView.Adapter<HolidaysAdapter.NotificationHolder>() {

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
        val shortCode = dialog.findViewById<TextInputEditText>(R.id.shortCode)
        val holidayNameET = dialog.findViewById<TextInputEditText>(R.id.holidayName)
        shortCode.setText(shortCodeTxt)
        holidayNameET.setText(holidayName)

        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        val save = dialog.findViewById<CardView>(R.id.saveBtn)


        val from_date = dialog.findViewById<TextView>(R.id.from_date)
        val to_date = dialog.findViewById<TextView>(R.id.to_date)
        from_date.text = from
        to_date.text = to

        from_date.setOnClickListener {
            showCalendarDialog(applicationContext,from_date)
            dialog.show()
        }
        to_date.setOnClickListener {
            showCalendarDialog(applicationContext,to_date)
            dialog.show()
        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }
        save.setOnClickListener {
            saveHoliday(applicationContext, dialog, shortCode.text.toString(), holidayNameET.text.toString(), from_date.text.toString(), to_date.text.toString(), holidayId)
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.END)

        dialog.show()
    }

    fun showCalendarDialog(context : Context, textDate: TextView) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                // Set the selected date on the EditText
                val selectedDate = "$dayOfMonth/${month+1}/$year"
                textDate.text = selectedDate
            },
            currentYear,
            currentMonth,
            currentDay
        )
        datePickerDialog.setCancelable(false)

        datePickerDialog.show()
    }

    private fun saveHoliday(context: Context, dialog: Dialog, shortCodeTxt : String, holidayName:String, startDate:String, endDate:String, holidayId: String) {
        val create = OAuthClient<GeneralsAPI>(context).create(GeneralsAPI::class.java).updateHolidayApi(
             holidayId, UserSessionManager(context).getUserId().toString(), UpdateHolidayDataClass(shortCodeTxt, holidayName, startDate, endDate)
        )
        create.enqueue(object : Callback<GetReservationTypeDataClass?> {
            override fun onResponse(
                call: Call<GetReservationTypeDataClass?>,
                response: Response<GetReservationTypeDataClass?>
            ) {
                Log.d( "holiday", "${response.code()} ${response.message()}")
                mListener?.onUpdate()
                dialog.dismiss()
            }

            override fun onFailure(call: Call<GetReservationTypeDataClass?>, t: Throwable) {
                Log.d("error", t.localizedMessage)
            }
        })
    }

}