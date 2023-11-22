package rconnect.retvens.technologies.dashboard.configuration.properties

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import rconnect.retvens.technologies.R

class ViewPropertiesAdapter(val context: Context, private var itemList: List<PropData>) : RecyclerView.Adapter<ViewPropertiesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.item_properties_2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        holder.propertyName.text = (item.propertyName)
        holder.location.text = "${item.city}, ${item.country}"

        if (item.hotelLogo.isEmpty()) {
            holder.propertyLogo.setImageResource(R.drawable.svg_add_property)
        } else {
            Glide.with(context).load(item.hotelLogo).into(holder.propertyLogo)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val propertyName = itemView.findViewById<TextView>(R.id.propertyName)
        val location = itemView.findViewById<TextView>(R.id.location)

        val propertyLogo = itemView.findViewById<ImageView>(R.id.propertyLogo)

    }

    fun filterList(inputFilteredList : List<PropData>){
        itemList = inputFilteredList
        notifyDataSetChanged()
    }
}