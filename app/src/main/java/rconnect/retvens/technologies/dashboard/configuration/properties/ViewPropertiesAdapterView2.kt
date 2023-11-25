package rconnect.retvens.technologies.dashboard.configuration.properties

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.AddImagesFragment

class ViewPropertiesAdapterView2(val context: Context, private var itemList: ArrayList<PropData>) : RecyclerView.Adapter<ViewPropertiesAdapterView2.ViewHolder>() {

    private var mListener : OnEditPropertyListener?= null
    fun setOnEditPropertyClickListener(listener : OnEditPropertyListener){
        mListener = listener
    }
    interface OnEditPropertyListener{
        fun onEditPropertyClick(propertyId : String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_properties_3, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        holder.hCode.text = item.propertyId
        holder.propertyType.text = item.propertyType
        holder.propertyName.text = (item.propertyName)
        holder.location.text = "${item.city}, ${item.country}"
        holder.totalRooms.text = "${item.totalRooms}"
        holder.amenitiesTxt.text = "${item.amenities}"

        holder.edit.setOnClickListener {
            mListener?.onEditPropertyClick(item.propertyId)
        }
        holder.delete.setOnClickListener {
            itemList.remove(item)
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val hCode = itemView.findViewById<TextView>(R.id.hCode)
        val propertyName = itemView.findViewById<TextView>(R.id.propertyName)
        val propertyType = itemView.findViewById<TextView>(R.id.propertyType)
        val location = itemView.findViewById<TextView>(R.id.location)
        val totalRooms = itemView.findViewById<TextView>(R.id.totalRoomsTxt)
        val amenitiesTxt = itemView.findViewById<TextView>(R.id.amenitiesTxt)

        val edit = itemView.findViewById<ImageView>(R.id.edit)
        val delete = itemView.findViewById<ImageView>(R.id.delete)

    }

    fun filterList(inputFilteredList : List<PropData>){
        itemList = inputFilteredList as ArrayList<PropData>
        notifyDataSetChanged()
    }

}