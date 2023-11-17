package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanPackage

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
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
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.AddInclusionsAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.CreateRateData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.CreateRateTypeAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.RatePlanDetailsAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.AddInclusionsDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetChargeRuleArray
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetPostingRuleArray
import rconnect.retvens.technologies.databinding.FragmentRatePlanPackageBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.shakeAnimation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.max


class RatePlanPackageFragment : Fragment(), AddInclusionsAdapter.OnUpdate {
    private lateinit var binding: FragmentRatePlanPackageBinding

    var totalInclusionCharges = 0.00
    val packageRateAdjustmentArray = ArrayList<PackageRateAdjustmentData>()
    var selectedInclusionsList = ArrayList<GetInclusionsData>()

    var minimumNights = 1
    var maximumNights = 1

    var percentage = ""
    var amount = ""

    var isPercentageType = true

    private  var postingRuleArray = ArrayList<String>()
    private  var chargeRuleArray = ArrayList<String>()

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

        binding.addInclusions.setOnClickListener {
            openAddInclusionDialog()
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
                saveData()
            }
        }

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
                UserSessionManager(requireContext()).getRoomTypeId().toString(),
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
                if (response.isSuccessful){

                }
                Log.d("error", response.code().toString())
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
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
    private fun openAddInclusionDialog() {
        val dialog = Dialog(requireContext())
        dialog.setCancelable(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_add_inclusion)

        val createNewInclusionBtn = dialog.findViewById<TextView>(R.id.createNewInclusionBtn)
        createNewInclusionBtn.setOnClickListener {
            openCreateInclusionDialog()
        }

        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        cancel.setOnClickListener {
            dialog.dismiss()
        }

        val saveBtn = dialog.findViewById<CardView>(R.id.saveBtn)
        saveBtn.setOnClickListener {
            dialog.dismiss()
        }

        val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        val identity = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).getInclusionApi(
            UserSessionManager(requireContext()).getUserId().toString(), UserSessionManager(requireContext()).getPropertyId().toString())
        identity.enqueue(object : Callback<GetInclusionsDataClass?> {
            override fun onResponse(
                call: Call<GetInclusionsDataClass?>,
                response: Response<GetInclusionsDataClass?>
            ) {
                if (response.isSuccessful) {
                    val adapter = AddInclusionsAdapter(response.body()!!.data, requireContext())
                    recyclerView.adapter = adapter
                    adapter.setOnUpdateListener(this@RatePlanPackageFragment)
                    adapter.notifyDataSetChanged()
                } else {
                    Log.d("error", "${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetInclusionsDataClass?>, t: Throwable) {
                Log.d("error", t.localizedMessage)
            }
        })

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

    }
    private fun openCreateInclusionDialog() {
        val dialog = Dialog(requireContext())
        dialog.setCancelable(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_create_inclusion)

        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        cancel.setOnClickListener {
            dialog.dismiss()
        }

        val inclusionName = dialog.findViewById<TextInputEditText>(R.id.inclusionName)
        val inclusionType = dialog.findViewById<TextInputEditText>(R.id.inclusionType)
        val shortCode = dialog.findViewById<TextInputEditText>(R.id.shortCode)
        val charge = dialog.findViewById<TextInputEditText>(R.id.charge)
        val chargeRule = dialog.findViewById<TextInputEditText>(R.id.chargeRule)
        val postingRule = dialog.findViewById<TextInputEditText>(R.id.postingRule)

        val save = dialog.findViewById<CardView>(R.id.saveBtn)

        save.setOnClickListener {
            saveInclusion(requireContext(), dialog, shortCode.text.toString(), charge.text.toString(), inclusionName.text.toString(), inclusionType.text.toString(), chargeRule.text.toString(), postingRule.text.toString())
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

    }
    private fun saveInclusion(context: Context, dialog: Dialog, shortCodeTxt : String, charge:String, inclusionName:String, inclusionType:String, chargeRule:String, postingRule:String) {
        val create = OAuthClient<GeneralsAPI>(context).create(GeneralsAPI::class.java).addInclusionsApi(
            AddInclusionsDataClass(UserSessionManager(context).getUserId().toString(), UserSessionManager(context).getPropertyId().toString(), shortCodeTxt, charge, inclusionName, inclusionType, chargeRule, postingRule)
        )

        create.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                Log.d( "inclusion", "${response.code()} ${response.message()}")
//                setUpRecycler()
                dialog.dismiss()
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                Log.d("error", t.localizedMessage)
            }
        })
    }

    override fun onUpdateList(selectedList: ArrayList<GetInclusionsData>) {

        selectedInclusionsList = selectedList
        binding.recyclerInclusion.layoutManager = LinearLayoutManager(requireContext())
        val createRateTypeAdapter = CreateRateTypeAdapter(requireContext(), selectedList, postingRuleArray, chargeRuleArray)
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

}