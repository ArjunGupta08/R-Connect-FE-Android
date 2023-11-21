package rconnect.retvens.technologies.dashboard.channelManager.RatesAndInventory

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R

class RatePlanPricesAdapter(val context:Context,private val rateList: ArrayList<BarRatePlan>) :
    RecyclerView.Adapter<RatePlanPricesAdapter.InventoryViewHolder>() {

    private lateinit var otaPricesAdapter: OtaPricesAdapter

    class InventoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mealPlanName = itemView.findViewById<TextView>(R.id.meal_Plan)
        val ratePrice1: TextView = itemView.findViewById(R.id.ratePrice_1)
        val ratePrice2: TextView = itemView.findViewById(R.id.ratePrice_2)
        val ratePrice3: TextView = itemView.findViewById(R.id.ratePrice_3)
        val ratePrice4: TextView = itemView.findViewById(R.id.ratePrice_4)
        val ratePrice5: TextView = itemView.findViewById(R.id.ratePrice_5)
        val ratePrice6: TextView = itemView.findViewById(R.id.ratePrice_6)
        val ratePrice7: TextView = itemView.findViewById(R.id.ratePrice_7)
        val ratePrice8: TextView = itemView.findViewById(R.id.ratePrice_8)
        val ratePrice9: TextView = itemView.findViewById(R.id.ratePrice_9)
        val ratePrice10: TextView = itemView.findViewById(R.id.ratePrice_10)
        val recyclerView = itemView.findViewById<RecyclerView>(R.id.ota_recycler_price);
        val dropDown = itemView.findViewById<ImageView>(R.id.dropDown);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_roomplan_price, parent, false)
        return InventoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        val currentItem = rateList[position]

        Log.e("currentRes",currentItem.baseRates.toString())

        holder.mealPlanName.text = currentItem.ratePlanName

        holder.ratePrice1.text = currentItem.baseRates.get(0).baseRate
        holder.ratePrice2.text = currentItem.baseRates.get(1).baseRate
        holder.ratePrice3.text = currentItem.baseRates.get(2).baseRate
        holder.ratePrice4.text = currentItem.baseRates.get(3).baseRate
        holder.ratePrice5.text = currentItem.baseRates.get(4).baseRate
        holder.ratePrice6.text = currentItem.baseRates.get(5).baseRate
        holder.ratePrice7.text = currentItem.baseRates.get(6).baseRate
        holder.ratePrice8.text = currentItem.baseRates.get(7).baseRate
        holder.ratePrice9.text = currentItem.baseRates.get(8).baseRate
        holder.ratePrice10.text = currentItem.baseRates.get(9).baseRate

        var isRecyclerViewVisible = false


            holder.dropDown.setOnClickListener {
                isRecyclerViewVisible = !isRecyclerViewVisible
                if (isRecyclerViewVisible) {
                    holder.recyclerView.visibility = View.VISIBLE
                } else {
                    holder.recyclerView.visibility = View.GONE
                }
            }

//        holder.recyclerView.layoutManager = LinearLayoutManager(context)
//        otaPricesAdapter = OtaPricesAdapter(stringList)
//
//        holder.recyclerView.adapter = otaPricesAdapter
//        otaPricesAdapter.notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return rateList.size
    }
}


