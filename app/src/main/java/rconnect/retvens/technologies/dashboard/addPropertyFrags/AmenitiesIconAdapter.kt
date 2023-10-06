package rconnect.retvens.technologies.dashboard.addPropertyFrags

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R

class AmenitiesIconAdapter(val context: Context, private val itemList: List<AmenitiesIconDataClass>) : RecyclerView.Adapter<AmenitiesIconAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_icon, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        holder.amenityIcon.setImageResource(item.amenityIcon)

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val amenityIcon = itemView.findViewById<ImageView>(R.id.icon)

    }
}