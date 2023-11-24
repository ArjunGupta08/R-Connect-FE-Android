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
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.guestsAndReservation.reservationType.GetReservationTypeData
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
    var selectedItemPos = -1
    var lastItemSelectedPos = -1

    private var mListener : MealUpdatedListener ?= null
    fun setOnMealUpdateListener(listener : MealUpdatedListener){
        mListener = listener
    }
    interface MealUpdatedListener {
        fun onMealUpdated()
    }

    class NotificationHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name_reservation_dialog)
        val card:MaterialCardView = itemView.findViewById(R.id.card_reservation_status)

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
            holder.name.text = item.receivedTo
            holder.card.setOnClickListener {
                setSelectedItem(position)
//                mListener?.onUpdateStatusType(item.status)
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


    fun filterList(filteredData: List<GetPaymentTypeData>) {
        list.clear()
        list.addAll(filteredData)
        notifyDataSetChanged()
    }
}