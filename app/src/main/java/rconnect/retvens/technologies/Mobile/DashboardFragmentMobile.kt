package rconnect.retvens.technologies.Mobile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.FragmentDashboardMobileBinding

class DashboardFragmentMobile : Fragment() {

    lateinit var binding:FragmentDashboardMobileBinding
    val list = ArrayList<ReservationData>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashboardMobileBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.reservationRecycler.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        val reservationAdapter = ReservationAdapter(list,requireContext())
        binding.reservationRecycler.adapter = reservationAdapter
        reservationAdapter.notifyDataSetChanged()

    }


}