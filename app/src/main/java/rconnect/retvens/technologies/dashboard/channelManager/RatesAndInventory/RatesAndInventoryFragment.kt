package rconnect.retvens.technologies.dashboard.channelManager.RatesAndInventory

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListPopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.AddReservationApis
import rconnect.retvens.technologies.Api.DropDownApis
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.RatesAndInventoryInterface
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.Api.configurationApi.ChainConfiguration
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment.RatePlanDataClass
import rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment.RatePlanItem
import rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment.RateType
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.GetRoomType
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.GetRoomTypeData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.mealPlan.GetMealPlanData
import rconnect.retvens.technologies.databinding.FragmentRatesAndInventoryBinding
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.showProgressDialog
import rconnect.retvens.technologies.utils.utilCreateDatePickerDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class RatesAndInventoryFragment : Fragment() {

    private lateinit var bindingTab:FragmentRatesAndInventoryBinding
    private lateinit var calenderAdapter: CalenderAdapter
    private lateinit var inventoryAdapter: RoomsInventoryAdapter
    private val cal = Calendar.getInstance(Locale.ENGLISH)
    private  var mList: ArrayList<ResponseData> = ArrayList();
    lateinit var dialog: Dialog
    private lateinit var robotoMedium : Typeface
    private var ratePlans:ArrayList<RatePlanItem> = ArrayList()
    private lateinit var roboto:Typeface
    private var inventoryList:ArrayList<RoomType> = ArrayList()
    var isDismissAllowed = true
    lateinit var fri:TextView
    lateinit var sat:TextView
    lateinit var sun:TextView
    lateinit var mon:TextView
    lateinit var tues:TextView
    lateinit var wed:TextView
    lateinit var thur:TextView
    lateinit var roomId:String
    lateinit var updateinventory:String
    lateinit var apistartDate:String
    lateinit var txt_all_days:TextView
    lateinit var txt_week_days:TextView
    lateinit var txt_weekends:TextView
    lateinit var room_typeText:TextInputEditText
    lateinit var rate_planText:TextInputEditText
    private lateinit var progressDialog: Dialog
    lateinit var txt_custom:TextView
    lateinit var startDatePickerDialog:DatePickerDialog
    lateinit var endDatePickerDialog: DatePickerDialog
     var startDate:Date? = null
     var endDate:Date? = null
    private var checkInDate: String? = null
    private var checkOutDate: String? = null
    var isBlocked = ""
    var isAddedInt = ""
    var otaList = ArrayList<String>()
    var otaDialogList = ArrayList<String>()
    var basePrice = ""
    var baseAdult = ""
    var baseChild = ""
    var roomTypeList = ArrayList<String>()
    var roomTypeDialogList = ArrayList<String>()
    var roomTypeId = ""
    var ratePlanId = ""
    var temporaryRoomType = ArrayList<GetRoomType>()
    var ratePlansList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       bindingTab = FragmentRatesAndInventoryBinding.inflate(layoutInflater,container,false)
        return bindingTab.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        robotoMedium = ResourcesCompat.getFont(requireContext(),R.font.roboto_medium)!!
        roboto = ResourcesCompat.getFont(requireContext(),R.font.roboto)!!


        getOta()
        getRoomType()


        val rateOptions = arrayOf("Rates", "Stop sell", "COA","COD","Min Night","Extra Adult Rates","Extra Child Rates")

        val rateAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item1, rateOptions)

        // Set a click listener for the end icon
        bindingTab.ratesText.setOnClickListener {
            // Show dropdown menu
            showDropdownMenu(bindingTab.ratesText,rateAdapter,it)
        }

        val roomAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item1, temporaryRoomType)
       // Set a click listener for the end icon
        bindingTab.roomTypeText.setOnClickListener {
            Log.e("roomTypeText button","clicked")
            Log.e("roomList",temporaryRoomType.toString())
            // Show dropdown menu



            showRoomDropdownMenu(roomAdapter,it)
        }


        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item1, otaList)
        // Set a click listener for the end icon
        bindingTab.sourceText.setOnClickListener {
            // Show dropdown menu
            showDropdownMenu(bindingTab.sourceText,adapter,it)
        }

        bindingTab.calenderRecycler.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)

//        bindingTab.inventoryRecycler.setOnTouchListener(OnTouchListener { v, event -> true });

        bindingTab.bulkUpdateCard.setOnClickListener {
            OpenBulkUpdateDialog()
        }



        calenderAdapter = CalenderAdapter()
        setUpCalendar()


        bindingTab.calenderRecycler.adapter = calenderAdapter
        calenderAdapter.notifyDataSetChanged()


        bindingTab.inventoryRecycler.layoutManager = LinearLayoutManager(requireContext())
        inventoryAdapter = RoomsInventoryAdapter(requireContext(),inventoryList,"Rates")
        bindingTab.inventoryRecycler.adapter = inventoryAdapter

        val userId = UserSessionManager(requireContext()).getUserId()
        val propertyId = UserSessionManager(requireContext()).getPropertyId()
        progressDialog = showProgressDialog(requireContext())
        setInventory(userId,propertyId,checkInDate,checkOutDate)

        bindingTab.saveCard.setOnClickListener {
            progressDialog = showProgressDialog(requireContext())
            UpdateSingleInventory(roomId,apistartDate,updateinventory,isBlocked,isAddedInt)
        }

    }

    private fun OpenBulkUpdateDialog() {
        if (isDismissAllowed) {        // here i gave a condition
            isDismissAllowed = false
            dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.dilog_bulk_update)
            dialog.window?.apply {
                setBackgroundDrawableResource(android.R.color.transparent) // Makes the background transparent
                setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
            Handler().postDelayed(Runnable {
                isDismissAllowed = true
            }, 1000)
        }
        fri = dialog.findViewById<TextView>(R.id.fri)
        sun = dialog.findViewById<TextView>(R.id.sunday)
        mon = dialog.findViewById<TextView>(R.id.monday)
        tues = dialog.findViewById<TextView>(R.id.tuesday)
        wed = dialog.findViewById<TextView>(R.id.wed)
        thur = dialog.findViewById<TextView>(R.id.thursday)
        txt_all_days = dialog.findViewById<TextView>(R.id.txt_all_days)
        txt_weekends = dialog.findViewById<TextView>(R.id.txt_weekends)
        txt_week_days = dialog.findViewById<TextView>(R.id.txt_week_days)
        txt_custom = dialog.findViewById<TextView>(R.id.txt_custom)
        sat = dialog.findViewById<TextView>(R.id.saturday)



        unSelectAllDays()
        txt_all_days.setBackgroundResource(R.drawable.rounded_border_light_black)
        txt_weekends.setBackgroundResource(R.drawable.rounded_border_light_black)
        txt_week_days.setBackgroundResource(R.drawable.rounded_border_light_black)
        selectCard(txt_custom)
        txt_custom.setBackgroundResource(R.drawable.rounded_border_black)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        var isMon = false
        var isTues = false
        var isWed = false
        var isThur = false
        var frid = false
        var satu = true
        var isSun = false




        val from = dialog.findViewById<TextInputEditText>(R.id.from_Text)
        val fromLayout = dialog.findViewById<TextInputLayout>(R.id.from_Layout)
        val to = dialog.findViewById<TextInputEditText>(R.id.to_Text)
        val toLayout = dialog.findViewById<TextInputLayout>(R.id.to_Layout)

//                setDate(from)
//                setDate(to)

        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        cancel.setOnClickListener {
            startDate = null
            endDate = null
            dialog.cancel()
        }

        startDatePickerDialog = utilCreateDatePickerDialog(requireContext(),from) { date->
            startDate = date
            val checkInDate = ApiformatDate(date.toString())
        }

        endDatePickerDialog = utilCreateDatePickerDialog(requireContext(),to){date->
            val checkOutDate = ApiformatDate(date.toString())
            endDatePickerDialog.datePicker.minDate = startDate!!.time

            if (startDate!=null&&date.before(startDate)){
//                    isRightEndDate = false

                Toast.makeText(requireContext(), "End date cannot be before start date", Toast.LENGTH_SHORT).show()
//                    showToast("End date cannot be before start date")
//                    to_date.text = "--/--/----"
//                    Handler().postDelayed(Runnable {
//                        isRightEndDate = true
//                    },1000)
            }
            else{
//                    isRightEndDate = true
//                Toast.makeText(requireContext(), "Karan ji", Toast.LENGTH_SHORT).show()
                endDate = date
            }
        }


        endDatePickerDialog = createDatePickerDialog(to){date->
            if (startDate!=null){
                endDatePickerDialog.datePicker.minDate = startDate!!.time
                endDate = date

            }
        }



        fromLayout.setStartIconOnClickListener {
            endDate = null

            // Set the minimum date for the start date picker to be the current date
            startDatePickerDialog.datePicker.minDate = System.currentTimeMillis()
            startDatePickerDialog.show()
//            showCalendarDialog(requireContext(),from_date)
//            startDatePickerDialog.show()
            dialog.show()
        }

        toLayout.setStartIconOnClickListener {
            if (startDate!=null){
                endDatePickerDialog.datePicker.minDate = startDate!!.time
                endDatePickerDialog.show()
                dialog.show()
            }
        }

        fri.setOnClickListener {
            if (!frid) {
                selectCard(fri)
                frid = true
            } else {
                unSelectCard(fri)
                frid = false
            }
        }
        sat.setOnClickListener {
            if (!satu) {
                selectCard(sat)
                satu = true
            } else {
                unSelectCard(sat)
                satu = false
            }
        }
        sun.setOnClickListener {
            if (!isSun) {
                selectCard(sun)
                isSun = true
            } else {
                unSelectCard(sun)
                isSun = false
            }
        }
        mon.setOnClickListener {
            if (!isMon) {
                selectCard(mon)
                isMon = true
            } else {
                unSelectCard(mon)
                isMon = false
            }
        }
        tues.setOnClickListener {
            if (!isTues) {
                selectCard(tues)
                isTues = true
            } else {
                unSelectCard(tues)
                isTues = false
            }
        }
        wed.setOnClickListener {
            if (!isWed) {
                selectCard(wed)
                isWed = true
            } else {
                unSelectCard(wed)
                isWed = false
            }
        }
        thur.setOnClickListener {
            if (!isThur) {
                selectCard(thur)
                isThur = true
            } else {
                unSelectCard(thur)
                isThur = false
            }
        }

        var isAllDay = false
        var isweekend = false
        var isWeekDay = false
        var isCustom = true


        fun allClickable() {
            sun.isClickable = true
            mon.isClickable = true
            tues.isClickable = true
            wed.isClickable = true
            thur.isClickable = true
            fri.isClickable = true
            sat.isClickable = true
        }

        fun selectHead(head: TextView) {
            unSelectCard(txt_all_days)
            unSelectCard(txt_week_days)
            unSelectCard(txt_weekends)
            unSelectCard(txt_custom)

            head.typeface = robotoMedium
            selectCard(head)
        }


        txt_all_days.setOnClickListener {

            selectHead(txt_all_days)
            selectCard(sun)
            selectCard(mon)
            selectCard(tues)
            selectCard(wed)
            selectCard(thur)
            selectCard(fri)
            selectCard(sat)
            notClickable()
            isAllDay = true

        }
        txt_weekends.setOnClickListener {

            selectHead(txt_weekends)
            unSelectAllDays()
            selectCard(sat)
            selectCard(sun)
            isweekend = true
            notClickable()

        }
        txt_week_days.setOnClickListener {

            selectHead(txt_week_days)
            unSelectAllDays()

            selectCard(mon)
            selectCard(tues)
            selectCard(wed)
            selectCard(thur)
            selectCard(fri)
            isWeekDay = true

        }
        txt_custom.setOnClickListener {

            selectHead(txt_custom)
            unSelectAllDays()
            allClickable()
            isCustom = true
        }

        val add_inventory = dialog.findViewById<ImageView>(R.id.add_inventory)
        val blockRoomCard = dialog.findViewById<MaterialCardView>(R.id.block_room)
        val addRoomCard = dialog.findViewById<MaterialCardView>(R.id.add_room_card)
        val sourceText = dialog.findViewById<TextInputEditText>(R.id.sourceText)
        val room_typeText = dialog.findViewById<TextInputEditText>(R.id.room_typeText)
        val rate_planText = dialog.findViewById<TextInputEditText>(R.id.rate_planText)
        val ratePlanLayout = dialog.findViewById<TextInputLayout>(R.id.rate_planLayout)
        val roomTypeLayout = dialog.findViewById<TextInputLayout>(R.id.room_typeLayout)
        val baseRate = dialog.findViewById<EditText>(R.id.baseRatePrice)
        val baseAdultRate = dialog.findViewById<EditText>(R.id.baseAdultPrice)
        val baseChildRate = dialog.findViewById<EditText>(R.id.baseChildPrice)
        val saveCard = dialog.findViewById<CardView>(R.id.continueBtn)
        val blockRoom = dialog.findViewById<EditText>(R.id.CountBlockRoom)
        val addRoom = dialog.findViewById<EditText>(R.id.count_AddRoom)


        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item1, otaDialogList)
        // Set a click listener for the end icon
        sourceText.setOnClickListener {
            // Show dropdown menu
            showDropdownMenu(sourceText,adapter,it)
        }

        val adapter2 = ArrayAdapter(requireContext(), R.layout.simple_spinner_item1, temporaryRoomType)
        // Set a click listener for the end icon
        room_typeText.setOnClickListener {
            Log.e("dialog","drop")
            showRoomDropdownMenuDialog(adapter2,it,roomTypeLayout)
        }
        val adapter3 = ArrayAdapter(requireContext(), R.layout.simple_spinner_item1, ratePlans)
        // Set a click listener for the end icon
        rate_planText.setOnClickListener {
            // Show dropdown menu
            showRatePlanDropdownMenuDialog(adapter3,it,ratePlanLayout,baseRate,baseAdultRate,baseChildRate)
        }

        var isInventoryOpen = false
        add_inventory.setOnClickListener {
            if (!isInventoryOpen) {
                add_inventory.setImageResource(R.drawable.svg_arrow_down)
                blockRoomCard.isVisible = true
                addRoomCard.isVisible = true
                isInventoryOpen = true
            } else {
                add_inventory.setImageResource(R.drawable.svg_add_icon)
                blockRoomCard.isVisible = false
                addRoomCard.isVisible = false
                isInventoryOpen = false
            }

        }
        val add_rates = dialog.findViewById<ImageView>(R.id.add_rate)
        val ll_rates = dialog.findViewById<LinearLayout>(R.id.ll_rate)
        var isRateOpen = false
        add_rates.setOnClickListener {
            if (!isRateOpen) {
                add_rates.setImageResource(R.drawable.svg_arrow_down)
                ll_rates.isVisible = true
                isRateOpen = true
            } else {
                add_rates.setImageResource(R.drawable.svg_add_icon)
                ll_rates.isVisible = false
                isRateOpen = false
            }

        }
        val add_restriction = dialog.findViewById<ImageView>(R.id.add_restriction)
        val ll_restriction = dialog.findViewById<LinearLayout>(R.id.ll_restriction)
        var isRestrictionOpen = false
        add_restriction.setOnClickListener {
            if (!isRestrictionOpen) {
                add_restriction.setImageResource(R.drawable.svg_arrow_down)
                ll_restriction.isVisible = true
                isRestrictionOpen = true
            } else {
                add_restriction.setImageResource(R.drawable.svg_add_icon)
                ll_restriction.isVisible = false
                isRestrictionOpen = false
            }

        }


        saveCard.setOnClickListener {
            progressDialog = showProgressDialog(requireContext())
            updateBulkinventory(sourceText,room_typeText,rate_planText,checkInDate!!,checkOutDate!!,blockRoom,addRoom)
        }

    }

    private fun updateBulkinventory(source:TextInputEditText,roomType:TextInputEditText,ratePlan:TextInputEditText,startDate:String,endDate:String,block:EditText,add:EditText) {

        val bulkUpdate = OAuthClient<RatesAndInventoryInterface>(requireContext()).create(RatesAndInventoryInterface::class.java).updateBulkInventory(
            UpdateBulkInventory(
                UserSessionManager(requireContext()).getUserId().toString(),
                UserSessionManager(requireContext()).getPropertyId().toString(),
                roomTypeId,
                startDate,
                endDate,
                true,
                false,
                add.text.toString(),
                source.text.toString()
            )
        )
        bulkUpdate.enqueue(object : Callback<rconnect.retvens.technologies.onboarding.ResponseData?> {
            override fun onResponse(
                call: Call<rconnect.retvens.technologies.onboarding.ResponseData?>,
                response: Response<rconnect.retvens.technologies.onboarding.ResponseData?>
            ) {
                if (response.isSuccessful){
                    progressDialog.dismiss()
                    val response = response.body()!!
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
                    val userId = UserSessionManager(requireContext()).getUserId()
                    val propertyId = UserSessionManager(requireContext()).getPropertyId()
                    progressDialog = showProgressDialog(requireContext())
                    dialog.dismiss()
                    setInventory(userId,propertyId,checkInDate,checkOutDate)
                }else{
                    progressDialog.dismiss()
                }
            }

            override fun onFailure(
                call: Call<rconnect.retvens.technologies.onboarding.ResponseData?>,
                t: Throwable
            ) {
                progressDialog.dismiss()
            }
        })

    }

    private fun UpdateSingleInventory(
        roomId: String,
        apistartDate: String,
        updateinventory: String,
        isBlock:String,
        isAdd:String
    ) {

        val body = UpdateSingleInventory(UserSessionManager(requireContext()).getUserId().toString(),
            UserSessionManager(requireContext()).getPropertyId().toString(),
            roomId,apistartDate,apistartDate,isAdd.toBoolean(),isBlock.toBoolean(),updateinventory,"OTA Common Pool")

        val updateInventory = OAuthClient<RatesAndInventoryInterface>(requireContext()).create(RatesAndInventoryInterface::class.java).updateInventory(body)

       updateInventory.enqueue(object : Callback<rconnect.retvens.technologies.onboarding.ResponseData?> {
           override fun onResponse(
               call: Call<rconnect.retvens.technologies.onboarding.ResponseData?>,
               response: Response<rconnect.retvens.technologies.onboarding.ResponseData?>
           ) {
               if (response.isSuccessful){
                   progressDialog.dismiss()
                   val response = response.body()!!
                   Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
                   val userId = UserSessionManager(requireContext()).getUserId()
                   val propertyId = UserSessionManager(requireContext()).getPropertyId()
                   progressDialog = showProgressDialog(requireContext())
                   setInventory(userId,propertyId,checkInDate,checkOutDate)
               }else{
                   progressDialog.dismiss()
               }
           }

           override fun onFailure(
               call: Call<rconnect.retvens.technologies.onboarding.ResponseData?>,
               t: Throwable
           ) {
              progressDialog.dismiss()
           }
       })
    }

    private fun getRatePlans() {
        val retrofit = OAuthClient<DropDownApis>(requireContext()).create(DropDownApis::class.java).getRatePlans(UserSessionManager(requireContext()).getUserId().toString(),roomTypeId)
        retrofit.enqueue(object : Callback<GetRatePlansData?> {
            override fun onResponse(
                call: Call<GetRatePlansData?>,
                response: Response<GetRatePlansData?>
            ) {
                if (response.isSuccessful){
                    try {
                    val data = response.body()!!.data
                    data.forEach {
                        ratePlansList.add(it.ratePlanName)
                    }
                } catch (e: NullPointerException){
                    e.printStackTrace()
                }
                }
                else{
                    Log.e("ratePlans",response.message().toString())
                }
            }

            override fun onFailure(call: Call<GetRatePlansData?>, t: Throwable) {

            }
        })

    }

    private fun showDropdownMenu(et:TextInputEditText,adapter: ArrayAdapter<String>, anchorView: View) {
        val listPopupWindow = ListPopupWindow(requireContext())
        listPopupWindow.setAdapter(adapter)
        listPopupWindow.anchorView = anchorView
        listPopupWindow.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = adapter.getItem(position)
            et.setText(selectedItem)
            inventoryAdapter = RoomsInventoryAdapter(requireContext(),inventoryList,selectedItem.toString())
            bindingTab.inventoryRecycler.adapter = inventoryAdapter
            inventoryAdapter.notifyDataSetChanged()
            listPopupWindow.dismiss()
        }


        listPopupWindow.show()
    }

    private fun showRoomDropdownMenu(adapter: ArrayAdapter<GetRoomType>, it: View?) {
        val listPopupWindow = ListPopupWindow(requireContext())

        // Create a custom adapter to display only the rateType property
        val customAdapter = object : ArrayAdapter<GetRoomType>(requireContext(), R.layout.simple_dropdown_item_1line, temporaryRoomType) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val rateType = getItem(position)?.roomTypeName

                (view as TextView).text = rateType
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val rateType = getItem(position)?.roomTypeName
                (view as TextView).text = rateType
                return view
            }
        }

        listPopupWindow.setAdapter(customAdapter)

        listPopupWindow.anchorView = it
        listPopupWindow.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = customAdapter.getItem(position)
            bindingTab.roomTypeLayout.editText?.setText(selectedItem?.roomTypeName)
            roomTypeId = selectedItem?.roomTypeId.toString()
            val filteredData = inventoryList.filter {
                it.roomTypeName.contains(selectedItem!!.roomTypeName, true)
            }
            inventoryAdapter.filterList(filteredData)
            listPopupWindow.dismiss()
        }

        listPopupWindow.show()
    }

    private fun showRoomDropdownMenuDialog(adapter: ArrayAdapter<GetRoomType>, it: View?,view: TextInputLayout) {
        val listPopupWindow = ListPopupWindow(requireContext())

        // Create a custom adapter to display only the rateType property
        val customAdapter = object : ArrayAdapter<GetRoomType>(requireContext(), R.layout.simple_dropdown_item_1line, temporaryRoomType) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val rateType = getItem(position)?.roomTypeName

                (view as TextView).text = rateType
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val rateType = getItem(position)?.roomTypeName
                (view as TextView).text = rateType
                return view
            }
        }

        listPopupWindow.setAdapter(customAdapter)

        listPopupWindow.anchorView = it
        listPopupWindow.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = customAdapter.getItem(position)
            view.editText?.setText(selectedItem?.roomTypeName)
            roomTypeId = selectedItem?.roomTypeId.toString()
            listPopupWindow.dismiss()
            getRatePlanList(roomTypeId)
        }

        listPopupWindow.show()
    }


    private fun showRatePlanDropdownMenuDialog(adapter: ArrayAdapter<RatePlanItem>, it: View?,view: TextInputLayout,baseRate:EditText,baseAdult:EditText,baseChild:EditText) {
        val listPopupWindow = ListPopupWindow(requireContext())

        // Create a custom adapter to display only the rateType property
        val customAdapter = object : ArrayAdapter<RatePlanItem>(requireContext(), R.layout.simple_dropdown_item_1line, ratePlans) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val rateType = getItem(position)?.ratePlanName

                (view as TextView).text = rateType
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val rateType = getItem(position)?.ratePlanName
                (view as TextView).text = rateType
                return view
            }
        }

        listPopupWindow.setAdapter(customAdapter)

        listPopupWindow.anchorView = it
        listPopupWindow.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = customAdapter.getItem(position)
            view.editText?.setText(selectedItem?.ratePlanName)
            ratePlanId = selectedItem?.roomTypeId.toString()
            baseRate.setText( selectedItem?.ratePlanTotal.toString())
            baseAdult.setText(selectedItem?.extraAdultRate.toString())
            baseChild.setText(selectedItem?.extraChildRate.toString())
            listPopupWindow.dismiss()
            getRatePlanList(roomTypeId)
        }

        listPopupWindow.show()
    }

    private fun getRatePlanList(roomTypeId: String) {

        val getRoomList = OAuthClient<AddReservationApis>(requireContext()).create(AddReservationApis::class.java).getRatePlan(roomTypeId,UserSessionManager(requireContext()).getUserId().toString())
        getRoomList.enqueue(object : Callback<RatePlanDataClass?> {
            override fun onResponse(
                call: Call<RatePlanDataClass?>,
                response: Response<RatePlanDataClass?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    ratePlans.clear()
                    ratePlans.addAll(response.data)
                }else{
                    Log.e("error",response.code().toString())
                }
            }
            override fun onFailure(call: Call<RatePlanDataClass?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }
        })
    }


    private fun getOta() {
        val retrofit = OAuthClient<DropDownApis>(requireContext()).create(DropDownApis::class.java).getOtaList( UserSessionManager(requireContext()).getUserId().toString(),UserSessionManager(requireContext()).getPropertyId().toString())
        retrofit.enqueue(object : Callback<GetOtaSourceData?> {
            override fun onResponse(
                call: Call<GetOtaSourceData?>,
                response: Response<GetOtaSourceData?>
            ) {
                if (response.isSuccessful){
                    val data = response.body()!!.data
                    data.forEach {
                        otaList.add(it.otaName)
                        otaDialogList.add(it.otaName)
                    }
                }
                else{
                    Toast.makeText(requireContext(), "failed", Toast.LENGTH_SHORT).show()
                    Log.e("error",response.message().toString())

                }
            }

            override fun onFailure(call: Call<GetOtaSourceData?>, t: Throwable) {
                Log.e("failure",t.message.toString())
            }
        })
    }

    private fun getRoomType() {
        val retrofit = OAuthClient<DropDownApis>(requireContext()).create(DropDownApis::class.java).getRoomList( UserSessionManager(requireContext()).getPropertyId().toString(),UserSessionManager(requireContext()).getUserId().toString())
        retrofit.enqueue(object : Callback<GetRoomTypeData?> {
            override fun onResponse(
                call: Call<GetRoomTypeData?>,
                response: Response<GetRoomTypeData?>
            ) {
                if (response.isSuccessful){

                    Log.e("response",response.body().toString())
                    val data = response.body()!!.data
                    temporaryRoomType.clear()
                    temporaryRoomType.addAll(data)
                }
                else{

                    Log.e("error",response.message().toString())
                    Log.e("error",response.body().toString())
                }
            }

            override fun onFailure(call: Call<GetRoomTypeData?>, t: Throwable) {
                Toast.makeText(requireContext(), "Mission failed SuccessFully", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun selectCard(day: TextView?) {
            day?.setBackgroundResource(R.drawable.rounded_border_black)
            day?.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
    }
     private fun unSelectCard(day: TextView?) {
            day?.setBackgroundResource(R.drawable.rounded_border_light_black)
            day?.setTextColor(ContextCompat.getColor(requireContext(),R.color.lightBlack))
        day?.typeface = roboto
    }
    private fun unSelectAllDays() {
        unSelectCard(sun)
        unSelectCard(mon)
        unSelectCard(tues)
        unSelectCard(wed)
        unSelectCard(thur)
        unSelectCard(fri)
        unSelectCard(sat)
    }
    private fun notClickable() {
        sun.isClickable = false
        mon.isClickable = false
        tues.isClickable = false
        wed.isClickable = false
        thur.isClickable = false
        fri.isClickable = false
        sat.isClickable = false
    }

    private fun setInventory(userId:String?,propertyId:String?,startDate:String?,endDate:String?) {

        Log.e("token",UserSessionManager(requireContext()).getToken().toString())

        val inventoryApi = OAuthClient<RatesAndInventoryInterface>(requireContext()).create(RatesAndInventoryInterface::class.java).getInventory(userId.toString(),propertyId.toString(),startDate.toString(),endDate.toString())

        inventoryApi.enqueue(object : Callback<ResponseData?>,
           RoomsInventoryAdapter.OnRateTypeListChangeListener {
           override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
               if (response.isSuccessful && isAdded){
                   val response = response.body()!!
                   Log.e("res",response.toString())
                   inventoryList.addAll(response.data)
                   inventoryAdapter.notifyDataSetChanged()
                   inventoryAdapter.setOnListUpdateListener(this)
                   progressDialog.dismiss()
               }else{
                   Log.e("error",response.code().toString())
                   Log.e("message",response.message().toString())
                   progressDialog.dismiss()
               }
           }
           override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
               Log.e("error",t.message.toString())
               progressDialog.dismiss()
           }

           override fun onRateTypeListChanged(
               roomTypeId: String,
               startDate: String,
               inventory: String,
               isBlock: String,
               isAdd: String
           ) {
               Log.e("finalInventory",inventory.toString())
               Log.e("isBlock",isBlock.toString())
               Log.e("isAdd",isAdd.toString())
               apistartDate = startDate
               roomId = roomTypeId
               updateinventory = inventory
               isBlocked = isBlock
               isAddedInt = isAdd
           }


       })


    }

    private fun setUpCalendar() {
        val calendarList = ArrayList<Calendar>()
        val today = Calendar.getInstance(Locale.ENGLISH)

        // Add the current date as the start date
        checkInDate = formatDate(today)

        // Add the current date
        calendarList.add(today.clone() as Calendar)

        // Get the next 9 days
        for (i in 1 until 10) {
            val nextDay = today.clone() as Calendar
            nextDay.add(Calendar.DAY_OF_MONTH, i)
            calendarList.add(nextDay)
        }

        // Calculate the end date
        checkOutDate = formatDate(calendarList[calendarList.size - 1])

        calenderAdapter.setData(calendarList)
        Log.d("CalendarValue", "CalenderAdapter data updated with ${calendarList.size} days")

        calenderAdapter.notifyDataSetChanged() // Notify the adapter after all changes
    }

    // Function to format a Calendar instance as "YYYY-MM-DD"
    fun formatDate(calendar: Calendar): String {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // Months are zero-based
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Use String.format to format the date as "YYYY-MM-DD"
        return String.format("%d-%02d-%02d", year, month, day)
    }
    fun showCalendarDialog(context : Context, editTextDate: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                // Set the selected date on the EditText
                val selectedDate = "$year-${month + 1}-$dayOfMonth"
                editTextDate.setText(selectedDate)
            },
            currentYear,
            currentMonth,
            currentDay
        )
        datePickerDialog.setCancelable(false)

        datePickerDialog.show()
    }

    fun setDate(edt:TextInputEditText){
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val selectedDate = "$currentYear-${currentMonth + 1}-$currentDay"
        edt.setText(selectedDate)

    }
    private fun createDatePickerDialog(textDate:TextView,onDateSetListener: (Date) -> Unit): DatePickerDialog {
        val calendar = Calendar.getInstance()
        return DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                // Create a Date object from the selected date
                calendar.set(year, month, dayOfMonth)
                val selectedDate = calendar.time
                // Invoke the provided listener
                onDateSetListener.invoke(selectedDate)
                // Set the selected date on the EditText
                    val selectedDate2 = "$dayOfMonth/${month+1}/$year"
                    textDate.text = selectedDate2

//                if (textDate==from_date){
//                    startDate = selectedDate
//                }
//                else{
//                    endDate = selectedDate
//                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }


    fun ApiformatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

        try {
            val date = inputFormat.parse(inputDate)
            return outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return inputDate // Return the original date if parsing fails
    }

}