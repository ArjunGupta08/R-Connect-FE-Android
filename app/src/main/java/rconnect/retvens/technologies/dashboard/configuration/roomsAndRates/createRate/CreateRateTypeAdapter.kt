package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R

class CreateRateTypeAdapter(val applicationContext:Context,val rateTypeList:ArrayList<CreateRateData>):RecyclerView.Adapter<CreateRateTypeAdapter.ViewHolder>() {
    class ViewHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {
        val inclusion:TextView = itemView.findViewById(R.id.inclusion)
        val posting:TextView = itemView.findViewById(R.id.posting)
        val charge:TextView = itemView.findViewById(R.id.charge)
        val rate:TextView = itemView.findViewById(R.id.rate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_inclusion_and_charges,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return rateTypeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = rateTypeList[position]
        holder.rate.text = currentData.rate
        holder.charge.text = currentData.charge
        holder.inclusion.text = currentData.meal
        holder.posting.text = currentData.day
    }
}