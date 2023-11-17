package rconnect.retvens.technologies.dashboard.configuration.billings

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
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.guestsAndReservation.reservationType.GetReservationTypeDataClass
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.generateShortCode
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentTypeAdapter(var list:ArrayList<GetPaymentTypeData>, val applicationContext: Context):RecyclerView.Adapter<PaymentTypeAdapter.NotificationHolder>() {

    private lateinit var progressBar : Dialog

    class NotificationHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {

         val shortCode = itemView.findViewById<TextView>(R.id.shortCode)
         val propertyType = itemView.findViewById<TextView>(R.id.propertyName)
         val receivedTo = itemView.findViewById<TextView>(R.id.bedType)
         val createdBy = itemView.findViewById<TextView>(R.id.adultChild)
         val lastModified = itemView.findViewById<TextView>(R.id.lastModified)

         val edit = itemView.findViewById<ImageView>(R.id.edit)
         val delete = itemView.findViewById<ImageView>(R.id.delete)
         val info = itemView.findViewById<ImageView>(R.id.info)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_payment_type,parent,false)
        return NotificationHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        val item = list[position]

        holder.shortCode.text = item.shortCode
        holder.propertyType.text = item.paymentMethodName
        holder.receivedTo.text = item.receivedTo
        holder.createdBy.text = "${item.createdBy} ${item.createdOn}"
        holder.lastModified.text = "${item.modifiedBy} ${item.modifiedOn}"

        holder.delete.setOnClickListener {
            list.remove(item)
            notifyDataSetChanged()
        }

        holder.edit.setOnClickListener {
            openCreateNewDialog(applicationContext, item.paymentTypeId, item.paymentMethodName, item.shortCode, item.receivedTo)
        }
    }

    private fun openCreateNewDialog(context: Context, paymentTypeId : String, paymentMethodNameTxt : String, shortCodeTxt : String, receivedToTxt : String) {
        val dialog = Dialog(context) // Use 'this' as the context, assuming this code is within an Activity
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dialog_create_payment_type)
        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent) // Makes the background transparent
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        val paymentMethodNameLayout = dialog.findViewById<TextInputLayout>(R.id.paymentMethodNameLayout)
        val shortCodeLayout = dialog.findViewById<TextInputLayout>(R.id.shortCodeLayout)
        val receivedToLayout = dialog.findViewById<TextInputLayout>(R.id.receivedToLayout)

        val paymentMethodName = dialog.findViewById<TextInputEditText>(R.id.paymentMethodName)
        val shortCode = dialog.findViewById<TextInputEditText>(R.id.shortCode)
        val receivedTo = dialog.findViewById<TextInputEditText>(R.id.receivedTo)

        paymentMethodName.setText(paymentMethodNameTxt)
        shortCode.setText(shortCodeTxt)
        receivedTo.setText(receivedToTxt)

        paymentMethodName.doAfterTextChanged {
            if (paymentMethodName.text!!.length > 2){
                shortCode.setText(generateShortCode(paymentMethodName.text.toString()))
            }
        }

        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        val save = dialog.findViewById<CardView>(R.id.saveBtn)

        cancel.setOnClickListener {
            dialog.dismiss()
        }
        save.setOnClickListener {
            if (paymentMethodName.text!!.isEmpty()) {
                shakeAnimation(paymentMethodNameLayout, applicationContext)
            } else if (shortCode.text!!.isEmpty()) {
                shakeAnimation(shortCodeLayout, applicationContext)
            } else if (receivedTo.text!!.isEmpty()) {
                shakeAnimation(receivedToLayout, applicationContext)
            } else {
                progressBar = showProgressDialog(applicationContext)
                updatePayment(
                    context,
                    dialog,
                    paymentTypeId,
                    shortCode.text.toString(),
                    paymentMethodName.text.toString(),
                    receivedTo.text.toString()
                )
            }
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.END)

        dialog.show()

    }

    private fun updatePayment(context: Context, dialog: Dialog, paymentTypeId : String, shortCode : String, paymentMethodName:String, receivedTo:String) {
        val create = OAuthClient<GeneralsAPI>(context).create(GeneralsAPI::class.java).updatePaymentTypeApi(UserSessionManager(context).getUserId().toString(), paymentTypeId,
            UpdatePaymentTypeDataClass(shortCode, paymentMethodName, receivedTo)
        )

        create.enqueue(object : Callback<GetReservationTypeDataClass?> {
            override fun onResponse(
                call: Call<GetReservationTypeDataClass?>,
                response: Response<GetReservationTypeDataClass?>
            ) {
                progressBar.dismiss()
                Log.d( "reservation", "${response.code()} ${response.message()}")
                dialog.dismiss()
            }

            override fun onFailure(call: Call<GetReservationTypeDataClass?>, t: Throwable) {
                progressBar.dismiss()
                Log.d("saveReservationError", "${t.localizedMessage}")
            }
        })
    }

    fun filterList(searchText: ArrayList<GetPaymentTypeData>){
        list = searchText
        notifyDataSetChanged()
    }

}