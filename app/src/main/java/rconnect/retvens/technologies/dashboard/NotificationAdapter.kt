package rconnect.retvens.technologies.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R

class NotificationAdapter(val notificationList:ArrayList<NotificationData>, val applicationContext: Context):RecyclerView.Adapter<NotificationAdapter.NotificationHolder>() {
    class NotificationHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {
        val notification_title:TextView = itemView.findViewById(R.id.notification_title)
        val notification_desc:TextView = itemView.findViewById(R.id.notification_desc)
        val time:TextView = itemView.findViewById(R.id.time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_recycler_notification_dialog,parent,false)
        return NotificationHolder(view)
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        val currentData = notificationList[position]
        holder.notification_title.text = currentData.title
        holder.notification_desc.text = currentData.desc
        holder.time.text = currentData.time
    }
}