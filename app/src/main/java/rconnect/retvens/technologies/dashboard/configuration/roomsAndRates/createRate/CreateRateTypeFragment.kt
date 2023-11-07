package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.RatePlanCompanyFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanPackage.RatePlanPackageFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlaneDiscount.RatePlanDiscountFragment
import rconnect.retvens.technologies.databinding.FragmentCreateRateTypeBinding
import rconnect.retvens.technologies.databinding.FragmentViewPropertiesBinding
import rconnect.retvens.technologies.utils.topInAnimation

class CreateRateTypeFragment : Fragment() {

    lateinit var binding:FragmentCreateRateTypeBinding

    private var isRateDropDownOpen = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateRateTypeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rateTypeSelection()

        replaceChildFrag(RatePlanCompanyFragment())
    }

    private fun rateTypeSelection() {

        binding.rateTypeET.setOnClickListener {
            if (isRateDropDownOpen) {
                binding.dropDownLayout.isVisible = false
                isRateDropDownOpen = false
            } else {
                binding.dropDownLayout.visibility = View.VISIBLE
                isRateDropDownOpen = true
                topInAnimation(binding.dropDownLayout, requireContext())
            }
        }

        binding.companyRateType.setOnClickListener {
            replaceChildFrag(RatePlanCompanyFragment())
            binding.rateTypeET.setText("Company")
            binding.companyNameLayout.isVisible = true
            binding.roomTypeLayout.isVisible = true
            binding.mealPlanLayout.isVisible = true
            binding.masterRatePlanLayout.isVisible = false
        }

        binding.barRateType.setOnClickListener {
            replaceChildFrag(RatePlanCompanyFragment())
            binding.rateTypeET.setText("Bar")
            binding.roomTypeLayout.isVisible = true
            binding.mealPlanLayout.isVisible = true
            binding.companyNameLayout.isVisible = false
            binding.masterRatePlanLayout.isVisible = false
        }

        binding.discountRateType.setOnClickListener {
            replaceChildFrag(RatePlanDiscountFragment())
            binding.rateTypeET.setText("Discount")
            binding.companyNameLayout.isVisible = false
            binding.mealPlanLayout.isVisible = false
            binding.roomTypeLayout.isVisible = false
            binding.masterRatePlanLayout.isVisible = false
        }

        binding.packageRateType.setOnClickListener {
            replaceChildFrag(RatePlanPackageFragment())
            binding.rateTypeET.setText("Package")
            binding.roomTypeLayout.isVisible = true
            binding.companyNameLayout.isVisible = false
            binding.mealPlanLayout.isVisible = false
            binding.masterRatePlanLayout.isVisible = true
        }

    }

    private fun replaceChildFrag(fragment: Fragment){
        val fragmentManager = childFragmentManager
        val fragmenTransaction = fragmentManager.beginTransaction()
        fragmenTransaction.replace(R.id.ratePlanFragContainer, fragment)
        fragmenTransaction.commit()

        binding.dropDownLayout.isVisible = false
        isRateDropDownOpen = false
    }

}