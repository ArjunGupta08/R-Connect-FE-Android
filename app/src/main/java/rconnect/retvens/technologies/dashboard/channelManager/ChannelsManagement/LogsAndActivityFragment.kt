package rconnect.retvens.technologies.dashboard.channelManager.ChannelsManagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.roomType.BookingsLogAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.roomType.ChannelLogAdapter
import rconnect.retvens.technologies.databinding.FragmentLogsAndActivityBinding

class LogsAndActivityFragment : Fragment() {

    lateinit var binding:FragmentLogsAndActivityBinding
    val list = ArrayList<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLogsAndActivityBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")

        binding.recyclerChannelLogs.layoutManager = LinearLayoutManager(requireContext())
        val channelLogAdapter = ChannelLogAdapter(list,requireContext())
        binding.recyclerChannelLogs.adapter = channelLogAdapter
        channelLogAdapter.notifyDataSetChanged()


        binding.recyclerBookings.layoutManager = LinearLayoutManager(requireContext())
        val bookingsLogAdapter = BookingsLogAdapter(list,requireContext())
        binding.recyclerBookings.adapter = bookingsLogAdapter
        bookingsLogAdapter.notifyDataSetChanged()



    }


}