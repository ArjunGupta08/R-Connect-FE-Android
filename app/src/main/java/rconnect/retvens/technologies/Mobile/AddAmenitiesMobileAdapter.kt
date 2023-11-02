package rconnect.retvens.technologies.Mobile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.AddAmenitiesDataClass

class AddAmenitiesMobileAdapter(val context: Context, private val itemList: List<AddAmenitiesDataClass>) : RecyclerView.Adapter<AddAmenitiesMobileAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_property_aminities_mobile, parent, false)
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

        val amenityIcon = itemView.findViewById<ImageView>(R.id.img_item)
        val remove = itemView.findViewById<ImageView>(R.id.del_item)
        val amenityName = itemView.findViewById<TextView>(R.id.txt_item)

    }
}