package rconnect.retvens.technologies.dashboard.addPropertyFrags

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R

class AddAmenitiesAdapter(val context: Context, private val itemList: List<AddAmenitiesDataClass>) : RecyclerView.Adapter<AddAmenitiesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_amenities, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        holder.amenityName.text = item.amenityName

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val amenityIcon = itemView.findViewById<ImageView>(R.id.amenityIcon)
        val remove = itemView.findViewById<ImageView>(R.id.remove)
        val amenityName = itemView.findViewById<TextView>(R.id.amenityName)

    }
}