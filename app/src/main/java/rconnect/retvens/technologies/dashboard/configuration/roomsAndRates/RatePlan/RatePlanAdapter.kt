package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.RatePlan

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.SystemClock
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
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.others.holiday.DisplayStatusData
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.generateShortCode
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RatePlanAdapter(var list:ArrayList<String>, val applicationContext: Context):RecyclerView.Adapter<RatePlanAdapter.NotificationHolder>() {

    var mListener : OnUpdate?= null

    fun setOnUpdateListener(listener : OnUpdate){
        mListener = listener
    }

    interface OnUpdate {
        fun onUpdateIdentityType()
    }

    lateinit var progressDialog: Dialog
    private var mLastClickTime : Long = 0

    class NotificationHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {

        val rateCode = itemView.findViewById<TextView>(R.id.shortCode)
        val ratePlanName = itemView.findViewById<TextView>(R.id.propertyName)
        val roomType = itemView.findViewById<TextView>(R.id.roomType)
        val rateType = itemView.findViewById<TextView>(R.id.bedType)
        val ratePlanTotal = itemView.findViewById<TextView>(R.id.ratePlanTotal)
        val extraAdult = itemView.findViewById<TextView>(R.id.maxOccupancy)
        val extraChild = itemView.findViewById<TextView>(R.id.baseRate)
        val amenitiesCountTxt = itemView.findViewById<TextView>(R.id.amenitiesCountTxt)

        val edit = itemView.findViewById<ImageView>(R.id.edit)
        val delete = itemView.findViewById<ImageView>(R.id.delete)
        val info = itemView.findViewById<ImageView>(R.id.info)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_rate_types,parent,false)
        return NotificationHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        val currentData = list[position]

        holder.delete.setOnClickListener {
            showDeleteConfirmationDialog(applicationContext){

            }

        }
    }

    fun filterList(inputString : ArrayList<String>) {
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