package rconnect.retvens.technologies.dashboard.channelManager.RatesAndInventory

import android.app.DatePickerDialog
import android.app.Dialog
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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.FragmentRatesAndInventoryBinding
import rconnect.retvens.technologies.utils.utilCreateDatePickerDialog
import java.util.Calendar
import java.util.Date
import java.util.Locale


class RatesAndInventoryFragment : Fragment() {

    private lateinit var bindingTab:FragmentRatesAndInventoryBinding
    private lateinit var calenderAdapter: CalenderAdapter
    private lateinit var inventoryAdapter: RoomsInventoryAdapter
    private val cal = Calendar.getInstance(Locale.ENGLISH)
    private  var mList: ArrayList<String> = ArrayList();
    lateinit var dialog: Dialog
    private lateinit var robotoMedium : Typeface
    private lateinit var roboto:Typeface
    var isDismissAllowed = true
    lateinit var fri:TextView
    lateinit var sat:TextView
    lateinit var sun:TextView
    lateinit var mon:TextView
    lateinit var tues:TextView
    lateinit var wed:TextView
    lateinit var thur:TextView
    lateinit var txt_all_days:TextView
    lateinit var txt_week_days:TextView
    lateinit var txt_weekends:TextView

    lateinit var txt_custom:TextView
    lateinit var startDatePickerDialog:DatePickerDialog
    lateinit var endDatePickerDialog: DatePickerDialog
     var startDate:Date? = null
     var endDate:Date? = null



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

        bindingTab.calenderRecycler.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)

//        bindingTab.inventoryRecycler.setOnTouchListener(OnTouchListener { v, event -> true });

        bindingTab.bulkUpdateCard.setOnClickListener {
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
            }

            endDatePickerDialog = utilCreateDatePickerDialog(requireContext(),to){date->

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


        }



        calenderAdapter = CalenderAdapter()
        setUpCalendar()


        bindingTab.calenderRecycler.adapter = calenderAdapter
        calenderAdapter.notifyDataSetChanged()


        bindingTab.inventoryRecycler.layoutManager = LinearLayoutManager(requireContext())
        inventoryAdapter = RoomsInventoryAdapter(requireContext(),mList)


        setInventory()
        bindingTab.inventoryRecycler.adapter = inventoryAdapter
        inventoryAdapter.notifyDataSetChanged()



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

    private fun setInventory() {
        mList.add("7")
        mList.add("7")
        mList.add("7")
        mList.add("6")
        mList.add("6")
        mList.add("6")
        mList.add("6")
        mList.add("6")
        mList.add("6")
        mList.add("6")
        mList.add("6")
        mList.add("7")
        mList.add("7")
        mList.add("7")
        mList.add("6")
        mList.add("6")
        mList.add("6")
        mList.add("6")
        mList.add("6")
        mList.add("6")
        mList.add("6")
        mList.add("6")

    }

    private fun setUpCalendar() {
        val calendarList = ArrayList<Calendar>()
        val today = Calendar.getInstance(Locale.ENGLISH)
        val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        val monthCalendar = cal.clone() as Calendar
        monthCalendar.set(Calendar.DAY_OF_MONTH, today.get(Calendar.DAY_OF_MONTH))

        for (i in 1..maxDaysInMonth) {
            calendarList.add(monthCalendar.clone() as Calendar)
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        calenderAdapter.setData(calendarList)
        Log.d("CalendarValue", "CalenderAdapter data updated with ${calendarList.size} days")

        calenderAdapter.notifyDataSetChanged() // Notify the adapter after all changes
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



}