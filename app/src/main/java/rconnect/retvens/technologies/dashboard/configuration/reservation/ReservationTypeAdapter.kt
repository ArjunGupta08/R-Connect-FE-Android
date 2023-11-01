package rconnect.retvens.technologies.dashboard.configuration.reservation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R

class ReservationTypeAdapter(val list:ArrayList<GetReservationTypeData>, val applicationContext: Context):RecyclerView.Adapter<ReservationTypeAdapter.NotificationHolder>() {

    private  var list2:ArrayList<String> = ArrayList()

    class NotificationHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {

        val reservationTypeName = itemView.findViewById<TextView>(R.id.shortCode);
        val status = itemView.findViewById<TextView>(R.id.propertyName);
        val craetedBy = itemView.findViewById<TextView>(R.id.bedType);
        val lastModified = itemView.findViewById<TextView>(R.id.adultChild);

        val edit = itemView.findViewById<ImageView>(R.id.edit);

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_reservation_types,parent,false)
        return NotificationHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        val item = list[position]

        holder.reservationTypeName.text = item.reservationName
        holder.status.text = item.status
        holder.craetedBy.text = "${item.createdBy} ${item.createdOn}"
        holder.lastModified.text = "${item.modifiedBy} ${item.modifiedOn}"

    }
}