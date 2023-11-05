package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.FragmentChargesAndRatesBinding


class ChargesAndRatesFragment : Fragment() {
    private lateinit var binding : FragmentChargesAndRatesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChargesAndRatesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}