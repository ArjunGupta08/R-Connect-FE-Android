package rconnect.retvens.technologies.dashboard.configuration.others.transportationTypes

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
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.others.holiday.DisplayStatusData
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransportationTypeAdapter(var list:ArrayList<GetTransportationTypeData>, val applicationContext: Context):RecyclerView.Adapter<TransportationTypeAdapter.NotificationHolder>() {

    var mListener : OnUpdate ?= null
    lateinit var loader:Dialog

    fun setOnUpdateListener(listener : OnUpdate){
        mListener = listener
    }

    interface OnUpdate {
        fun onUpdateTransportationType()
    }

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

        holder.reservationTypeName.text = item.shortCode
        holder.status.text = item.transportationModeName
        holder.craetedBy.text = "${item.createdBy} ${item.createdOn}"
        holder.lastModified.text = "${item.modifiedBy} ${item.modifiedOn}"

        holder.edit.setOnClickListener {
            openCreateNewDialog(applicationContext, item.shortCode, item.transportationModeName, item.transportationId)
        }
        holder.delete.setOnClickListener {
            showDeleteConfirmationDialog(applicationContext){
                loader = showProgressDialog(applicationContext)
                deleteIdentity(applicationContext,item.transportationId,item)
            }
        }
    }

    private fun openCreateNewDialog(context : Context, shortCodeTxt:String , transportationMode:String , identityTypeIdTxt : String) {
        val dialog = Dialog(context) // Use 'this' as the context, assuming this code is within an Activity
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dialog_create_transportation_type)
        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent) // Makes the background transparent
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        val shortCode = dialog.findViewById<TextInputEditText>(R.id.shortCode)
        val transportationModeText = dialog.findViewById<TextInputEditText>(R.id.transportationModeText)
        shortCode.setText(shortCodeTxt)
        transportationModeText.setText(transportationMode)

        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        val save = dialog.findViewById<CardView>(R.id.saveBtn)

        cancel.setOnClickListener {
            dialog.dismiss()
        }
        save.setOnClickListener {
            loader = showProgressDialog(applicationContext)
            saveIdentity(context, dialog, shortCode.text.toString(), transportationModeText.text.toString(), identityTypeIdTxt)
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.END)

        dialog.show()
    }

    private fun saveIdentity(context: Context, dialog: Dialog, shortCodeTxt:String , transportationModeTxt:String , transportationModeIdTxt : String) {
        val create = OAuthClient<GeneralsAPI>(context).create(GeneralsAPI::class.java).updateTransportationModeTypeApi(UserSessionManager(context).getUserId().toString(), transportationModeIdTxt, UpdateTransportationTypeDataClass(shortCodeTxt, transportationModeTxt))
        create.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                loader.dismiss()
                    Log.d( "reservation", "${response.code()} ${response.message()}")
                    mListener?.onUpdateTransportationType()
                    dialog.dismiss()
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                loader.dismiss()
                Log.d("error", t.localizedMessage)
            }
        })
//        create.enqueue(object : Callback<GetReservationTypeDataClass?> {
//            override fun onResponse(
//                call: Call<GetReservationTypeDataClass?>,
//                response: Response<GetReservationTypeDataClass?>
//            ) {
//                    Log.d( "reservation", "${response.code()} ${response.message()}")
//                    mListener?.onUpdateTransportationType()
//                    dialog.dismiss()
//            }
//
//            override fun onFailure(call: Call<GetReservationTypeDataClass?>, t: Throwable) {
//                Log.d("error", t.localizedMessage)
//            }
//        })
    }
    private fun deleteIdentity(
        context: Context,
        transportationModeIdTxt: String,
        getTransportationTypeData: GetTransportationTypeData) {
        val create = OAuthClient<GeneralsAPI>(context).create(GeneralsAPI::class.java).deleteTransportationModeTypeApi(UserSessionManager(context).getUserId().toString(), transportationModeIdTxt,
            DisplayStatusData("0")
        )
        create.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                loader.dismiss()
                    Log.d( "reservation", "${response.code()} ${response.message()}")
                list.remove(getTransportationTypeData)
                notifyDataSetChanged()
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                loader.dismiss()
                Log.d("error", t.localizedMessage)
            }
        })
//        create.enqueue(object : Callback<GetReservationTypeDataClass?> {
//            override fun onResponse(
//                call: Call<GetReservationTypeDataClass?>,
//                response: Response<GetReservationTypeDataClass?>
//            ) {
//                    Log.d( "reservation", "${response.code()} ${response.message()}")
//                    mListener?.onUpdateTransportationType()
//                    dialog.dismiss()
//            }
//
//            override fun onFailure(call: Call<GetReservationTypeDataClass?>, t: Throwable) {
//                Log.d("error", t.localizedMessage)
//            }
//        })
    }

    fun filterList(inputString : ArrayList<GetTransportationTypeData>) {
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
}