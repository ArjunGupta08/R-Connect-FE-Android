package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.roomType

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R

class MappingRoomTypeAdapter(val applicationContext: Context, val roomList:ArrayList<MappingRoomTypeData>):RecyclerView.Adapter<MappingRoomTypeAdapter.ViewHolder>() {
    class ViewHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {
        val x:TextView = itemView.findViewById(R.id.txt_deluxe)
        val recyclerView:RecyclerView = itemView.findViewById(R.id.recycler_child_room_type)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_recycler_room_type,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return roomList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        val mappingRoomTypeChildAdapter = MappingRoomTypeChildAdapter(applicationContext, roomList[position].list)
        holder.recyclerView.adapter = mappingRoomTypeChildAdapter
    }
}