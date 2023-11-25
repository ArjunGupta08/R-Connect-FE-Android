package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.InclusionPlan
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.CreateInclusionDialogSheet
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsDataClass
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.generateShortCode
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showDropdownMenu
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddInclusionDialogSheet(private val selectedInclusionsArray : ArrayList<InclusionPlan>) : DialogFragment(), AddInclusionsAdapter.OnUpdate, CreateInclusionDialogSheet.OnInclusionSave {

    private var inclusionListener: OnInclusionAdd? = null
    fun setOnAddInclusionDialogListener(listener: OnInclusionAdd) {
        inclusionListener = listener
    }

    interface OnInclusionAdd {
        fun onInclusionAdded(selectedInclusionPlan: ArrayList<InclusionPlan>, totalInclusionCharges : Double)
    }

    companion object {
        const val TAG = "AddRecipientWalletSheet"
    }

    val inclusionsData = ArrayList<GetInclusionsData>()
    val selectedInclusions = ArrayList<InclusionPlan>()
    private var totalInclusionCharges = 0.00

    private lateinit var adapter: AddInclusionsAdapter
    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    private lateinit var search : EditText
    private lateinit var progressDialog: Dialog
    lateinit var recyclerView : RecyclerView

    var mLastClickTime : Long = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_add_inclusion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        search = view.findViewById<EditText>(R.id.search)

        val createNewInclusionBtn = view.findViewById<TextView>(R.id.createNewInclusionBtn)
        createNewInclusionBtn.setOnClickListener {
            // mis-clicking prevention, using threshold of 1000 ms
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return@setOnClickListener;
            }
            mLastClickTime = SystemClock.elapsedRealtime()

            val openDialog = CreateInclusionDialogSheet()
            val fragManager = childFragmentManager
            fragManager.let{openDialog.show(it, CreateInclusionDialogSheet.TAG)}
            openDialog.setOnInclusionDialogListener(this)
        }

        val cancel = view.findViewById<TextView>(R.id.cancel)
        cancel.setOnClickListener {
            dismiss()
        }

        val saveBtn = view.findViewById<CardView>(R.id.saveBtn)
        saveBtn.setOnClickListener {
            inclusionListener?.onInclusionAdded(selectedInclusions, totalInclusionCharges)
            dismiss()
        }

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        setUpRecycler()

//        val adapter = AddInclusionsAdapter(inclusionsArray, requireContext())
//        recyclerView.adapter = adapter
//        adapter.setOnUpdateListener(this@AddInclusionDialogSheet)
//        adapter.notifyDataSetChanged()
    }

    fun setUpRecycler() {
        progressDialog = showProgressDialog(requireContext())

        val identity = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).getInclusionApi(
            UserSessionManager(requireContext()).getUserId().toString(), UserSessionManager(requireContext()).getPropertyId().toString())
        identity.enqueue(object : Callback<GetInclusionsDataClass?> {
            override fun onResponse(
                call: Call<GetInclusionsDataClass?>,
                response: Response<GetInclusionsDataClass?>
            ) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    val data = response.body()!!.data

                    try {
                        if (selectedInclusionsArray.isNotEmpty()) {
                            adapter = AddInclusionsAdapter(data, requireContext())
                            recyclerView.adapter = adapter
                            adapter.setOnUpdateListener(this@AddInclusionDialogSheet)
                            adapter.notifyDataSetChanged()

                            selectedInclusionsArray.forEach { inclusionId ->
                               data.removeAll(data.filter {
                                   it.inclusionId.equals(inclusionId)
                               }.toSet())
                                adapter.filterData(data)
                            }

                        } else {
                            adapter = AddInclusionsAdapter(data, requireContext())
                            recyclerView.adapter = adapter
                            adapter.setOnUpdateListener(this@AddInclusionDialogSheet)
                            adapter.notifyDataSetChanged()
                        }

                        search.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                s: CharSequence?,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {

                            }

                            override fun onTextChanged(
                                s: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {
                                val filterData = data.filter {
                                    it.inclusionName.contains(s.toString(), true)
                                }
                                adapter.filterData(filterData as ArrayList<GetInclusionsData>)
                            }

                            override fun afterTextChanged(s: Editable?) {

                            }
                        })

                } catch (e : Exception) {
                    e.printStackTrace()
                }

            } else {
                    Log.d("error", "${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetInclusionsDataClass?>, t: Throwable) {
                progressDialog.dismiss()
                Log.d("error", t.localizedMessage)
            }
        })

    }

    override fun onUpdateList(selectedList: ArrayList<GetInclusionsData>) {
        selectedList.forEach { getInclusionData ->
            val inclusionPlan = InclusionPlan(
                inclusionId = getInclusionData.inclusionId,
                inclusionType = getInclusionData.inclusionType,
                inclusionName = getInclusionData.inclusionName,
                postingRule = getInclusionData.postingRule,
                chargeRule = getInclusionData.chargeRule,
                rate = getInclusionData.charge
            )

            if (!selectedInclusions.contains(inclusionPlan)) {
                selectedInclusions.add(inclusionPlan)
                totalInclusionCharges += inclusionPlan.rate.toDouble()
            }
        }
    }

    override fun onInclusionSave() {
        setUpRecycler()
    }
}