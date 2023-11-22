package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions

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

class InclusionsAdapter(var list:ArrayList<GetInclusionsData>, val applicationContext: Context):RecyclerView.Adapter<InclusionsAdapter.NotificationHolder>() {

    var mListener : OnUpdate?= null

    fun setOnUpdateListener(listener : OnUpdate){
        mListener = listener
    }

    interface OnUpdate {
        fun onUpdateIdentityType(currentData : GetInclusionsData)
    }

    lateinit var progressDialog: Dialog
    private var mLastClickTime : Long = 0

    class NotificationHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {

        val shortCode = itemView.findViewById<TextView>(R.id.shortCode)
        val text2 = itemView.findViewById<TextView>(R.id.text2)
        val text3 = itemView.findViewById<TextView>(R.id.text3)
        val text4 = itemView.findViewById<TextView>(R.id.text4)
        val text5 = itemView.findViewById<TextView>(R.id.text5)
        val createdBy = itemView.findViewById<TextView>(R.id.text6)
        val lastModified = itemView.findViewById<TextView>(R.id.text7)

        val edit = itemView.findViewById<ImageView>(R.id.edit)
        val delete = itemView.findViewById<ImageView>(R.id.delete)
        val info = itemView.findViewById<ImageView>(R.id.info)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_inclusion,parent,false)
        return NotificationHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        val item = list[position]

        holder.shortCode.text = item.shortCode
        holder.text2.text = item.inclusionName
        holder.text3.text = item.inclusionType
        holder.text4.text = item.postingRule
        holder.text5.text = item.chargeRule
        holder.createdBy.text = "${item.createdBy} ${item.createdOn}"
        holder.lastModified.text = "${item.modifiedBy} ${item.modifiedOn}"

        holder.delete.setOnClickListener {
            showDeleteConfirmationDialog(applicationContext){
                progressDialog = showProgressDialog(applicationContext)
                deleteInclusion(applicationContext,item.inclusionId,item)
            }
        }
        holder.edit.setOnClickListener {
            // mis-clicking prevention, using threshold of 1000 ms
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return@setOnClickListener;
            }
            mLastClickTime = SystemClock.elapsedRealtime()

            mListener?.onUpdateIdentityType(item)
        }
    }

    fun deleteInclusion(applicationContext: Context,inclusionId:String,getInclusionsData: GetInclusionsData){
        val create = OAuthClient<GeneralsAPI>(applicationContext).create(GeneralsAPI::class.java).deleteInclusionsApi(UserSessionManager(applicationContext).getUserId().toString(),inclusionId,
            DisplayStatusData("0")
        )
        create.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                progressDialog.dismiss()
                if (response.isSuccessful){
                    list.remove(getInclusionsData)
                    notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                progressDialog.dismiss()
            }
        })
    }

    fun filterList(inputString : ArrayList<GetInclusionsData>) {
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