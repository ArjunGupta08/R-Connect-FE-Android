package rconnect.retvens.technologies.onboarding.singleHotelOnboarding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R

class FourOnboardingRoomAdapter(val context: Context, private val itemList: List<FourOnboardingRoomDataClass>) : RecyclerView.Adapter<FourOnboardingRoomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_four_onboarding_room_details, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        holder.roomTypeName.text = "${item.roomTypeName}x${item.roomInventory}"
        holder.maxAdult.text = item.maxAdult
        holder.maxChild.text = item.maxChild
        holder.maxOccupancy.text = item.maxOccupancy

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val roomTypeName = itemView.findViewById<TextView>(R.id.roomTypeName)
        val maxOccupancy = itemView.findViewById<TextView>(R.id.maxOccupancy)
        val maxAdult = itemView.findViewById<TextView>(R.id.maxAdult)
        val maxChild = itemView.findViewById<TextView>(R.id.maxChild)

    }
}