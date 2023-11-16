package rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment

import android.content.Context
import android.os.TestLooperManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListPopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.w3c.dom.Text
import rconnect.retvens.technologies.Api.AddReservationApis
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.utils.UserSessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddReservationAdapter(val applicationContext: Context,val reservationList:ArrayList<RoomDetail>,val availableList:ArrayList<AvailableRoomType>):RecyclerView.Adapter<AddReservationAdapter.ReservationHolder>() {
    var cc = 1
    var ac = 1
    var isCharge = false
    private var roomDetailList:ArrayList<RoomDetail> = ArrayList()
    private var roomTypeList:ArrayList<RoomItem> = ArrayList()
    private var ratePlanList:ArrayList<RatePlanItem> = ArrayList()
    private var roomDetailPricess:ArrayList<RoomDetailPrices> = ArrayList()
    var roomTypeId:String = ""
    var userId:String = ""
    var propertyId:String = ""
    var clickListener : OnItemClick ?= null


    fun setOnClickListener (listener : OnItemClick){
        clickListener = listener
    }
    interface OnItemClick{
        fun onItemDelete(count:String)
        fun updateRates()
    }

    init {
        // Initialize roomDetailList with the initial values from reservationList
        userId = UserSessionManager(applicationContext).getUserId().toString()
        propertyId = UserSessionManager(applicationContext).getPropertyId().toString()
    }

    class ReservationHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {
        val grandTotal:TextView = itemView.findViewById(R.id.total_price)
        val delete:ImageView = itemView.findViewById(R.id.img_del)
        val room_count:TextView = itemView.findViewById(R.id.item_room_count)
        val addChild:ImageView = itemView.findViewById(R.id.addChild)
        val removeChild:ImageView = itemView.findViewById(R.id.removeChild)
        val countChild:TextView = itemView.findViewById(R.id.item_child_count)
        val addDuplicate:ImageView = itemView.findViewById(R.id.img_copy)
        val addAdult:ImageView = itemView.findViewById(R.id.addAdult)
        val removeAdult:ImageView = itemView.findViewById(R.id.removeAdult)
        val countAdult:TextView = itemView.findViewById(R.id.item_adult_count)
        val roomTypeLayout:TextInputLayout = itemView.findViewById(R.id.RoomType_layout)
        val roomTypeText:TextInputEditText = itemView.findViewById(R.id.roomType_text)
        val drop:ImageView = itemView.findViewById(R.id.drop)
        val chargesDetails:ConstraintLayout = itemView.findViewById(R.id.charges_details)
        val ratePlanLayout:TextInputLayout = itemView.findViewById(R.id.RatePlan_layout)
        val ratePlanText:TextInputEditText = itemView.findViewById(R.id.ratePlan_text)
        val chargesPrice:TextView = itemView.findViewById(R.id.charges_price)
        val roomCharges:TextView = itemView.findViewById(R.id.room_charge)
        val extraAdultCharges:TextView = itemView.findViewById(R.id.ex_ad_charge)
        val extraChildCharges:TextView = itemView.findViewById(R.id.ex_cc_charge)
        val extraInclusionCharges:TextView = itemView.findViewById(R.id.ex_in_charge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationHolder {
        return ReservationHolder(LayoutInflater.from(applicationContext).inflate(R.layout.item_recycler_room_details,parent,false))
    }

    override fun getItemCount(): Int {
        return reservationList.size
    }


    fun addItem() {

        // Create a new RoomDetails object or obtain it from your data source
        val emptyRoomDetails = RoomDetail(
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
        )

        val emptyPrices = RoomDetailPrices(0,0,0,0,0,0,0)

        // Add the new item to the list
        reservationList.add(emptyRoomDetails)
        roomDetailList.add(emptyRoomDetails)
        roomDetailPricess.add(emptyPrices)
        // Notify the adapter about the change
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ReservationHolder, position: Int) {


        val roomDetails = roomDetailList[position]
        val roomDetailPrices = roomDetailPricess[position]

        holder.room_count.text = "Room ${position+1}"
        holder.delete.setOnClickListener {
            if (position >= 1 && position < reservationList.size && position < roomDetailList.size) {
                reservationList.removeAt(position)
                roomDetailList.removeAt(position)
                notifyDataSetChanged()
                clickListener?.onItemDelete(reservationList.size.toString())
            } else {
                // Handle the case where position is out of bounds
                // You can show a message or perform some other action
                // For example, you can display a Toast message:

            }
        }

        if (roomDetails.charge != ""){
            var totalCharges = roomDetailPrices.totalCharges
            totalCharges += roomDetailPrices.totalPriceForExtraAdult + roomDetailPrices.totalPriceForExtraChild
            holder.grandTotal.text = "₹${totalCharges}.00"
            roomDetails.charge = totalCharges.toString()
            clickListener?.updateRates()
        }



        holder.addChild.setOnClickListener {
            cc++
            holder.countChild.text = "$cc"
            roomDetails.childs = cc.toString()

            // Calculate the increase in child count
            val increaseInChildCount = cc - roomDetailPrices.baseChildCount.toInt()

            // Calculate the total price for extra children
            roomDetailPrices.totalPriceForExtraChild = increaseInChildCount * roomDetailPrices.pricePerExtraChild.toInt()

            // Update roomDetails.extraChild with the calculated total price
            holder.extraChildCharges.text = "₹${roomDetailPrices.totalPriceForExtraChild}.00"
            roomDetails.extraChild = roomDetailPrices.totalPriceForExtraChild.toString()

            notifyDataSetChanged()
        }
        holder.removeChild.setOnClickListener {
            if (cc > roomDetailPrices.baseChildCount.toInt()) {
                cc--
                holder.countChild.text = "$cc"
                roomDetails.childs = cc.toString()

                // Calculate the decrease in child count, ensuring it's non-negative
                val decreaseInChildCount = maxOf(0, cc - roomDetailPrices.baseChildCount.toInt())

                // Calculate the total price for reduced extra children
                roomDetailPrices.totalPriceForExtraChild = decreaseInChildCount * roomDetailPrices.pricePerExtraChild.toInt()
                // Update roomDetails.extraChild with the calculated total price
                holder.extraChildCharges.text = "₹ ${roomDetailPrices.totalPriceForExtraChild.toString()}.00"

                notifyDataSetChanged()
            }
        }



        holder.addAdult.setOnClickListener {
            ac++
            holder.countAdult.text = "$ac"
            roomDetails.adults = ac.toString()

            // Calculate the increase in adult count
            val increaseInAdultCount = ac - roomDetailPrices.baseAdultCount.toInt()

            // Calculate the total price for extra adults
            roomDetailPrices.totalPriceForExtraAdult = increaseInAdultCount * roomDetailPrices.pricePerExtraAdult.toInt()

            // Update roomDetails.extraAdult with the calculated total price
            holder.extraAdultCharges.text = "₹${roomDetailPrices.totalPriceForExtraAdult}.00"
            roomDetails.extraAdult = roomDetailPrices.totalPriceForExtraAdult.toString()

           notifyDataSetChanged()

        }
        holder.removeAdult.setOnClickListener {
            if (ac > roomDetailPrices.baseAdultCount.toInt()) {
                ac--
                holder.countAdult.text = "$ac"
                roomDetails.adults = ac.toString()

                // Calculate the decrease in adult count, ensuring it's non-negative
                val decreaseInAdultCount = maxOf(0, ac - roomDetailPrices.baseAdultCount.toInt())

                // Calculate the total price for reduced extra adults
                roomDetailPrices.totalPriceForExtraAdult = decreaseInAdultCount * roomDetailPrices.pricePerExtraAdult.toInt()

                holder.extraAdultCharges.text = "₹${roomDetailPrices.totalPriceForExtraAdult}.00"

                notifyDataSetChanged()
            }
        }
        holder.drop.setOnClickListener {
            if (isCharge){
                holder.chargesDetails.isVisible = false
                holder.drop.rotation = 0f
                isCharge = false
            }
            else{
                holder.chargesDetails.isVisible = true
                holder.drop.rotation = 180f
                isCharge= true
            }
        }

        holder.addDuplicate.setOnClickListener {
            val positionClicked = holder.adapterPosition

            // Check if the clicked position is valid and there is at least one item in the list
            if (positionClicked in 0 until roomDetailList.size) {
                val itemToDuplicate = roomDetailList[positionClicked]
                val itemToPrice = roomDetailPricess[positionClicked]

                // Create a duplicate item using the copy method (assuming your RoomDetails class has a copy method)
                val duplicatedItem = itemToDuplicate.copy()
                val duplicatePrice = itemToPrice.copy()

                // Add the duplicated item to both lists
                reservationList.add(positionClicked + 1, duplicatedItem)
                roomDetailList.add(positionClicked + 1, duplicatedItem)  // Fix this line
                roomDetailPricess.add(positionClicked + 1, duplicatePrice)  // Fix this line

                // Notify the adapter about the change
                notifyItemInserted(positionClicked + 1)
            }
        }

        val roomTypeAdapter = ArrayAdapter(applicationContext,R.layout.simple_dropdown_item_1line,availableList)

        holder.roomTypeText.setOnClickListener {
            showRoomTypeDropDown(roomTypeAdapter,it,holder.roomTypeLayout,holder.countAdult,holder.countChild,holder.position)
        }

        val ratePlanAdapter = ArrayAdapter(applicationContext,R.layout.simple_dropdown_item_1line,ratePlanList)


        holder.ratePlanText.setOnClickListener {
            showRatePlan(ratePlanAdapter,it,holder.ratePlanLayout,holder.chargesPrice,holder.position,holder.grandTotal,holder.roomCharges)
        }

    }

    private fun showRatePlan(
        ratePlanAdapter: ArrayAdapter<RatePlanItem>,
        it: View?,
        ratePlanLayout: TextInputLayout,
        charges:TextView,
        positions: Int,
        total:TextView,
        roomCharges:TextView
    ) {
        val listPopupWindow = ListPopupWindow(applicationContext)

        // Create a custom adapter to display only the rateType property
        val customAdapter = object : ArrayAdapter<RatePlanItem>(applicationContext, R.layout.simple_dropdown_item_1line, ratePlanList) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val ratePlan = getItem(position)?.ratePlanName
                (view as TextView).text = ratePlan
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val ratePlan = getItem(position)?.ratePlanName
                (view as TextView).text = ratePlan
                return view
            }
        }

        listPopupWindow.setAdapter(customAdapter)

        listPopupWindow.anchorView = it
        listPopupWindow.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = customAdapter.getItem(position)
            ratePlanLayout.editText?.setText(selectedItem?.ratePlanName)
            charges.text = selectedItem?.ratePlanTotal
            roomCharges.text = "₹ ${selectedItem?.ratePlanTotal}"
            roomDetailList[positions].charge = selectedItem?.ratePlanTotal.toString()
            roomDetailPricess[positions].pricePerExtraAdult = selectedItem?.extraAdultRate!!.toInt()
            roomDetailPricess[positions].pricePerExtraChild = selectedItem?.extraChildRate!!.toInt()
            roomDetailPricess[positions].totalCharges = selectedItem?.ratePlanTotal!!.toInt()
            total.text = "₹ ${selectedItem?.ratePlanTotal}"
            listPopupWindow.dismiss()
            clickListener?.updateRates()
        }

            listPopupWindow.show()

    }



    private fun showRoomTypeDropDown(roomTypeAdapter: ArrayAdapter<AvailableRoomType>, it: View?,view:TextInputLayout,adult:TextView,child:TextView,positions: Int) {
        val listPopupWindow = ListPopupWindow(applicationContext)

        // Create a custom adapter to display only the rateType property
        val customAdapter = object : ArrayAdapter<AvailableRoomType>(applicationContext, R.layout.simple_dropdown_item_1line, availableList) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val roomType = getItem(position)?.roomTypeName
                (view as TextView).text = roomType
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val roomType = getItem(position)?.roomTypeName
                (view as TextView).text = roomType
                return view
            }
        }

        listPopupWindow.setAdapter(customAdapter)

        listPopupWindow.anchorView = it
        listPopupWindow.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = customAdapter.getItem(position)
            view.editText?.setText(selectedItem?.roomTypeName)
            roomTypeId = selectedItem?.roomTypeId.toString()
            adult.text = selectedItem?.baseAdult
            cc = selectedItem?.baseChild!!.toInt()
            ac = selectedItem?.baseAdult!!.toInt()
            child.text = selectedItem?.baseChild
            roomDetailList[positions].ratePlan = selectedItem?.roomTypeName.toString()
            roomDetailList[positions].adults = selectedItem?.baseAdult.toString()
            roomDetailList[positions].childs = selectedItem?.baseChild.toString()
            roomDetailList[positions].roomTypeId = selectedItem?.roomTypeId.toString()
            roomDetailPricess[positions].baseChildCount = selectedItem.baseChild.toInt()
            roomDetailPricess[positions].baseAdultCount = selectedItem.baseAdult.toInt()
            listPopupWindow.dismiss()
            getRatePlan()
        }

        listPopupWindow.show()
    }



    fun getRatePlan(){
        Log.e("roomTypeId",roomTypeId.toString())
        Log.e("userId",userId.toString())
        Log.e("token",UserSessionManager(applicationContext).getToken().toString())
        val getRatePlan =  OAuthClient<AddReservationApis>(applicationContext).create(AddReservationApis::class.java).getRatePlan(roomTypeId,userId)

        getRatePlan.enqueue(object : Callback<RatePlanDataClass?> {
            override fun onResponse(
                call: Call<RatePlanDataClass?>,
                response: Response<RatePlanDataClass?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    ratePlanList.clear()
                    ratePlanList.addAll(response.data)
                }else{
                    Log.e("error",response.code().toString())
                    ratePlanList.clear()
                    Toast.makeText(applicationContext,"No Rate Plan Found",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RatePlanDataClass?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }
        })

    }

}