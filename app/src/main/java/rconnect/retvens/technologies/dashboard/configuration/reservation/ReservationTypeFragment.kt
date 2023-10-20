package rconnect.retvens.technologies.dashboard.configuration.reservation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.addRoomType.AddRoomTypeFragment
import rconnect.retvens.technologies.dashboard.configuration.roomType.RoomTypeAdapter
import rconnect.retvens.technologies.databinding.FragmentReservationTypeBinding
import rconnect.retvens.technologies.databinding.FragmentRoomTypeBinding
import rconnect.retvens.technologies.utils.Const


class ReservationTypeFragment : Fragment() {

    private lateinit var binding : FragmentReservationTypeBinding

    private lateinit var reservationTypeAdapter: ReservationTypeAdapter
    private var reservationTypeList = ArrayList<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReservationTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        reservationTypeRecycler()

//        binding.createNewBtn.setOnClickListener {
////            Const.isAddingNewRoom = true
//
//            val childFragment: Fragment = AddRoomTypeFragment()
//            val transaction = requireActivity().supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.dashboardFragmentContainer,childFragment)
//            transaction.commit()
//
//        }

    }


    private fun reservationTypeRecycler() {
        binding.reservationTypeRecycler.layoutManager = LinearLayoutManager(requireContext())

        reservationTypeList.add("4")
        reservationTypeList.add("4")
        reservationTypeList.add("4")
        reservationTypeList.add("4")
        reservationTypeList.add("4")
        reservationTypeList.add("4")
        reservationTypeList.add("4")
        reservationTypeList.add("4")
        reservationTypeList.add("4")
        reservationTypeList.add("4")
        reservationTypeList.add("4")
        reservationTypeList.add("4")
        reservationTypeList.add("4")
        reservationTypeList.add("4")

        reservationTypeAdapter = ReservationTypeAdapter(reservationTypeList, requireContext())
        binding.reservationTypeRecycler.adapter = reservationTypeAdapter
        reservationTypeAdapter.notifyDataSetChanged()
    }



}