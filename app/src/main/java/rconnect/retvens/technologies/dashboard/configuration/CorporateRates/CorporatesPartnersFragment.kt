package rconnect.retvens.technologies.dashboard.configuration.CorporateRates

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.CorporateRates.AddCompany.AddCompanyFragment
import rconnect.retvens.technologies.dashboard.configuration.properties.ViewPropertiesFragment
import rconnect.retvens.technologies.databinding.FragmentCorporatesPartnersBinding

class CorporatesPartnersFragment : Fragment() {
    val corporatesList = ArrayList<CorporatesData>()
    lateinit var binding: FragmentCorporatesPartnersBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCorporatesPartnersBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        corporatesList.add(CorporatesData("Retvens"))
        corporatesList.add(CorporatesData("HCL"))
        corporatesList.add(CorporatesData("IBM"))
        corporatesList.add(CorporatesData("SAMSUNG"))
        corporatesList.add(CorporatesData("AMAZON"))
        corporatesList.add(CorporatesData("GOOGLE"))
        corporatesList.add(CorporatesData("MICROSOFT"))
        corporatesList.add(CorporatesData("TESLA"))
        corporatesList.add(CorporatesData("META"))
        binding.corporatePartnersRecycler.layoutManager = LinearLayoutManager(requireContext())
        val corporatePartnersAdapter = CorporatePartnersAdapter(corporatesList,requireContext())
        binding.corporatePartnersRecycler.adapter = corporatePartnersAdapter
        corporatePartnersAdapter.notifyDataSetChanged()

        binding.createNewBtn.setOnClickListener {

            val childFragment: Fragment = AddCompanyFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.dashboardFragmentContainer,childFragment)
            transaction.commit()
        }
    }


}