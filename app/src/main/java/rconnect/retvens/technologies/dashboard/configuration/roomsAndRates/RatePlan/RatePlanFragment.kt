package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.RatePlan

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import okhttp3.internal.filterList
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.configurationApi.SingleConfiguration
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.CreateRateTypeFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.GetAllRatePlans
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.RatePlan
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanBar.BarData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanBar.RatePlanBarUpdateAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanBar.RatePlanBarUpdateFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.RatePlanCompanyUpdateFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanPackage.RatePlanPackageFragment
import rconnect.retvens.technologies.databinding.FragmentRatePlanBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RatePlanFragment : Fragment(), RatePlanAdapter.OnUpdate {

    lateinit var binding: FragmentRatePlanBinding

    lateinit var progressDialog : Dialog
    lateinit var ratePlanAdapter: RatePlanAdapter
    var ratePlanList = ArrayList<RatePlan>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRatePlanBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rateTypeRecycler.layoutManager = LinearLayoutManager(requireContext())
        ratePlanAdapter = RatePlanAdapter(ratePlanList,requireContext())
        binding.rateTypeRecycler.adapter = ratePlanAdapter
        ratePlanAdapter.setOnUpdateListener(this)

        roomTypeRecycler()

        binding.createNewBtn.setOnClickListener {
            replaceFragment(CreateRateTypeFragment())
        }

    }

    private fun roomTypeRecycler() {
        progressDialog = showProgressDialog(requireContext())

        val get = OAuthClient<SingleConfiguration>(requireContext()).create(SingleConfiguration::class.java).getAllRatePlans(
            UserSessionManager(requireContext()).getPropertyId().toString(),
            UserSessionManager(requireContext()).getUserId().toString()
        )

        get.enqueue(object : Callback<GetAllRatePlans?> {
            override fun onResponse(
                call: Call<GetAllRatePlans?>,
                response: Response<GetAllRatePlans?>
            ) {
                try {
                    if (response.isSuccessful && isAdded) {
                        Log.e("ratePlan", response.body().toString())
                        progressDialog.dismiss()
                        val responseData = response.body()!!
                        ratePlanList.clear()
                        ratePlanList.addAll(responseData.barRatePlan)
                        ratePlanList.addAll(responseData.companyRatePlan)
                        ratePlanList.addAll(responseData.packageRatePlan)
                        ratePlanList.addAll(responseData.discountplans)
                        ratePlanAdapter.notifyDataSetChanged()
                        Log.d("ratePlan", responseData.packageRatePlan.toString())

                        binding.search.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                s: CharSequence?,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {

                            }

                            override fun onTextChanged(
                                s: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {
                                val filterList = ratePlanList.filterList {
                                    this.ratePlanName.contains(s.toString(), true)
                                }
                                ratePlanAdapter.filterList(filterList as ArrayList<RatePlan>)
                            }

                            override fun afterTextChanged(s: Editable?) {

                            }
                        })

                    } else {
                        progressDialog.dismiss()
                        Log.e("error", response.code().toString())
                    }
                } catch (e:Exception){
                    e.printStackTrace()
                }
            }
            override fun onFailure(call: Call<GetAllRatePlans?>, t: Throwable) {
                progressDialog.dismiss()
                Log.e("error",t.message.toString())
            }
        })
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.dashboardFragmentContainer, fragment)
            transaction.commit()
        }
    }

    override fun onUpdateIdentityType(rateTypeId: String, type: String, currentData : RatePlan) {
        if (type == "Bar"){
            replaceFragment(RatePlanBarUpdateFragment(rateTypeId))
        }else if (type == "Company"){
            replaceFragment(RatePlanCompanyUpdateFragment(rateTypeId))
        }else if (type == "Package"){
            replaceFragment(RatePlanPackageFragment(BarData("", "","" , currentData.ratePlanTotal, arrayListOf(), currentData.extraChildRate, currentData.extraAdultRate, currentData.ratePlanName), rateTypeId))
        }
    }
}