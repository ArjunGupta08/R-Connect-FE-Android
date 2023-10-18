package rconnect.retvens.technologies.dashboard.channelManager.RatesAndInventory

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.inputmethodservice.InputMethodService
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat.createDeviceProtectedStorageContext
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R


class RoomsInventoryAdapter(val context: Context, private val stringList: ArrayList<String>) :
    RecyclerView.Adapter<RoomsInventoryAdapter.InventoryViewHolder>() {

    private lateinit var ratePlanPricesAdapter: RatePlanPricesAdapter
    private  var mList:ArrayList<String> = ArrayList()



    class InventoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val inventory1: TextView = itemView.findViewById(R.id.inventory_1)
        val inventory2: TextView = itemView.findViewById(R.id.inventory_2)
        val inventory3: TextView = itemView.findViewById(R.id.inventory_3)
        val inventory4: TextView = itemView.findViewById(R.id.inventory_4)
        val inventory5: TextView = itemView.findViewById(R.id.inventory_5)
        val inventory6: TextView = itemView.findViewById(R.id.inventory_6)
        val inventory7: TextView = itemView.findViewById(R.id.inventory_7)
        val inventory8: TextView = itemView.findViewById(R.id.inventory_8)
        val inventory9: TextView = itemView.findViewById(R.id.inventory_9)
        val inventory10: TextView = itemView.findViewById(R.id.inventory_10)
        val recyclerView = itemView.findViewById<RecyclerView>(R.id.roomPlan_recycler)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_remaining_inventory, parent, false)
        return InventoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        val currentItem = stringList[position]


        holder.inventory1.setText(currentItem)

        holder.inventory2.setText(currentItem)
        holder.inventory3.setText(currentItem)
        holder.inventory4.setText(currentItem)
        holder.inventory5.setText(currentItem)
        holder.inventory6.setText(currentItem)
        holder.inventory7.setText(currentItem)
        holder.inventory8.setText(currentItem)
        holder.inventory9.setText(currentItem)
        holder.inventory10.setText(currentItem)

        holder.inventory1.isCursorVisible = true;
        holder.inventory1.inputType = InputType.TYPE_CLASS_NUMBER;

        holder.inventory1.setOnClickListener{
            holder.inventory1.isCursorVisible = true;
            holder.inventory1.isFocusableInTouchMode = true;
            holder.inventory1.inputType = InputType.TYPE_CLASS_NUMBER;

            holder.inventory1.requestFocus(); //to trigger the soft input

        }

        holder.recyclerView.layoutManager = LinearLayoutManager(context)
        ratePlanPricesAdapter = RatePlanPricesAdapter(context, mList)

        setPlan()
        holder.recyclerView.adapter = ratePlanPricesAdapter
        ratePlanPricesAdapter.notifyDataSetChanged()
    }

    private fun setPlan() {
        mList.clear()
        mList.add("3000")
        mList.add("5000")



    }

    override fun getItemCount(): Int {
        return stringList.size
    }
}


