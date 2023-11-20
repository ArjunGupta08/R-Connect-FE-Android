package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.roomType

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.GetRoomDataClass

class RoomTypeAdapter(var list:ArrayList<GetRoomDataClass>, val applicationContext: Context):RecyclerView.Adapter<RoomTypeAdapter.NotificationHolder>() {

    private var mListener : SetOnEditClickListener ?= null
    fun setOnEditButtonClickListener(listener : SetOnEditClickListener)  {
        mListener = listener
    }
    interface SetOnEditClickListener{
        fun onEditButtonClick()
    }
    class NotificationHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {
        val shortCode : TextView = itemView.findViewById(R.id.shortCode)
        val roomTypeName : TextView = itemView.findViewById(R.id.propertyName)
        val bedCountText : TextView = itemView.findViewById(R.id.bedType)
        val adultCountRangeTxt : TextView = itemView.findViewById(R.id.adultCountRangeTxt)
        val childCountRangeTxt : TextView = itemView.findViewById(R.id.childCountRangeTxt)
        val maxOccupancy : TextView = itemView.findViewById(R.id.maxOccupancy)
        val baseRate : TextView = itemView.findViewById(R.id.baseRate)
        val amenitiesCountTxt : TextView = itemView.findViewById(R.id.amenitiesCountTxt)

        val edit : ImageView = itemView.findViewById(R.id.edit)
        val delete : ImageView = itemView.findViewById(R.id.delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_room_types,parent,false)
        return NotificationHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        val currentData = list[position]

        holder.shortCode.text = currentData.shortCode
        holder.roomTypeName.text = currentData.roomTypeName
        holder.bedCountText.text = currentData.noOfBeds
        holder.adultCountRangeTxt.text = "${currentData.baseAdult}-${currentData.maxAdult}"
        holder.childCountRangeTxt.text = "${currentData.baseChild}-${currentData.maxChild}"
        holder.maxOccupancy.text = "${currentData.maxOccupancy}"
        holder.baseRate.text = "${currentData.baseRate}"
        holder.amenitiesCountTxt.text = "${currentData.amenities}"

        holder.edit.setOnClickListener {
            mListener?.onEditButtonClick()
        }

        holder.delete.setOnClickListener {
            list.remove(currentData)
            notifyDataSetChanged()
        }

    }

    fun filterList(inputString : ArrayList<GetRoomDataClass>) {
        list = inputString
        notifyDataSetChanged()
    }
}