package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.Api.DropDownApis
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanBar.RatePlanBarFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.RatePlanCompanyFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanPackage.RatePlanPackageFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlaneDiscount.RatePlanDiscountFragment
import rconnect.retvens.technologies.databinding.FragmentCreateRateTypeBinding
import rconnect.retvens.technologies.databinding.FragmentViewPropertiesBinding
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.topInAnimation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateRateTypeFragment : Fragment() {

    lateinit var binding:FragmentCreateRateTypeBinding

    var roomTypeList = ArrayList<String>()
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
        getRateType()

        val options = arrayOf("Deluxe", "Premium", "Elite")

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, roomTypeList)
        // Set a click listener for the end icon
        binding.dropRoom.setOnClickListener {
            // Show dropdown menu
            showDropdownMenu(adapter,it)
        }
        replaceChildFrag(RatePlanCompanyFragment())
    }


    private fun showDropdownMenu(adapter: ArrayAdapter<String>, anchorView: View) {
        val listPopupWindow = ListPopupWindow(requireContext())
        listPopupWindow.setAdapter(adapter)
        listPopupWindow.anchorView = anchorView
        listPopupWindow.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = adapter.getItem(position)
            binding.dropRoom.setText(selectedItem)
            listPopupWindow.dismiss()
        }


        listPopupWindow.show()
    }

    private fun getRateType() {
        val retrofit = OAuthClient<DropDownApis>(requireContext()).create(DropDownApis::class.java).getRoomList(UserSessionManager(requireContext()).getPropertyId().toString(),UserSessionManager(requireContext()).getUserId().toString())
        retrofit.enqueue(object : Callback<GetRoomTypeData?> {
            override fun onResponse(
                call: Call<GetRoomTypeData?>,
                response: Response<GetRoomTypeData?>
            ) {
                if (response.isSuccessful){
                    Toast.makeText(requireContext(), "Mission SuccessFull", Toast.LENGTH_SHORT).show()
                    val data = response.body()!!.data
                    data.forEach{
                        val roomType = it.roomTypeName
                        roomTypeList.add(roomType)
                    }

                }
                else{
                    Toast.makeText(requireContext(), response.code().toString(), Toast.LENGTH_SHORT).show()
                    Log.e("error",response.message().toString())
                    Log.e("error",response.body().toString())
                }
            }

            override fun onFailure(call: Call<GetRoomTypeData?>, t: Throwable) {
                Toast.makeText(requireContext(), "Mission failed SuccessFully", Toast.LENGTH_SHORT).show()
            }
        })


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
            binding.mealPlanLayout.isVisible = false
            binding.masterRatePlanLayout.isVisible = false
        }

        binding.barRateType.setOnClickListener {
            replaceChildFrag(RatePlanBarFragment())
            binding.rateTypeET.setText("Bar")
            binding.roomTypeLayout.isVisible = true
            binding.mealPlanLayout.isVisible = false
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