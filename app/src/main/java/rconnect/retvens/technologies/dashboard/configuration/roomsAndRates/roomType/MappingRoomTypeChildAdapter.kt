package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.roomType

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R

class MappingRoomTypeChildAdapter(val applicationContext: Context, val roomList:ArrayList<MappingRoomTypeChildData>):RecyclerView.Adapter<MappingRoomTypeChildAdapter.ViewHolder>() {
    class ViewHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {
        val x:TextView = itemView.findViewById(R.id.txt_deluxe_child)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_recycler_room_type_child,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return roomList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.x.text = roomList[position].roomType
    }
}