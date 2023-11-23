package rconnect.retvens.technologies.dashboard.configuration.billings

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
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.guestsAndReservation.reservationType.GetReservationTypeDataClass
import rconnect.retvens.technologies.dashboard.configuration.others.holiday.DisplayStatusData
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.generateShortCode
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentTypeDialogAdapter(var list:ArrayList<GetPaymentTypeData>, val applicationContext: Context,val isName:Boolean):RecyclerView.Adapter<PaymentTypeDialogAdapter.NotificationHolder>() {

    private lateinit var progressBar : Dialog

    private var mListener : MealUpdatedListener ?= null
    fun setOnMealUpdateListener(listener : MealUpdatedListener){
        mListener = listener
    }
    interface MealUpdatedListener {
        fun onMealUpdated()
    }

    class NotificationHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name_reservation_dialog)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_reservation_dialog_layout,parent,false)
        return NotificationHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        val item = list[position]
        if (isName){
            holder.name.text = item.paymentMethodName
        }
        else{
            holder.name.text = item.receivedTo
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
                mListener?.onMealUpdated()
                dialog.dismiss()
            }

            override fun onFailure(call: Call<GetReservationTypeDataClass?>, t: Throwable) {
                progressBar.dismiss()
                Log.d("saveReservationError", "${t.localizedMessage}")
            }
        })
    }
    private fun deletePayment(context: Context,paymentTypeId: String,getPaymentTypeData: GetPaymentTypeData) {
        val create = OAuthClient<GeneralsAPI>(context).create(GeneralsAPI::class.java).deletePaymentTypeApi(UserSessionManager(applicationContext).getUserId().toString(),paymentTypeId,
            DisplayStatusData(("0"))
        )
        create.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                progressBar.dismiss()
                if (response.isSuccessful){
                    list.remove(getPaymentTypeData)
                    notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                progressBar.dismiss()
            }
        })

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

    fun filterList(searchText: ArrayList<GetPaymentTypeData>){
        list = searchText
        notifyDataSetChanged()
    }

}