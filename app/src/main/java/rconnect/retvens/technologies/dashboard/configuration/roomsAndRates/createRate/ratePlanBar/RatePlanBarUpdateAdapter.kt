package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanBar

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
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
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.AddInclusionDialogSheet
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.AddInclusionsAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.RatePlanDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.CreateRateTypeAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.AddCompanyRatePlanDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.InclusionPlan
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.AddInclusionsDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.CreateInclusionDialogSheet
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

class RatePlanBarUpdateAdapter(
    val applicationContext:Context,
    val supportFragmentManager: androidx.fragment.app.FragmentManager,
    val rateTypeList:ArrayList<UpdateRatePlanDataClass>):RecyclerView.Adapter<RatePlanBarUpdateAdapter.ViewHolder>(){

    private var updatedRateTypeList = ArrayList<UpdateRatePlanDataClass>()

    private var onRateTypeListChangeListener : OnRateTypeListChangeListener ?= null
    fun setOnListUpdateListener (listener : OnRateTypeListChangeListener) {
        onRateTypeListChangeListener = listener
    }
    interface OnRateTypeListChangeListener {
        fun onRateTypeListChanged(updatedRateTypeList: ArrayList<UpdateRatePlanDataClass>, position: Int)
        fun onRateTypeDelete(position: Int)
    }

    private var mLastClickTime : Long = 0

    lateinit var inclusionsArray : ArrayList<GetInclusionsData>

    init {
        getInclusions()
        getChargeRule()
        getPostingRule()
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
        var selectedInclusions = ArrayList<InclusionPlan>()
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
        if (currentData.inclusionPlan.size == 0) {
            holder.totalInclusions.text = "No Inclusions"
        } else {
            holder.totalInclusions.text = "${currentData.inclusionPlan.size} Inclusions"
        }
        holder.extraAdultRateTxt.text = currentData.extraAdultRate
        holder.extraChildRateTxt.text = currentData.extraChildRate
        holder.ratePlanTotalTxt.text = currentData.ratePlanTotal

        holder.selectedInclusions = rateTypeList[position].inclusionPlan

        holder.delete.visibility = View.GONE

        holder.delete.setOnClickListener {
            if (position in 0 until rateTypeList.size) {
                rateTypeList.removeAt(position)
                notifyItemRemoved(position)
            }
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
        holder.mealPlanETxt.setText("")

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


        var totalInclusionCharge = 0.00
        var ratePlanTotalTxtCalculated = 0.00
        var barRoomBaseRate = holder.barRoomBaseRateET.text.toString().toDouble()
        var mealCharges = holder.mealChargesET.text.toString().toDouble()

        if (holder.selectedInclusions.isNotEmpty()) {
            holder.selectedInclusions.forEach {
                totalInclusionCharge += it.rate.toDouble()
            }
        }

        ratePlanTotalTxtCalculated = mealCharges + barRoomBaseRate + totalInclusionCharge

        holder.totalInclusionChargesET.setText("$totalInclusionCharge")
        holder.ratePlanTotalTxtCalculated.setText("$ratePlanTotalTxtCalculated")
        holder.ratePlanTotalTxt.text = ("$ratePlanTotalTxtCalculated")
        Log.e("selectdInclusionList",holder.selectedInclusions.toString())
        holder.recycler_inclusion.layoutManager = LinearLayoutManager(applicationContext)
        val createRateTypeAdapter = CreateRateTypeAdapter(
            applicationContext,
            ArrayList(holder.selectedInclusions),
            postingRuleArray,
            chargeRuleArray
        )
        holder.recycler_inclusion.adapter = createRateTypeAdapter
        createRateTypeAdapter.notifyDataSetChanged()

        /*On Change Inclusion Posting Rule, Charge Rule, Rate, Interface Implementation from AddInclusion Dialog Sheet*/
        val onInclusionChangeInterface = object : CreateRateTypeAdapter.OnInclusionChange {

            override fun onInclusionDelete(item: InclusionPlan) {
                holder.selectedInclusions.remove(item)
                totalInclusionCharge = 0.0

                holder.selectedInclusions.forEach {
                    totalInclusionCharge += it.rate.toDouble()
                }
                barRoomBaseRate = holder.barRoomBaseRateET.text.toString().toDouble()
                mealCharges = holder.mealChargesET.text.toString().toDouble()
                ratePlanTotalTxtCalculated = mealCharges + barRoomBaseRate + totalInclusionCharge

                holder.totalInclusionChargesET.setText("$totalInclusionCharge")
                holder.ratePlanTotalTxtCalculated.setText("$ratePlanTotalTxtCalculated")
                holder.ratePlanTotalTxt.text = ("$ratePlanTotalTxtCalculated")
            }

            override fun onInclusionUpdate(updatedInclusionList: ArrayList<InclusionPlan>) {
                holder.selectedInclusions = updatedInclusionList

                totalInclusionCharge = 0.0

                holder.selectedInclusions.forEach {
                    totalInclusionCharge += it.rate.toDouble()
                }
                barRoomBaseRate = holder.barRoomBaseRateET.text.toString().toDouble()
                mealCharges = holder.mealChargesET.text.toString().toDouble()
                ratePlanTotalTxtCalculated = mealCharges + barRoomBaseRate + totalInclusionCharge

                holder.totalInclusionChargesET.setText("$totalInclusionCharge")
                holder.ratePlanTotalTxtCalculated.setText("$ratePlanTotalTxtCalculated")
                holder.ratePlanTotalTxt.text = ("$ratePlanTotalTxtCalculated")

            }

            override fun onInclusionPriceUpdate(position : Int, price : String) {
                holder.selectedInclusions.get(position).rate = price
                totalInclusionCharge = 0.0

                holder.selectedInclusions.forEach {
                    totalInclusionCharge += it.rate.toDouble()
                }
                barRoomBaseRate = holder.barRoomBaseRateET.text.toString().toDouble()
                mealCharges = holder.mealChargesET.text.toString().toDouble()
                ratePlanTotalTxtCalculated = mealCharges + barRoomBaseRate + totalInclusionCharge

                holder.totalInclusionChargesET.setText("$totalInclusionCharge")
                holder.ratePlanTotalTxtCalculated.setText("$ratePlanTotalTxtCalculated")
                holder.ratePlanTotalTxt.text = ("$ratePlanTotalTxtCalculated")
            }
        }


        /*On Add Inclusion Interface Implementation from AddInclusion Dialog Sheet*/
        val myInterfaceImplementation = object : AddInclusionDialogSheet.OnInclusionAdd {
            override fun onInclusionAdded(
                selectedInclusionPlan: ArrayList<InclusionPlan>,
                totalInclusionCharges: Double
            ) {

                holder.selectedInclusions.addAll(selectedInclusionPlan)

                holder.recycler_inclusion.layoutManager = LinearLayoutManager(applicationContext)
                val createRateTypeAdapter = CreateRateTypeAdapter(
                    applicationContext,
                    ArrayList(holder.selectedInclusions), // Create a new instance of the list
                    postingRuleArray,
                    chargeRuleArray
                )
                holder.recycler_inclusion.adapter = createRateTypeAdapter
                createRateTypeAdapter.setOnInclusionChangeListener(onInclusionChangeInterface)
                createRateTypeAdapter.notifyDataSetChanged()

                totalInclusionCharge = 0.0

                holder.selectedInclusions.forEach {
                    totalInclusionCharge += it.rate.toDouble()
                }
                barRoomBaseRate = holder.barRoomBaseRateET.text.toString().toDouble()
                mealCharges = holder.mealChargesET.text.toString().toDouble()
                ratePlanTotalTxtCalculated = mealCharges + barRoomBaseRate + totalInclusionCharge

                holder.totalInclusionChargesET.setText("$totalInclusionCharge")
                holder.ratePlanTotalTxtCalculated.setText("$ratePlanTotalTxtCalculated")
                holder.ratePlanTotalTxt.text = ("$ratePlanTotalTxtCalculated")

            }
        }

        holder.addInclusions.setOnClickListener {

            // mis-clicking prevention, using threshold of 1000 ms
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return@setOnClickListener;
            }
            mLastClickTime = SystemClock.elapsedRealtime()

            val openDialog = AddInclusionDialogSheet(holder.selectedInclusions)
            val fragManager = supportFragmentManager
            fragManager.let { openDialog.show(supportFragmentManager, CreateInclusionDialogSheet.TAG) }
            openDialog.setOnAddInclusionDialogListener(myInterfaceImplementation)
        }

        holder.ratePlanTotalTxtCalculated.doAfterTextChanged {
            if (holder.ratePlanTotalTxtCalculated.text.isNotEmpty() && holder.ratePlanTotalTxtCalculated.text.toString() != "."){
                var newT = holder.ratePlanTotalTxtCalculated.text.toString().toDouble()
                if ( newT > ratePlanTotalTxtCalculated) {
                    holder.roundUpET.setText("${newT - ratePlanTotalTxtCalculated}")
                } else {
                    holder.roundUpET.setText("${ratePlanTotalTxtCalculated - newT}")
                }
                holder.ratePlanTotalTxt.text = holder.ratePlanTotalTxtCalculated.text.toString()
            }
        }

        holder.saveIC.setOnClickListener {
            if (holder.selectedInclusions.isEmpty()) {
                shakeAnimation(holder.addInclusions, applicationContext)
            } else {
                holder.editCard.isVisible = false
                holder.detailCard.isVisible = true
                isRateCardEdit = true

                // Create a deep copy of the selectedInclusions
                val selectedInclusionsCopy = ArrayList(holder.selectedInclusions.map { it.copy() })

                rateTypeList[position].userId = currentData.userId
                rateTypeList[position].propertyId = currentData.propertyId
                rateTypeList[position].roomTypeId = currentData.roomTypeId
                rateTypeList[position].rateType = currentData.rateType
                rateTypeList[position].ratePlanName = holder.ratePlanEText.text.toString()
                rateTypeList[position].shortCode = holder.rateCode.text.toString()
                rateTypeList[position].inclusionPlan = selectedInclusionsCopy  // Use the copied list
                rateTypeList[position].roomBaseRate = holder.barRoomBaseRateET.text.toString()
                rateTypeList[position].mealCharge = holder.mealChargesET.text.toString()
                rateTypeList[position].inclusionCharge = holder.totalInclusions.text.toString()
                rateTypeList[position].roundUp = holder.roundUpET.text.toString()
                rateTypeList[position].extraAdultRate = holder.extraAdultMealRateET.text.toString()
                rateTypeList[position].extraChildRate = holder.extraChildMealRateET.text.toString()
                rateTypeList[position].ratePlanTotal = holder.ratePlanTotalTxtCalculated.text.toString()


                onRateTypeListChangeListener?.onRateTypeListChanged(rateTypeList, position)
                notifyItemChanged(position)
                Log.e("fuckingFinalList", rateTypeList.toString())
            }
        }



    }

    private  var postingRuleArray = ArrayList<String>()
    private  var chargeRuleArray = ArrayList<String>()

    fun getPostingRule() {
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

    fun getChargeRule() {
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

    fun getInclusions() {

        val identity = OAuthClient<GeneralsAPI>(applicationContext).create(GeneralsAPI::class.java).getInclusionApi(
            UserSessionManager(applicationContext).getUserId().toString(), UserSessionManager(applicationContext).getPropertyId().toString())
        identity.enqueue(object : Callback<GetInclusionsDataClass?> {
            override fun onResponse(
                call: Call<GetInclusionsDataClass?>,
                response: Response<GetInclusionsDataClass?>
            ) {
                if (response.isSuccessful) {
                    inclusionsArray = response.body()!!.data
                } else {
                    Log.d("error", "${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetInclusionsDataClass?>, t: Throwable) {
                Log.d("error", t.localizedMessage)
            }
        })

    }
}