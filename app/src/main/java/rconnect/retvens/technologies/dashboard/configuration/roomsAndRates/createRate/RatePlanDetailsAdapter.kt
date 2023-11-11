package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.RatePlanDataClass

class RatePlanDetailsAdapter(val applicationContext:Context, val rateTypeList:ArrayList<RatePlanDataClass>):RecyclerView.Adapter<RatePlanDetailsAdapter.ViewHolder>() {
    class ViewHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {

        val ratePlanText = itemView.findViewById<TextView>(R.id.ratePlanText)
        val rateCode = itemView.findViewById<TextView>(R.id.rateCode)
        val totalInclusions = itemView.findViewById<TextView>(R.id.totalInclusions)
        val extraAdultRateTxt = itemView.findViewById<TextView>(R.id.extraAdultRateTxt)
        val extraChildRateTxt = itemView.findViewById<TextView>(R.id.extraChildRateTxt)
        val ratePlanTotalTxt = itemView.findViewById<TextView>(R.id.ratePlanTotalTxt)

        val delete = itemView.findViewById<ImageView>(R.id.delete)
        val edit = itemView.findViewById<ImageView>(R.id.edit)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_rate_plan_details,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return rateTypeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = rateTypeList[position]

        holder.ratePlanText.text = currentData.ratePlan
        holder.rateCode.text = currentData.rateCode
        holder.totalInclusions.text = currentData.totalInclusions
        holder.extraAdultRateTxt.text = currentData.extraAdultRate
        holder.extraChildRateTxt.text = currentData.extraChildRate
        holder.ratePlanTotalTxt.text = currentData.ratePlanTotal

        holder.delete.setOnClickListener {
            rateTypeList.remove(currentData)
            notifyDataSetChanged()
        }
    }
}