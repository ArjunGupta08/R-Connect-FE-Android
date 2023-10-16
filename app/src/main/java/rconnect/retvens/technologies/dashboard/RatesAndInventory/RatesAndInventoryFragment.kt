package rconnect.retvens.technologies.dashboard.RatesAndInventory

import android.app.Dialog
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.FragmentRatesAndInventoryBinding
import java.util.Calendar
import java.util.Locale

class RatesAndInventoryFragment : Fragment() {

    private lateinit var bindingTab:FragmentRatesAndInventoryBinding
    private lateinit var calenderAdapter:CalenderAdapter
    private lateinit var inventoryAdapter: RoomsInventoryAdapter
    private val cal = Calendar.getInstance(Locale.ENGLISH)
    private  var mList: ArrayList<String> = ArrayList();
    lateinit var dialog: Dialog
    private lateinit var robotoMedium : Typeface
    private lateinit var roboto:Typeface


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

        bindingTab.calenderRecycler.isNestedScrollingEnabled = false

        bindingTab.bulkUpdateCard.setOnClickListener {
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
            var isMon = false
            var isTues = false
            var isWed = false
            var isThur = false
            var frid = false
            var satu = true
            var isSun = false

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

            val cancel = dialog.findViewById<TextView>(R.id.cancel)
            cancel.setOnClickListener { dialog.cancel() }
            val sat = dialog.findViewById<TextView>(R.id.saturday)

            val fri = dialog.findViewById<TextView>(R.id.fri)
            val sun = dialog.findViewById<TextView>(R.id.sunday)
            val mon = dialog.findViewById<TextView>(R.id.monday)
            val tues = dialog.findViewById<TextView>(R.id.tuesday)
            val wed = dialog.findViewById<TextView>(R.id.wed)
            val thur = dialog.findViewById<TextView>(R.id.thursday)
            fri.setOnClickListener {
                if (!frid){
                    selectCard(fri)
                    frid = true
                }
                else{
                    unSelectCard(fri)
                    frid = false
                }
            }
            sat.setOnClickListener {
                if (!satu){
                    selectCard(sat)
                    satu = true
                }
                else{
                    unSelectCard(sat)
                    satu = false
                }
            }
            sun.setOnClickListener {
                if (!isSun){
                    selectCard(sun)
                    isSun = true
                }
                else{
                    unSelectCard(sun)
                    isSun = false
                }
            }
            mon.setOnClickListener {
                if (!isMon){
                    selectCard(mon)
                    isMon = true
                }
                else{
                    unSelectCard(mon)
                    isMon = false
                }
            }
            tues.setOnClickListener {
                if (!isTues){
                    selectCard(tues)
                    isTues = true
                }
                else{
                    unSelectCard(tues)
                    isTues = false
                }
            }
            wed.setOnClickListener {
                if (!isWed){
                    selectCard(wed)
                    isWed = true
                }
                else{
                    unSelectCard(wed)
                    isWed = false
                }
            }
            thur.setOnClickListener {
                if (!isThur){
                    selectCard(thur)
                    isThur = true
                }
                else{
                    unSelectCard(thur)
                    isThur = false
                }
            }
            val txt_all_days = dialog.findViewById<TextView>(R.id.txt_all_days)
            val txt_weekends = dialog.findViewById<TextView>(R.id.txt_weekends)
            val txt_week_days = dialog.findViewById<TextView>(R.id.txt_week_days)
            val txt_custom = dialog.findViewById<TextView>(R.id.txt_custom)

            var isAllDay = false
            var isweekend = false
            var isWeekDay = false
            var isCustom = false

            fun notClickable(){
                sun.isClickable = false
                mon.isClickable = false
                tues.isClickable = false
                wed.isClickable = false
                thur.isClickable = false
                fri.isClickable = false
                sat.isClickable = false
            }
            fun allClickable(){
                sun.isClickable = true
                mon.isClickable = true
                tues.isClickable = true
                wed.isClickable = true
                thur.isClickable = true
                fri.isClickable = true
                sat.isClickable = true
            }
            fun selectHead(head:TextView){
                unSelectCard(txt_all_days)
                unSelectCard(txt_week_days)
                unSelectCard(txt_weekends)
                unSelectCard(txt_custom)

                head.typeface = robotoMedium
                selectCard(head)
            }
            fun unSelectAllDays(){
                unSelectCard(sun)
                unSelectCard(mon)
                unSelectCard(tues)
                unSelectCard(wed)
                unSelectCard(thur)
                unSelectCard(fri)
                unSelectCard(sat)
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
                if (!isInventoryOpen){
                    add_inventory.setImageResource(R.drawable.svg_arrow_down)
                    blockRoomCard.isVisible = true
                    addRoomCard.isVisible = true
                    isInventoryOpen = true
                }
                else{
                    add_inventory.setImageResource(R.drawable.svg_add_icon)
                    blockRoomCard.isVisible = false
                    addRoomCard.isVisible = false
                    isInventoryOpen = false
                }

            }



            val calender = Calendar.getInstance()
            dialog.findViewById<TextInputLayout>(R.id.from_Layout).setOnClickListener {

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
            day?.setBackgroundResource(R.drawable.rounded_black_border)
            day?.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
    }
    private fun unSelectCard(day: TextView?) {
            day?.setBackgroundResource(R.drawable.rounded_white_border)
            day?.setTextColor(ContextCompat.getColor(requireContext(),R.color.lightBlack))
        day?.typeface = roboto
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

}