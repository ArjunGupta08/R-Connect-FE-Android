package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.generateShortCode
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showDropdownMenu
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateInclusionDialogSheet(
    val currentData : GetInclusionsData ?= GetInclusionsData(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        ""
    )
) : DialogFragment() {

    private var inclusionListener : OnInclusionSave ?= null
    fun setOnInclusionDialogListener(listener : OnInclusionSave) {
        inclusionListener = listener
    }
    interface OnInclusionSave {
        fun onInclusionSave()
    }

    companion object {
        const val TAG = "AddRecipientWalletSheet"
    }


    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    private var postingRuleArray = ArrayList<String>()
    private var chargeRuleArray = ArrayList<String>()

    private lateinit var progressDialog : Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_create_inclusion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getPostingRule()
        getChargeRule()

        val postingRuleLayout = view.findViewById<TextInputLayout>(R.id.postingRuleLayout)
        val chargeRuleLayout = view.findViewById<TextInputLayout>(R.id.chargeRuleLayout)
        val inclusionNameLayout = view.findViewById<TextInputLayout>(R.id.inclusionNameLayout)
        val shortCodeLayout = view.findViewById<TextInputLayout>(R.id.shortCodeLayout)
        val chargeLayout = view.findViewById<TextInputLayout>(R.id.chargeLayout)

        val inclusionName = view.findViewById<TextInputEditText>(R.id.inclusionName)
        val inclusionType = view.findViewById<TextInputEditText>(R.id.inclusionType)
        val shortCode = view.findViewById<TextInputEditText>(R.id.shortCode)
        val charge = view.findViewById<TextInputEditText>(R.id.charge)
        val chargeRule = view.findViewById<TextInputEditText>(R.id.chargeRule)
        val postingRule = view.findViewById<TextInputEditText>(R.id.postingRule)

        inclusionName.doAfterTextChanged {
            if (inclusionName.text!!.length > 3) {
                shortCode.setText(generateShortCode(inclusionName.text.toString()))
            }
        }

        postingRule.setOnClickListener {
            showDropdownMenu(requireContext(), postingRule, it, postingRuleArray)
        }

        chargeRule.setOnClickListener {
            showDropdownMenu(requireContext(), chargeRule, it, chargeRuleArray)
        }

        val cancel = view.findViewById<TextView>(R.id.cancel)
        val save = view.findViewById<CardView>(R.id.saveBtn)

        if (currentData!!.inclusionId != "") {
            inclusionName.setText(currentData.inclusionName)
            shortCode.setText(currentData.shortCode)
            chargeRule.setText(currentData.chargeRule)
            postingRule.setText(currentData.postingRule)
            charge.setText(currentData.charge)
        }

        cancel.setOnClickListener {
            dismiss()
        }
        save.setOnClickListener {
            if (inclusionName.text!!.isEmpty()) {
                shakeAnimation(inclusionNameLayout, requireContext())
            } else if (shortCode.text!!.isEmpty()) {
                shakeAnimation(shortCodeLayout, requireContext())
            } else if (chargeRule.text!!.isEmpty()) {
                shakeAnimation(chargeRuleLayout, requireContext())
            } else if (postingRule.text!!.isEmpty()) {
                shakeAnimation(postingRuleLayout, requireContext())
            } else if (charge.text!!.isEmpty()) {
                shakeAnimation(chargeLayout, requireContext())
            } else {
                progressDialog = showProgressDialog(requireContext())

                if (currentData.inclusionId != "") {
                    updateInclusion(
                        requireContext(),
                        shortCode.text.toString(),
                        charge.text.toString(),
                        inclusionName.text.toString(),
                        inclusionType.text.toString(),
                        chargeRule.text.toString(),
                        postingRule.text.toString(),
                        currentData.inclusionId
                    )
                } else {
                    saveInclusion(
                        requireContext(),
                        shortCode.text.toString(),
                        charge.text.toString(),
                        inclusionName.text.toString(),
                        inclusionType.text.toString(),
                        chargeRule.text.toString(),
                        postingRule.text.toString()
                    )
                }
            }
        }

    }

    private fun saveInclusion(context: Context, shortCodeTxt : String, charge:String, inclusionName:String, inclusionType:String, chargeRule:String, postingRule:String) {
        val create = OAuthClient<GeneralsAPI>(context).create(GeneralsAPI::class.java).addInclusionsApi(
            AddInclusionsDataClass(UserSessionManager(context).getUserId().toString(), UserSessionManager(context).getPropertyId().toString(), shortCodeTxt, charge, inclusionName, inclusionType, chargeRule, postingRule))

        create.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                Log.d( "inclusion", "${response.code()} ${response.message()}")
                progressDialog.dismiss()
                dismiss()
                inclusionListener?.onInclusionSave()
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                progressDialog.dismiss()
                Log.d("error", t.localizedMessage)
            }
        })
    }
    private fun updateInclusion(context: Context, shortCodeTxt : String, charge:String, inclusionName:String, inclusionType:String, chargeRule:String, postingRule:String, inclusionId:String) {
        val create = OAuthClient<GeneralsAPI>(context).create(GeneralsAPI::class.java).updateInclusionsApi(
            UserSessionManager(context).getUserId().toString(), inclusionId, UpdateInclusionDataClass(shortCodeTxt, charge, inclusionName, inclusionType, chargeRule, postingRule))

        create.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                progressDialog.dismiss()
                Log.d( "inclusion", "${response.code()} ${response.message()}")
                inclusionListener?.onInclusionSave()
                dismiss()
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                progressDialog.dismiss()
                dismiss()
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