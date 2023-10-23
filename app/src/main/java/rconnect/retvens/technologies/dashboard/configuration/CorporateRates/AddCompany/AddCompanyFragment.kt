package rconnect.retvens.technologies.dashboard.configuration.CorporateRates.AddCompany

import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.CorporateRates.ViewCompany.ViewCompanyFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.properties.ViewPropertiesFragment
import rconnect.retvens.technologies.databinding.FragmentAddCompanyBinding
import rconnect.retvens.technologies.utils.leftInAnimation
import rconnect.retvens.technologies.utils.rightInAnimation


class AddCompanyFragment : Fragment() {
    private lateinit var binding : FragmentAddCompanyBinding

    private lateinit var roboto : Typeface
    private lateinit var robotoMedium : Typeface

    private var page = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddCompanyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        roboto = ResourcesCompat.getFont(requireContext(), R.font.roboto)!!
        robotoMedium = ResourcesCompat.getFont(requireContext(), R.font.roboto_medium)!!

        leftInAnimation(binding.companyDetailsFrag, requireContext())
        changeChildFragment(CompanyDetailsChildFragment())


        binding.continueBtn.setOnClickListener {

            if (page == 1){

                page = 2
                binding.buttonTxt.text = "Save"

                binding.contractDetailsFag.textSize = 18.0f
                binding.contractDetailsFag.typeface = robotoMedium
                binding.contractDetailsFag.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))

                binding.companyDetailsFrag.textSize = 14.0f
                binding.companyDetailsFrag.typeface = roboto

                binding.companyDetailsFrag.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
                binding.contractDetailsFag.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_white_background))

                changeChildFragment(ContractDetailsChildFragment())
                rightInAnimation(binding.addCompanyFragContainer, requireContext())

            } else {

                val childFragment: Fragment = ViewCompanyFragment()
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.dashboardFragmentContainer,childFragment)
                transaction.commit()

            }
        }


        binding.companyDetailsFrag.setOnClickListener {
            page = 1
            binding.buttonTxt.text = "Save"

            binding.companyDetailsFrag.textSize = 18.0f
            binding.companyDetailsFrag.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))
            binding.companyDetailsFrag.typeface = robotoMedium

            binding.contractDetailsFag.textSize = 14.0f
            binding.contractDetailsFag.typeface = roboto

            leftInAnimation(binding.addCompanyFragContainer, requireContext())
            changeChildFragment(CompanyDetailsChildFragment())

            binding.contractDetailsFag.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
            binding.companyDetailsFrag.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_white_background))
        }

        binding.contractDetailsFag.setOnClickListener {
            page = 2

            binding.contractDetailsFag.textSize = 18.0f
            binding.contractDetailsFag.typeface = robotoMedium
            binding.contractDetailsFag.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))

            binding.companyDetailsFrag.textSize = 14.0f
            binding.companyDetailsFrag.typeface = roboto

            changeChildFragment(ContractDetailsChildFragment())

            rightInAnimation(binding.addCompanyFragContainer, requireContext())

            binding.companyDetailsFrag.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
            binding.contractDetailsFag.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_white_background))
        }


    }

    fun changeChildFragment( fragment: Fragment){
        val childFragment: Fragment = fragment
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.addCompanyFragContainer,childFragment)
        transaction.commit()
    }
}