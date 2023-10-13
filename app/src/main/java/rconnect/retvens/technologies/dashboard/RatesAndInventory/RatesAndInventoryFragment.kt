package rconnect.retvens.technologies.dashboard.RatesAndInventory

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
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

        bindingTab.calenderRecycler.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)

        bindingTab.calenderRecycler.isNestedScrollingEnabled = false


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