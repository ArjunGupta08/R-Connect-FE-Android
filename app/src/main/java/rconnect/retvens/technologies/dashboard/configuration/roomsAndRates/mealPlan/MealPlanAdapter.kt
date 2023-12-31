package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.mealPlan

import android.app.AlertDialog
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
import rconnect.retvens.technologies.dashboard.configuration.others.holiday.DisplayStatusData
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.generateShortCode
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealPlanAdapter(var list:ArrayList<GetMealPlanData>, val applicationContext: Context):RecyclerView.Adapter<MealPlanAdapter.NotificationHolder>() {

    var mListener : OnUpdate?= null

    fun setOnUpdateListener(listener : OnUpdate){
        mListener = listener
    }

    interface OnUpdate {
        fun onUpdateMealPlan()
    }

    private lateinit var progressDialog: Dialog
    class NotificationHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {

        val shortCode = itemView.findViewById<TextView>(R.id.shortCode)
        val text2 = itemView.findViewById<TextView>(R.id.text2)
        val text3 = itemView.findViewById<TextView>(R.id.text3)
        val text5 = itemView.findViewById<TextView>(R.id.text5)
        val lastModified = itemView.findViewById<TextView>(R.id.text6)

        val edit = itemView.findViewById<ImageView>(R.id.edit)
        val delete = itemView.findViewById<ImageView>(R.id.delete)
        val info = itemView.findViewById<ImageView>(R.id.info)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_meal_plan,parent,false)
        return NotificationHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        val item = list[position]

        holder.shortCode.text = item.shortCode
        holder.text2.text = item.mealPlanName
        holder.text3.text = item.chargesPerOccupancy
        holder.text5.text = "${item.createdBy} ${item.createdOn}"
        holder.lastModified.text = "${item.modifiedBy} ${item.modifiedOn}"

        holder.delete.setOnClickListener {
            showDeleteConfirmationDialog(applicationContext){
                progressDialog = showProgressDialog(applicationContext)
                deleteMealPlan(applicationContext,item.mealPlanId,item)
            }
        }

        holder.edit.setOnClickListener {
            openCreateNewDialog(item.shortCode, item.mealPlanName, item.chargesPerOccupancy, item.mealPlanId)
        }
    }

    private fun openCreateNewDialog(shortCodeTxt: String, mealPlanNameTxt: String, chargesPerOccupancyTxt: String, mealPlanId: String) {
        val dialog = Dialog(applicationContext, ) // Use 'this' as the context, assuming this code is within an Activity
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
        val mealPlanNameLayout = dialog.findViewById<TextInputLayout>(R.id.mealPlanNameLayout)
        val shortCodeLayout = dialog.findViewById<TextInputLayout>(R.id.shortCodeLayout)
        val chargesPerOccupancyLayout = dialog.findViewById<TextInputLayout>(R.id.chargesPerOccupancyLayout)

        val mealPlanName = dialog.findViewById<TextInputEditText>(R.id.mealPlanName)
        val shortCode = dialog.findViewById<TextInputEditText>(R.id.shortCode)
        val chargesPerOccupancy = dialog.findViewById<TextInputEditText>(R.id.chargesPerOccupancy)

        shortCode.setText(shortCodeTxt)
        mealPlanName.setText(mealPlanNameTxt)
        chargesPerOccupancy.setText(chargesPerOccupancyTxt)

        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        val save = dialog.findViewById<CardView>(R.id.saveBtn)

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        mealPlanName.doAfterTextChanged {
            if (mealPlanName.text!!.length > 3) {
                shortCode.setText(generateShortCode(mealPlanName.text.toString()))
            }
        }

        save.setOnClickListener {
            if (mealPlanName.text!!.isEmpty()){
                shakeAnimation(mealPlanNameLayout, applicationContext)
            } else if (shortCode.text!!.isEmpty()){
                shakeAnimation(shortCodeLayout, applicationContext)
            } else if (chargesPerOccupancy.text!!.isEmpty()){
                shakeAnimation(chargesPerOccupancyLayout, applicationContext)
            } else {
                progressDialog = showProgressDialog(applicationContext)
                saveMealPlan(
                    applicationContext,
                    dialog,
                    shortCode.text.toString(),
                    mealPlanName.text.toString(),
                    chargesPerOccupancy.text.toString(),
                    mealPlanId
                )
            }
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.END)

        dialog.show()

    }
    private fun saveMealPlan(context: Context, dialog: Dialog, shortCode: String, mealPlanName: String, chargesPerOccupancy: String, mealPlanId: String) {
        val mP = OAuthClient<GeneralsAPI>(context).create(GeneralsAPI::class.java)
            .updateMealPlanApi(
                UserSessionManager(context).getUserId().toString(),
                mealPlanId,
                UpdateMealPlanData(
                    shortCode,
                    mealPlanName,
                    chargesPerOccupancy
                )
            )

        mP.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    dialog.dismiss()
                    mListener?.onUpdateMealPlan()
                } else {
                    Log.d("error", "${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                progressDialog.dismiss()
                Log.d("error", t.localizedMessage)
            }
        })
    }
    private fun deleteMealPlan(context: Context, mealPlanId: String,getMealPlanData: GetMealPlanData) {
        val mP = OAuthClient<GeneralsAPI>(context).create(GeneralsAPI::class.java)
            .deleteMealPlanApi(UserSessionManager(applicationContext).getUserId().toString(),mealPlanId,
                DisplayStatusData("0")
            )

        mP.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    list.remove(getMealPlanData)
                    notifyDataSetChanged()
                } else {
                    Log.d("error", "${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                progressDialog.dismiss()
                Log.d("error", t.localizedMessage)
            }
        })
    }

    fun filterList(inputString : ArrayList<GetMealPlanData>) {
        list = inputString
        notifyDataSetChanged()
    }

    fun showDeleteConfirmationDialog(context: Context, onDeleteConfirmed: () -> Unit) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Confirm Deletion")
        alertDialogBuilder.setMessage("Do you really want to delete this item?")

        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            // User clicked "Yes"
            onDeleteConfirmed.invoke()
        }

        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
            // User clicked "No"
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}