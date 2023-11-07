package rconnect.retvens.technologies.dashboard.channelManager.ChannelsManagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.roomType.MappingRoomTypeAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.roomType.MappingRoomTypeChildData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.roomType.MappingRoomTypeData
import rconnect.retvens.technologies.databinding.FragmentConnectionSettingBinding
import rconnect.retvens.technologies.databinding.FragmentMappingBinding


class MappingFragment : Fragment() {

    private lateinit var bindingTab:FragmentMappingBinding
    private var list = ArrayList<MappingRoomTypeData>()
    private var childList = ArrayList<MappingRoomTypeChildData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        bindingTab = FragmentMappingBinding.inflate(layoutInflater,container,false)
        return bindingTab.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childList.add(MappingRoomTypeChildData("Deluxe Room + Breakfast"))
        childList.add(MappingRoomTypeChildData("Deluxe Room + Breakfast"))
        childList.add(MappingRoomTypeChildData("Deluxe Room + Breakfast"))
        childList.add(MappingRoomTypeChildData("Deluxe Room + Breakfast"))
        list.add(MappingRoomTypeData("Deluxe",childList))
        list.add(MappingRoomTypeData("Deluxe",childList))
        list.add(MappingRoomTypeData("Deluxe",childList))
        list.add(MappingRoomTypeData("Deluxe",childList))
        bindingTab.recyclerRoomType.layoutManager = LinearLayoutManager(requireContext())
        val mappingRoomTypeAdapter = MappingRoomTypeAdapter(requireContext(),list)
        bindingTab.recyclerRoomType.adapter = mappingRoomTypeAdapter
    }


}