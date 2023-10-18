package rconnect.retvens.technologies.dashboard.channelManager.QuickReservation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R

class QuickReservationAdapter(val list:ArrayList<String>, val applicationContext: Context):RecyclerView.Adapter<QuickReservationAdapter.NotificationHolder>() {

    private  var list2:ArrayList<String> = ArrayList()

    class NotificationHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {

        val recyclerView = itemView.findViewById<RecyclerView>(R.id.nestedroomDetails_recycler);
        val add = itemView.findViewById<ImageView>(R.id.addBaseAdult);

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_rooms_details,parent,false)
        return NotificationHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {

        list2.clear()
        list2.add("1")

        holder.recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        val adapter =  NestedQuickReservationAdapter(list2,applicationContext)
        holder.recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        holder.add.setOnClickListener {
            list2.add("1")
            adapter.notifyDataSetChanged()
        }
    }
}