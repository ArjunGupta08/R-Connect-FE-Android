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

class ReservationTypeDialogAdapter(var list:ArrayList<GetReservationTypeData>, val applicationContext: Context,val isName:Boolean):RecyclerView.Adapter<ReservationTypeDialogAdapter.NotificationHolder>() {

    var mListener : OnUpdate?= null

    fun setOnUpdateListener(listener : OnUpdate){
        mListener = listener
    }

    interface OnUpdate {
        fun onUpdateReservationType()
    }

    private lateinit var progressDialog : Dialog

    class NotificationHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {

        val name = itemView.findViewById<TextView>(R.id.name_reservation_dialog)

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
            holder.name.text = item.reservationName
        }
        else{
            holder.name.text = item.status
        }

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