package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.AmenitiesIconAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.AmenitiesIconDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.CreateRateData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.CreateRateTypeAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.RatePlanDetailsAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.AddInclusionsDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.InclusionsAdapter
import rconnect.retvens.technologies.databinding.FragmentChargesAndRatesBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChargesAndRatesFragment : Fragment() {
    private lateinit var binding : FragmentChargesAndRatesBinding

    private lateinit var ratePlanDetailsAdapter : RatePlanDetailsAdapter
    private val ratePlanDetailsList = ArrayList<RatePlanDataClass>()

    var cpCheckBox = false
    var epCheckBox = false
    var apCheckBox = false
    var mapCheckBox = false

    var inclusions = 0
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

        setUpRecycler()

        binding.cpCheckBox.setOnClickListener {
            val selectedList: ArrayList<GetInclusionsData> = arrayListOf()

            if (cpCheckBox) {
                ratePlanDetailsList.remove(RatePlanDataClass( "Deluxe CP", "DLXCP", "CP",selectedList, binding.countExtraAdultRate.text.toString(), binding.countMaxChildRate.text.toString(), binding.countBaseRate.text.toString()))
                cpCheckBox = false
            } else {
                ratePlanDetailsList.add(RatePlanDataClass( "Deluxe CP", "DLXCP", "CP",selectedList, binding.countExtraAdultRate.text.toString(), binding.countMaxChildRate.text.toString(), binding.countBaseRate.text.toString()))
                cpCheckBox = true
            }
            ratePlanDetailsAdapter.notifyDataSetChanged()
        }

        binding.epCheckBox.setOnClickListener {
            val selectedList: ArrayList<GetInclusionsData> = arrayListOf()

            if (epCheckBox) {
                ratePlanDetailsList.remove(RatePlanDataClass( "Deluxe EP", "DLXEP", "EP",selectedList, binding.countExtraAdultRate.text.toString(), binding.countMaxChildRate.text.toString(), binding.countBaseRate.text.toString()))
                epCheckBox = false
            } else {
                ratePlanDetailsList.add(RatePlanDataClass( "Deluxe EP", "DLXEP", "EP",selectedList, binding.countExtraAdultRate.text.toString(), binding.countMaxChildRate.text.toString(), binding.countBaseRate.text.toString()))
                epCheckBox = true
            }
            ratePlanDetailsAdapter.notifyDataSetChanged()
        }
        binding.apCheckBox.setOnClickListener {

            val selectedList: ArrayList<GetInclusionsData> = arrayListOf()

            if (apCheckBox) {
                ratePlanDetailsList.remove(RatePlanDataClass( "Deluxe AP", "DLXAP", "AP",selectedList, binding.countExtraAdultRate.text.toString(), binding.countMaxChildRate.text.toString(), binding.countBaseRate.text.toString()))
                apCheckBox = false
            } else {
                ratePlanDetailsList.add(RatePlanDataClass( "Deluxe AP", "DLXAP", "AP",selectedList, binding.countExtraAdultRate.text.toString(), binding.countMaxChildRate.text.toString(), binding.countBaseRate.text.toString()))
                apCheckBox = true
            }
            ratePlanDetailsAdapter.notifyDataSetChanged()
        }
        binding.mapCheckBox.setOnClickListener {

            val selectedList: ArrayList<GetInclusionsData> = arrayListOf()

            if (mapCheckBox) {
                ratePlanDetailsList.remove(RatePlanDataClass( "Deluxe MAP", "DLXMAP", "MAP",selectedList, binding.countExtraAdultRate.text.toString(), binding.countMaxChildRate.text.toString(), binding.countBaseRate.text.toString()))
                mapCheckBox = false
            } else {
                ratePlanDetailsList.add(RatePlanDataClass( "Deluxe MAP", "DLXMAP", "MAP",selectedList, binding.countExtraAdultRate.text.toString(), binding.countMaxChildRate.text.toString(), binding.countBaseRate.text.toString()))
                mapCheckBox = true
            }
            ratePlanDetailsAdapter.notifyDataSetChanged()
        }

    }
    private fun setUpRecycler() {

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        ratePlanDetailsAdapter = RatePlanDetailsAdapter(requireContext(),ratePlanDetailsList)
        binding.recyclerView.adapter = ratePlanDetailsAdapter
        ratePlanDetailsAdapter.notifyDataSetChanged()

    }

}