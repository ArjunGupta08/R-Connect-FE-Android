package rconnect.retvens.technologies.dashboard.configuration.CorporateRates.AddCompany

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.Api.DropDownApis
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.configurationApi.SingleConfiguration
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.CorporateRates.CorporatesPartnersFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.GetRoomType
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.GetRoomTypeData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanBar.GetMealData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanBar.GetMealPlanItem
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanBar.RatePlanBarFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanBar.RoomTypePlanDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.AddCompanyRatePlanDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.RatePlanCompanyFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.RoomTypeCompanyPlanAdapter
import rconnect.retvens.technologies.databinding.FragmentCorporatePlanBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CorporatePlanFragment(val companyId:String) : Fragment(),
    RoomTypeCompanyPlanAdapter.OnRateTypeListChangeListener {

    private lateinit var binding:FragmentCorporatePlanBinding
    public var roomTypeList = ArrayList<GetRoomType>()
    private val selectedItemsList = mutableListOf<Int>()
    private val selectedItemsMealList = mutableListOf<Int>()
    private var roomList:ArrayList<GetRoomType> = ArrayList()
    private lateinit var progressDialog: Dialog
    private  var roomTypePlanList:ArrayList<RoomTypePlanDataClass> = ArrayList()
    private lateinit var ratePlanDetailsAdapter : RoomTypeCompanyPlanAdapter
    private  var mealList:ArrayList<GetMealPlanItem> = ArrayList()
    private  var mealPlanList:ArrayList<GetMealPlanItem> = ArrayList()
    private var ratePlanDetailsList = ArrayList<AddCompanyRatePlanDataClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCorporatePlanBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getRateType()
        getMealPlan()

        setUpRecycler()

        binding.dropRoom.setOnClickListener {
            showMultiChoiceDialog()
        }

        binding.mealPlanET.setOnClickListener {
            showMultiChoiceMealDialog()
        }

        val save = requireActivity().findViewById<CardView>(R.id.continueBtn)
        save?.setOnClickListener {
            Log.e("click","checkingg")
            progressDialog = showProgressDialog(requireContext())
            sendData()
        }

    }

    private fun sendData() {
        ratePlanDetailsList.forEach {
            val send = OAuthClient<SingleConfiguration>(requireContext()).create(SingleConfiguration::class.java).addCompanyRatePlanApi(it)
            send.enqueue(object : Callback<ResponseData?> {
                override fun onResponse(
                    call: Call<ResponseData?>,
                    response: Response<ResponseData?>
                ) {
                    if (response.isSuccessful){
                        progressDialog.dismiss()
                        val response = response.body()!!
                        Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                        replaceFragment(CorporatesPartnersFragment())
                    }else{
                        progressDialog.dismiss()
                        Log.e("error",response.code().toString())
                    }
                }

                override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                    progressDialog.dismiss()
                    Log.d("error", t.localizedMessage)
                }
            })
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment !=null){
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.dashboardFragmentContainer,fragment)
            transaction.commit()
        }


    }

    private fun setUpRecycler() {
        roomList.forEach {
            roomTypePlanList.add(RoomTypePlanDataClass(it.propertyId,it.roomTypeId,it.roomTypeName,it.extraAdultRate,it.extraChildRate,it.baseRate,mealList))
        }


        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        ratePlanDetailsAdapter = RoomTypeCompanyPlanAdapter(requireContext(),roomTypePlanList,companyId,childFragmentManager)
        binding.recyclerView.adapter = ratePlanDetailsAdapter
        ratePlanDetailsAdapter.notifyDataSetChanged()

        ratePlanDetailsAdapter.setOnListUpdateListener(this)
    }

    override fun onRateTypeListChanged(updatedRateTypeList: ArrayList<AddCompanyRatePlanDataClass>) {
        ratePlanDetailsList.clear()
        ratePlanDetailsList.addAll(updatedRateTypeList)
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
                roomList.add(roomTypeList[i])

            } else {
                // When checkbox unselected
                // Remove position from selected list
                selectedItemsList.remove(i)
                // Remove corresponding room type from selectedRoomTypeList
                roomList.removeAll { it == roomTypeList[i] }
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
            for (j in roomList.indices) {
                // Concat array value
                stringBuilder.append(roomList[j].roomTypeName)
                // Check condition
                if (j != roomList.size - 1) {
                    // When j value not equal to selected list size - 1
                    // Add comma
                    stringBuilder.append(", ")
                }
            }

            binding.dropRoom.setText(stringBuilder.toString())
            setUpRecycler()

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
            roomList.clear()
            // Clear text view value
            binding.dropRoom.setText("")
            setUpRecycler()
        }

        // Show dialog
        builder.show()


    }

    private fun getRateType() {
        val retrofit = OAuthClient<DropDownApis>(requireContext()).create(DropDownApis::class.java).getRoomList(
            UserSessionManager(requireContext()).getPropertyId().toString(),
            UserSessionManager(requireContext()).getUserId().toString())
        retrofit.enqueue(object : Callback<GetRoomTypeData?> {
            override fun onResponse(
                call: Call<GetRoomTypeData?>,
                response: Response<GetRoomTypeData?>
            ) {
                if (response.isSuccessful){
//                    Toast.makeText(requireContext(), "Mission SuccessFull", Toast.LENGTH_SHORT).show()
                    val data = response.body()!!.data
                    roomTypeList.addAll(data)
                    Log.e("ress",data.toString())
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
                mealList.add(mealPlanList[i])
            } else {
                // When checkbox unselected
                // Remove position from selected list
                selectedItemsMealList.remove(i)
                // Remove corresponding room type from selectedRoomTypeList
                mealList.removeAll { it == mealPlanList[i] }
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
            for (j in mealList.indices) {
                // Concat array value
                stringBuilder.append(mealList[j].mealPlanName)
                // Check condition
                if (j != mealList.size - 1) {
                    // When j value not equal to selected list size - 1
                    // Add comma
                    stringBuilder.append(", ")
                }
            }
            // Set text on textView
            binding.mealPlanET.setText(stringBuilder.toString())
            setUpRecycler()
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
            mealList.clear()
            // Clear text view value
            binding.mealPlanET.setText("")
            setUpRecycler()
        }

        // Show dialog
        builder.show()
    }


}