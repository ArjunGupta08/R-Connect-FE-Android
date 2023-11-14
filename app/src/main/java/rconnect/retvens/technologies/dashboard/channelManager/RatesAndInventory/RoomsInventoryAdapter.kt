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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import rconnect.retvens.technologies.R

class RoomsInventoryAdapter(val context: Context, private val inventory:ResponseData) :
    RecyclerView.Adapter<RoomsInventoryAdapter.InventoryViewHolder>() {

    private lateinit var ratePlanPricesAdapter: RatePlanPricesAdapter
    private  var mList:ArrayList<String> = ArrayList()


    class InventoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val roomType:TextView = itemView.findViewById(R.id.roomType)
        val inventory1: TextView = itemView.findViewById(R.id.inventory_)
        val inventory2: TextView = itemView.findViewById(R.id.inventory_20)
        val inventory3: TextView = itemView.findViewById(R.id.inventory_30)
        val inventory4: TextView = itemView.findViewById(R.id.inventory_40)
        val inventory5: TextView = itemView.findViewById(R.id.inventory_50)
        val inventory6: TextView = itemView.findViewById(R.id.inventory_60)
        val inventory7: TextView = itemView.findViewById(R.id.inventory_70)
        val inventory8: TextView = itemView.findViewById(R.id.inventory_80)
        val inventory9: TextView = itemView.findViewById(R.id.inventory_90)
        val inventory10: TextView = itemView.findViewById(R.id.inventory_100)

//        val inventory: TextView = itemView.findViewById(R.id.inventory_1)
//        val inventory20: TextView = itemView.findViewById(R.id.inventory_2)
//        val inventory30: TextView = itemView.findViewById(R.id.inventory_3)
//        val inventory40: TextView = itemView.findViewById(R.id.inventory_4)
//        val inventory50: TextView = itemView.findViewById(R.id.inventory_5)
//        val inventory60: TextView = itemView.findViewById(R.id.inventory_6)
//        val inventory70: TextView = itemView.findViewById(R.id.inventory_7)
//        val inventory80: TextView = itemView.findViewById(R.id.inventory_8)
//        val inventory90: TextView = itemView.findViewById(R.id.inventory_9)
//        val inventory100: TextView = itemView.findViewById(R.id.inventory_10)
        val recyclerView = itemView.findViewById<RecyclerView>(R.id.roomPlan_recycler)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_remaining_inventory, parent, false)
        return InventoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        val data = inventory.data
        val room = data[position]
        val inventory = room.calculatedInventoryData[position]


        holder.roomType.setText(room.roomTypeName)

        holder.inventory1.text = inventory.inventory.toString()

        holder.inventory2.text = inventory.inventory.toString()
        holder.inventory3.text = inventory.inventory.toString()
        holder.inventory4.text = inventory.inventory.toString()
        holder.inventory5.text = inventory.inventory.toString()
        holder.inventory6.text = inventory.inventory.toString()
        holder.inventory7.text = inventory.inventory.toString()
        holder.inventory8.text = inventory.inventory.toString()
        holder.inventory9.text = inventory.inventory.toString()
        holder.inventory10.text = inventory.inventory.toString()

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
        return inventory.data.size
    }
}


