package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType

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
import rconnect.retvens.technologies.databinding.FragmentChargesAndRatesBinding


class ChargesAndRatesFragment : Fragment() {
    private lateinit var binding : FragmentChargesAndRatesBinding

    val rateList = ArrayList<CreateRateData>()
    val ratePlanDetailsList = ArrayList<String>()

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
        setUpRecycler()

        var minRate = 1000.00
        var maxRate = 5000.00
        var baseRate = 2500.00
        var adultRate = 1500.00
        var childRate = 600.00

        binding.removeMinimumRate.setOnClickListener {
            if (minRate>0){
                minRate -= 100
                binding.countMinimumRate.text = "₹ ${minRate}"
            }
            }
        binding.addMinimumRate.setOnClickListener {
            minRate += 100
            binding.countMinimumRate.text = "₹ ${minRate}"
        }

        binding.addMaximumCharges.setOnClickListener {
            maxRate += 100
            binding.countMaximumCharges.text = "₹ ${maxRate}" }
        binding.removeMaximumCharges.setOnClickListener {
            if (maxRate>0){
                maxRate -= 100
                binding.countMaximumCharges.text = "₹ ${maxRate}"
            }
        }

        binding.addBaseRate.setOnClickListener {
            baseRate+=100
            binding.countBaseRate.text = "₹ ${baseRate}"
        }
        binding.removeBaseRate.setOnClickListener {
            if (baseRate>0){
                baseRate-=100
                binding.countBaseRate.text = "₹ ${baseRate}"
            }
        }
        binding.addExtraAdultRate.setOnClickListener {
            adultRate+=100
            binding.countExtraAdultRate.text = "₹ ${adultRate}"
        }
        binding.removeExtraAdultRate.setOnClickListener {
            if (adultRate>0){
                adultRate-=100
                binding.countExtraAdultRate.text = "₹ ${adultRate}"
            }
        }
        binding.addMaxChildRate.setOnClickListener {
            childRate+=100
            binding.countMaxChildRate.text = "₹ ${childRate}"
        }
        binding.removeMaxChildRate.setOnClickListener {
            if (childRate>0){
                childRate-=100
                binding.countMaxChildRate.text = "₹ ${childRate}"
            }
        }









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
        ratePlanDetailsAdapter.notifyDataSetChanged()

    }

}