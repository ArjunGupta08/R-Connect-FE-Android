package rconnect.retvens.technologies.dashboard.channelManager.RatesAndInventory

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R
import java.lang.NullPointerException

class OtaPricesAdapter(val context: Context,private val stringList: List<OTA>) :
    RecyclerView.Adapter<OtaPricesAdapter.InventoryViewHolder>() {


    class InventoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val otaPrice1: TextView = itemView.findViewById(R.id.otaPrice_1)
        val otaPrice2: TextView = itemView.findViewById(R.id.otaPrice_2)
        val otaPrice3: TextView = itemView.findViewById(R.id.otaPrice_3)
        val otaPrice4: TextView = itemView.findViewById(R.id.otaPrice_4)
        val otaPrice5: TextView = itemView.findViewById(R.id.otaPrice_5)
        val otaPrice6: TextView = itemView.findViewById(R.id.otaPrice_6)
        val otaPrice7: TextView = itemView.findViewById(R.id.otaPrice_7)
        val otaPrice8: TextView = itemView.findViewById(R.id.otaPrice_8)
        val otaPrice9: TextView = itemView.findViewById(R.id.otaPrice_9)
        val otaPrice10: TextView = itemView.findViewById(R.id.otaPrice_10)
        val recyclerView = itemView.findViewById<RecyclerView>(R.id.ota_recycler_price);
        val otaName = itemView.findViewById<TextView>(R.id.txt_ota)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_ota_price, parent, false)
        return InventoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        val currentItem = stringList[position]

        try {
            holder.otaName.text = currentItem.otaName

            holder.otaPrice1.text = currentItem.OTARates.get(0).baseRate
            holder.otaPrice2.text = currentItem.OTARates.get(1).baseRate
            holder.otaPrice3.text = currentItem.OTARates.get(2).baseRate
            holder.otaPrice4.text = currentItem.OTARates.get(3).baseRate
            holder.otaPrice5.text = currentItem.OTARates.get(4).baseRate
            holder.otaPrice6.text = currentItem.OTARates.get(5).baseRate
            holder.otaPrice7.text = currentItem.OTARates.get(6).baseRate
            holder.otaPrice8.text = currentItem.OTARates.get(7).baseRate
            holder.otaPrice9.text = currentItem.OTARates.get(8).baseRate
            holder.otaPrice10.text = currentItem.OTARates.get(9).baseRate
        }catch (e:NullPointerException){
            Log.e("error",e.message.toString())
        }

    }

    override fun getItemCount(): Int {
        return stringList.size
    }
}


