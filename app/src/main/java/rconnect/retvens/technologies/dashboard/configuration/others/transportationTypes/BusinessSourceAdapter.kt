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
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.guestsAndReservation.reservationType.GetReservationTypeDataClass
import rconnect.retvens.technologies.dashboard.configuration.others.businessSource.Business_sourcesFragment
import rconnect.retvens.technologies.dashboard.configuration.others.businessSource.GetBusinessSourceData
import rconnect.retvens.technologies.dashboard.configuration.others.businessSource.GetBusinessSourceDataClass
import rconnect.retvens.technologies.dashboard.configuration.others.businessSource.UpdateBusinessSourceDataClass
import rconnect.retvens.technologies.dashboard.configuration.others.holiday.DisplayStatusData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.mealPlan.GetMealPlanData
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BusinessSourceAdapter(var list:ArrayList<GetBusinessSourceData>, val applicationContext: Context):RecyclerView.Adapter<BusinessSourceAdapter.NotificationHolder>() {

    var mListener : OnUpdate ?= null
    lateinit var loader:Dialog

    fun setOnUpdateListener(listener: OnUpdate){
        mListener = listener
    }

    interface OnUpdate {
        fun onUpdateBusinessSource()
    }

    class NotificationHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {


        val status = itemView.findViewById<TextView>(R.id.propertyName0)

        val reservationTypeName = itemView.findViewById<TextView>(R.id.shortCode0)
        val craetedBy = itemView.findViewById<TextView>(R.id.bedType0)
        val lastModified = itemView.findViewById<TextView>(R.id.adultChild0)

        val edit = itemView.findViewById<ImageView>(R.id.edit0)
        val delete = itemView.findViewById<ImageView>(R.id.delete0)
        val info = itemView.findViewById<ImageView>(R.id.info0)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_business_sources,parent,false)
        return NotificationHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        val item = list[position]

        holder.reservationTypeName.text = item.shortCode
        holder.status.text = item.sourceName
        holder.craetedBy.text = "${item.createdBy} ${item.createdOn}"
        holder.lastModified.text = "${item.modifiedBy} ${item.modifiedOn}"

        holder.edit.setOnClickListener {
            openCreateNewDialog(applicationContext, item.shortCode, item.sourceName, item.sourceId)
        }
        holder.delete.setOnClickListener {
            showDeleteConfirmationDialog(applicationContext){
                loader = showProgressDialog(applicationContext)
                deleteIdentity(applicationContext,item.sourceId,item)
            }
        }
    }

    private fun openCreateNewDialog(context : Context, shortCodeTxt:String , sourceName:String , sourceId : String) {
        val dialog = Dialog(context) // Use 'this' as the context, assuming this code is within an Activity
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dialog_business_source)
        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent) // Makes the background transparent
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        val shortCode = dialog.findViewById<TextInputEditText>(R.id.b_short_code_text)
        val sourceText = dialog.findViewById<TextInputEditText>(R.id.businessSource_text)
        val shortLayout = dialog.findViewById<TextInputLayout>(R.id.shortLayout1)
        val sourceLayout = dialog.findViewById<TextInputLayout>(R.id.businessSourceLayout1)
        shortCode.setText(shortCodeTxt)
        sourceText.setText(sourceName)

        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        val save = dialog.findViewById<CardView>(R.id.saveBtn)

        cancel.setOnClickListener {
            dialog.dismiss()
        }
        save.setOnClickListener {
            if (sourceText.text!!.isEmpty()){
                shakeAnimation(sourceLayout,applicationContext)
            }
            else if (shortCode.text!!.isEmpty()){
            shakeAnimation(shortLayout,applicationContext)
        }
            else if (shortCode.text!!.isNotEmpty()&&sourceText.text!!.isNotEmpty()){
                loader = showProgressDialog(applicationContext)
                saveIdentity(context, dialog, shortCode.text.toString(), sourceText.text.toString(), sourceId)
            }
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.END)

        dialog.show()
    }

    private fun saveIdentity(context: Context, dialog: Dialog, shortCodeTxt : String , sourceText : String, sourceId: String) {
        val create = OAuthClient<GeneralsAPI>(context).create(GeneralsAPI::class.java).updateBusinessSourceApi(
            UserSessionManager(context).getUserId().toString(),
            sourceId,
        UpdateBusinessSourceDataClass(shortCodeTxt, sourceText))
        create.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                loader.dismiss()
                if (response.isSuccessful){
                    Log.d( "reservation", "${response.code()} ${response.message()}")
                    mListener?.onUpdateBusinessSource()
                    dialog.dismiss()
                }
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                Log.d("Error",t.localizedMessage.toString())
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
    private fun deleteIdentity(context: Context, sourceId: String,getBusinessSourceData: GetBusinessSourceData) {
        val create = OAuthClient<GeneralsAPI>(context).create(GeneralsAPI::class.java).deleteBusinessSourceApi(
            UserSessionManager(context).getUserId().toString(),
            sourceId,
        DisplayStatusData("0")
        )
        create.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                loader.dismiss()
                if (response.isSuccessful){
                    list.remove(getBusinessSourceData)
                    notifyDataSetChanged()

                    Log.d( "reservation", "${response.code()} ${response.message()}")
                    mListener?.onUpdateBusinessSource()
                }
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                Log.d("Error",t.localizedMessage.toString())
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
    fun filterList(inputString : ArrayList<GetBusinessSourceData>) {
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