package rconnect.retvens.technologies.dashboard.channelManager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.channelManager.Reservations.ReservationsAdapter
import rconnect.retvens.technologies.dashboard.channelManager.Reservations.ReserveData
import rconnect.retvens.technologies.databinding.FragmentBookingBinding
import rconnect.retvens.technologies.utils.showDropdownMenu


class BookingFragment : Fragment() {
    lateinit var binding:FragmentBookingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBookingBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = ArrayList<ReserveData>()
        list.add(ReserveData("ABCD123456789"))
        list.add(ReserveData("ABCD123456789"))
        list.add(ReserveData("ABCD123456789"))
        list.add(ReserveData("ABCD123456789"))
        list.add(ReserveData("ABCD123456789"))
        list.add(ReserveData("ABCD123456789"))
        list.add(ReserveData("ABCD123456789"))
        list.add(ReserveData("ABCD123456789"))
        list.add(ReserveData("ABCD123456789"))
        list.add(ReserveData("ABCD123456789"))

        binding.reservationRecycler.layoutManager = LinearLayoutManager(requireContext())
        val reservationsAdapter = ReservationsAdapter(requireContext(),list)
        binding.reservationRecycler.adapter = reservationsAdapter
        reservationsAdapter.notifyDataSetChanged()

        binding.sourceText.setOnClickListener {
            val property = ArrayList<String>()
            property.add("Commercial")
            property.add("Personal")
            property.add("Null")
            showDropdownMenu(requireContext(),binding.sourceText,it,property)
        }

    }





}