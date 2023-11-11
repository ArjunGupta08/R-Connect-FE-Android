package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanBar

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
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.AddInclusionsAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.CreateRateData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.CreateRateTypeAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.AddInclusionsDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsDataClass
import rconnect.retvens.technologies.databinding.FragmentRatePlanBarBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RatePlanBarFragment : Fragment(), AddInclusionsAdapter.OnUpdate {
    private lateinit var binding : FragmentRatePlanBarBinding

    val rateList = ArrayList<CreateRateData>()
    val ratePlanDetailsList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRatePlanBarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecycler()


        binding.addName.setOnClickListener {
            openAddInclusionDialog()
        }
    }

    private fun setUpRecycler() {

        ratePlanDetailsList.add("5")
        ratePlanDetailsList.add("5")

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        val ratePlanDetailsAdapter = RatePlanDetailsAdapter(requireContext(),ratePlanDetailsList)
//        binding.recyclerView.adapter = ratePlanDetailsAdapter
//        binding.recyclerView2.adapter = ratePlanDetailsAdapter
//        ratePlanDetailsAdapter.notifyDataSetChanged()

    }

    private fun openAddInclusionDialog() {
        val dialog = Dialog(requireContext())
        dialog.setCancelable(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_add_inclusion)

        val createNewInclusionBtn = dialog.findViewById<TextView>(R.id.createNewInclusionBtn)
        createNewInclusionBtn.setOnClickListener {
            openCreateInclusionDialog(dialog)
        }

        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        cancel.setOnClickListener {
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

                if (isAdded) {
                    if (response.isSuccessful) {
                        val adapter = AddInclusionsAdapter(response.body()!!.data, requireContext())
                        recyclerView.adapter = adapter
                        adapter.setOnUpdateListener(this@RatePlanBarFragment)
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

    private fun openCreateInclusionDialog(pDialog: Dialog) {
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
            pDialog.dismiss()
            openAddInclusionDialog()
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

        binding.recyclerInclusion.layoutManager = LinearLayoutManager(requireContext())
        val createRateTypeAdapter = CreateRateTypeAdapter(requireContext(), selectedList)
        binding.recyclerInclusion.adapter = createRateTypeAdapter
        createRateTypeAdapter.notifyDataSetChanged()

    }

}