package rconnect.retvens.technologies.dashboard.channelManager.promotions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.addPropertyFrags.AddAmenitiesDataClass

class PromotionsAdapter(val context: Context, private val itemList: List<PromotionsDataClass>) : RecyclerView.Adapter<PromotionsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_promotions, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        holder.offerHeadingTxt.text = item.amenityIcon

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val offerHeadingTxt = itemView.findViewById<TextView>(R.id.offerHeadingTxt)

    }
}