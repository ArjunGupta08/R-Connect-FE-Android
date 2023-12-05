package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlaneDiscount

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.checkbox.MaterialCheckBox
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.CreateRateData

class CreateRatePlanAdapter(val applicationContext:Context, val rateTypeList:ArrayList<RoomData>,val discountAmount:String,val discountPercentage:String):RecyclerView.Adapter<CreateRatePlanAdapter.ViewHolder>(),
    RoomTypeCategoryAdapter.OnRateTypeListChangeListener {

    private var onRateTypeListChangeListener : OnRateTypeListChangeListener ?= null
    public var discountList:ArrayList<AddDiscountDataClass> = ArrayList()
    fun setOnListUpdateListener (listener : OnRateTypeListChangeListener) {
        onRateTypeListChangeListener = listener
    }

    interface OnRateTypeListChangeListener {
        fun onRateTypeList()
    }
    class ViewHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {
        val roomType:TextView = itemView.findViewById(R.id.roomType)
        val selectedCount:TextView = itemView.findViewById(R.id.selectedCount)
        val recycler:RecyclerView = itemView.findViewById(R.id.recycler)
        val item: ConstraintLayout = itemView.findViewById(R.id.wholeItem)
        val checkBox:MaterialCheckBox = itemView.findViewById(R.id.checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_applicable_room_type,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return rateTypeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = rateTypeList[position]
        holder.roomType.text = currentData.roomTypeName
//        holder.selectedCount.text = currentData.selectedCount.toString()


        Log.e("check",discountPercentage.toString())
        Log.e("amount",discountAmount.toString())

        var clickCount = 0

        holder.item.setOnClickListener {
            clickCount++
            if (clickCount % 2 == 1) {
                // Odd click count, make it visible
                holder.recycler.visibility = View.VISIBLE
            } else {
                // Even click count, make it gone
                holder.recycler.visibility = View.GONE
            }
        }

        holder.checkBox.setOnCheckedChangeListener { checkbox, isChecked ->
            if (isChecked){
                holder.recycler.layoutManager=LinearLayoutManager(applicationContext)
                val adapter = RoomTypeCategoryAdapter(applicationContext,currentData.barRatePlans,discountAmount,discountPercentage,true,position,currentData.roomTypeId)
                holder.recycler.adapter = adapter
                adapter.notifyDataSetChanged()
            }else{
                holder.recycler.layoutManager=LinearLayoutManager(applicationContext)
                val adapter = RoomTypeCategoryAdapter(applicationContext, currentData.barRatePlans,discountAmount,discountPercentage,false,position,currentData.roomTypeId)
                holder.recycler.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        }


        holder.recycler.layoutManager=LinearLayoutManager(applicationContext)
        val adapter = RoomTypeCategoryAdapter(applicationContext, currentData.barRatePlans,discountAmount,discountPercentage,false,position,currentData.roomTypeId)
        holder.recycler.adapter = adapter
        adapter.notifyDataSetChanged()

        adapter.setOnListUpdateListener(this)

    }

    override fun onRateTypeListChanged(
        updatedRateTypeList: ArrayList<AddDiscountDataClass>,
        position: Int
    ) {
        Log.e("fuckingList",updatedRateTypeList.toString())
        val roomId = rateTypeList[position].roomTypeId
        onRateTypeListChangeListener?.onRateTypeList()
        discountList.addAll(updatedRateTypeList)
    }
}