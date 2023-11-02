package rconnect.retvens.technologies.dashboard.configuration.reservation

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
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IdentityTypeAdapter(val list:ArrayList<GetIdentityTypeData>, val applicationContext: Context):RecyclerView.Adapter<IdentityTypeAdapter.NotificationHolder>() {

    var mListener : OnUpdate ?= null

    fun setOnUpdateListener(listener : OnUpdate){
        mListener = listener
    }

    interface OnUpdate {
        fun onUpdateIdentityType()
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
        holder.status.text = item.identityType
        holder.craetedBy.text = "${item.createdBy} ${item.createdOn}"
        holder.lastModified.text = "${item.modifiedBy} ${item.modifiedOn}"

        Toast.makeText(applicationContext, "item.identityTypeId", Toast.LENGTH_SHORT).show()
        holder.edit.setOnClickListener {
            openCreateNewDialog(applicationContext, item.shortCode, item.identityType, item.identityTypeId)
        }
    }

    private fun openCreateNewDialog(context : Context, shortCodeTxt : String, identityTypeTxt:String , identityTypeIdTxt : String) {
        val dialog = Dialog(context) // Use 'this' as the context, assuming this code is within an Activity
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dialog_create_identity_type)
        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent) // Makes the background transparent
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        val shortCode = dialog.findViewById<TextInputEditText>(R.id.shortCode)
        val identityTypeName = dialog.findViewById<TextInputEditText>(R.id.identityTypeName)

        shortCode.setText(shortCodeTxt)
        identityTypeName.setText(identityTypeTxt)


        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        val save = dialog.findViewById<CardView>(R.id.saveBtn)

        cancel.setOnClickListener {
            dialog.dismiss()
        }
        save.setOnClickListener {
            saveIdentity(context, dialog, shortCodeTxt, identityTypeTxt, identityTypeIdTxt)
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.END)

        dialog.show()
    }

    private fun saveIdentity(context: Context, dialog: Dialog, shortCodeTxt : String, identityTypeTxt:String , identityTypeIdTxt : String) {
        val create = OAuthClient<GeneralsAPI>(context).create(GeneralsAPI::class.java).updateIdentityTypeApi(UserSessionManager(context).getUserId().toString(), identityTypeIdTxt, UpdateIdentityTypeDataClass(shortCodeTxt, identityTypeTxt))
        create.enqueue(object : Callback<GetReservationTypeDataClass?> {
            override fun onResponse(
                call: Call<GetReservationTypeDataClass?>,
                response: Response<GetReservationTypeDataClass?>
            ) {
                Log.d( "reservation", "${response.code()} ${response.message()}")
                mListener?.onUpdateIdentityType()
                dialog.dismiss()
            }

            override fun onFailure(call: Call<GetReservationTypeDataClass?>, t: Throwable) {
                Log.d("error", t.localizedMessage)
            }
        })
    }
}