package rconnect.retvens.technologies.dashboard.Dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.ViewPropertiesDataClass

class ViewPropertiesAdapter(val context: Context, private val itemList: List<ViewPropertiesDataClass>) : RecyclerView.Adapter<ViewPropertiesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_properties, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        holder.roomType.text = (item.roomType)
        holder.cityName.text = (item.cityName)

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val roomType = itemView.findViewById<TextView>(R.id.roomType)
        val cityName = itemView.findViewById<TextView>(R.id.cityName)

    }
}