package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanBar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.CreateRateData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.CreateRateTypeAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.RatePlanDetailsAdapter
import rconnect.retvens.technologies.databinding.FragmentRatePlanBarBinding


class RatePlanBarFragment : Fragment() {
    private lateinit var binding : FragmentRatePlanBarBinding

    val rateList = ArrayList<CreateRateData>()
    val ratePlanDetailsList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRatePlanBarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecycler()
    }

    private fun setUpRecycler() {

        rateList.add(CreateRateData("Breakfast","Sunday","Per Adult","200"))
        rateList.add(CreateRateData("Breakfast","Sunday","Per Adult","200"))
        rateList.add(CreateRateData("Breakfast","Sunday","Per Adult","200"))
        rateList.add(CreateRateData("Breakfast","Sunday","Per Adult","200"))

        ratePlanDetailsList.add("5")
        ratePlanDetailsList.add("5")

        binding.recyclerInclusion.layoutManager = LinearLayoutManager(requireContext())
        val createRateTypeAdapter = CreateRateTypeAdapter(requireContext(),rateList)
        binding.recyclerInclusion.adapter = createRateTypeAdapter
        createRateTypeAdapter.notifyDataSetChanged()


        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val ratePlanDetailsAdapter = RatePlanDetailsAdapter(requireContext(),ratePlanDetailsList)
        binding.recyclerView.adapter = ratePlanDetailsAdapter
        binding.recyclerView2.adapter = ratePlanDetailsAdapter
        ratePlanDetailsAdapter.notifyDataSetChanged()

    }
}