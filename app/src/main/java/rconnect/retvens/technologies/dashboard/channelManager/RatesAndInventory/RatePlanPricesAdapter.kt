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
import com.google.android.material.checkbox.MaterialCheckBox
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlaneDiscount.BarRatePlan

class RatePlanPricesAdapter(val context:Context,private val rateList: List<RatePlan>,val updateFilter:String) :
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

        val checkBox1: MaterialCheckBox = itemView.findViewById(R.id.checkBox_1)
        val checkBox2: MaterialCheckBox = itemView.findViewById(R.id.checkBox_2)
        val checkBox3: MaterialCheckBox = itemView.findViewById(R.id.checkBox_3)
        val checkBox4: MaterialCheckBox = itemView.findViewById(R.id.checkBox_4)
        val checkBox5: MaterialCheckBox = itemView.findViewById(R.id.checkBox_5)
        val checkBox6: MaterialCheckBox = itemView.findViewById(R.id.checkBox_6)
        val checkBox7: MaterialCheckBox = itemView.findViewById(R.id.checkBox_7)
        val checkBox8: MaterialCheckBox = itemView.findViewById(R.id.checkBox_8)
        val checkBox9: MaterialCheckBox = itemView.findViewById(R.id.checkBox_9)
        val checkBox10: MaterialCheckBox = itemView.findViewById(R.id.checkBox_10)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_roomplan_price, parent, false)
        return InventoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        val currentItem = rateList[position]
        holder.mealPlanName.text = currentItem.ratePlanName

        if (updateFilter == "Rates"){
            holder.ratePrice1.visibility = View.VISIBLE
            holder.ratePrice2.visibility = View.VISIBLE
            holder.ratePrice3.visibility = View.VISIBLE
            holder.ratePrice4.visibility = View.VISIBLE
            holder.ratePrice5.visibility = View.VISIBLE
            holder.ratePrice6.visibility = View.VISIBLE
            holder.ratePrice7.visibility = View.VISIBLE
            holder.ratePrice8.visibility = View.VISIBLE
            holder.ratePrice9.visibility = View.VISIBLE
            holder.ratePrice10.visibility = View.VISIBLE
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
        }else if (updateFilter == "Stop sell"){
            holder.checkBox1.visibility = View.VISIBLE
            holder.checkBox2.visibility = View.VISIBLE
            holder.checkBox3.visibility = View.VISIBLE
            holder.checkBox4.visibility = View.VISIBLE
            holder.checkBox5.visibility = View.VISIBLE
            holder.checkBox6.visibility = View.VISIBLE
            holder.checkBox7.visibility = View.VISIBLE
            holder.checkBox8.visibility = View.VISIBLE
            holder.checkBox9.visibility = View.VISIBLE
            holder.checkBox10.visibility = View.VISIBLE

            holder.checkBox1.isChecked == currentItem.baseRates.getOrNull(0)?.stopSell?.toBoolean() ?: false
            holder.checkBox2.isChecked == currentItem.baseRates.getOrNull(1)?.stopSell?.toBoolean() ?: false
            holder.checkBox3.isChecked == currentItem.baseRates.getOrNull(2)?.stopSell?.toBoolean() ?: false
            holder.checkBox4.isChecked == currentItem.baseRates.getOrNull(3)?.stopSell?.toBoolean() ?: false
            holder.checkBox5.isChecked == currentItem.baseRates.getOrNull(4)?.stopSell?.toBoolean() ?: false
            holder.checkBox6.isChecked == currentItem.baseRates.getOrNull(5)?.stopSell?.toBoolean() ?: false
            holder.checkBox7.isChecked == currentItem.baseRates.getOrNull(6)?.stopSell?.toBoolean() ?: false
            holder.checkBox8.isChecked == currentItem.baseRates.getOrNull(7)?.stopSell?.toBoolean() ?: false
            holder.checkBox9.isChecked == currentItem.baseRates.getOrNull(8)?.stopSell?.toBoolean() ?: false
            holder.checkBox10.isChecked == currentItem.baseRates.getOrNull(9)?.stopSell?.toBoolean() ?: false
        }else if (updateFilter == "COA"){
            holder.checkBox1.visibility = View.VISIBLE
            holder.checkBox2.visibility = View.VISIBLE
            holder.checkBox3.visibility = View.VISIBLE
            holder.checkBox4.visibility = View.VISIBLE
            holder.checkBox5.visibility = View.VISIBLE
            holder.checkBox6.visibility = View.VISIBLE
            holder.checkBox7.visibility = View.VISIBLE
            holder.checkBox8.visibility = View.VISIBLE
            holder.checkBox9.visibility = View.VISIBLE
            holder.checkBox10.visibility = View.VISIBLE

            holder.checkBox1.isChecked == currentItem.baseRates.getOrNull(0)?.COA?.toBoolean() ?: false
            holder.checkBox2.isChecked == currentItem.baseRates.getOrNull(1)?.COA?.toBoolean() ?: false
            holder.checkBox3.isChecked == currentItem.baseRates.getOrNull(2)?.COA?.toBoolean() ?: false
            holder.checkBox4.isChecked == currentItem.baseRates.getOrNull(3)?.COA?.toBoolean() ?: false
            holder.checkBox5.isChecked == currentItem.baseRates.getOrNull(4)?.COA?.toBoolean() ?: false
            holder.checkBox6.isChecked == currentItem.baseRates.getOrNull(5)?.COA?.toBoolean() ?: false
            holder.checkBox7.isChecked == currentItem.baseRates.getOrNull(6)?.COA?.toBoolean() ?: false
            holder.checkBox8.isChecked == currentItem.baseRates.getOrNull(7)?.COA?.toBoolean() ?: false
            holder.checkBox9.isChecked == currentItem.baseRates.getOrNull(8)?.COA?.toBoolean() ?: false
            holder.checkBox10.isChecked == currentItem.baseRates.getOrNull(9)?.COA?.toBoolean() ?: false
        }else if (updateFilter == "COD"){
            holder.checkBox1.visibility = View.VISIBLE
            holder.checkBox2.visibility = View.VISIBLE
            holder.checkBox3.visibility = View.VISIBLE
            holder.checkBox4.visibility = View.VISIBLE
            holder.checkBox5.visibility = View.VISIBLE
            holder.checkBox6.visibility = View.VISIBLE
            holder.checkBox7.visibility = View.VISIBLE
            holder.checkBox8.visibility = View.VISIBLE
            holder.checkBox9.visibility = View.VISIBLE
            holder.checkBox10.visibility = View.VISIBLE

            holder.checkBox1.isChecked == currentItem.baseRates.getOrNull(0)?.COA?.toBoolean() ?: false
            holder.checkBox2.isChecked == currentItem.baseRates.getOrNull(1)?.COA?.toBoolean() ?: false
            holder.checkBox3.isChecked == currentItem.baseRates.getOrNull(2)?.COA?.toBoolean() ?: false
            holder.checkBox4.isChecked == currentItem.baseRates.getOrNull(3)?.COA?.toBoolean() ?: false
            holder.checkBox5.isChecked == currentItem.baseRates.getOrNull(4)?.COA?.toBoolean() ?: false
            holder.checkBox6.isChecked == currentItem.baseRates.getOrNull(5)?.COA?.toBoolean() ?: false
            holder.checkBox7.isChecked == currentItem.baseRates.getOrNull(6)?.COA?.toBoolean() ?: false
            holder.checkBox8.isChecked == currentItem.baseRates.getOrNull(7)?.COA?.toBoolean() ?: false
            holder.checkBox9.isChecked == currentItem.baseRates.getOrNull(8)?.COA?.toBoolean() ?: false
            holder.checkBox10.isChecked == currentItem.baseRates.getOrNull(9)?.COA?.toBoolean() ?: false
        }else if (updateFilter == "Min Night"){
                holder.ratePrice1.visibility = View.VISIBLE
                holder.ratePrice2.visibility = View.VISIBLE
                holder.ratePrice3.visibility = View.VISIBLE
                holder.ratePrice4.visibility = View.VISIBLE
                holder.ratePrice5.visibility = View.VISIBLE
                holder.ratePrice6.visibility = View.VISIBLE
                holder.ratePrice7.visibility = View.VISIBLE
                holder.ratePrice8.visibility = View.VISIBLE
                holder.ratePrice9.visibility = View.VISIBLE
                holder.ratePrice10.visibility = View.VISIBLE
                holder.ratePrice1.text = currentItem.baseRates.get(0).minimumLOS.toString()
                holder.ratePrice2.text = currentItem.baseRates.get(1).minimumLOS.toString()
                holder.ratePrice3.text = currentItem.baseRates.get(2).minimumLOS.toString()
                holder.ratePrice4.text = currentItem.baseRates.get(3).minimumLOS.toString()
                holder.ratePrice5.text = currentItem.baseRates.get(4).minimumLOS.toString()
                holder.ratePrice6.text = currentItem.baseRates.get(5).minimumLOS.toString()
                holder.ratePrice7.text = currentItem.baseRates.get(6).minimumLOS.toString()
                holder.ratePrice8.text = currentItem.baseRates.get(7).minimumLOS.toString()
                holder.ratePrice9.text = currentItem.baseRates.get(8).minimumLOS.toString()
                holder.ratePrice10.text = currentItem.baseRates.get(9).minimumLOS.toString()
        }else if (updateFilter == "Extra Adult Rates"){
            holder.ratePrice1.visibility = View.VISIBLE
            holder.ratePrice2.visibility = View.VISIBLE
            holder.ratePrice3.visibility = View.VISIBLE
            holder.ratePrice4.visibility = View.VISIBLE
            holder.ratePrice5.visibility = View.VISIBLE
            holder.ratePrice6.visibility = View.VISIBLE
            holder.ratePrice7.visibility = View.VISIBLE
            holder.ratePrice8.visibility = View.VISIBLE
            holder.ratePrice9.visibility = View.VISIBLE
            holder.ratePrice10.visibility = View.VISIBLE
            holder.ratePrice1.text = currentItem.baseRates.get(0).extraAdultRate
            holder.ratePrice2.text = currentItem.baseRates.get(1).extraAdultRate
            holder.ratePrice3.text = currentItem.baseRates.get(2).extraAdultRate
            holder.ratePrice4.text = currentItem.baseRates.get(3).extraAdultRate
            holder.ratePrice5.text = currentItem.baseRates.get(4).extraAdultRate
            holder.ratePrice6.text = currentItem.baseRates.get(5).extraAdultRate
            holder.ratePrice7.text = currentItem.baseRates.get(6).extraAdultRate
            holder.ratePrice8.text = currentItem.baseRates.get(7).extraAdultRate
            holder.ratePrice9.text = currentItem.baseRates.get(8).extraAdultRate
            holder.ratePrice10.text = currentItem.baseRates.get(9).extraAdultRate
        }else if (updateFilter == "Extra Child Rates"){
            holder.ratePrice1.visibility = View.VISIBLE
            holder.ratePrice2.visibility = View.VISIBLE
            holder.ratePrice3.visibility = View.VISIBLE
            holder.ratePrice4.visibility = View.VISIBLE
            holder.ratePrice5.visibility = View.VISIBLE
            holder.ratePrice6.visibility = View.VISIBLE
            holder.ratePrice7.visibility = View.VISIBLE
            holder.ratePrice8.visibility = View.VISIBLE
            holder.ratePrice9.visibility = View.VISIBLE
            holder.ratePrice10.visibility = View.VISIBLE
            holder.ratePrice1.text = currentItem.baseRates.get(0).extraChildRate
            holder.ratePrice2.text = currentItem.baseRates.get(1).extraChildRate
            holder.ratePrice3.text = currentItem.baseRates.get(2).extraChildRate
            holder.ratePrice4.text = currentItem.baseRates.get(3).extraChildRate
            holder.ratePrice5.text = currentItem.baseRates.get(4).extraChildRate
            holder.ratePrice6.text = currentItem.baseRates.get(5).extraChildRate
            holder.ratePrice7.text = currentItem.baseRates.get(6).extraChildRate
            holder.ratePrice8.text = currentItem.baseRates.get(7).extraChildRate
            holder.ratePrice9.text = currentItem.baseRates.get(8).extraChildRate
            holder.ratePrice10.text = currentItem.baseRates.get(9).extraChildRate
        }


        var isRecyclerViewVisible = false
            holder.dropDown.setOnClickListener {
                isRecyclerViewVisible = !isRecyclerViewVisible
                if (isRecyclerViewVisible) {
                    holder.recyclerView.visibility = View.VISIBLE
                } else {
                    holder.recyclerView.visibility = View.GONE
                }
            }
        holder.recyclerView.layoutManager = LinearLayoutManager(context)
        otaPricesAdapter = OtaPricesAdapter(context,currentItem.OTA!!)

        holder.recyclerView.adapter = otaPricesAdapter
        otaPricesAdapter.notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return rateList.size
    }
}


