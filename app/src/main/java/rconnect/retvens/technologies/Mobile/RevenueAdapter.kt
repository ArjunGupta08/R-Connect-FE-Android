package rconnect.retvens.technologies.Mobile

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R

class RevenueAdapter(val revenueList:ArrayList<RevenueData>,val applicationContext:Context):RecyclerView.Adapter<RevenueAdapter.ViewHolder>() {
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val x:TextView = itemView.findViewById(R.id.txt_seller)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_revenue_recycler,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return revenueList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.x.text = revenueList[position].room
    }

}