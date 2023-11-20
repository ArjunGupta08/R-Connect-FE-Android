package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate

import android.app.AlertDialog
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
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanBar.GetMealData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanBar.GetMealPlanItem
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanBar.RatePlanBarAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanBar.RatePlanBarFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.AddCompanyRatePlanDataClass
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

class CreateRateTypeFragment : Fragment(), RatePlanBarAdapter.OnRateTypeListChangeListener,
    RatePlanBarFragment.OnRateTypeListChangeListener {

    lateinit var binding:FragmentCreateRateTypeBinding

    public var roomTypeList = ArrayList<GetRoomType>()
    private var isRateDropDownOpen = false
    private lateinit var selectedRoomType: BooleanArray
    private lateinit var selectedMealPlan:BooleanArray
    private val langList = mutableListOf<Int>()
    private val selectedItemsList = mutableListOf<Int>()
    private val selectedRoomTypeList = ArrayList<GetRoomType>()
    private var mealPlanList = ArrayList<GetMealPlanItem>()
    private val selectedItemsMealList = mutableListOf<Int>()
    private val selectedMealList = ArrayList<GetMealPlanItem>()
    var type = ""
    private val ratePlanBarList:ArrayList<AddCompanyRatePlanDataClass> = ArrayList()
    interface DataUpdateListener {
        fun onDataUpdated(updatedDataList: ArrayList<GetRoomType>)
    }

    // Instance of the listener
    private var dataUpdateListener: DataUpdateListener? = null

    // Function to set the listener
    fun setDataUpdateListener(listener: DataUpdateListener) {
        this.dataUpdateListener = listener
    }

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
        getMealPlan()

        selectedRoomType = BooleanArray(roomTypeList.size)
        selectedMealPlan = BooleanArray(mealPlanList.size)


        val options = arrayOf("Deluxe", "Premium", "Elite")

        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item1, roomTypeList)
        // Set a click listener for the end icon



        binding.dropRoom.setOnClickListener {
            showMultiChoiceDialog()
        }

        binding.mealPlanET.setOnClickListener {
            showMultiChoiceMealDialog()
        }


    }

    private fun showMultiChoiceDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val selectedItems = BooleanArray(roomTypeList.size)

        builder.setMultiChoiceItems(roomTypeList.map { it.roomTypeName }.toTypedArray(), selectedItems) { _, i, b ->
            // Check condition
            if (b) {
                // When checkbox selected
                // Add position in selected list
                selectedItemsList.add(i)
                // Add corresponding room type to selectedRoomTypeList
                selectedRoomTypeList.add(roomTypeList[i])

            } else {
                // When checkbox unselected
                // Remove position from selected list
                selectedItemsList.remove(i)
                // Remove corresponding room type from selectedRoomTypeList
                selectedRoomTypeList.removeAll { it == roomTypeList[i] }
            }
        }

        // Initialize selectedItems array based on selectedItemsList
        for (position in selectedItemsList) {
            selectedItems[position] = true
        }

        builder.setPositiveButton("OK") { _, _ ->
            // Initialize string builder
            val stringBuilder = StringBuilder()
            // Use for loop
            for (j in selectedRoomTypeList.indices) {
                // Concat array value
                stringBuilder.append(selectedRoomTypeList[j].roomTypeName)
                // Check condition
                if (j != selectedRoomTypeList.size - 1) {
                    // When j value not equal to selected list size - 1
                    // Add comma
                    stringBuilder.append(", ")
                }
            }

            // Set text on textView
            if (type == "Bar"){
                replaceChildFrag(RatePlanBarFragment(selectedRoomTypeList,selectedMealList,false))
            }else if (type == "Company"){
                    Log.e("check11",type.toString())
                replaceChildFrag(RatePlanCompanyFragment(selectedRoomTypeList,selectedMealList,false))
            }
            binding.dropRoom.setText(stringBuilder.toString())
        }

        builder.setNegativeButton("Cancel") { dialogInterface, _ ->
            // Dismiss dialog
            dialogInterface.dismiss()
        }

        builder.setNeutralButton("Clear All") { _, _ ->
            // Use for loop
            for (j in selectedItems.indices) {
                // Remove all selection
                selectedItems[j] = false
            }
            // Clear the selected items list
            selectedItemsList.clear()
            // Clear the selected room type list
            selectedRoomTypeList.clear()
            // Clear text view value
            binding.dropRoom.setText("")
        }

        // Show dialog
        builder.show()


    }

    private fun updateDataList() {
        // Perform your logic to update dataList

        // Notify the child fragment about the update
        dataUpdateListener?.onDataUpdated(roomTypeList)
    }


    private fun getMealPlan() {
        val mp = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).getMealPlanApi2(UserSessionManager(requireContext()).getUserId().toString(), UserSessionManager(requireContext()).getPropertyId().toString())
        mp.enqueue(object : Callback<GetMealData?> {
            override fun onResponse(
                call: Call<GetMealData?>,
                response: Response<GetMealData?>
            ) {

                if (isAdded) {
                    if (response.isSuccessful) {
                        try {
                            val response = response.body()!!
                            mealPlanList.addAll(response.data)

                            Log.e("mealPlanList",response.toString())
                        } catch (e : NullPointerException){
                            print(e)
                        }
                    } else {
                        Log.d("error", "${response.code()} ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<GetMealData?>, t: Throwable) {
                Log.d("error", t.localizedMessage)
            }
        })
    }

    private fun showMultiChoiceMealDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val selectedItems = BooleanArray(mealPlanList.size)

        builder.setMultiChoiceItems(mealPlanList.map { it.mealPlanName }.toTypedArray(), selectedItems) { _, i, b ->
            // Check condition
            if (b) {
                // When checkbox selected
                // Add position in selected list
                selectedItemsMealList.add(i)
                // Add corresponding room type to selectedRoomTypeList
                selectedMealList.add(mealPlanList[i])
            } else {
                // When checkbox unselected
                // Remove position from selected list
                selectedItemsMealList.remove(i)
                // Remove corresponding room type from selectedRoomTypeList
                selectedMealList.removeAll { it == mealPlanList[i] }
            }
        }

        // Initialize selectedItems array based on selectedItemsList
        for (position in selectedItemsMealList) {
            selectedItems[position] = true
        }

        builder.setPositiveButton("OK") { _, _ ->
            // Initialize string builder
            val stringBuilder = StringBuilder()
            // Use for loop
            for (j in selectedMealList.indices) {
                // Concat array value
                stringBuilder.append(selectedMealList[j].mealPlanName)
                // Check condition
                if (j != selectedMealList.size - 1) {
                    // When j value not equal to selected list size - 1
                    // Add comma
                    stringBuilder.append(", ")
                }
            }
            // Set text on textView
            binding.mealPlanET.setText(stringBuilder.toString())
            if (type == "Bar"){
                replaceChildFrag(RatePlanBarFragment(selectedRoomTypeList,selectedMealList,false))
            }else if (type == "Company"){
                Log.e("check11",type.toString())
                replaceChildFrag(RatePlanCompanyFragment(selectedRoomTypeList,selectedMealList,false))
            }
            Log.e("list",selectedMealList.toString())
        }

        builder.setNegativeButton("Cancel") { dialogInterface, _ ->
            // Dismiss dialog
            dialogInterface.dismiss()
        }

        builder.setNeutralButton("Clear All") { _, _ ->
            // Use for loop
            for (j in selectedItems.indices) {
                // Remove all selection
                selectedItems[j] = false
            }
            // Clear the selected items list
            selectedItemsMealList.clear()
            // Clear the selected room type list
            selectedMealList.clear()
            // Clear text view value
            binding.mealPlanET.setText("")
        }

        // Show dialog
        builder.show()
    }
    private fun showDropdownMenu(adapter: ArrayAdapter<GetRoomType>, anchorView: View) {
        val listPopupWindow = ListPopupWindow(requireContext())
        listPopupWindow.setAdapter(adapter)
        listPopupWindow.anchorView = anchorView
        listPopupWindow.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = adapter.getItem(position)
            binding.dropRoom.setText(selectedItem?.roomTypeName)
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
//                    Toast.makeText(requireContext(), "Mission SuccessFull", Toast.LENGTH_SHORT).show()
                    val data = response.body()!!.data
                    roomTypeList.addAll(data)

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
            replaceChildFrag(RatePlanCompanyFragment(selectedRoomTypeList,selectedMealList,false))
            binding.rateTypeET.setText("Company")
            type = "Company"
            binding.companyNameLayout.isVisible = true
            binding.roomTypeLayout.isVisible = true
            binding.mealPlanLayout.isVisible = true
            binding.masterRatePlanLayout.isVisible = false
        }

        binding.barRateType.setOnClickListener {
            val ratePlanBarFragment = RatePlanBarFragment(selectedRoomTypeList,selectedMealList,false)
            ratePlanBarFragment.setOnListUpdateListener(this)
            replaceChildFrag(ratePlanBarFragment)
            binding.rateTypeET.setText("Bar")
            type = "Bar"
            binding.roomTypeLayout.isVisible = true
            binding.mealPlanLayout.isVisible = true
            binding.companyNameLayout.isVisible = false
            binding.masterRatePlanLayout.isVisible = false
        }

        binding.discountRateType.setOnClickListener {
            replaceChildFrag(RatePlanDiscountFragment())
            binding.rateTypeET.setText("Discount")
            type = "Discount"
            binding.companyNameLayout.isVisible = false
            binding.mealPlanLayout.isVisible = false
            binding.roomTypeLayout.isVisible = false
            binding.masterRatePlanLayout.isVisible = false
        }

        binding.packageRateType.setOnClickListener {
            replaceChildFrag(RatePlanPackageFragment())
            binding.rateTypeET.setText("Package")
            type = "Package"
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

    override fun onRateTypeListChanged(updatedRateTypeList: ArrayList<AddCompanyRatePlanDataClass>) {
        ratePlanBarList.addAll(updatedRateTypeList)
        Log.e("ratePlanBarList",ratePlanBarList.toString())
    }

}