package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.Api.configurationApi.SingleConfiguration
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.AddAmenitiesAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity.AmenityDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.AddMealPlanAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.RatePlanDetailsAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanBar.AddBarRatePlanDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanBar.AddBarsRatePlanDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.AddCompanyRatePlanDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.InclusionPlan
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.mealPlan.GetMealPlanData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.mealPlan.GetMealPlanDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.mealPlan.MealPlanAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.mealPlan.MealPlanDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.roomType.RoomTypeFragment
import rconnect.retvens.technologies.databinding.FragmentChargesAndRatesBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.Const
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChargesAndRatesFragment(val roomTypeId:String) : Fragment(),
    AddMealPlanAdapter.OnUpdate,
    RatePlanDetailsAdapter.OnRateTypeListChangeListener {

    private lateinit var binding : FragmentChargesAndRatesBinding

    private lateinit var ratePlanDataClass : AddBarsRatePlanDataClass
    private lateinit var ratePlanDetailsAdapter : RatePlanDetailsAdapter
    private  var selectedMealList:ArrayList<GetMealPlanData> = ArrayList()
    private var ratePlanDetailsList = ArrayList<AddBarsRatePlanDataClass>()
//    private val rateTypeList = ArrayList<RatePlanDataClass>()
    var minRate = 1000.00
    var maxRate = 5000.00
    var baseRate = 2500.00
    var adultRate = 1500.00
    var childRate = 600.00

    private lateinit var progressDialog : Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChargesAndRatesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handlePlusMinus()

        setUpRecycler()

        val continueBtn = requireActivity().findViewById<CardView>(R.id.continueBtn)
        continueBtn?.setOnClickListener {
                progressDialog = showProgressDialog(requireContext())
                updateRoomAndSendRatePlaneData()
        }

        binding.addMealPlanCard.setOnClickListener {
            openAddMealDialog()
        }

    }
    private fun updateRoomAndSendRatePlaneData() {
        Log.e("1", binding.countExtraAdultRate.text.toString())
        Log.e("2",binding.countBaseRate.text.toString())

        Log.e("propertyId",UserSessionManager(requireContext()).getPropertyId().toString())
        val update = OAuthClient<SingleConfiguration>(requireContext()).create(SingleConfiguration::class.java).updateRoomApi(
            roomTypeId,
            UpdateRoomData(
                UserSessionManager(requireContext()).getUserId().toString(),
                UserSessionManager(requireContext()).getPropertyId().toString(),
                binding.countBaseRate.text.toString(),
                binding.countExtraAdultRate.text.toString(),
                binding.countMaxChildRate.text.toString(),
                binding.countMinimumRate.text.toString() ,
                binding.countMaximumCharges.text.toString()
            )
        )
        update.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                if (isAdded){
                    Log.e("errorUpdateRoom","${response.message().toString()}, ${response.code().toString()}")
                    if (response.isSuccessful) {
                        val responseData = response.body()!!
                        Log.e("res",responseData.message.toString())
                        if (ratePlanDetailsList.size != 0){
                            sendRatePlanData()
                        }else{
                            Const.isAddingNewRoom = false

                            val childFragment: Fragment = RoomTypeFragment()
                            val transaction = requireActivity().supportFragmentManager.beginTransaction()
                            transaction.replace(R.id.dashboardFragmentContainer,childFragment)
                            transaction.commit()
                            progressDialog.dismiss()
                        }
                    } else {
                        progressDialog.dismiss()
                        Log.e("error",response.code().toString())
                    }
                }
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                progressDialog.dismiss()
                Log.e("errorUpdateRoom", t.localizedMessage)
            }
        })
    }

//    val addCompanyRatePlanDataClass = AddBarRatePlanDataClass(
//        UserSessionManager(requireContext()).getUserId().toString(),
//        UserSessionManager(requireContext()).getPropertyId().toString(),
//        "Company",
//        "${it.rateTypeId}",
//        "${it.ratePlanName}",
//        "${it.mealPlanId}",
//        "${it.shortCode}",
//        it.ratePlanInclusion,
//        "${it.roomBaseRate}",
//        "${it.mealCharge}",
//        "${it.inclusionCharge}",
//        "${it.roundUp}",
//        "${it.extraAdultRate}",
//        "${it.extraChildRate}",
//        "${it.ratePlanTotal}",
//    )

    private fun sendRatePlanData() {
        ratePlanDetailsList.forEach {
            val send = OAuthClient<SingleConfiguration>(requireContext()).create(SingleConfiguration::class.java).barRatePlanApi(it)
            send.enqueue(object : Callback<ResponseData?> {
                override fun onResponse(
                    call: Call<ResponseData?>,
                    response: Response<ResponseData?>
                ) {
                    progressDialog.dismiss()
                    if (response.isSuccessful) {

                        Const.isAddingNewRoom = false

                        val childFragment: Fragment = RoomTypeFragment()
                        val transaction = requireActivity().supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.dashboardFragmentContainer,childFragment)
                        transaction.commit()

                    } else {
                        Log.d("errorSend", response.message())
                    }
                }

                override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                    progressDialog.dismiss()
                    Log.d("errorSend", t.localizedMessage)
                }
            })
        }
    }

    private fun openAddMealDialog() {

        val dialog = Dialog(requireContext())
        dialog.setCancelable(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_add_inclusion)

        val aA = dialog.findViewById<TextView>(R.id.aA)
        aA.text = "Add Meal Plan"
        val create = dialog.findViewById<TextView>(R.id.createNewInclusionBtn)

        create.setOnClickListener {
            openCreateNewDialog()
        }

        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        cancel.setOnClickListener {
            dialog.dismiss()
        }

        val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        val mp = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).getMealPlanApi(UserSessionManager(requireContext()).getUserId().toString(), UserSessionManager(requireContext()).getPropertyId().toString())
        mp.enqueue(object : Callback<GetMealPlanDataClass?> {
            override fun onResponse(
                call: Call<GetMealPlanDataClass?>,
                response: Response<GetMealPlanDataClass?>
            ) {

                if (isAdded) {
                    if (response.isSuccessful) {
                        try {
                            val adapter = AddMealPlanAdapter(response.body()!!.data, requireContext())
                            recyclerView.adapter = adapter
                            adapter.setOnUpdateListener(this@ChargesAndRatesFragment)
                            adapter.notifyDataSetChanged()
                        } catch (e : NullPointerException){
                            print(e)
                        }
                    } else {
                        Log.d("error", "${response.code()} ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<GetMealPlanDataClass?>, t: Throwable) {
                Log.d("error", t.localizedMessage)
            }
        })

        val saveBtn = dialog.findViewById<CardView>(R.id.saveBtn)
        saveBtn.setOnClickListener {
            selectedMealList.forEach {
                val selectedInclusionList: ArrayList<InclusionPlan> = arrayListOf()

                ratePlanDataClass =  AddBarsRatePlanDataClass(
                    UserSessionManager(requireContext()).getUserId().toString(),
                    UserSessionManager(requireContext()).getPropertyId().toString(),
                    roomTypeId,
                    "Bar",
                    "",
                    "",
                    "${Const.addedRoomTypeName} ${it.shortCode}",
                    "${it.mealPlanId}",
                    "${Const.addedRoomTypeShortCode}${it.shortCode}",
                    selectedInclusionList,
                    binding.countBaseRate.text.toString(),
                    "${it.chargesPerOccupancy}",
                    "",
                    "0.00",
                    binding.countExtraAdultRate.text.toString(),
                    binding.countMaxChildRate.text.toString(),
                    "${binding.countBaseRate.text.toString()}",
                    "${it.mealPlanName}"
                )
                if (!ratePlanDetailsList.contains(ratePlanDataClass)) {
                    ratePlanDetailsList.add(ratePlanDataClass)
                    ratePlanDetailsAdapter.notifyDataSetChanged()
                }
            }
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
    }
    private fun openCreateNewDialog() {
        val dialog = Dialog(requireContext()) // Use 'this' as the context, assuming this code is within an Activity
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dialog_create_meal_plan)
        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent) // Makes the background transparent
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        val mealPlanName = dialog.findViewById<TextInputEditText>(R.id.mealPlanName)
        val shortCode = dialog.findViewById<TextInputEditText>(R.id.shortCode)
        val chargesPerOccupancy = dialog.findViewById<TextInputEditText>(R.id.chargesPerOccupancy)

        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        val save = dialog.findViewById<CardView>(R.id.saveBtn)

        cancel.setOnClickListener {
            dialog.dismiss()
        }
        save.setOnClickListener {
            saveMealPlan(requireContext(), dialog, mealPlanName.text.toString(), shortCode.text.toString(), chargesPerOccupancy.text.toString())
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.END)

        dialog.show()

    }
    private fun saveMealPlan(context: Context, dialog: Dialog, mealPlanName: String, shortCode: String, chargesPerOccupancy: String) {
        val mP = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).postMealPlanApi(
            MealPlanDataClass(UserSessionManager(requireContext()).getUserId().toString(), UserSessionManager(requireContext()).getPropertyId().toString(), mealPlanName, shortCode, chargesPerOccupancy)
        )

        mP.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                if (isAdded){
                    if (response.isSuccessful) {
                        dialog.dismiss()
                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("error", "${response.code()} ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                Log.d("error", t.localizedMessage)
            }
        })

    }

    private fun setUpRecycler() {

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        ratePlanDetailsAdapter = RatePlanDetailsAdapter(requireContext(), childFragmentManager,ratePlanDetailsList)
        binding.recyclerView.adapter = ratePlanDetailsAdapter
        ratePlanDetailsAdapter.notifyDataSetChanged()
        ratePlanDetailsAdapter.getChargeRule()
        ratePlanDetailsAdapter.getPostingRule()
        ratePlanDetailsAdapter.getInclusions()
        ratePlanDetailsAdapter.setOnListUpdateListener(this)

    }

    private fun handlePlusMinus() {
        binding.removeMinimumRate.setOnClickListener {
            if (minRate>0){
                minRate -= 100
                binding.countMinimumRate.text = "${minRate}"
            }
        }
        binding.addMinimumRate.setOnClickListener {
            minRate += 100
            binding.countMinimumRate.text = "${minRate}"
        }

        binding.addMaximumCharges.setOnClickListener {
            maxRate += 100
            binding.countMaximumCharges.text = "${maxRate}" }
        binding.removeMaximumCharges.setOnClickListener {
            if (maxRate>0){
                maxRate -= 100
                binding.countMaximumCharges.text = "${maxRate}"
            }
        }

        binding.addBaseRate.setOnClickListener {
            baseRate+=100
            binding.countBaseRate.text = "${baseRate}"
        }
        binding.removeBaseRate.setOnClickListener {
            if (baseRate>0){
                baseRate-=100
                binding.countBaseRate.text = "${baseRate}"
            }
        }
        binding.addExtraAdultRate.setOnClickListener {
            adultRate+=100
            binding.countExtraAdultRate.text = "${adultRate}"
        }
        binding.removeExtraAdultRate.setOnClickListener {
            if (adultRate>0){
                adultRate-=100
                binding.countExtraAdultRate.text = "${adultRate}"
            }
        }
        binding.addMaxChildRate.setOnClickListener {
            childRate+=100
            binding.countMaxChildRate.text = "${childRate}"
        }
        binding.removeMaxChildRate.setOnClickListener {
            if (childRate>0){
                childRate-=100
                binding.countMaxChildRate.text = "${childRate}"
            }
        }
    }

    override fun onUpdateMealPlan(selectedList: ArrayList<GetMealPlanData>) {
        selectedMealList.clear()
        selectedMealList.addAll(selectedList)

    }


    override fun onRateTypeListChanged(updatedRateTypeList: AddBarsRatePlanDataClass, position : Int) {
        ratePlanDetailsList.removeAt(position)
        ratePlanDetailsList.add(updatedRateTypeList)
        Log.d("ratePlanDL", ratePlanDetailsList.toString())
        setUpRecycler()
    }

    override fun onRateTypeDelete(position: Int) {
        ratePlanDetailsList.removeAt(position)
    }
}