package rconnect.retvens.technologies.dashboard.configuration.others.seasons

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
import rconnect.retvens.technologies.dashboard.configuration.others.holiday.DisplayStatusData
import rconnect.retvens.technologies.dashboard.configuration.others.holiday.GetHotelData
import rconnect.retvens.technologies.dashboard.configuration.others.holiday.UpdateHolidayDataClass
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.util.Date

class SeasonAdapter(var list:ArrayList<GetSeasonData>, val applicationContext: Context):RecyclerView.Adapter<SeasonAdapter.NotificationHolder>() {

    lateinit var loader:Dialog

    var mListener : OnUpdate?= null
    fun setOnUpdateListener(listener : OnUpdate){
        mListener = listener
    }
    interface OnUpdate {
        fun onUpdate()
        fun onEdit(item : GetSeasonData)
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

    private fun deleteSeason(context: Context,seasonId:String, getSeasonData: GetSeasonData) {
        val create = UserSessionManager(applicationContext).getUserId()?.let {
            OAuthClient<GeneralsAPI>(context).create(GeneralsAPI::class.java).deleteSeasonTypeApi(
                UserSessionManager(applicationContext).getUserId().toString(),seasonId,DisplayStatusData("0")
            )
        }
        create?.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                loader.dismiss()
                if (response.isSuccessful){
                    response.body()
                }
                Log.d( "season delete", "${response.code()} ${response.message()}")
                list.remove(getSeasonData)
                notifyDataSetChanged()
                mListener?.onUpdate()
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                loader.dismiss()
                Log.e("error", t.localizedMessage)
            }
        })
    }


    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        val item = list[position]

        holder.shortCode.text = item.shortCode
        holder.text2.text = item.seasonName
        holder.text3.text = item.startDate
        holder.text4.text = item.endDate
        holder.text5.text = "${item.createdBy} ${item.createdOn}"
        holder.lastModified.text = "${item.modifiedBy} ${item.modifiedOn}"

        Toast.makeText(applicationContext, item.days.toString(), Toast.LENGTH_SHORT).show()
        holder.edit.setOnClickListener {
            mListener?.onEdit(item)
        }
        holder.delete.setOnClickListener {
            showDeleteConfirmationDialog(applicationContext){
                loader = showProgressDialog(applicationContext)
                deleteSeason(applicationContext,item.seasonId.toString(),item)
            }

        }
    }

    fun filterList(inputString : ArrayList<GetSeasonData>) {
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