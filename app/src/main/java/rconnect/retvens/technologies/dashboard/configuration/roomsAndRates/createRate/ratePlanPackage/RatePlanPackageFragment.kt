package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanPackage

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.Api.configurationApi.SingleConfiguration
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.AddInclusionDialogSheet
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.AddInclusionsAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.CreateRateData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.CreateRateTypeAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.RatePlanDetailsAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanBar.BarData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.GetCompanyDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.InclusionPlan
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.AddInclusionsDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.CreateInclusionDialogSheet
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetChargeRuleArray
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetPostingRuleArray
import rconnect.retvens.technologies.databinding.FragmentRatePlanPackageBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.max


class RatePlanPackageFragment(val barData : BarData) : Fragment(), AddInclusionsAdapter.OnUpdate, AddInclusionDialogSheet.OnInclusionAdd, CreateRateTypeAdapter.OnInclusionChange {
    private lateinit var binding: FragmentRatePlanPackageBinding

    var totalInclusionCharges = 0.00
    val packageRateAdjustmentArray = ArrayList<PackageRateAdjustmentData>()
    var selectedInclusionsList = ArrayList<InclusionPlan>()

    var minimumNights = 1
    var maximumNights = 1

    var percentage = ""
    var amount = ""

    var isPercentageType = true

    private  var postingRuleArray = ArrayList<String>()
    private  var chargeRuleArray = ArrayList<String>()

    private var mLastClickTime : Long = 0

    private var adjustment = 0.0
    private var packageTotal = 0.0

    private lateinit var progressDialog : Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRatePlanPackageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedInclusionsList = barData.inclusion


        setUpAddRemoveClicks()

        binding.percentageCard.strokeWidth = 2

        binding.percentageCard.setOnClickListener {
            binding.percentageCard.strokeWidth = 2
            binding.amountCard.strokeWidth = 0
            binding.adjustmentET.hint = "%"
            binding.adjistmentLayout.hint = "Discount %"
            isPercentageType = true
        }

        binding.amountCard.setOnClickListener {
            binding.percentageCard.strokeWidth = 0
            binding.amountCard.strokeWidth = 2
            binding.adjustmentET.hint = "₹"
            binding.adjistmentLayout.hint = "Discount ₹"
            isPercentageType = false
        }

        getPostingRule()
        getChargeRule()

        binding.packageTotalText.text = barData.ratePlanTotal.toString()
        packageTotal = barData.ratePlanTotal.toDouble()

        binding.addInclusions.setOnClickListener {
            // mis-clicking prevention, using threshold of 1000 ms
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return@setOnClickListener;
            }
            mLastClickTime = SystemClock.elapsedRealtime()

            val openDialog = AddInclusionDialogSheet(selectedInclusionsList)
            val fragManager = childFragmentManager
            fragManager.let{openDialog.show(it, AddInclusionDialogSheet.TAG)}
            openDialog.setOnAddInclusionDialogListener(this)
        }

        binding.adjustmentET.doAfterTextChanged {
            if ( binding.adjustmentET.text.toString() != "" &&  binding.adjustmentET.text.toString() != ".") {
                adjustment = binding.adjustmentET.text.toString().toDouble()
            }
        }

        binding.addImg.setOnClickListener {
            if (isPercentageType) {
                var packageAdjustment = packageTotal * (adjustment/100)
                Toast.makeText(requireContext(), "$packageAdjustment", Toast.LENGTH_SHORT).show()
                packageTotal += packageAdjustment
                packageTotal = String.format("%.2f", packageTotal).toDouble()
                binding.packageTotalText.text = packageTotal.toString()
            } else {
                packageTotal += adjustment
                binding.packageTotalText.text = packageTotal.toString()
            }
        }

        binding.removeImg.setOnClickListener {
            if (isPercentageType) {
                var packageAdjustment = String.format("%.2f", packageTotal * (adjustment/100) ).toDouble()
                Toast.makeText(requireContext(), "$packageAdjustment", Toast.LENGTH_SHORT).show()
                packageTotal -= packageAdjustment
                packageTotal = String.format("%.2f", packageTotal).toDouble()
                binding.packageTotalText.text = packageTotal.toString()
            } else {
                packageTotal -= adjustment
                binding.packageTotalText.text = packageTotal.toString()
            }
        }

        inclusionsRecycler()

        val continueBtn = requireActivity().findViewById<CardView>(R.id.continueBtnRate)
        continueBtn?.setOnClickListener {
            if (binding.ratePlanNameET.text!!.isEmpty()) {
                shakeAnimation(binding.ratePlanNameET, requireContext())
            } else if (binding.shortCodeText.text!!.isEmpty()) {
                shakeAnimation(binding.shortCodeLayout, requireContext())
            } else if (selectedInclusionsList.isEmpty()) {
                shakeAnimation(binding.addInclusions, requireContext())
            } else {
                if (isPercentageType) {
                    percentage = binding.adjustmentET.text.toString()
                } else {
                    amount = binding.adjustmentET.text.toString()
                }
                progressDialog = showProgressDialog(requireContext())
                saveData()
            }
        }

        binding.saveIC.setOnClickListener {
            if (binding.ratePlanNameET.text!!.isEmpty()) {
                shakeAnimation(binding.ratePlanNameET, requireContext())
            } else if (binding.shortCodeText.text!!.isEmpty()) {
                shakeAnimation(binding.shortCodeLayout, requireContext())
            } else if (selectedInclusionsList.isEmpty()) {
                shakeAnimation(binding.addInclusions, requireContext())
            } else {
                if (isPercentageType) {
                    percentage = binding.adjustmentET.text.toString()
                } else {
                    amount = binding.adjustmentET.text.toString()
                }
                progressDialog = showProgressDialog(requireContext())

                saveData()
            }
        }

    }

    private fun inclusionsRecycler() {

        totalInclusionCharges = 0.0

        selectedInclusionsList.forEach {
            totalInclusionCharges += it.rate.toDouble()
        }

        binding.recyclerInclusion.layoutManager = LinearLayoutManager(requireContext())

        val createRateTypeAdapter = CreateRateTypeAdapter(requireContext(), selectedInclusionsList, postingRuleArray, chargeRuleArray)
        binding.recyclerInclusion.adapter = createRateTypeAdapter
        createRateTypeAdapter.setOnInclusionChangeListener(this)
        createRateTypeAdapter.notifyDataSetChanged()
    }

    private fun saveData() {

        packageRateAdjustmentArray.add(
            PackageRateAdjustmentData(
            binding.adjustmentET.text.toString(),
            percentage,
            amount,
            binding.packageTotalText.text.toString()
            )
        )

        val send = OAuthClient<SingleConfiguration>(requireContext()).create(SingleConfiguration::class.java).addPackage(
            AddPackageDataClass(
                UserSessionManager(requireContext()).getUserId().toString(),
                UserSessionManager(requireContext()).getPropertyId().toString(),
                "Package",
                roomTypeId = barData.roomTypeId,
                "",
                binding.shortCodeText.text.toString(),
                selectedInclusionsList,
                binding.ratePlanNameET.text.toString(),
                binding.minNightsTextView.text.toString(),
                binding.maxNightsTextView.text.toString(),
                packageRateAdjustmentArray,
                "$totalInclusionCharges",
                binding.packageTotalText.text.toString()
            )
        )

        send.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                progressDialog.dismiss()
                if (response.isSuccessful){

                }
                Log.d("error", response.code().toString())
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                progressDialog.dismiss()
                Log.d("error", t.localizedMessage)
            }
        })
    }

    private fun setUpAddRemoveClicks() {

        binding.addMaxNights.setOnClickListener {
            maximumNights++
            binding.maxNightsTextView.text = "$maximumNights"
        }
        binding.removeMaxNights.setOnClickListener {
            if (maximumNights > 1) {
                maximumNights--
                binding.maxNightsTextView.text = "$maximumNights"
            }
        }

        binding.addMinNights.setOnClickListener {
            minimumNights++
            binding.minNightsTextView.text = "$minimumNights"
        }
        binding.removeMinNights.setOnClickListener {
            if (minimumNights > 1) {
                minimumNights--
                binding.minNightsTextView.text = "$minimumNights"
            }
        }

    }

    override fun onUpdateList(selectedList: ArrayList<GetInclusionsData>) {

        val seletedInclusion:ArrayList<InclusionPlan> = ArrayList()

        selectedList.forEach { getInclusionData ->
            val inclusionPlan = InclusionPlan(
                inclusionId = getInclusionData.inclusionId,
                inclusionType = getInclusionData.inclusionType,
                inclusionName = getInclusionData.inclusionName,
                postingRule = getInclusionData.postingRule,
                chargeRule = getInclusionData.chargeRule,
                rate = getInclusionData.charge
            )

            seletedInclusion.add(inclusionPlan)
        }
        binding.recyclerInclusion.layoutManager = LinearLayoutManager(requireContext())
        val createRateTypeAdapter = CreateRateTypeAdapter(requireContext(), seletedInclusion, postingRuleArray, chargeRuleArray)
        binding.recyclerInclusion.adapter = createRateTypeAdapter
        createRateTypeAdapter.notifyDataSetChanged()

        selectedList.forEach {
            totalInclusionCharges += it.charge.toDouble()
        }

    }

    private fun getPostingRule() {
        val get = RetrofitObject.dropDownApis.getPostingRulesModels()
        get.enqueue(object : Callback<GetPostingRuleArray?> {
            override fun onResponse(
                call: Call<GetPostingRuleArray?>,
                response: Response<GetPostingRuleArray?>
            ) {
                Log.d("error", response.code().toString())
                if (response.isSuccessful) {
                    try {
                        val data = response.body()!!.data
                        data.forEach {
                            postingRuleArray.add(it.postingRule)
                        }
                    } catch (e : Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<GetPostingRuleArray?>, t: Throwable) {
                Log.d("error", t.localizedMessage)
            }
        })
    }

    private fun getChargeRule() {
        val get = RetrofitObject.dropDownApis.getChargeRulesModels()
        get.enqueue(object : Callback<GetChargeRuleArray?> {
            override fun onResponse(
                call: Call<GetChargeRuleArray?>,
                response: Response<GetChargeRuleArray?>
            ) {
                if (response.isSuccessful) {
                    try {
                        val data = response.body()!!.data
                        data.forEach {
                            chargeRuleArray.add(it.chargeRule)
                        }
                    } catch (e : Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<GetChargeRuleArray?>, t: Throwable) {
                Log.d("error", t.localizedMessage)
            }
        })
    }

    override fun onInclusionAdded(selectedInclusionPlan: ArrayList<InclusionPlan>, totalInclusionCharges: Double) {
        selectedInclusionPlan.forEach {
            if (!selectedInclusionsList.contains(it)) {
                selectedInclusionsList.add(it)
            }
        }
        packageTotal += totalInclusionCharges
        binding.packageTotalText.text = packageTotal.toString()
        inclusionsRecycler()

    }

    override fun onInclusionDelete(item: InclusionPlan) {

        selectedInclusionsList.remove(item)
        barData.inclusion.remove(item)

        inclusionsRecycler()

        packageTotal -= item.rate.toDouble()
        binding.packageTotalText.text = packageTotal.toString()
    }

    override fun onInclusionUpdate(updatedInclusionList: ArrayList<InclusionPlan>) {
        selectedInclusionsList = updatedInclusionList
        inclusionsRecycler()
    }

    override fun onInclusionPriceUpdate(position: Int, price: String) {
        packageTotal -= selectedInclusionsList.get(position).rate.toDouble()
        selectedInclusionsList.get(position).rate = price
        packageTotal += price.toDouble()

        binding.packageTotalText.text = packageTotal.toString()

        inclusionsRecycler()
    }

}