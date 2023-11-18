package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.mealPlan

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.InclusionsAdapter
import rconnect.retvens.technologies.databinding.FragmentMealPlanBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.generateShortCode
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MealPlanFragment : Fragment(), MealPlanAdapter.OnUpdate {
    private lateinit var binding : FragmentMealPlanBinding

    private lateinit var progressDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMealPlanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecycler()

        binding.createNewBtn.setOnClickListener {
            openCreateNewDialog()
        }

    }
    private fun setUpRecycler() {
        progressDialog = showProgressDialog(requireContext())

        binding.paymentTypeRecycler.layoutManager = LinearLayoutManager(requireContext())

        val mp = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).getMealPlanApi(UserSessionManager(requireContext()).getUserId().toString(), UserSessionManager(requireContext()).getPropertyId().toString())
        mp.enqueue(object : Callback<GetMealPlanDataClass?> {
            override fun onResponse(
                call: Call<GetMealPlanDataClass?>,
                response: Response<GetMealPlanDataClass?>
            ) {

                progressDialog.dismiss()
                if (response.isSuccessful) {
                    if (isAdded) {
                        try {
                            val data = response.body()!!.data
                            val adapter = MealPlanAdapter(data, requireContext())
                            binding.paymentTypeRecycler.adapter = adapter
                            adapter.setOnUpdateListener(this@MealPlanFragment)
                            adapter.notifyDataSetChanged()

                            binding.search.addTextChangedListener(object : TextWatcher {
                                override fun beforeTextChanged(
                                    p0: CharSequence?,
                                    p1: Int,
                                    p2: Int,
                                    p3: Int
                                ) {

                                }

                                override fun onTextChanged(
                                    p0: CharSequence?,
                                    p1: Int,
                                    p2: Int,
                                    p3: Int
                                ) {
                                    val filteredData = data.filter {
                                        it.mealPlanName.contains(p0.toString(), true)
                                    }
                                    adapter.filterList(filteredData as ArrayList<GetMealPlanData>)
                                }

                                override fun afterTextChanged(p0: Editable?) {

                                }
                            })

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } else {
                        Log.d("error", "${response.code()} ${response.message()}")
                    }
            }

            override fun onFailure(call: Call<GetMealPlanDataClass?>, t: Throwable) {
                progressDialog.dismiss()
                Log.d("error", t.localizedMessage)
            }
        })

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

        val mealPlanNameLayout = dialog.findViewById<TextInputLayout>(R.id.mealPlanNameLayout)
        val shortCodeLayout = dialog.findViewById<TextInputLayout>(R.id.shortCodeLayout)
        val chargesPerOccupancyLayout = dialog.findViewById<TextInputLayout>(R.id.chargesPerOccupancyLayout)

        val mealPlanName = dialog.findViewById<TextInputEditText>(R.id.mealPlanName)
        val shortCode = dialog.findViewById<TextInputEditText>(R.id.shortCode)
        val chargesPerOccupancy = dialog.findViewById<TextInputEditText>(R.id.chargesPerOccupancy)

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
                shakeAnimation(mealPlanNameLayout, requireContext())
            } else if (shortCode.text!!.isEmpty()){
                shakeAnimation(shortCodeLayout, requireContext())
            } else if (chargesPerOccupancy.text!!.isEmpty()){
                shakeAnimation(chargesPerOccupancyLayout, requireContext())
            } else {
                progressDialog.show()
                saveMealPlan(
                    requireContext(),
                    dialog,
                    mealPlanName.text.toString(),
                    shortCode.text.toString(),
                    chargesPerOccupancy.text.toString()
                )
            }
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.END)

        dialog.show()

    }
    private fun saveMealPlan(context: Context, dialog: Dialog, mealPlanName: String, shortCode: String, chargesPerOccupancy: String) {
        val mP = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).postMealPlanApi(
            MealPlanDataClass(
                UserSessionManager(requireContext()).getUserId().toString(),
                UserSessionManager(requireContext()).getPropertyId().toString(),
                shortCode,
                mealPlanName,
                chargesPerOccupancy))

        mP.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                progressDialog.dismiss()
                dialog.dismiss()
                if (isAdded){
                    if (response.isSuccessful) {
                        setUpRecycler()
                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("error", "${response.code()} ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                dialog.dismiss()
                progressDialog.dismiss()
                Log.d("error", t.localizedMessage)
            }
        })

    }

    override fun onUpdateMealPlan() {
        setUpRecycler()
    }

}