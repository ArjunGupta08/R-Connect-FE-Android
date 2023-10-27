package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.CreateRateData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.CreateRateTypeAdapter
import rconnect.retvens.technologies.databinding.FragmentRatePlanCompanyBinding


class RatePlanCompanyFragment : Fragment() {
    private lateinit var binding : FragmentRatePlanCompanyBinding

    val rateList = ArrayList<CreateRateData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRatePlanCompanyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rateList.add(CreateRateData("Breakfast","Sunday","Per Adult","200"))
        rateList.add(CreateRateData("Breakfast","Sunday","Per Adult","200"))
        rateList.add(CreateRateData("Breakfast","Sunday","Per Adult","200"))
        rateList.add(CreateRateData("Breakfast","Sunday","Per Adult","200"))

        binding.recyclerInclusion.layoutManager = LinearLayoutManager(requireContext())
        val createRateTypeAdapter = CreateRateTypeAdapter(requireContext(),rateList)
        binding.recyclerInclusion.adapter = createRateTypeAdapter
        createRateTypeAdapter.notifyDataSetChanged()

    }

}