package rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment

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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsData
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GuestInfoAdapter(val list:ArrayList<GuestInfo>, val applicationContext: Context):RecyclerView.Adapter<GuestInfoAdapter.GuestInfoHolder>() {

    var selectedList = ArrayList<GetInclusionsData>()

    var mListener : OnUpdate?= null
    fun setOnUpdateListener(listener : OnUpdate){
        mListener = listener
    }
    interface OnUpdate {
        fun onUpdateList( selectedList : ArrayList<GetInclusionsData>)
    }

    class GuestInfoHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {

        val guestLayout = itemView.findViewById<TextInputLayout>(R.id.guestLayout)
        val guestText = itemView.findViewById<TextInputEditText>(R.id.guestText)
        val phoneLayout = itemView.findViewById<TextInputLayout>(R.id.phoneLayout)
        val phoneText = itemView.findViewById<TextInputEditText>(R.id.phoneText)
        val emailLayout = itemView.findViewById<TextInputLayout>(R.id.emailLayout)
        val emailText = itemView.findViewById<TextInputEditText>(R.id.emailText)
        val addressLine1Layout = itemView.findViewById<TextInputLayout>(R.id.addressLayout1)
        val addressLine1Text = itemView.findViewById<TextInputEditText>(R.id.addressText1)
        val addressLine2Layout = itemView.findViewById<TextInputLayout>(R.id.addressLine2Layout)
        val addressLine2Text = itemView.findViewById<TextInputEditText>(R.id.addressText2)
        val countryLayout = itemView.findViewById<TextInputLayout>(R.id.countryLayout)
        val countryText = itemView.findViewById<TextInputEditText>(R.id.countryText)
        val stateLayout = itemView.findViewById<TextInputLayout>(R.id.stateLayout)
        val stateText = itemView.findViewById<TextInputEditText>(R.id.stateText)
        val cityLayout = itemView.findViewById<TextInputLayout>(R.id.cityLayout)
        val cityText = itemView.findViewById<TextInputEditText>(R.id.cityText)
        val pincodeLayout = itemView.findViewById<TextInputLayout>(R.id.pinLayout)
        val pinText = itemView.findViewById<TextInputEditText>(R.id.pinText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuestInfoHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_card_guest,parent,false)
        return GuestInfoHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: GuestInfoHolder, position: Int) {
        val guestData = list[position]

        guestData.guestName = holder.guestText.text.toString()
        guestData.addressLine1 = holder.addressLine1Text.text.toString()
        guestData.addressLine2 = holder.addressLine2Text.text.toString()
        guestData.emailAddress = holder.emailText.text.toString()
        guestData.country = holder.countryText.text.toString()
        guestData.state = holder.stateText.text.toString()
        guestData.city = holder.cityText.text.toString()
        guestData.pincode = holder.pinText.toString()

    }

    fun removeItem(position: Int) {
        if (position in 0 until list.size) {
            list.removeAt(position)
            notifyItemRemoved(position)

            // Notify the listener about the updated list
            mListener?.onUpdateList(selectedList)
        }
    }
}