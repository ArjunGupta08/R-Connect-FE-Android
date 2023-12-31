package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate

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
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.mealPlan.GetMealPlanData
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt

class AddMealPlanAdapter(val list:ArrayList<GetMealPlanData>, val applicationContext: Context):RecyclerView.Adapter<AddMealPlanAdapter.NotificationHolder>() {

    var selectedList = ArrayList<GetMealPlanData>()

    var mListener : OnUpdate?= null

    fun setOnUpdateListener(listener : OnUpdate){
        mListener = listener
    }

    interface OnUpdate {
        fun onUpdateMealPlan(selectedList : ArrayList<GetMealPlanData>)
    }

    class NotificationHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {

        val mealName = itemView.findViewById<TextView>(R.id.inclusionName)
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

        holder.mealName.text = item.mealPlanName
        holder.charges.text = "₹${item.chargesPerOccupancy}/Per Occupancy"

        holder.card.setOnClickListener {
            if (selectedList.contains(item)) {
                // Item is already selected, so remove it to unselect
                selectedList.remove(item)
                holder.card.strokeColor = ContextCompat.getColor(applicationContext, R.color.grey40)
                holder.card.strokeWidth = 1.5.roundToInt() // Assuming you want to remove the stroke
                // You can set the stroke color to the default color or remove the stroke as needed
            } else {
                // Item is not selected, so add it to select
                selectedList.add(item)
                holder.card.strokeWidth = 3
                holder.card.strokeColor = ContextCompat.getColor(applicationContext, R.color.black)
            }

            mListener?.onUpdateMealPlan(selectedList)
        }

    }
}