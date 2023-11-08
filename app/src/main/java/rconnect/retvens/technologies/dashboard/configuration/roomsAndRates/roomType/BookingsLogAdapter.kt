package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.roomType

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R

class BookingsLogAdapter(val list:ArrayList<String>, val applicationContext: Context):RecyclerView.Adapter<BookingsLogAdapter.ViewHolder>() {
    class ViewHolder(val itemView: View):RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_recycler_bookings_logs,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }
}