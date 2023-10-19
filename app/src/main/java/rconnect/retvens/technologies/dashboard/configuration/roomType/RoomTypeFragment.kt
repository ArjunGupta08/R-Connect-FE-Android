package rconnect.retvens.technologies.dashboard.configuration.roomType

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.addRoomType.AddRoomTypeFragment
import rconnect.retvens.technologies.dashboard.configuration.properties.ViewPropertiesFragment
import rconnect.retvens.technologies.databinding.FragmentAddRoomTypeBinding
import rconnect.retvens.technologies.databinding.FragmentRoomTypeBinding
import rconnect.retvens.technologies.utils.Const


class RoomTypeFragment : Fragment() {

    private lateinit var binding : FragmentRoomTypeBinding

    private lateinit var roomTypeAdapter: RoomTypeAdapter
    private var roomTypeList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRoomTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        roomTypeRecycler()

        binding.createNewBtn.setOnClickListener {
            Const.isAddingNewRoom = true

            val childFragment: Fragment = AddRoomTypeFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.dashboardFragmentContainer,childFragment)
            transaction.commit()

        }
    }

    private fun roomTypeRecycler() {
        binding.roomTypeRecycler.layoutManager = LinearLayoutManager(requireContext())

        roomTypeList.add("4")
        roomTypeList.add("4")
        roomTypeList.add("4")
        roomTypeList.add("4")
        roomTypeList.add("4")
        roomTypeList.add("4")
        roomTypeList.add("4")
        roomTypeList.add("4")
        roomTypeList.add("4")
        roomTypeList.add("4")
        roomTypeList.add("4")
        roomTypeList.add("4")
        roomTypeList.add("4")

        roomTypeAdapter = RoomTypeAdapter(roomTypeList, requireContext())
        binding.roomTypeRecycler.adapter = roomTypeAdapter
        roomTypeAdapter.notifyDataSetChanged()
    }
}