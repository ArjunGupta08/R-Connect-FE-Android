package rconnect.retvens.technologies.dashboard.channelManager.ChannelsManagement

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.AddRoomTypeFragment

class ChannelManagementAdapter(val context: FragmentActivity, private val itemList: List<String>) : RecyclerView.Adapter<ChannelManagementAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_active_channel, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        holder.manage.setOnClickListener {
            val childFragment = ChannelDetailFragment()
            context.supportFragmentManager.beginTransaction()
                .replace(R.id.dashboardFragmentContainer, childFragment)
                .addToBackStack(null)
                .commit()
        }



    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val offerHeadingTxt = itemView.findViewById<TextView>(R.id.offerHeadingTxt)

        val manage:CardView = itemView.findViewById(R.id.card_manage_grid)

    }
}