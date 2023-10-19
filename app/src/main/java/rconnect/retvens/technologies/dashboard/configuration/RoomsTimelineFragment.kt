package rconnect.retvens.technologies.dashboard.configuration

import android.app.Dialog
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.channelManager.RatesAndInventory.CalenderAdapter
import rconnect.retvens.technologies.dashboard.channelManager.RatesAndInventory.RoomsInventoryAdapter
import rconnect.retvens.technologies.databinding.FragmentRoomTypeBinding
import rconnect.retvens.technologies.databinding.FragmentRoomsTimelineBinding
import java.util.Calendar
import java.util.Locale


class RoomsTimelineFragment : Fragment() {
    private lateinit var binding : FragmentRoomsTimelineBinding

    private lateinit var calenderAdapter: CalenderAdapter
    private lateinit var inventoryAdapter: RoomsInventoryAdapter
    private val cal = Calendar.getInstance(Locale.ENGLISH)
    private  var mList: ArrayList<String> = ArrayList();
    lateinit var dialog: Dialog
    private lateinit var robotoMedium : Typeface
    private lateinit var roboto: Typeface

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRoomsTimelineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calenderRecycler.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)

        calenderAdapter = CalenderAdapter()
        setUpCalendar()

        binding.calenderRecycler.adapter = calenderAdapter
        calenderAdapter.notifyDataSetChanged()

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