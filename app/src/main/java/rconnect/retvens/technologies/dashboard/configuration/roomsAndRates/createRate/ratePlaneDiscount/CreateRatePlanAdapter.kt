package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlaneDiscount

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.CreateRateData

class CreateRatePlanAdapter(val applicationContext:Context, val rateTypeList:ArrayList<RatePlanDiscountData>):RecyclerView.Adapter<CreateRatePlanAdapter.ViewHolder>() {
    class ViewHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {
        val roomType:TextView = itemView.findViewById(R.id.roomType)
        val selectedCount:TextView = itemView.findViewById(R.id.selectedCount)
        val recycler:RecyclerView = itemView.findViewById(R.id.recycler)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_applicable_room_type,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return rateTypeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = rateTypeList[position]
//        holder.roomType.text = currentData.
//        holder.selectedCount.text = currentData.selectedCount.toString()

        holder.roomType.setOnClickListener{
            holder.recycler.visibility = View.VISIBLE
        }

        holder.recycler.layoutManager=LinearLayoutManager(applicationContext)
//        val adapter = RoomTypeCategoryAdapter(applicationContext, currentData.list)
//        holder.recycler.adapter = adapter
//        adapter.notifyDataSetChanged()
    }
}