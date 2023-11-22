package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity.GetAmenityData

class AddAmenitiesAdapter(val context: Context, private var itemList: ArrayList<GetAmenityData>) : RecyclerView.Adapter<AddAmenitiesAdapter.ViewHolder>() {

    val selectedAmenitiesList = ArrayList<GetAmenityData>()

    var clickListener : OnItemClick ?= null

    fun setOnClickListener (listener : OnItemClick){
        clickListener = listener
    }
    interface OnItemClick{
        fun onItemListUpdate(selectedAmenitiesList : ArrayList<GetAmenityData>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_amenities, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        holder.amenityName.text = item.amenityName
        Glide.with(context).load(item.amenityIconLink).into(holder.amenityIcon)
//            .onLoadStarted(ContextCompat.getDrawable(context, R.drawable.image_loader))

//        if (selectedAmenitiesLisFinal.contains(item)) {
//            itemList.remove(item)
//            notifyDataSetChanged()
//        }

        holder.card.setOnClickListener {
            if (!selectedAmenitiesList.contains(item)){
                holder.card.strokeWidth = 3
                selectedAmenitiesList.add(item)
            } else {
                selectedAmenitiesList.remove(item)
                holder.card.strokeWidth = 0
            }
            clickListener?.onItemListUpdate(selectedAmenitiesList)
        }

        holder.remove.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val card = itemView.findViewById<MaterialCardView>(R.id.card)

        val amenityIcon = itemView.findViewById<ImageView>(R.id.amenityIcon)
        val remove = itemView.findViewById<ImageView>(R.id.remove)
        val amenityName = itemView.findViewById<TextView>(R.id.amenityName)

    }

    fun filterList(inputString : ArrayList<GetAmenityData>) {
        itemList = inputString
        notifyDataSetChanged()
    }
}