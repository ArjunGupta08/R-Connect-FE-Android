package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.generateShortCode
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InclusionsAdapter(var list:ArrayList<GetInclusionsData>, val applicationContext: Context):RecyclerView.Adapter<InclusionsAdapter.NotificationHolder>() {

    var mListener : OnUpdate?= null

    fun setOnUpdateListener(listener : OnUpdate){
        mListener = listener
    }

    interface OnUpdate {
        fun onUpdateIdentityType()
    }

    lateinit var progressDialog: Dialog
    class NotificationHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {

        val shortCode = itemView.findViewById<TextView>(R.id.shortCode)
        val text2 = itemView.findViewById<TextView>(R.id.text2)
        val text3 = itemView.findViewById<TextView>(R.id.text3)
        val text4 = itemView.findViewById<TextView>(R.id.text4)
        val text5 = itemView.findViewById<TextView>(R.id.text5)
        val lastModified = itemView.findViewById<TextView>(R.id.text6)

        val edit = itemView.findViewById<ImageView>(R.id.edit)
        val delete = itemView.findViewById<ImageView>(R.id.delete)
        val info = itemView.findViewById<ImageView>(R.id.info)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_inclusion,parent,false)
        return NotificationHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        val item = list[position]

        holder.shortCode.text = item.shortCode
        holder.text2.text = item.inclusionName
        holder.text3.text = item.inclusionType
        holder.text4.text = item.postingRule
        holder.text5.text = item.chargeRule
        holder.lastModified.text = "${item.createdBy} ${item.createdOn}"
//        holder.lastModified.text = "${item.modifiedBy} ${item.modifiedOn}"

        holder.delete.setOnClickListener {
            list.remove(item)
            notifyDataSetChanged()
        }
        holder.edit.setOnClickListener {
            openCreateNewDialog(item.shortCode, item.inclusionName, item.inclusionType, item.charge, item.chargeRule, item.postingRule, item.inclusionId)
        }
    }

    private fun openCreateNewDialog(shortCodeTxt: String, inclusionNameTxt: String, inclusionTypeTxt: String, chargeTxt: String, chargeRuleTxt: String, postingRuleTxt: String, inclusionId: String) {
        val dialog = Dialog(applicationContext, ) // Use 'this' as the context, assuming this code is within an Activity
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dialog_create_inclusion)
        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent) // Makes the background transparent
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        val inclusionNameLayout = dialog.findViewById<TextInputLayout>(R.id.inclusionNameLayout)
        val shortCodeLayout = dialog.findViewById<TextInputLayout>(R.id.shortCodeLayout)
        val chargeLayout = dialog.findViewById<TextInputLayout>(R.id.chargeLayout)
        val postingRuleLayout = dialog.findViewById<TextInputLayout>(R.id.postingRuleLayout)
        val chargeRuleLayout = dialog.findViewById<TextInputLayout>(R.id.chargeRuleLayout)


        val inclusionName = dialog.findViewById<TextInputEditText>(R.id.inclusionName)
        val inclusionType = dialog.findViewById<TextInputEditText>(R.id.inclusionType)
        val shortCode = dialog.findViewById<TextInputEditText>(R.id.shortCode)
        val charge = dialog.findViewById<TextInputEditText>(R.id.charge)
        val chargeRule = dialog.findViewById<TextInputEditText>(R.id.chargeRule)
        val postingRule = dialog.findViewById<TextInputEditText>(R.id.postingRule)

        inclusionName.setText(inclusionNameTxt)
        inclusionType.setText(inclusionTypeTxt)
        shortCode.setText(shortCodeTxt)
        charge.setText(chargeTxt)
        chargeRule.setText(chargeRuleTxt)
        postingRule.setText(postingRuleTxt)

        inclusionName.doAfterTextChanged {
            if (inclusionName.text!!.length > 3)
                shortCode.setText(generateShortCode(inclusionName.text.toString()))
        }


        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        val save = dialog.findViewById<CardView>(R.id.saveBtn)

        cancel.setOnClickListener {
            dialog.dismiss()
        }
        save.setOnClickListener {

            if (inclusionName.text!!.isEmpty()) {
                shakeAnimation(inclusionNameLayout, applicationContext)
            } else if (shortCode.text!!.isEmpty()) {
                shakeAnimation(shortCodeLayout, applicationContext)
            } else if (chargeRule.text!!.isEmpty()) {
                shakeAnimation(chargeRuleLayout, applicationContext)
            } else if (postingRule.text!!.isEmpty()) {
                shakeAnimation(postingRuleLayout, applicationContext)
            } else if (charge.text!!.isEmpty()) {
                shakeAnimation(chargeLayout, applicationContext)
            } else {
                progressDialog = showProgressDialog(applicationContext)
                saveInclusion(
                    applicationContext,
                    dialog,
                    shortCode.text.toString(),
                    charge.text.toString(),
                    inclusionName.text.toString(),
                    inclusionType.text.toString(),
                    chargeRule.text.toString(),
                    postingRule.text.toString(),
                    inclusionId
                )
            }
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.END)

        dialog.show()

    }

    private fun saveInclusion(context: Context, dialog: Dialog, shortCodeTxt : String, charge:String, inclusionName:String, inclusionType:String, chargeRule:String, postingRule:String, inclusionId:String) {
        val create = OAuthClient<GeneralsAPI>(context).create(GeneralsAPI::class.java).updateInclusionsApi(
            UserSessionManager(context).getUserId().toString(), inclusionId, UpdateInclusionDataClass(shortCodeTxt, charge, inclusionName, inclusionType, chargeRule, postingRule))

        create.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                progressDialog.dismiss()
                Log.d( "inclusion", "${response.code()} ${response.message()}")
                mListener?.onUpdateIdentityType()
                dialog.dismiss()
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                progressDialog.dismiss()
                Log.d("error", t.localizedMessage)
            }
        })
    }

    fun filterList(inputString : ArrayList<GetInclusionsData>) {
        list = inputString
        notifyDataSetChanged()
    }
}