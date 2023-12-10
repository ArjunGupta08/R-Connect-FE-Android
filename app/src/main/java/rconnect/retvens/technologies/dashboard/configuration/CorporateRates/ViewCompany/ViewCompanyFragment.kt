package rconnect.retvens.technologies.dashboard.configuration.CorporateRates.ViewCompany

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.CorporateRates.AddCompany.CompanyDetailsChildFragment
import rconnect.retvens.technologies.dashboard.configuration.CorporateRates.AddCompany.ContractDetailsChildFragment
import rconnect.retvens.technologies.databinding.FragmentViewCompanyBinding
import rconnect.retvens.technologies.utils.leftInAnimation

class ViewCompanyFragment(val companyId:String) : Fragment() {
    lateinit var binding:FragmentViewCompanyBinding

    private lateinit var roboto : Typeface
    private lateinit var robotoMedium : Typeface

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewCompanyBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        roboto = ResourcesCompat.getFont(requireContext(), R.font.roboto)!!
        robotoMedium = ResourcesCompat.getFont(requireContext(), R.font.roboto_medium)!!

        leftInAnimation(binding.viewCompanyFragContainer, requireContext())
        tabSelected(binding.companyDetailsFrag)
        changeChildFragment(CompanyDetailsUpdateFragment(companyId))

        binding.companyDetailsFrag.setOnClickListener {
            tabSelected(binding.companyDetailsFrag)
            changeChildFragment(CompanyDetailsUpdateFragment(companyId))
        }
        binding.stayHistory.setOnClickListener {
            tabSelected(binding.stayHistory)
            changeChildFragment(StayHistoryChildFragment())
        }
        binding.companyLedger.setOnClickListener {
            tabSelected(binding.companyLedger)
            changeChildFragment(CompanyLedgerChildFragment())
        }
        binding.ratePlan.setOnClickListener {
            tabSelected(binding.ratePlan)
            changeChildFragment(RatePlanChildFragment())
        }
        binding.contractDetailsFag.setOnClickListener {
            tabSelected(binding.contractDetailsFag)
            changeChildFragment(ContractDetailsUpdateFragment(companyId))
        }

    }

    private fun tabSelected(selectedText: TextView) {

        binding.companyDetailsFrag.textSize = 14.0f
        binding.companyDetailsFrag.typeface = roboto
        binding.stayHistory.textSize = 14.0f
        binding.stayHistory.typeface = roboto
        binding.companyLedger.textSize = 14.0f
        binding.companyLedger.typeface = roboto
        binding.ratePlan.textSize = 14.0f
        binding.ratePlan.typeface = roboto
        binding.contractDetailsFag.textSize = 14.0f
        binding.contractDetailsFag.typeface = roboto

        leftInAnimation(binding.viewCompanyFragContainer, requireContext())

        binding.companyDetailsFrag.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
        binding.stayHistory.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
        binding.companyLedger.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
        binding.ratePlan.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
        binding.contractDetailsFag.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))

        selectedText.textSize = 18.0f
        selectedText.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))
        selectedText.typeface = robotoMedium
        selectedText.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_white_background))
    }

    fun changeChildFragment( fragment: Fragment){
        val childFragment: Fragment = fragment
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.viewCompanyFragContainer,childFragment)
        transaction.commit()
    }
}