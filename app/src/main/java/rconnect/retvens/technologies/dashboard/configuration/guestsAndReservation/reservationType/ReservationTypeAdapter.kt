package rconnect.retvens.technologies.dashboard.configuration.guestsAndReservation.reservationType

import android.app.AlertDialog
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
import android.widget.Switch
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.others.holiday.DisplayStatusData
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReservationTypeAdapter(var list:ArrayList<GetReservationTypeData>, val applicationContext: Context):RecyclerView.Adapter<ReservationTypeAdapter.NotificationHolder>() {

    var mListener : OnUpdate?= null

    fun setOnUpdateListener(listener : OnUpdate){
        mListener = listener
    }

    interface OnUpdate {
        fun onUpdateReservationType()
    }

    private lateinit var progressDialog : Dialog

    class NotificationHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {

        val reservationTypeName = itemView.findViewById<TextView>(R.id.shortCode)
        val status = itemView.findViewById<TextView>(R.id.propertyName)
        val craetedBy = itemView.findViewById<TextView>(R.id.bedType)
        val lastModified = itemView.findViewById<TextView>(R.id.adultChild)

        val edit = itemView.findViewById<ImageView>(R.id.edit)
        val delete = itemView.findViewById<ImageView>(R.id.delete)
        val info = itemView.findViewById<ImageView>(R.id.info)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_reservation_types,parent,false)
        return NotificationHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        val item = list[position]

        holder.reservationTypeName.text = item.reservationName
        holder.status.text = item.status
        holder.craetedBy.text = "${item.createdBy} ${item.createdOn}"
        holder.lastModified.text = "${item.modifiedBy} ${item.modifiedOn}"

        holder.delete.setOnClickListener {
            showDeleteConfirmationDialog(applicationContext){
                progressDialog = showProgressDialog(applicationContext)
                deleteReservation(applicationContext,item.reservationTypeId,item)
            }
        }

        holder.edit.setOnClickListener {
            openCreateNewDialog(applicationContext, item.reservationName, item.status, item.reservationTypeId)
        }
    }

    private fun openCreateNewDialog(context : Context, rName : String, status:String , rId : String) {
        val dialog = Dialog(context) // Use 'this' as the context, assuming this code is within an Activity
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dialog_create_reservation_type)
        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent) // Makes the background transparent
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        val reservationNameLayout = dialog.findViewById<TextInputLayout>(R.id.reservationNameLayout)
        val reservationNameET = dialog.findViewById<TextInputEditText>(R.id.reservationNameET)
        val isConfirmedSwitch = dialog.findViewById<Switch>(R.id.isConfirmedSwitch)

        reservationNameET.setText(rName)

        isConfirmedSwitch.isChecked = status == "Confirmed"

        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        val save = dialog.findViewById<CardView>(R.id.saveBtn)

        cancel.setOnClickListener {
            dialog.dismiss()
        }
        save.setOnClickListener {
            if (reservationNameET.text!!.isEmpty()) {
                shakeAnimation(reservationNameLayout, applicationContext)
            } else {
                progressDialog = showProgressDialog(applicationContext)
                saveReservation(
                    context,
                    dialog,
                    reservationNameET.text.toString(),
                    if (isConfirmedSwitch.isChecked) {
                        "Confirmed"
                    } else {
                        "Unconfirmed"
                    },
                    rId
                )
            }
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.END)

        dialog.show()
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

    private fun saveReservation(context: Context, dialog: Dialog, rName : String, status:String, rId : String) {
        val create = OAuthClient<GeneralsAPI>(context).create(GeneralsAPI::class.java).updateReservationTypeApi(UserSessionManager(context).getUserId().toString(), rId, UpdateReservationTypeDataClass(rName, status))
        create.enqueue(object : Callback<GetReservationTypeDataClass?> {
            override fun onResponse(
                call: Call<GetReservationTypeDataClass?>,
                response: Response<GetReservationTypeDataClass?>
            ) {
                Log.d( "reservation", "${response.code()} ${response.message()}")
                mListener?.onUpdateReservationType()
                progressDialog.dismiss()
                dialog.dismiss()
            }

            override fun onFailure(call: Call<GetReservationTypeDataClass?>, t: Throwable) {
                progressDialog.dismiss()
                Log.d("error", t.localizedMessage)
            }
        })
    }
    private fun deleteReservation(context: Context,reservationId:String,getReservationTypeData: GetReservationTypeData) {
        val create = OAuthClient<GeneralsAPI>(context).create(GeneralsAPI::class.java).deleteReservationTypeApi(UserSessionManager(context).getUserId().toString(),reservationId,
            DisplayStatusData(("0"))
        )
        create.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                progressDialog.dismiss()
                if (response.isSuccessful){
                    Log.d("response success",response.message().toString())
                    list.remove(getReservationTypeData)
                    notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun filterList(inputText: ArrayList<GetReservationTypeData>) {
        list = inputText
        notifyDataSetChanged()
    }
}