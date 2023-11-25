package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.RatePlan

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.configurationApi.SingleConfiguration
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.CreateRateTypeFragment
import rconnect.retvens.technologies.databinding.FragmentRatePlanBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RatePlanFragment : Fragment() {

    lateinit var binding: FragmentRatePlanBinding

    lateinit var progressDialog : Dialog
    lateinit var ratePlanAdapter: RatePlanAdapter
    var ratePlanList = ArrayList<String>()

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
        get.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                if (isAdded) {
                    try {
                        Toast.makeText(requireContext(), response.message(), Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()

                        ratePlanList.add("1")
                        ratePlanList.add("1")
                        ratePlanList.add("1")

                        ratePlanAdapter = RatePlanAdapter(ratePlanList, requireContext())
                        binding.rateTypeRecycler.adapter = ratePlanAdapter
                    } catch (e : Exception) {
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                progressDialog.dismiss()
                Log.d("error", t.localizedMessage)
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
}