package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType

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
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsData
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddInclusionsAdapter(val list:ArrayList<GetInclusionsData>, val applicationContext: Context):RecyclerView.Adapter<AddInclusionsAdapter.NotificationHolder>() {

    var selectedList = ArrayList<GetInclusionsData>()

    var mListener : OnUpdate?= null
    fun setOnUpdateListener(listener : OnUpdate){
        mListener = listener
    }
    interface OnUpdate {
        fun onUpdateList( selectedList : ArrayList<GetInclusionsData>)
    }

    class NotificationHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {

        val inclusionName = itemView.findViewById<TextView>(R.id.inclusionName)
        val charges = itemView.findViewById<TextView>(R.id.charges)

        val card = itemView.findViewById<MaterialCardView>(R.id.card)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_add_inclusion,parent,false)
        return NotificationHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        val item = list[position]

        holder.inclusionName.text = item.inclusionName
        holder.charges.text = "₹${item.charge}/Person"

        holder.card.setOnClickListener {
            if (selectedList.contains(item)) {
                selectedList.remove(item)
                holder.card.strokeWidth = 1
                holder.card.strokeColor = ContextCompat.getColor(applicationContext, R.color.lightBlack)
            } else {
                selectedList.add(item)
                holder.card.strokeWidth = 3
                holder.card.strokeColor = ContextCompat.getColor(applicationContext, R.color.black)
            }
            mListener?.onUpdateList(selectedList)
        }

    }
}