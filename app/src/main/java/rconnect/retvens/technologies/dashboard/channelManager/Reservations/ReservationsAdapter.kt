package rconnect.retvens.technologies.dashboard.channelManager.Reservations

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R

class ReservationsAdapter(val applicationContext: Context,val list:ArrayList<ReserveData>):RecyclerView.Adapter<ReservationsAdapter.ReservationHolder>() {
    class ReservationHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val reservationId:TextView = itemView.findViewById(R.id.reservationId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_recycler_reservations,parent,false)
        return ReservationHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ReservationHolder, position: Int) {
        holder.reservationId.text = list[position].x
    }
}