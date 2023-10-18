package rconnect.retvens.technologies.dashboard.channelManager.QuickReservation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R

class NestedQuickReservationAdapter(val list2:ArrayList<String>, val applicationContext: Context):RecyclerView.Adapter<NestedQuickReservationAdapter.NotificationHolder>() {
    class NotificationHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_nested_rooms_details,parent,false)
        return NotificationHolder(view)
    }

    override fun getItemCount(): Int {
        return list2.size
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {



    }
}