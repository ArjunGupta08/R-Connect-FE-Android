package rconnect.retvens.technologies.dashboard.configuration

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R

class StayViewAdapter(val list:ArrayList<String>, val applicationContext: Context):RecyclerView.Adapter<StayViewAdapter.NotificationHolder>() {

    private  var list2 = arrayListOf<String>(
        "sdsd",
        "sds",
        "sds",
        "sds",
        "sds",
        "sds"
    )

    class NotificationHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {

        val recyclerView = itemView.findViewById<RecyclerView>(R.id.recyclerView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_stay_view,parent,false)
        return NotificationHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {

        holder.recyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        val adapter =  ProgressAdapter(list2,applicationContext)
        holder.recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

    }
}