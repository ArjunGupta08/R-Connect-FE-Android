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
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.others.holiday.DisplayStatusData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.AmenitiesIconAdapter
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReservationTypeDialogAdapter(var list:ArrayList<GetReservationTypeData>, val applicationContext: Context,val isName:Boolean):RecyclerView.Adapter<ReservationTypeDialogAdapter.NotificationHolder>() {


    var mListener : OnUpdate?= null
    var selectedItemPos = -1
    var lastItemSelectedPos = -1

    fun setOnStatusUpdateListener(listener : OnUpdate){
        mListener = listener
    }

    interface OnUpdate {
        fun onUpdateStatusType(getReservationTypeData: String)
    }

    private lateinit var progressDialog : Dialog

    class NotificationHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {

        val name = itemView.findViewById<TextView>(R.id.name_reservation_dialog)
        val card = itemView.findViewById<MaterialCardView>(R.id.card_reservation_status)

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
            holder.card.setOnClickListener {
                setSelectedItem(position)
                mListener?.onUpdateStatusType(item.status)
            }
            // Set background based on selection
            if (selectedItemPos == position) {
                holder.card.setBackgroundResource(R.drawable.rounded_border_black)
            } else {
                holder.card.setBackgroundResource(R.drawable.rounded_border_light_black)
            }
            }
        }
    private fun setSelectedItem(position: Int) {
        selectedItemPos = position
        notifyItemChanged(lastItemSelectedPos)
        lastItemSelectedPos = selectedItemPos
        notifyItemChanged(selectedItemPos)
    }
    fun filterList(filteredData: List<GetReservationTypeData>) {
        list.clear()
        list.addAll(filteredData)
        notifyDataSetChanged()
    }


}

