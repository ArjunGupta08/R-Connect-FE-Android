package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlaneDiscount

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.checkbox.MaterialCheckBox
import okhttp3.internal.isSensitiveHeader
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.CreateRateData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.AddCompanyRatePlanDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsData

class RoomTypeCategoryAdapter(val applicationContext:Context, val rateTypeList:List<GetBar>,val discountAmount:String,val discountPercentage:String,val isCheck:Boolean,val positions: Int):RecyclerView.Adapter<RoomTypeCategoryAdapter.ViewHolder>() {

    private var updateList:ArrayList<RatePlan> = ArrayList()
    private var onRateTypeListChangeListener : OnRateTypeListChangeListener ?= null
    fun setOnListUpdateListener (listener : OnRateTypeListChangeListener) {
        onRateTypeListChangeListener = listener
    }

    interface OnRateTypeListChangeListener {
        fun onRateTypeListChanged(updatedRateTypeList: ArrayList<RatePlan>,position: Int)
    }

    class ViewHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {
        val rateType:TextView = itemView.findViewById(R.id.roomType)
        val ratePlanPrice:TextView = itemView.findViewById(R.id.ratePlanPrice)
        val checkBox: MaterialCheckBox = itemView.findViewById(R.id.checkbox)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_room_type_category,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return rateTypeList.size
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentData = rateTypeList[position]
        holder.rateType.text = currentData.ratePlanName
        holder.ratePlanPrice.text = currentData.ratePlanTotal

        holder.checkBox.isChecked = isCheck

        if (isCheck){
            try {
                // Assuming ratePlanTotal and discountPercentage are strings obtained from your data
                val ratePlanTotalStr = currentData.ratePlanTotal // Replace with your actual ratePlanTotal
                val discountPercentageStr = discountPercentage // Replace with your actual discountPercentage
                val amount = discountAmount

                // Check if the strings are not empty
                if (discountPercentageStr.isNotEmpty()) {
                    // Attempt to convert the strings to doubles
                    val ratePlanTotal = ratePlanTotalStr.toDouble()
                    val percentage = discountPercentageStr.toDouble()

                    // Calculate the discounted amount
                    val discountAmount = (percentage / 100.0) * ratePlanTotal

                    // Calculate the discounted price
                    val discountedPrice = ratePlanTotal - discountAmount

                    // Now, you can set the discounted price to your TextView
                    holder.ratePlanPrice.text = discountedPrice.toString()
                    updateList.add(RatePlan(currentData.barRatePlanId,discountedPrice.toString()))
                    onRateTypeListChangeListener?.onRateTypeListChanged(updateList, positions)
                } else if (amount.isNotEmpty()){
                    val total = ratePlanTotalStr.toDouble() - amount.toDouble()
                    holder.ratePlanPrice.text = total.toString()

                }
            } catch (e: NumberFormatException) {
                // Handle the exception, e.g., log an error or show a message to the user
                Log.e("error",e.toString())
            }
        }else{
            holder.ratePlanPrice.text = currentData.ratePlanTotal
            updateList.removeIf { it.rateplanId == currentData.barRatePlanId }
            onRateTypeListChangeListener?.onRateTypeListChanged(updateList,positions)
        }

        holder.checkBox.setOnCheckedChangeListener { checkbox, isChecked ->

            if (isChecked){
                try {
                    // Assuming ratePlanTotal and discountPercentage are strings obtained from your data
                    val ratePlanTotalStr = currentData.ratePlanTotal // Replace with your actual ratePlanTotal
                    val discountPercentageStr = discountPercentage // Replace with your actual discountPercentage
                    val amount = discountAmount

                    // Check if the strings are not empty
                    if (discountPercentageStr.isNotEmpty()) {
                        // Attempt to convert the strings to doubles
                        val ratePlanTotal = ratePlanTotalStr.toDouble()
                        val percentage = discountPercentageStr.toDouble()

                        // Calculate the discounted amount
                        val discountAmount = (percentage / 100.0) * ratePlanTotal

                        // Calculate the discounted price
                        val discountedPrice = ratePlanTotal - discountAmount

                        // Now, you can set the discounted price to your TextView
                        holder.ratePlanPrice.text = discountedPrice.toString()
                        updateList.add(RatePlan(currentData.barRatePlanId,discountedPrice.toString()))
                        onRateTypeListChangeListener?.onRateTypeListChanged(updateList, positions)
                    } else if (amount.isNotEmpty()){
                            val total = ratePlanTotalStr.toDouble() - amount.toDouble()
                        holder.ratePlanPrice.text = total.toString()

                    }

                } catch (e: NumberFormatException) {
                    // Handle the exception, e.g., log an error or show a message to the user
                    Log.e("error",e.toString())
                }
            }else{
                holder.ratePlanPrice.text = currentData.ratePlanTotal
                updateList.removeIf { it.rateplanId == currentData.barRatePlanId }
                onRateTypeListChangeListener?.onRateTypeListChanged(updateList,positions)
            }
        }

    }
}