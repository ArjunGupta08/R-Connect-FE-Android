package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlaneDiscount

import android.app.DatePickerDialog
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
import rconnect.retvens.technologies.utils.utilCreateDatePickerDialog
import java.util.Date

class RatePlanDiscountFragment : Fragment() {
    private lateinit var binding : FragmentRatePlanDiscountBinding

    private val planList = ArrayList<RatePlanDiscountData>()
    private val roomList = ArrayList<RatePlanRoomType>()
    var startDate: Date?= null
    var endDate: Date? = null
    lateinit var startDatePickerDialog: DatePickerDialog
    lateinit var endDatePickerDialog: DatePickerDialog

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

        startDatePickerDialog = utilCreateDatePickerDialog(requireContext(),binding.from) {date->
            startDate = date
        }
        endDatePickerDialog = utilCreateDatePickerDialog(requireContext(),binding.to){date->
            endDate = date
        }


        binding.fromLayout.setEndIconOnClickListener {
            endDate = null

            // Set the minimum date for the start date picker to be the current date
            startDatePickerDialog.datePicker.minDate = System.currentTimeMillis()
            startDatePickerDialog.show()
        }
        binding.toLayout.setEndIconOnClickListener {
            if (startDate!=null){
                endDatePickerDialog.datePicker.minDate = startDate!!.time
                endDatePickerDialog.show()
            }
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