package rconnect.retvens.technologies.dashboard.configuration.properties

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R

class ViewPropertiesAdapter(val context: Context, private val itemList: List<ViewPropertiesDataClass>, val viewT : Int) : RecyclerView.Adapter<ViewPropertiesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View

        if (viewT == 1) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_properties, parent, false)
        } else if (viewT == 2){
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_properties_2, parent, false)
        } else if (viewT == 3){
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_properties_3, parent, false)
        } else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_properties, parent, false)
        }

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        holder.propertyName.text = (item.roomType)
        holder.location.text = (item.cityName)

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val propertyName = itemView.findViewById<TextView>(R.id.propertyName)
        val location = itemView.findViewById<TextView>(R.id.location)

    }
}