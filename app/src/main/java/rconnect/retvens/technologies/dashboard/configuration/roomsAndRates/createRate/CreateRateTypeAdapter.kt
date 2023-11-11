package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsData

class CreateRateTypeAdapter(val applicationContext:Context,val rateTypeList:ArrayList<GetInclusionsData>):RecyclerView.Adapter<CreateRateTypeAdapter.ViewHolder>() {
    class ViewHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {
        val inclusion:TextInputEditText = itemView.findViewById(R.id.inclusion)
        val posting:TextInputEditText = itemView.findViewById(R.id.posting)
        val charge:TextInputEditText = itemView.findViewById(R.id.charge)
        val rate:TextInputEditText = itemView.findViewById(R.id.rate)

        val img_del:ImageView = itemView.findViewById(R.id.img_del)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_inclusion_and_charges,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return rateTypeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = rateTypeList[position]
        holder.rate.setText(currentData.charge)
        holder.charge.setText(currentData.chargeRule)
        holder.inclusion.setText(currentData.inclusionName)
        holder.posting.setText(currentData.postingRule)

        holder.img_del.setOnClickListener {
            rateTypeList.remove(currentData)
            notifyDataSetChanged()
        }
    }
}