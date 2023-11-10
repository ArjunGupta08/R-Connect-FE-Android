package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.roomType

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.channelManager.ChannelsManagement.ChannelDetailFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.AddRoomTypeFragment

class ChannelListAdapter(val applicationContext: FragmentActivity,val list:ArrayList<String>):RecyclerView.Adapter<ChannelListAdapter.ViewHolder>() {
    class ViewHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {
        val x:TextView = itemView.findViewById(R.id.channel_code)
        val manage:CardView = itemView.findViewById(R.id.card_manage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_recycler_channel_management,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.x.text = list[position]
        holder.manage.setOnClickListener {
            val childFragment = ChannelDetailFragment()
            applicationContext.supportFragmentManager.beginTransaction()
                .replace(R.id.dashboardFragmentContainer, childFragment)
                .addToBackStack(null)  // Optional: Add to back stack for back navigation
                .commit()
        }
    }
}