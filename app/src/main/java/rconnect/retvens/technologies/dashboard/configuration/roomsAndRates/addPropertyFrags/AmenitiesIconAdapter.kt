package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity.GetAmenityIconData

class AmenitiesIconAdapter(val context: Context, private var itemList: List<GetAmenityIconData>) : RecyclerView.Adapter<AmenitiesIconAdapter.ViewHolder>() {

    var clickListener : OnIconClick ?= null
    fun setOnIconClick(listener : OnIconClick){
        clickListener = listener
    }
    interface OnIconClick{
        fun getIconLinkOnClick(amenityIconLinkUrl : String)
    }

    var selectedItemPos = -1
    var lastItemSelectedPos = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_icon, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        Glide.with(context).load(item.amenityIconLink).into(holder.amenityIcon)

        if (position == selectedItemPos) {
            holder.card.strokeWidth = 3
        } else {
            holder.card.strokeWidth = 0
        }

        holder.card.setOnClickListener {
            selectedItemPos = position
            if(lastItemSelectedPos == -1) {
                lastItemSelectedPos = selectedItemPos
            } else {
                notifyItemChanged(lastItemSelectedPos)
                lastItemSelectedPos = selectedItemPos
            }
            notifyItemChanged(selectedItemPos)
            clickListener?.getIconLinkOnClick(item.amenityIconLink)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val amenityIcon = itemView.findViewById<ImageView>(R.id.icon)
        val card = itemView.findViewById<MaterialCardView>(R.id.card)

    }

    fun filterList(inputString : ArrayList<GetAmenityIconData>) {
        itemList = inputString
        notifyDataSetChanged()
    }
}