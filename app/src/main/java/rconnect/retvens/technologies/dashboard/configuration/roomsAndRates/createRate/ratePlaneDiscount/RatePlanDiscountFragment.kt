package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlaneDiscount

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.card.MaterialCardView
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.CreateRateData
import rconnect.retvens.technologies.databinding.FragmentRatePlanDiscountBinding

class RatePlanDiscountFragment : Fragment() {
    private lateinit var binding : FragmentRatePlanDiscountBinding

    private val planList = ArrayList<RatePlanDiscountData>()
    private val roomList = ArrayList<RatePlanRoomType>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRatePlanDiscountBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        roomList.add(RatePlanRoomType("Every Day"))
        roomList.add(RatePlanRoomType("Every Day"))
        roomList.add(RatePlanRoomType("Every Day"))
        planList.add(RatePlanDiscountData(1,"Every Day", roomList))
        planList.add(RatePlanDiscountData(1,"Every Day", arrayListOf()))
        planList.add(RatePlanDiscountData(1,"Every Day", roomList))
        planList.add(RatePlanDiscountData(1,"Every Day", arrayListOf()))

        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        val createRatePlanAdapter = CreateRatePlanAdapter(requireContext(),planList)
        binding.recycler.adapter = createRatePlanAdapter
        createRatePlanAdapter.notifyDataSetChanged()

        binding.amountCard.setOnClickListener {
            binding.discountTextLayout.hint = "Discount ₹"
            cardSelected(binding.amountCard, binding.amountText)
        }
        binding.percentCard.setOnClickListener {
            binding.discountTextLayout.hint = "Discount %"
            cardSelected(binding.percentCard, binding.percentText)
        }
    }
    private fun cardSelected(card : MaterialCardView, text : TextView){
        binding.amountCard.strokeColor = ContextCompat.getColor(requireContext(), R.color.textInputStrokeColor)
        binding.percentCard.strokeColor = ContextCompat.getColor(requireContext(), R.color.textInputStrokeColor)
        binding.amountText.setTextColor(ContextCompat.getColor(requireContext(), R.color.subtitle))
        binding.percentText.setTextColor(ContextCompat.getColor(requireContext(), R.color.subtitle))

        card.strokeColor = ContextCompat.getColor(requireContext(), R.color.secondary)
        text.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))
    }
}