package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.DescendantOffsetUtils
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.AddInclusionsAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.RatePlanDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.AddCompanyRatePlanDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.InclusionPlan
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.AddInclusionsDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetChargeRuleArray
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetPostingRuleArray
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.Const
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showDropdownMenu
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RatePlanDetailsAdapter(val applicationContext:Context, val rateTypeList:ArrayList<AddCompanyRatePlanDataClass>):RecyclerView.Adapter<RatePlanDetailsAdapter.ViewHolder>(){

    private var updatedRateTypeList = ArrayList<AddCompanyRatePlanDataClass>()

    private var onRateTypeListChangeListener : OnRateTypeListChangeListener ?= null
    fun setOnListUpdateListener (listener : OnRateTypeListChangeListener) {
        onRateTypeListChangeListener = listener
    }
    interface OnRateTypeListChangeListener {
        fun onRateTypeListChanged(updatedRateTypeList: AddCompanyRatePlanDataClass, position: Int)
        fun onDeleteButtonClick(updatedRateTypeList: AddCompanyRatePlanDataClass, position: Int)
    }
    class ViewHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {

        val ratePlanText = itemView.findViewById<TextView>(R.id.ratePlanText)
        val rateCode = itemView.findViewById<TextView>(R.id.rateCode)
        val totalInclusions = itemView.findViewById<TextView>(R.id.totalInclusions)
        val extraAdultRateTxt = itemView.findViewById<TextView>(R.id.extraAdultRateTxt)
        val extraChildRateTxt = itemView.findViewById<TextView>(R.id.extraChildRateTxt)
        val ratePlanTotalTxt = itemView.findViewById<TextView>(R.id.ratePlanTotalTxt)

        val delete = itemView.findViewById<ImageView>(R.id.delete)
        val edit = itemView.findViewById<ImageView>(R.id.edit)
        val saveIC = itemView.findViewById<CardView>(R.id.saveIC)
        val cancel = itemView.findViewById<TextView>(R.id.cancel)

        val detailCard = itemView.findViewById<CardView>(R.id.detailCard)
        val editCard = itemView.findViewById<CardView>(R.id.editCard)

        val addInclusions = itemView.findViewById<CardView>(R.id.addInclusions)
        val recycler_inclusion = itemView.findViewById<RecyclerView>(R.id.recycler_inclusion)
        val ratePlanEText = itemView.findViewById<TextInputEditText>(R.id.ratePlan_text)
        val rate_codeEText = itemView.findViewById<TextInputEditText>(R.id.rate_code_text)
        val mealPlanETxt = itemView.findViewById<TextInputEditText>(R.id.mealPlanETxt)

        val ratePlanTotalTxtCalculated = itemView.findViewById<EditText>(R.id.ratePlanTotalTxtCalculated)

        val barRoomBaseRateLayout = itemView.findViewById<TextInputLayout>(R.id.barRoomBaseRateLayout)
        val barRoomBaseRateET = itemView.findViewById<TextInputEditText>(R.id.barRoomBaseRateET)
        val mealChargesLayout = itemView.findViewById<TextInputLayout>(R.id.mealChargesLayout)
        val mealChargesET = itemView.findViewById<TextInputEditText>(R.id.mealChargesET)
        val totalInclusionChargesLayout = itemView.findViewById<TextInputLayout>(R.id.totalInclusionChargesLayout)
        val totalInclusionChargesET = itemView.findViewById<TextInputEditText>(R.id.totalInclusionChargesET)
        val extraChildMealRateLayout = itemView.findViewById<TextInputLayout>(R.id.extraChildMealRateLayout)
        val extraChildMealRateET = itemView.findViewById<TextInputEditText>(R.id.extraChildMealRateET)
        val roundUpET = itemView.findViewById<TextInputEditText>(R.id.roundUpET)
        val extraAdultMealRateLayout = itemView.findViewById<TextInputLayout>(R.id.extraAdultMealRateLayout)
        val extraAdultMealRateET = itemView.findViewById<TextInputEditText>(R.id.extraAdultMealRateET)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_rate_plan_details,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return rateTypeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val currentData = rateTypeList[position]

        var isRateCardEdit = true

        holder.ratePlanText.text = currentData.ratePlanName
        holder.rateCode.text = currentData.shortCode
        if (currentData.ratePlanInclusion.size == 0) {
            holder.totalInclusions.text = "No Inclusions"
        } else {
            holder.totalInclusions.text = "${currentData.ratePlanInclusion.size} Inclusions"
        }
        holder.extraAdultRateTxt.text = currentData.extraAdultRate
        holder.extraChildRateTxt.text = currentData.extraChildRate
        holder.ratePlanTotalTxt.text = currentData.ratePlanTotal

        holder.delete.setOnClickListener {
            onRateTypeListChangeListener?.onDeleteButtonClick(currentData, position)
        }

        holder.edit.setOnClickListener {
            if (isRateCardEdit) {
                holder.editCard.isVisible = true
                holder.detailCard.isVisible = false
                isRateCardEdit = false
            } else {
                holder.editCard.isVisible = false
                holder.detailCard.isVisible = true
                isRateCardEdit = true
            }
        }
        holder.cancel.setOnClickListener {
                holder.editCard.isVisible = false
                holder.detailCard.isVisible = true
                isRateCardEdit = true
        }

        holder.ratePlanEText.setText(currentData.ratePlanName)
        holder.rate_codeEText.setText(currentData.shortCode)
        holder.mealPlanETxt.setText(currentData.mealPlanName)

        try {
            holder.barRoomBaseRateET.setText(currentData.roomBaseRate.toString())
            holder.mealChargesET.setText(currentData.mealCharge.toString())
            holder.totalInclusionChargesET.setText(currentData.inclusionCharge.toString())
            holder.extraChildMealRateET.setText(currentData.extraChildRate.toString())
            holder.extraAdultMealRateET.setText(currentData.extraAdultRate.toString())
            holder.roundUpET.setText(currentData.roundUp)
            holder.ratePlanTotalTxtCalculated.setText(currentData.ratePlanTotal)
        } catch (e:Exception) {
            e.printStackTrace()
        }

        var totalInclusionCharges = 0.00

        getPostingRule()
        getChargeRule()

        var selectedInclusions = currentData.ratePlanInclusion
        holder.recycler_inclusion.layoutManager = LinearLayoutManager(applicationContext)
        val createRateTypeAdapter = CreateRateTypeAdapter(applicationContext, selectedInclusions, postingRuleArray, chargeRuleArray)
        holder.recycler_inclusion.adapter = createRateTypeAdapter
        createRateTypeAdapter.notifyDataSetChanged()

        val myInterfaceImplementation = object : AddInclusionsAdapter.OnUpdate {
            override fun onUpdateList(selectedList: ArrayList<GetInclusionsData>) {
                selectedList.forEach {
                    val currentItem = InclusionPlan(
                        it.inclusionId,
                        it.inclusionType,
                        it.inclusionName,
                        it.postingRule, it.chargeRule, it.charge)
                    if (!selectedInclusions.contains(currentItem)) {
                        selectedInclusions.add(currentItem)
                    }
                }
                holder.recycler_inclusion.layoutManager = LinearLayoutManager(applicationContext)
                val createRateTypeAdapter = CreateRateTypeAdapter(applicationContext, selectedInclusions, postingRuleArray, chargeRuleArray)
                holder.recycler_inclusion.adapter = createRateTypeAdapter
                createRateTypeAdapter.notifyDataSetChanged()

                selectedInclusions.forEach {
                    totalInclusionCharges += it.rate.toDouble()
                }

                holder.totalInclusionChargesET.setText("$totalInclusionCharges")

            }
        }

        holder.addInclusions.setOnClickListener {
            openAddInclusionDialog(myInterfaceImplementation)
        }

        var ratePlanTotalTxtCalculated = 0.00
        var barRoomBaseRate = 0.00
        var mealCharges = 0.00
        var extraAdultMealRate = 0.00
        var extraChildMealRate = 0.00


        holder.barRoomBaseRateET.doAfterTextChanged {
            if (holder.barRoomBaseRateET.text!!.isNotEmpty()) {
                if ( holder.barRoomBaseRateET.text.toString() == ".") {
                    holder.barRoomBaseRateET.setText("")
                } else {
                    barRoomBaseRate = holder.barRoomBaseRateET.text.toString().toDouble()
                    ratePlanTotalTxtCalculated = barRoomBaseRate + mealCharges + totalInclusionCharges + extraChildMealRate + extraAdultMealRate
                    holder.ratePlanTotalTxtCalculated.setText("$ratePlanTotalTxtCalculated")
                }
            } else {
                barRoomBaseRate = 0.00
                ratePlanTotalTxtCalculated = barRoomBaseRate + mealCharges + totalInclusionCharges + extraChildMealRate + extraAdultMealRate
                holder.ratePlanTotalTxtCalculated.setText("$ratePlanTotalTxtCalculated")
            }
        }

        holder.mealChargesET.doAfterTextChanged {
            if (holder.mealChargesET.text!!.isNotEmpty()) {
                if ( holder.mealChargesET.text.toString() == ".") {
                    holder.mealChargesET.setText("")
                } else {
                    mealCharges = holder.mealChargesET.text.toString().toDouble()
                    ratePlanTotalTxtCalculated = barRoomBaseRate + mealCharges + totalInclusionCharges + extraChildMealRate + extraAdultMealRate
                    holder.ratePlanTotalTxtCalculated.setText("$ratePlanTotalTxtCalculated")
                }
            } else {
                mealCharges = 0.00
                ratePlanTotalTxtCalculated = barRoomBaseRate + mealCharges + totalInclusionCharges + extraChildMealRate + extraAdultMealRate
                holder.ratePlanTotalTxtCalculated.setText("$ratePlanTotalTxtCalculated")
            }
        }

        holder.totalInclusionChargesET.doAfterTextChanged {
            if (holder.totalInclusionChargesET.text!!.isNotEmpty()) {
                if (holder.totalInclusionChargesET.text.toString() == ".") {
                    holder.totalInclusionChargesET.setText("")
                } else {
                    totalInclusionCharges = holder.totalInclusionChargesET.text.toString().toDouble()
                    ratePlanTotalTxtCalculated = barRoomBaseRate + mealCharges + totalInclusionCharges + extraChildMealRate + extraAdultMealRate
                    holder.ratePlanTotalTxtCalculated.setText("$ratePlanTotalTxtCalculated")
                }
            } else {
                totalInclusionCharges = 0.00
                ratePlanTotalTxtCalculated = barRoomBaseRate + mealCharges + totalInclusionCharges + extraChildMealRate + extraAdultMealRate
                holder.ratePlanTotalTxtCalculated.setText("$ratePlanTotalTxtCalculated")
            }
        }

        holder.extraChildMealRateET.doAfterTextChanged {
            if (holder.extraChildMealRateET.text!!.isNotEmpty()) {
                if (holder.extraChildMealRateET.text.toString() == ".") {
                    holder.extraChildMealRateET.setText("")
                } else {
                    extraChildMealRate = holder.extraChildMealRateET.text.toString().toDouble()
                    ratePlanTotalTxtCalculated = barRoomBaseRate + mealCharges + totalInclusionCharges + extraChildMealRate + extraAdultMealRate
                    holder.ratePlanTotalTxtCalculated.setText("$ratePlanTotalTxtCalculated")
                }
            } else {
                extraChildMealRate = 0.00
                ratePlanTotalTxtCalculated = barRoomBaseRate + mealCharges + totalInclusionCharges + extraChildMealRate + extraAdultMealRate
                holder.ratePlanTotalTxtCalculated.setText("$ratePlanTotalTxtCalculated")
            }
        }

        holder.extraAdultMealRateET.doAfterTextChanged {
            if (holder.extraAdultMealRateET.text!!.isNotEmpty()) {
                if (holder.extraAdultMealRateET.text.toString() == ".") {
                    holder.extraAdultMealRateET.setText("")
                } else {
                    extraAdultMealRate = holder.extraAdultMealRateET.text.toString().toDouble()
                    ratePlanTotalTxtCalculated = barRoomBaseRate + mealCharges + totalInclusionCharges + extraChildMealRate + extraAdultMealRate
                    holder.ratePlanTotalTxtCalculated.setText("$ratePlanTotalTxtCalculated")
                    holder.ratePlanTotalTxt.text = ("$ratePlanTotalTxtCalculated")
                }
            } else {
                extraAdultMealRate = 0.00
                ratePlanTotalTxtCalculated = barRoomBaseRate + mealCharges + totalInclusionCharges + extraChildMealRate + extraAdultMealRate
                holder.ratePlanTotalTxtCalculated.setText("$ratePlanTotalTxtCalculated")
                holder.ratePlanTotalTxt.text = ("$ratePlanTotalTxtCalculated")
            }
        }

        holder.ratePlanTotalTxtCalculated.doAfterTextChanged {
            if (holder.ratePlanTotalTxtCalculated.text.isNotEmpty() && holder.ratePlanTotalTxtCalculated.text.toString() != "."){
                holder.ratePlanTotalTxt.text = holder.ratePlanTotalTxtCalculated.text.toString()
            }
        }

        holder.saveIC.setOnClickListener {
            if (selectedInclusions.isEmpty()) {
                shakeAnimation(holder.addInclusions, applicationContext)
            } else {
                holder.editCard.isVisible = false
                holder.detailCard.isVisible = true
                isRateCardEdit = true

                val ratePlanDataClass = AddCompanyRatePlanDataClass(
                    "${currentData.userId}",
                    "${currentData.propertyId}",
                    "${currentData.roomTypeId}",
                    "${currentData.rateType}",
                    "${currentData.rateTypeId}",
                    "",
                    "${currentData.ratePlanName}",
                    "${currentData.mealPlanId}",
                    "${currentData.shortCode}",
                    selectedInclusions,
                    "${holder.barRoomBaseRateET.text.toString()}",
                    "${holder.mealChargesET.text.toString()}",
                    "${holder.totalInclusionChargesET.text.toString()}",
                    "${holder.roundUpET.text.toString()}",
                    "${holder.extraAdultMealRateET.text.toString()}",
                    "${holder.extraChildMealRateET.text.toString()}",
                    "${holder.ratePlanTotalTxtCalculated.text.toString()}",
                    "${holder.mealPlanETxt.text.toString()}",
                )
                Log.d("ratePlanData", ratePlanDataClass.toString())

                    updatedRateTypeList.add(ratePlanDataClass)


                onRateTypeListChangeListener?.onRateTypeListChanged(currentData, position)
            }
        }
    }

    private  var postingRuleArray = ArrayList<String>()
    private  var chargeRuleArray = ArrayList<String>()

    private fun openAddInclusionDialog(myInterfaceImplementation: AddInclusionsAdapter.OnUpdate) {
        val dialog = Dialog(applicationContext)
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
        recyclerView.layoutManager = GridLayoutManager(applicationContext, 2)

        val identity = OAuthClient<GeneralsAPI>(applicationContext).create(GeneralsAPI::class.java).getInclusionApi(
            UserSessionManager(applicationContext).getUserId().toString(), UserSessionManager(applicationContext).getPropertyId().toString())
        identity.enqueue(object : Callback<GetInclusionsDataClass?> {
            override fun onResponse(
                call: Call<GetInclusionsDataClass?>,
                response: Response<GetInclusionsDataClass?>
            ) {
                if (response.isSuccessful) {
                    try {
                        val adapter =
                            AddInclusionsAdapter(response.body()!!.data, applicationContext)
                        recyclerView.adapter = adapter
                        adapter.setOnUpdateListener(myInterfaceImplementation)
                        myInterfaceImplementation
                        adapter.notifyDataSetChanged()
                    }catch (e : Exception) {
                        e.printStackTrace()
                    }
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
        val dialog = Dialog(applicationContext)
        dialog.setCancelable(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_create_inclusion)

//        getPostingRule()
//        getChargeRule()

        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        cancel.setOnClickListener {
            dialog.dismiss()
        }

        val inclusionNameLayout = dialog.findViewById<TextInputLayout>(R.id.inclusionNameLayout)
        val shortCodeLayout = dialog.findViewById<TextInputLayout>(R.id.shortCodeLayout)
        val chargeLayout = dialog.findViewById<TextInputLayout>(R.id.chargeLayout)

        val inclusionName = dialog.findViewById<TextInputEditText>(R.id.inclusionName)
        val inclusionType = dialog.findViewById<TextInputEditText>(R.id.inclusionType)
        val shortCode = dialog.findViewById<TextInputEditText>(R.id.shortCode)
        val charge = dialog.findViewById<TextInputEditText>(R.id.charge)
        val chargeRule = dialog.findViewById<TextInputEditText>(R.id.chargeRule)
        val postingRule = dialog.findViewById<TextInputEditText>(R.id.postingRule)

        val save = dialog.findViewById<CardView>(R.id.saveBtn)

        val postingRuleLayout = dialog.findViewById<TextInputLayout>(R.id.postingRuleLayout)
        val chargeRuleLayout = dialog.findViewById<TextInputLayout>(R.id.chargeRuleLayout)

        postingRule.setOnClickListener {
            showDropdownMenu(applicationContext, postingRule, it, postingRuleArray)
        }

        chargeRule.setOnClickListener {
            showDropdownMenu(applicationContext, chargeRule, it, chargeRuleArray)
        }

        save.setOnClickListener {
            if (inclusionName.text!!.isEmpty()) {
                inclusionNameLayout.isErrorEnabled = true
                shakeAnimation(inclusionNameLayout, applicationContext)
            } else if (shortCode.text!!.isEmpty()) {
                inclusionNameLayout.isErrorEnabled = false
                shortCodeLayout.isErrorEnabled = true
                shakeAnimation(shortCodeLayout, applicationContext)
            } else if (chargeRule.text!!.isEmpty()) {
                inclusionNameLayout.isErrorEnabled = false
                shortCodeLayout.isErrorEnabled = false
                chargeRuleLayout.isErrorEnabled = true
                shakeAnimation(chargeRuleLayout, applicationContext)
            } else if (postingRule.text!!.isEmpty()) {
                inclusionNameLayout.isErrorEnabled = false
                shortCodeLayout.isErrorEnabled = false
                chargeRuleLayout.isErrorEnabled = false
                postingRuleLayout.isErrorEnabled = true
                shakeAnimation(postingRuleLayout, applicationContext)
            } else if (charge.text!!.isEmpty()) {
                inclusionNameLayout.isErrorEnabled = false
                shortCodeLayout.isErrorEnabled = false
                chargeRuleLayout.isErrorEnabled = false
                postingRuleLayout.isErrorEnabled = false
                chargeLayout.isErrorEnabled = true
                shakeAnimation(chargeLayout, applicationContext)
            } else {
                saveInclusion(
                    applicationContext,
                    dialog,
                    shortCode.text.toString(),
                    charge.text.toString(),
                    inclusionName.text.toString(),
                    inclusionType.text.toString(),
                    chargeRule.text.toString(),
                    postingRule.text.toString()
                )
            }
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