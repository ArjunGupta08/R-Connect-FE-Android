package rconnect.retvens.technologies.dashboard.channelManager.DashboardFragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R


class BookingDetailsAdapter(val context: Context, private val stringList: ArrayList<String>) :
    RecyclerView.Adapter<BookingDetailsAdapter.InventoryViewHolder>() {





    class InventoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_booking_details, parent, false)
        return InventoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        val currentItem = stringList[position]


    }



    override fun getItemCount(): Int {
        return stringList.size
    }
}


