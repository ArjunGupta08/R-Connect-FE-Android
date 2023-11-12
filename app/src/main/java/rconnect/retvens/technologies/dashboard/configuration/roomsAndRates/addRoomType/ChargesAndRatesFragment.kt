package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType

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
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.AmenitiesIconAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.AmenitiesIconDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.CreateRateData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.CreateRateTypeAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.RatePlanDetailsAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.AddInclusionsDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.InclusionsAdapter
import rconnect.retvens.technologies.databinding.FragmentChargesAndRatesBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChargesAndRatesFragment : Fragment(), AddInclusionsAdapter.OnUpdate {
    private lateinit var binding : FragmentChargesAndRatesBinding

    private val ratePlanDetailsList = ArrayList<RatePlanDataClass>()

    var inclusions = 0
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

        setUpRecycler()

        var minRate = 1000.00
        var maxRate = 5000.00
        var baseRate = 2500.00
        var adultRate = 1500.00
        var childRate = 600.00

        binding.removeMinimumRate.setOnClickListener {
            if (minRate>0){
                minRate -= 100
                binding.countMinimumRate.text = "₹ ${minRate}"
            }
            }
        binding.addMinimumRate.setOnClickListener {
            minRate += 100
            binding.countMinimumRate.text = "₹ ${minRate}"
        }

        binding.addMaximumCharges.setOnClickListener {
            maxRate += 100
            binding.countMaximumCharges.text = "₹ ${maxRate}" }
        binding.removeMaximumCharges.setOnClickListener {
            if (maxRate>0){
                maxRate -= 100
                binding.countMaximumCharges.text = "₹ ${maxRate}"
            }
        }

        binding.addBaseRate.setOnClickListener {
            baseRate+=100
            binding.countBaseRate.text = "₹ ${baseRate}"
        }
        binding.removeBaseRate.setOnClickListener {
            if (baseRate>0){
                baseRate-=100
                binding.countBaseRate.text = "₹ ${baseRate}"
            }
        }
        binding.addExtraAdultRate.setOnClickListener {
            adultRate+=100
            binding.countExtraAdultRate.text = "₹ ${adultRate}"
        }
        binding.removeExtraAdultRate.setOnClickListener {
            if (adultRate>0){
                adultRate-=100
                binding.countExtraAdultRate.text = "₹ ${adultRate}"
            }
        }
        binding.addMaxChildRate.setOnClickListener {
            childRate+=100
            binding.countMaxChildRate.text = "₹ ${childRate}"
        }
        binding.removeMaxChildRate.setOnClickListener {
            if (childRate>0){
                childRate-=100
                binding.countMaxChildRate.text = "₹ ${childRate}"
            }
        }

        binding.cpCheckBox.isChecked = false
        binding.epCheckBox.isChecked = false
        binding.apCheckBox.isChecked = false
        binding.mapCheckBox.isChecked = false

        binding.cpCheckBox.setOnClickListener {
            val selectedList: ArrayList<GetInclusionsData> = arrayListOf()

//            if (binding.cpCheckBox.isChecked) {
//                ratePlanDetailsList.remove(RatePlanDataClass( "Deluxe CP", "DLXCP", "CP",selectedList, binding.countExtraAdultRate.text.toString(), binding.countMaxChildRate.text.toString(), binding.countBaseRate.text.toString()))
//                setUpRecycler()
//                binding.cpCheckBox.isChecked = false
//            } else {
                ratePlanDetailsList.add(RatePlanDataClass( "Deluxe CP", "DLXCP", "CP",selectedList, binding.countExtraAdultRate.text.toString(), binding.countMaxChildRate.text.toString(), binding.countBaseRate.text.toString()))
                setUpRecycler()
                binding.cpCheckBox.isChecked = true
//            }
        }
        binding.epCheckBox.setOnClickListener {
            val selectedList: ArrayList<GetInclusionsData> = arrayListOf()

//            if (binding.epCheckBox.isChecked) {
//                ratePlanDetailsList.remove(RatePlanDataClass( "Deluxe EP", "DLXEP", "EP",selectedList, binding.countExtraAdultRate.text.toString(), binding.countMaxChildRate.text.toString(), binding.countBaseRate.text.toString()))
//                setUpRecycler()
//                binding.epCheckBox.isChecked = false
//            } else {
                ratePlanDetailsList.add(RatePlanDataClass( "Deluxe EP", "DLXEP", "EP",selectedList, binding.countExtraAdultRate.text.toString(), binding.countMaxChildRate.text.toString(), binding.countBaseRate.text.toString()))
                setUpRecycler()
                binding.epCheckBox.isChecked = true

//            }

        }
        binding.apCheckBox.setOnClickListener {

            val selectedList: ArrayList<GetInclusionsData> = arrayListOf()

//            if (binding.apCheckBox.isChecked) {
//                ratePlanDetailsList.remove(RatePlanDataClass( "Deluxe AP", "DLXAP", "AP",selectedList, binding.countExtraAdultRate.text.toString(), binding.countMaxChildRate.text.toString(), binding.countBaseRate.text.toString()))
//                setUpRecycler()
//                binding.apCheckBox.isChecked = false
//            } else {
                ratePlanDetailsList.add(RatePlanDataClass( "Deluxe AP", "DLXAP", "AP",selectedList, binding.countExtraAdultRate.text.toString(), binding.countMaxChildRate.text.toString(), binding.countBaseRate.text.toString()))
                setUpRecycler()
                binding.apCheckBox.isChecked = true
//            }

        }
        binding.mapCheckBox.setOnClickListener {

            val selectedList: ArrayList<GetInclusionsData> = arrayListOf()

//            if (binding.mapCheckBox.isChecked) {
//                ratePlanDetailsList.remove(RatePlanDataClass( "Deluxe MAP", "DLXMAP", "MAP",selectedList, binding.countExtraAdultRate.text.toString(), binding.countMaxChildRate.text.toString(), binding.countBaseRate.text.toString()))
//                setUpRecycler()
//                binding.mapCheckBox.isChecked = false
//            } else {
                ratePlanDetailsList.add(RatePlanDataClass( "Deluxe MAP", "DLXMAP", "MAP",selectedList, binding.countExtraAdultRate.text.toString(), binding.countMaxChildRate.text.toString(), binding.countBaseRate.text.toString()))
                setUpRecycler()
                binding.mapCheckBox.isChecked = true
//            }

        }

    }
    private fun setUpRecycler() {

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val ratePlanDetailsAdapter = RatePlanDetailsAdapter(requireContext(),ratePlanDetailsList)
        binding.recyclerView.adapter = ratePlanDetailsAdapter
        ratePlanDetailsAdapter.notifyDataSetChanged()

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

        val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        val identity = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).getInclusionApi(UserSessionManager(requireContext()).getUserId().toString(), UserSessionManager(requireContext()).getPropertyId().toString())
        identity.enqueue(object : Callback<GetInclusionsDataClass?> {
            override fun onResponse(
                call: Call<GetInclusionsDataClass?>,
                response: Response<GetInclusionsDataClass?>
            ) {

                if (isAdded) {
                    if (response.isSuccessful) {
                        val adapter = AddInclusionsAdapter(response.body()!!.data, requireContext())
                        recyclerView.adapter = adapter
                        adapter.setOnUpdateListener(this@ChargesAndRatesFragment)
                        adapter.notifyDataSetChanged()
                    } else {
                        Log.d("error", "${response.code()} ${response.message()}")
                    }
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
                setUpRecycler()
                dialog.dismiss()
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                Log.d("error", t.localizedMessage)
            }
        })
    }
    override fun onUpdateList(selectedList: ArrayList<GetInclusionsData>) {

        inclusions = selectedList.size

    }

}