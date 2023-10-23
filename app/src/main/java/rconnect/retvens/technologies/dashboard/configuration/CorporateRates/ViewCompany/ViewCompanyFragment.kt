package rconnect.retvens.technologies.dashboard.configuration.CorporateRates.ViewCompany

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.FragmentViewCompanyBinding

class ViewCompanyFragment : Fragment() {
    lateinit var binding:FragmentViewCompanyBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewCompanyBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}