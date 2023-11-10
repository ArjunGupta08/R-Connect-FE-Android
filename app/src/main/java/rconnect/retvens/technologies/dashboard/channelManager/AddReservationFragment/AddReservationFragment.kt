package rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.FragmentAddReservationBinding

class AddReservationFragment : Fragment() {
    lateinit var binding:FragmentAddReservationBinding
    var rList = ArrayList<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddReservationBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rList.addAll(List(1){""})

        binding.recyclerRoomDetails.layoutManager = LinearLayoutManager(requireContext())
        val addReservationAdapter = AddReservationAdapter(requireContext(),rList)
        binding.recyclerRoomDetails.adapter = addReservationAdapter

        binding.roomCount.text = "(${rList.size} Rooms Added)"

        binding.llAddRoom.setOnClickListener {
            val position = rList.size
            rList.add("")
//            addReservationAdapter.notifyDataSetChanged()
            addReservationAdapter.notifyItemInserted(position)
            binding.roomCount.text = "(${rList.size} Rooms Added)"
        }


    }


}