package rconnect.retvens.technologies.dashboard.configuration.CorporateRates.AddCompany

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.FragmentCompanyDetailsChildBinding


class ContractDetailsChildFragment : Fragment() {
    private lateinit var binding : FragmentCompanyDetailsChildBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCompanyDetailsChildBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }

}