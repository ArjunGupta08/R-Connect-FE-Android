package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanBar

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
import rconnect.retvens.technologies.Api.configurationApi.SingleConfiguration
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.RatePlan.RatePlanAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.RatePlan.RatePlanFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.AddInclusionsAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.AddMealPlanAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.CreateRateData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.CreateRateTypeAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.CreateRateTypeFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.GetRoomType
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.RatePlanDetailsAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.AddCompanyRatePlanDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.InclusionPlan
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.AddInclusionsDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.mealPlan.GetMealPlanData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.mealPlan.GetMealPlanDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.mealPlan.MealPlanDataClass
import rconnect.retvens.technologies.databinding.FragmentRatePlanBarBinding
import rconnect.retvens.technologies.databinding.FragmentRatePlanUpdateBarBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.Const
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RatePlanBarUpdateFragment(val barId:String) : Fragment(),

    RoomTypePlanAdapter.OnRateTypeListChangeListener {
    private lateinit var progressDialog: Dialog
    private lateinit var binding : FragmentRatePlanUpdateBarBinding
    private  var roomTypePlanList:ArrayList<RoomTypePlanDataClass> = ArrayList()
    val rateList = ArrayList<CreateRateData>()
    private lateinit var ratePlanDetailsAdapter : RatePlanBarUpdateAdapter
    private var ratePlanDetailsList = ArrayList<AddBarsRatePlanDataClass>()
    private var onRateTypeListChangeListener : OnRateTypeListChangeListener ?= null
    private var getBarRates:ArrayList<UpdateRatePlanDataClass> = ArrayList()
    fun setOnListUpdateListener (listener : OnRateTypeListChangeListener) {
        onRateTypeListChangeListener = listener
    }

    interface OnRateTypeListChangeListener {
        fun onRateTypeListChanged(updatedRateTypeList: ArrayList<AddCompanyRatePlanDataClass>)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRatePlanUpdateBarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecycler()

        progressDialog = showProgressDialog(requireContext())
        getBarRatesData()


        val continueBtn = requireActivity().findViewById<CardView>(R.id.continueBtnRate)
        continueBtn?.setOnClickListener {
            progressDialog = showProgressDialog(requireContext())
            sendRatePlanData()
        }

    }

    private fun getBarRatesData() {

        val getData = OAuthClient<SingleConfiguration>(requireContext()).create(SingleConfiguration::class.java).getBarRatePlan(
            UserSessionManager(requireContext()).getUserId().toString(),
            barId
        )

        getData.enqueue(object : Callback<GetBarDataClass?> {
            override fun onResponse(
                call: Call<GetBarDataClass?>,
                response: Response<GetBarDataClass?>
            ) {
                if (response.isSuccessful){
                    progressDialog.dismiss()
                    val response = response.body()!!

                    val addBarsRatePlanDataClass = UpdateRatePlanDataClass(
                        UserSessionManager(requireContext()).getUserId().toString(),
                        UserSessionManager(requireContext()).getPropertyId().toString(),
                        "Bar",
                        response.data.roomTypeId,
                        response.data.ratePlanName,
                        response.data.shortCode,
                        response.data.inclusion,
                        response.data.barRates.roomBaseRate,
                        response.data.barRates.mealCharge,
                        response.data.barRates.inclusionCharge,
                        response.data.barRates.roundUp,
                        response.data.barRates.extraAdultRate,
                        response.data.barRates.extraChildRate,
                        response.data.barRates.ratePlanTotal
                    )
                    getBarRates.add(addBarsRatePlanDataClass)
                    ratePlanDetailsAdapter.notifyDataSetChanged()
                }else{
                    progressDialog.dismiss()
                    Log.e("error",response.code().toString())
                }
            }

            override fun onFailure(call: Call<GetBarDataClass?>, t: Throwable) {
                progressDialog.dismiss()
                Log.e("error",t.message.toString())

            }
        })

    }

    private fun sendRatePlanData() {

        ratePlanDetailsAdapter.rateTypeList.forEach {
            val send = OAuthClient<SingleConfiguration>(requireContext()).create(SingleConfiguration::class.java).updateBar(barId,it)
            send.enqueue(object : Callback<ResponseData?> {
                override fun onResponse(
                    call: Call<ResponseData?>,
                    response: Response<ResponseData?>
                ) {
                    if (response.isSuccessful) {
                        progressDialog.dismiss()
                        Toast.makeText(requireContext(),response.body()?.message,Toast.LENGTH_SHORT).show()
                        replaceFragment(RatePlanFragment())
                    } else {
                        progressDialog.dismiss()
                        Log.e("error", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                    progressDialog.dismiss()
                    Log.e("error", t.localizedMessage)
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
        ratePlanDetailsAdapter = RatePlanBarUpdateAdapter(requireContext(),childFragmentManager,getBarRates)
        binding.recyclerView.adapter = ratePlanDetailsAdapter
        ratePlanDetailsAdapter.notifyDataSetChanged()



    }

    override fun onRateTypeListChanged(updatedRateTypeList: ArrayList<AddBarsRatePlanDataClass>) {
        ratePlanDetailsList.addAll(updatedRateTypeList)
        Log.e("finalList",ratePlanDetailsList.toString())
        ratePlanDetailsAdapter.notifyDataSetChanged()

    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.dashboardFragmentContainer, fragment)
            transaction.commit()
        }
    }

}