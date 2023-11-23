package rconnect.retvens.technologies.dashboard.channelManager.RatesAndInventory

import android.app.Dialog
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.res.ColorStateList
import android.inputmethodservice.InputMethodService
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.RatesAndInventoryInterface
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.AddCompanyRatePlanDataClass
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IndexOutOfBoundsException
import java.lang.NullPointerException

class RoomsInventoryAdapter(val context: Context, private val inventory:ResponseData) :
    RecyclerView.Adapter<RoomsInventoryAdapter.InventoryViewHolder>() {

    private lateinit var ratePlanPricesAdapter: RatePlanPricesAdapter
    private  var mList:ArrayList<String> = ArrayList()

    private lateinit var progressDialog:Dialog
    private var onRateTypeListChangeListener : OnRateTypeListChangeListener ?= null
    fun setOnListUpdateListener (listener : OnRateTypeListChangeListener) {
        onRateTypeListChangeListener = listener
    }

    interface OnRateTypeListChangeListener {
        fun onRateTypeListChanged(roomTypeId:String,startDate:String,inventory:String,isBlock:String,isAdd:String)
    }

    class InventoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val roomType:TextView = itemView.findViewById(R.id.roomType)
        val inventory1: TextInputEditText = itemView.findViewById(R.id.inventory_)
        val inventory2: TextInputEditText = itemView.findViewById(R.id.inventory_20)
        val inventory3: TextInputEditText = itemView.findViewById(R.id.inventory_30)
        val inventory4: TextInputEditText = itemView.findViewById(R.id.inventory_40)
        val inventory5: TextInputEditText = itemView.findViewById(R.id.inventory_50)
        val inventory6: TextInputEditText = itemView.findViewById(R.id.inventory_60)
        val inventory7: TextInputEditText = itemView.findViewById(R.id.inventory_70)
        val inventory8: TextInputEditText = itemView.findViewById(R.id.inventory_80)
        val inventory9: TextInputEditText = itemView.findViewById(R.id.inventory_90)
        val inventory10: TextInputEditText = itemView.findViewById(R.id.inventory_100)

        val inventoryCard: MaterialCardView = itemView.findViewById(R.id.inventory_1)
        val inventoryCard20: MaterialCardView = itemView.findViewById(R.id.inventory_2)
        val inventoryCard30: MaterialCardView = itemView.findViewById(R.id.inventory_3)
        val inventoryCard40: MaterialCardView = itemView.findViewById(R.id.inventory_4)
        val inventoryCard50: MaterialCardView = itemView.findViewById(R.id.inventory_5)
        val inventoryCard60: MaterialCardView = itemView.findViewById(R.id.inventory_6)
        val inventoryCard70: MaterialCardView = itemView.findViewById(R.id.inventory_7)
        val inventoryCard80: MaterialCardView = itemView.findViewById(R.id.inventory_8)
        val inventoryCard90: MaterialCardView = itemView.findViewById(R.id.inventory_9)
        val inventoryCard100: MaterialCardView = itemView.findViewById(R.id.inventory_10)
        val recyclerView = itemView.findViewById<RecyclerView>(R.id.roomPlan_recycler)

        val unAvailableLock1 = itemView.findViewById<ImageView>(R.id.lock_1)
        val unAvailableLock2 = itemView.findViewById<ImageView>(R.id.lock_2)
        val unAvailableLock3 = itemView.findViewById<ImageView>(R.id.lock_3)
        val unAvailableLock4 = itemView.findViewById<ImageView>(R.id.lock_4)
        val unAvailableLock5 = itemView.findViewById<ImageView>(R.id.lock_5)
        val unAvailableLock6 = itemView.findViewById<ImageView>(R.id.lock_6)
        val unAvailableLock7 = itemView.findViewById<ImageView>(R.id.lock_7)
        val unAvailableLock8 = itemView.findViewById<ImageView>(R.id.lock_8)
        val unAvailableLock9 = itemView.findViewById<ImageView>(R.id.lock_9)
        val unAvailableLock10 = itemView.findViewById<ImageView>(R.id.lock_10)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_remaining_inventory, parent, false)
        return InventoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        val data = inventory.data
        val room = data[position]


        Log.e("current",inventory.toString())

        holder.roomType.setText(room.roomTypeName)
        holder.inventory1.setText(room.calculatedInventoryData.get(0).inventory.toString())
        holder.inventory2.setText(room.calculatedInventoryData.get(1).inventory.toString())
        holder.inventory3.setText(room.calculatedInventoryData.get(2).inventory.toString())
        holder.inventory4.setText(room.calculatedInventoryData.get(3).inventory.toString())
        holder.inventory5.setText(room.calculatedInventoryData.get(4).inventory.toString())
        holder.inventory6.setText(room.calculatedInventoryData.get(5).inventory.toString())
        holder.inventory7.setText(room.calculatedInventoryData.get(6).inventory.toString())
        holder.inventory8.setText(room.calculatedInventoryData.get(7).inventory.toString())
        holder.inventory9.setText(room.calculatedInventoryData.get(8).inventory.toString())
        holder.inventory10.setText(room.calculatedInventoryData.get(9).inventory.toString())

        if (room.calculatedInventoryData.get(0).inventory.toString() == "0"){
            holder.inventoryCard.setBackgroundResource(R.drawable.unavailable_room_card)
            val tintColor = ContextCompat.getColor(holder.inventoryCard.context, R.color.UnavailableRoom)
            val colorStateList = ColorStateList.valueOf(tintColor)
            ViewCompat.setBackgroundTintList(holder.inventoryCard, colorStateList)
            val strokeColor = ContextCompat.getColor(holder.inventoryCard.context, R.color.UnavailableRoomStroke)
            val strokeWidth = holder.inventoryCard.resources.getDimensionPixelSize(R.dimen.strokewidth)
            holder.inventoryCard.strokeColor = strokeColor
            holder.inventoryCard.strokeWidth = strokeWidth
        }
        if (room.calculatedInventoryData.get(1).inventory.toString() == "0"){
            val tintColor = ContextCompat.getColor(holder.inventoryCard20.context, R.color.UnavailableRoom)
            val colorStateList = ColorStateList.valueOf(tintColor)
            ViewCompat.setBackgroundTintList(holder.inventoryCard20, colorStateList)
            val strokeColor = ContextCompat.getColor(holder.inventoryCard20.context, R.color.UnavailableRoomStroke)
            val strokeWidth = holder.inventoryCard20.resources.getDimensionPixelSize(R.dimen.strokewidth)
            holder.inventoryCard20.strokeColor = strokeColor
            holder.inventoryCard20.strokeWidth = strokeWidth
        }
        if (room.calculatedInventoryData.get(2).inventory.toString() == "0"){
            val tintColor = ContextCompat.getColor(holder.inventoryCard30.context, R.color.UnavailableRoom)

            val colorStateList = ColorStateList.valueOf(tintColor)
// Set the tint color using ColorStateList
            ViewCompat.setBackgroundTintList(holder.inventoryCard30, colorStateList)
            val strokeColor = ContextCompat.getColor(holder.inventoryCard30.context, R.color.UnavailableRoomStroke)
            val strokeWidth = holder.inventoryCard30.resources.getDimensionPixelSize(R.dimen.strokewidth)
            holder.inventoryCard30.strokeColor = strokeColor
            holder.inventoryCard30.strokeWidth = strokeWidth
        }
        if (room.calculatedInventoryData.get(3).inventory.toString() == "0"){
            val tintColor = ContextCompat.getColor(holder.inventoryCard40.context, R.color.UnavailableRoom)

            val colorStateList = ColorStateList.valueOf(tintColor)
// Set the tint color using ColorStateList
            ViewCompat.setBackgroundTintList(holder.inventoryCard40, colorStateList)
            val strokeColor = ContextCompat.getColor(holder.inventoryCard40.context, R.color.UnavailableRoomStroke)
            val strokeWidth = holder.inventoryCard40.resources.getDimensionPixelSize(R.dimen.strokewidth)
            holder.inventoryCard40.strokeColor = strokeColor
            holder.inventoryCard40.strokeWidth = strokeWidth
        }
        if (room.calculatedInventoryData.get(4).inventory.toString() == "0"){
            Log.e("check","true")
            val tintColor = ContextCompat.getColor(holder.inventoryCard50.context, R.color.UnavailableRoom)
            val colorStateList = ColorStateList.valueOf(tintColor)
            ViewCompat.setBackgroundTintList(holder.inventoryCard50, colorStateList)
            val strokeColor = ContextCompat.getColor(holder.inventoryCard50.context, R.color.UnavailableRoomStroke)
            val strokeWidth = holder.inventoryCard50.resources.getDimensionPixelSize(R.dimen.strokewidth)
            holder.inventoryCard50.strokeColor = strokeColor
            holder.inventoryCard50.strokeWidth = strokeWidth
        }
        if (room.calculatedInventoryData.get(5).inventory.toString() == "0"){
            val tintColor = ContextCompat.getColor(holder.inventoryCard60.context, R.color.UnavailableRoom)
            val colorStateList = ColorStateList.valueOf(tintColor)
            ViewCompat.setBackgroundTintList(holder.inventoryCard60, colorStateList)
            val strokeColor = ContextCompat.getColor(holder.inventoryCard60.context, R.color.UnavailableRoomStroke)
            val strokeWidth = holder.inventoryCard60.resources.getDimensionPixelSize(R.dimen.strokewidth)
            holder.inventoryCard60.strokeColor = strokeColor
            holder.inventoryCard60.strokeWidth = strokeWidth
        }
        if (room.calculatedInventoryData.get(6).inventory.toString() == "0"){
            val tintColor = ContextCompat.getColor(holder.inventoryCard70.context, R.color.UnavailableRoom)

            val colorStateList = ColorStateList.valueOf(tintColor)
            ViewCompat.setBackgroundTintList(holder.inventoryCard70, colorStateList)
            val strokeColor = ContextCompat.getColor(holder.inventoryCard70.context, R.color.UnavailableRoomStroke)
            val strokeWidth = holder.inventoryCard70.resources.getDimensionPixelSize(R.dimen.strokewidth)
            holder.inventoryCard70.strokeColor = strokeColor
            holder.inventoryCard70.strokeWidth = strokeWidth
        }
        if (room.calculatedInventoryData.get(7).inventory.toString() == "0"){
            val tintColor = ContextCompat.getColor(holder.inventoryCard80.context, R.color.UnavailableRoom)

            val colorStateList = ColorStateList.valueOf(tintColor)
            ViewCompat.setBackgroundTintList(holder.inventoryCard80, colorStateList)
            val strokeColor = ContextCompat.getColor(holder.inventoryCard80.context, R.color.UnavailableRoomStroke)
            val strokeWidth = holder.inventoryCard80.resources.getDimensionPixelSize(R.dimen.strokewidth)
            holder.inventoryCard80.strokeColor = strokeColor
            holder.inventoryCard80.strokeWidth = strokeWidth
        }
        if (room.calculatedInventoryData.get(8).inventory.toString() == "0"){
            val tintColor = ContextCompat.getColor(holder.inventoryCard90.context, R.color.UnavailableRoom)
            val colorStateList = ColorStateList.valueOf(tintColor)
            ViewCompat.setBackgroundTintList(holder.inventoryCard90, colorStateList)
            val strokeColor = ContextCompat.getColor(holder.inventoryCard90.context, R.color.UnavailableRoomStroke)
            val strokeWidth = holder.inventoryCard90.resources.getDimensionPixelSize(R.dimen.strokewidth)
            holder.inventoryCard90.strokeColor = strokeColor
            holder.inventoryCard90.strokeWidth = strokeWidth
        }
        if (room.calculatedInventoryData.get(9).inventory.toString() == "0"){
            val tintColor = ContextCompat.getColor(holder.inventoryCard100.context, R.color.UnavailableRoom)
            val colorStateList = ColorStateList.valueOf(tintColor)
            ViewCompat.setBackgroundTintList(holder.inventoryCard100, colorStateList)
            val strokeColor = ContextCompat.getColor(holder.inventoryCard100.context, R.color.UnavailableRoomStroke)
            val strokeWidth = holder.inventoryCard100.resources.getDimensionPixelSize(R.dimen.strokewidth)
            holder.inventoryCard100.strokeColor = strokeColor
            holder.inventoryCard100.strokeWidth = strokeWidth
        }

        // For UnAvailable
        if (room.calculatedInventoryData.get(0).isBlocked == "true"){
            holder.inventoryCard.setBackgroundResource(R.drawable.unavailable_room_card)
            val tintColor = ContextCompat.getColor(holder.inventoryCard.context, R.color.BlockedInventoryColor)
            val colorStateList = ColorStateList.valueOf(tintColor)
            ViewCompat.setBackgroundTintList(holder.inventoryCard, colorStateList)
            val strokeColor = ContextCompat.getColor(holder.inventoryCard.context, R.color.BlockedInventoryStrokeColor)
            val strokeWidth = holder.inventoryCard.resources.getDimensionPixelSize(R.dimen.strokewidth)

            holder.inventoryCard.strokeColor = strokeColor
            holder.inventoryCard.strokeWidth = strokeWidth

            holder.unAvailableLock1.visibility = View.VISIBLE
        }
        if (room.calculatedInventoryData.get(1).isBlocked == "true"){
            val tintColor = ContextCompat.getColor(holder.inventoryCard20.context, R.color.BlockedInventoryColor)
            val colorStateList = ColorStateList.valueOf(tintColor)
            ViewCompat.setBackgroundTintList(holder.inventoryCard20, colorStateList)
            holder.inventoryCard20.setBackgroundResource(R.drawable.unavailable_room_card)
            val strokeColor = ContextCompat.getColor(holder.inventoryCard20.context, R.color.BlockedInventoryStrokeColor)
            val strokeWidth = holder.inventoryCard20.resources.getDimensionPixelSize(R.dimen.strokewidth)
            holder.inventoryCard20.strokeColor = strokeColor
            holder.inventoryCard20.strokeWidth = strokeWidth
            holder.unAvailableLock2.visibility = View.VISIBLE
        }
        if (room.calculatedInventoryData.get(2).isBlocked == "true"){
            val tintColor = ContextCompat.getColor(holder.inventoryCard30.context, R.color.BlockedInventoryColor)

            val colorStateList = ColorStateList.valueOf(tintColor)
            ViewCompat.setBackgroundTintList(holder.inventoryCard30, colorStateList)
            holder.inventoryCard30.setBackgroundResource(R.drawable.unavailable_room_card)
            val strokeColor = ContextCompat.getColor(holder.inventoryCard30.context, R.color.BlockedInventoryStrokeColor)
            val strokeWidth = holder.inventoryCard30.resources.getDimensionPixelSize(R.dimen.strokewidth)
            holder.inventoryCard30.strokeColor = strokeColor
            holder.inventoryCard30.strokeWidth = strokeWidth

            holder.unAvailableLock3.visibility = View.VISIBLE
        }
        if (room.calculatedInventoryData.get(3).isBlocked == "true"){
            val tintColor = ContextCompat.getColor(holder.inventoryCard40.context, R.color.BlockedInventoryColor)
            val colorStateList = ColorStateList.valueOf(tintColor)
            ViewCompat.setBackgroundTintList(holder.inventoryCard40, colorStateList)
            holder.inventoryCard40.setBackgroundResource(R.drawable.unavailable_room_card)
            val strokeColor = ContextCompat.getColor(holder.inventoryCard40.context, R.color.BlockedInventoryStrokeColor)
            val strokeWidth = holder.inventoryCard40.resources.getDimensionPixelSize(R.dimen.strokewidth)
            holder.inventoryCard40.strokeColor = strokeColor
            holder.inventoryCard40.strokeWidth = strokeWidth

            holder.unAvailableLock4.visibility = View.VISIBLE
        }
        if (room.calculatedInventoryData.get(4).isBlocked == "true"){
            Log.e("check","true")
            val tintColor = ContextCompat.getColor(holder.inventoryCard50.context, R.color.BlockedInventoryColor)
            val colorStateList = ColorStateList.valueOf(tintColor)
            ViewCompat.setBackgroundTintList(holder.inventoryCard50, colorStateList)
            val strokeColor = ContextCompat.getColor(holder.inventoryCard50.context, R.color.BlockedInventoryStrokeColor)
            val strokeWidth = holder.inventoryCard50.resources.getDimensionPixelSize(R.dimen.strokewidth)

            holder.inventoryCard50.strokeColor = strokeColor
            holder.inventoryCard50.strokeWidth = strokeWidth

            holder.unAvailableLock5.visibility = View.VISIBLE
        }
        if (room.calculatedInventoryData.get(5).isBlocked == "true"){
            val tintColor = ContextCompat.getColor(holder.inventoryCard60.context, R.color.BlockedInventoryColor)
            val colorStateList = ColorStateList.valueOf(tintColor)
            ViewCompat.setBackgroundTintList(holder.inventoryCard60, colorStateList)
            val strokeColor = ContextCompat.getColor(holder.inventoryCard60.context, R.color.BlockedInventoryStrokeColor)
            val strokeWidth = holder.inventoryCard60.resources.getDimensionPixelSize(R.dimen.strokewidth)
            holder.inventoryCard60.strokeColor = strokeColor
            holder.inventoryCard60.strokeWidth = strokeWidth

            holder.unAvailableLock6.visibility = View.VISIBLE
        }
        if (room.calculatedInventoryData.get(6).isBlocked == "true"){
            val tintColor = ContextCompat.getColor(holder.inventoryCard70.context, R.color.BlockedInventoryColor)
            val colorStateList = ColorStateList.valueOf(tintColor)
            ViewCompat.setBackgroundTintList(holder.inventoryCard70, colorStateList)
            val strokeColor = ContextCompat.getColor(holder.inventoryCard70.context, R.color.BlockedInventoryStrokeColor)
            val strokeWidth = holder.inventoryCard70.resources.getDimensionPixelSize(R.dimen.strokewidth)

// Set the stroke for the MaterialCardView
            holder.inventoryCard70.strokeColor = strokeColor
            holder.inventoryCard70.strokeWidth = strokeWidth
            holder.unAvailableLock7.visibility = View.VISIBLE
        }
        if (room.calculatedInventoryData.get(7).isBlocked == "true"){
            val tintColor = ContextCompat.getColor(holder.inventoryCard80.context, R.color.BlockedInventoryColor)
            val colorStateList = ColorStateList.valueOf(tintColor)
            ViewCompat.setBackgroundTintList(holder.inventoryCard80, colorStateList)
            holder.inventoryCard80.setBackgroundResource(R.drawable.unavailable_room_card)
            val strokeColor = ContextCompat.getColor(holder.inventoryCard80.context, R.color.BlockedInventoryStrokeColor)
            val strokeWidth = holder.inventoryCard80.resources.getDimensionPixelSize(R.dimen.strokewidth)
            holder.inventoryCard80.strokeColor = strokeColor
            holder.inventoryCard80.strokeWidth = strokeWidth

            holder.unAvailableLock8.visibility = View.VISIBLE
        }
        if (room.calculatedInventoryData.get(8).isBlocked == "true"){
            val tintColor = ContextCompat.getColor(holder.inventoryCard90.context, R.color.BlockedInventoryColor)
            val colorStateList = ColorStateList.valueOf(tintColor)
            ViewCompat.setBackgroundTintList(holder.inventoryCard90, colorStateList)
            val strokeColor = ContextCompat.getColor(holder.inventoryCard90.context, R.color.BlockedInventoryStrokeColor)
            val strokeWidth = holder.inventoryCard90.resources.getDimensionPixelSize(R.dimen.strokewidth)

            holder.inventoryCard90.strokeColor = strokeColor
            holder.inventoryCard90.strokeWidth = strokeWidth

            holder.unAvailableLock9.visibility = View.VISIBLE
        }
        if (room.calculatedInventoryData.get(9).isBlocked == "true"){
            val tintColor = ContextCompat.getColor(holder.inventoryCard100.context, R.color.BlockedInventoryColor)
            val colorStateList = ColorStateList.valueOf(tintColor)
            ViewCompat.setBackgroundTintList(holder.inventoryCard100, colorStateList)
            val strokeColor = ContextCompat.getColor(holder.inventoryCard100.context, R.color.BlockedInventoryStrokeColor)
            val strokeWidth = holder.inventoryCard100.resources.getDimensionPixelSize(R.dimen.strokewidth)

            holder.inventoryCard100.strokeColor = strokeColor
            holder.inventoryCard100.strokeWidth = strokeWidth

            holder.unAvailableLock10.visibility = View.VISIBLE
        }

//        holder.inventory1.setOnClickListener {
//            setInputEditText(holder.inventory1)
//        }
        setInputEditText(holder.inventory1,room.roomTypeId.toString(),room.calculatedInventoryData.get(0).date,room.calculatedInventoryData.get(0).inventory.toString())
        setInputEditText(holder.inventory2,room.roomTypeId.toString(),room.calculatedInventoryData.get(1).date,room.calculatedInventoryData.get(1).inventory.toString())
        setInputEditText(holder.inventory3,room.roomTypeId.toString(),room.calculatedInventoryData.get(2).date,room.calculatedInventoryData.get(2).inventory.toString())
        setInputEditText(holder.inventory4,room.roomTypeId.toString(),room.calculatedInventoryData.get(3).date,room.calculatedInventoryData.get(3).inventory.toString())
        setInputEditText(holder.inventory5,room.roomTypeId.toString(),room.calculatedInventoryData.get(4).date,room.calculatedInventoryData.get(4).inventory.toString())
        setInputEditText(holder.inventory6,room.roomTypeId.toString(),room.calculatedInventoryData.get(5).date,room.calculatedInventoryData.get(5).inventory.toString())
        setInputEditText(holder.inventory7,room.roomTypeId.toString(),room.calculatedInventoryData.get(6).date,room.calculatedInventoryData.get(6).inventory.toString())
        setInputEditText(holder.inventory8,room.roomTypeId.toString(),room.calculatedInventoryData.get(7).date,room.calculatedInventoryData.get(7).inventory.toString())
        setInputEditText(holder.inventory9,room.roomTypeId.toString(),room.calculatedInventoryData.get(8).date,room.calculatedInventoryData.get(7).inventory.toString())
        setInputEditText(holder.inventory10,room.roomTypeId.toString(),room.calculatedInventoryData.get(9).date,room.calculatedInventoryData.get(9).inventory.toString())

        holder.recyclerView.layoutManager = LinearLayoutManager(context)



        progressDialog = showProgressDialog(context)
        getRates(position,room.roomTypeId,holder.recyclerView)
        progressDialog.dismiss()
    }

    private fun setInputEditText(input: TextInputEditText,roomTypeId: String,startDate: String,inventory:String) {
        input.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val updateInventory = p0.toString()
                if (updateInventory >= inventory){
                    val finalInventory = updateInventory.toInt() - inventory.toInt()
                            onRateTypeListChangeListener?.onRateTypeListChanged(roomTypeId,startDate,finalInventory.toString(),"false","true")
                }else{
                    val finalInventory = inventory.toInt() - updateInventory.toInt()
                    onRateTypeListChangeListener?.onRateTypeListChanged(roomTypeId,startDate,finalInventory.toString(),"true","false")
                }

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    private fun getRates(position: Int,roomTypeId:String,recyclerView: RecyclerView) {

        val userId = UserSessionManager(context).getUserId().toString()
        Log.e("userId",userId.toString())
        Log.e("roomTypeId",roomTypeId.toString())

        val getRate = OAuthClient<RatesAndInventoryInterface>(context).create(RatesAndInventoryInterface::class.java).getRates(roomTypeId,"2023-11-25","2023-12-04",userId)
        getRate.enqueue(object : Callback<RatesDataClass?> {
            override fun onResponse(call: Call<RatesDataClass?>, response: Response<RatesDataClass?>) {
                if (response.isSuccessful){
                    try {
                        val response = response.body()!!
                        ratePlanPricesAdapter = RatePlanPricesAdapter(context,response.data!!)
                        recyclerView.adapter = ratePlanPricesAdapter
                        Log.e("response",response.toString())
                        ratePlanPricesAdapter.notifyItemChanged(position)
                        progressDialog.dismiss()
                    }catch (e:IndexOutOfBoundsException){
                        Log.e("error",e.message.toString())
                    }

                }else{
                    Log.e("error",response.code().toString())
                    progressDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<RatesDataClass?>, t: Throwable) {
                Log.e("error",t.message.toString())
                progressDialog.dismiss()
            }
        })

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


