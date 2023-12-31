package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanBar

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.GetRoomType
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.AddCompanyRatePlanDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.InclusionPlan
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.showDropdownMenu

class RoomTypePlanAdapter(val applicationContext:Context, val rateTypeList:ArrayList<RoomTypePlanDataClass>,val supportFragmentManager: androidx.fragment.app.FragmentManager):RecyclerView.Adapter<RoomTypePlanAdapter.ViewHolder>(),
    RatePlanBarAdapter.OnRateTypeListChangeListener {

    private var ratePlan:ArrayList<AddBarsRatePlanDataClass> = ArrayList()
    val selectedInclusionList: ArrayList<InclusionPlan> = arrayListOf()
    private var onRateTypeListChangeListener : OnRateTypeListChangeListener ?= null
    fun setOnListUpdateListener (listener : OnRateTypeListChangeListener) {
        onRateTypeListChangeListener = listener
    }

    interface OnRateTypeListChangeListener {
        fun onRateTypeListChanged(updatedRateTypeList: AddBarsRatePlanDataClass,position: Int)
    }

    class ViewHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {
        val roomTypeName = itemView.findViewById<TextView>(R.id.roomType_Name)
        val recyclerRoom = itemView.findViewById<RecyclerView>(R.id.recyclerview_roomRatePlan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_roomtype_rateplan,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return rateTypeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = rateTypeList[position]



        holder.roomTypeName.text = currentData.roomTypeName

        ratePlan.clear()

        currentData.getMeal.forEach {
            val mealName = it.mealPlanName
            val shortCode = generateShortCode(currentData.roomTypeName,mealName)


        val ratePlans = AddBarsRatePlanDataClass(
            UserSessionManager(applicationContext).getUserId().toString(),currentData.propertyId,currentData.roomTypeId,"Bar","",
            "","${currentData.roomTypeName+it.shortCode}",it.mealPlanId,shortCode, selectedInclusionList,currentData.roomBasePrice,it.chargesPerOccupancy,"","",currentData.extraAdultRate,currentData.extraChildRate,currentData.roomBasePrice,it.mealPlanName
        )

        ratePlan.add(ratePlans)
        }

        holder.recyclerRoom.layoutManager = LinearLayoutManager(applicationContext)
        val adapter = RatePlanBarAdapter(applicationContext,supportFragmentManager,ratePlan)
        holder.recyclerRoom.adapter = adapter
        adapter.notifyDataSetChanged()

        adapter.setOnListUpdateListener(this)
        Log.e("fuckList",ratePlan.toString())
    }

    fun generateShortCode(type1: String, type2: String): String {
        // Take the first two characters from the first type (or pad with 'X' if it's shorter)
        val code1 = type1.substring(0, minOf(2, type1.length)).padEnd(2, 'X')

        // Take the first two characters from the second type (or pad with 'X' if it's shorter)
        val code2 = type2.substring(0, minOf(2, type2.length)).padEnd(2, 'X')

        // Combine the two codes and convert to uppercase
        return (code1 + code2).toUpperCase()
    }


    override fun onRateTypeListChanged(
        updatedRateTypeList: AddBarsRatePlanDataClass,
        position: Int
    ) {
        onRateTypeListChangeListener?.onRateTypeListChanged(updatedRateTypeList,position)
    }

    override fun onRateTypeDelete(position: Int) {

    }
}