package rconnect.retvens.technologies.dashboard.channelManager.ChannelsManagement

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
import rconnect.retvens.technologies.dashboard.configuration.CorporateRates.ViewCompany.CompanyLedgerChildFragment
import rconnect.retvens.technologies.dashboard.configuration.CorporateRates.ViewCompany.StayHistoryChildFragment
import rconnect.retvens.technologies.dashboard.configuration.CorporateRates.ViewCompany.ViewCompanyDetailsChildFragment
import rconnect.retvens.technologies.databinding.FragmentChannelDetailBinding
import rconnect.retvens.technologies.databinding.FragmentChannelManagementBinding
import rconnect.retvens.technologies.utils.leftInAnimation


class ChannelDetailFragment : Fragment() {

    private lateinit var roboto : Typeface
    private lateinit var robotoMedium : Typeface

    private lateinit var bindingTab:FragmentChannelDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bindingTab = FragmentChannelDetailBinding.inflate(layoutInflater)
        return bindingTab.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        roboto = ResourcesCompat.getFont(requireContext(), R.font.roboto)!!
        robotoMedium = ResourcesCompat.getFont(requireContext(), R.font.roboto_medium)!!
        leftInAnimation(bindingTab.connSettingContainer, requireContext())
        changeChildFragment(ViewCompanyDetailsChildFragment())

        bindingTab.overviewFrag.setOnClickListener {
            tabSelected(bindingTab.overviewFrag)
            changeChildFragment(OverviewFragment())
        }
        bindingTab.mappingFrag.setOnClickListener {
            tabSelected(bindingTab.mappingFrag)
            changeChildFragment(MappingFragment())
        }
        bindingTab.logFragment.setOnClickListener {
            tabSelected(bindingTab.logFragment)
            changeChildFragment(LogsAndActivityFragment())
        }
    }



    private fun tabSelected(selectedText: TextView) {

        bindingTab.overviewFrag.textSize = 14.0f
        bindingTab.overviewFrag.typeface = roboto
        bindingTab.mappingFrag.textSize = 14.0f
        bindingTab.mappingFrag.typeface = roboto
        bindingTab.logFragment.textSize = 14.0f
        bindingTab.logFragment.typeface = roboto


        leftInAnimation(bindingTab.connSettingContainer, requireContext())

        bindingTab.overviewFrag.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
        bindingTab.mappingFrag.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
        bindingTab.logFragment.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))

        selectedText.textSize = 18.0f
        selectedText.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))
        selectedText.typeface = robotoMedium
        selectedText.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_white_background))
    }

    fun changeChildFragment( fragment: Fragment){
        val childFragment: Fragment = fragment
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.connSettingContainer,childFragment)
        transaction.commit()
    }
}