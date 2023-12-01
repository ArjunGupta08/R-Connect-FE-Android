package rconnect.retvens.technologies.dashboard.configuration.CorporateRates

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.Api.CorporatesApi
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.CorporateRates.AddCompany.AddCompanyFragment
import rconnect.retvens.technologies.dashboard.configuration.CorporateRates.AddCompany.Company
import rconnect.retvens.technologies.dashboard.configuration.CorporateRates.AddCompany.CorporatesDataClass
import rconnect.retvens.technologies.dashboard.configuration.CorporateRates.ViewCompany.ViewCompanyFragment
import rconnect.retvens.technologies.dashboard.configuration.properties.ViewPropertiesFragment
import rconnect.retvens.technologies.databinding.FragmentCorporatesPartnersBinding
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CorporatesPartnersFragment : Fragment(), CorporatePartnersAdapter.OnInclusionChange {
    val corporatesList = ArrayList<Company>()
    private lateinit var progressDialog:Dialog
    lateinit var binding: FragmentCorporatesPartnersBinding
    private lateinit var corporatePartnersAdapter:CorporatePartnersAdapter
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

        progressDialog = showProgressDialog(requireContext())
        getCorporate()

        binding.corporatePartnersRecycler.layoutManager = LinearLayoutManager(requireContext())
        corporatePartnersAdapter = CorporatePartnersAdapter(corporatesList,requireContext())
        binding.corporatePartnersRecycler.adapter = corporatePartnersAdapter
        corporatePartnersAdapter.notifyDataSetChanged()
        corporatePartnersAdapter.setOnInclusionChangeListener(this)

        binding.createNewBtn.setOnClickListener {

            val childFragment: Fragment = AddCompanyFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.dashboardFragmentContainer,childFragment)
            transaction.commit()
        }
    }

    private fun getCorporate() {
        val getCorporate = OAuthClient<CorporatesApi>(requireContext()).create(CorporatesApi::class.java).getCorporates(
            UserSessionManager(requireContext()).getPropertyId().toString(),
            UserSessionManager(requireContext()).getUserId().toString()
        )

        getCorporate.enqueue(object : Callback<CorporatesDataClass?> {
            override fun onResponse(
                call: Call<CorporatesDataClass?>,
                response: Response<CorporatesDataClass?>
            ) {
                if (response.isSuccessful){
                    progressDialog.dismiss()
                    val response = response.body()!!
                    corporatesList.addAll(response.data)
                    corporatePartnersAdapter.notifyDataSetChanged()
                }else{
                    progressDialog.dismiss()
                    Log.e("error",response.code().toString())
                }
            }

            override fun onFailure(call: Call<CorporatesDataClass?>, t: Throwable) {
                progressDialog.dismiss()
                Log.e("error",t.message.toString())
            }
        })

    }


    override fun onClick(companyId: String) {
        val childFragment: Fragment = ViewCompanyFragment(companyId)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.dashboardFragmentContainer,childFragment)
        transaction.commit()
    }


}