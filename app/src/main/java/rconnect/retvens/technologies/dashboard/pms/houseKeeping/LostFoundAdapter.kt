package rconnect.retvens.technologies.dashboard.pms.houseKeeping

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R

class LostFoundAdapter(val list:ArrayList<String>, val applicationContext: Context):RecyclerView.Adapter<LostFoundAdapter.NotificationHolder>() {


    class NotificationHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {

//        val recyclerView = itemView.findViewById<RecyclerView>(R.id.nestedroomDetails_recycler);
//        val add = itemView.findViewById<ImageView>(R.id.addBaseAdult);

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_lost_found,parent,false)
        return NotificationHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {

    }
}