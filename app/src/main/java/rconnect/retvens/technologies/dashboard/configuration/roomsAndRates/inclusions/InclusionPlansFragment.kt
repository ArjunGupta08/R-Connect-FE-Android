package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.others.holiday.AddHolidayDataClass
import rconnect.retvens.technologies.dashboard.configuration.others.holiday.GetHotelDataClass
import rconnect.retvens.technologies.dashboard.configuration.others.holiday.HolidaysAdapter
import rconnect.retvens.technologies.databinding.FragmentInclusionPlansBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.generateShortCode
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showDropdownMenu
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InclusionPlansFragment : Fragment(), InclusionsAdapter.OnUpdate, CreateInclusionDialogSheet.OnInclusionSave {
    private lateinit var binding : FragmentInclusionPlansBinding

    private lateinit var progressDialog: Dialog

    private var mLastClickTime : Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInclusionPlansBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecycler()

        binding.createNewBtn.setOnClickListener {
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

    }

    private fun setUpRecycler() {
        progressDialog = showProgressDialog(requireContext())

        binding.paymentTypeRecycler.layoutManager = LinearLayoutManager(requireContext())

        val identity = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).getInclusionApi(UserSessionManager(requireContext()).getUserId().toString(), UserSessionManager(requireContext()).getPropertyId().toString())
        identity.enqueue(object : Callback<GetInclusionsDataClass?> {
            override fun onResponse(
                call: Call<GetInclusionsDataClass?>,
                response: Response<GetInclusionsDataClass?>
            ) {
                progressDialog.dismiss()
                if (isAdded) {
                    if (response.isSuccessful) {
                        try {
                            val data = response.body()!!.data
                            val adapter = InclusionsAdapter(data, requireContext())
                            binding.paymentTypeRecycler.adapter = adapter
                            adapter.setOnUpdateListener(this@InclusionPlansFragment)
                            adapter.notifyDataSetChanged()

                            binding.search.addTextChangedListener(object : TextWatcher {
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
                                    adapter.filterList(filterData as ArrayList<GetInclusionsData>)
                                }

                                override fun afterTextChanged(s: Editable?) {

                                }
                            })

                        } catch (e : Exception) {
                            e.printStackTrace()
                        }
                    } else {
//                        openCreateNewDialog()
                        Log.d("error", "${response.code()} ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<GetInclusionsDataClass?>, t: Throwable) {
                progressDialog.dismiss()
                Log.d("error", t.localizedMessage)
            }
        })

    }

    override fun onUpdateIdentityType(currentData : GetInclusionsData) {
        val openDialog = CreateInclusionDialogSheet(currentData)
        val fragManager = childFragmentManager
        fragManager.let{openDialog.show(it, CreateInclusionDialogSheet.TAG)}
        openDialog.setOnInclusionDialogListener(this)
    }

    override fun onInclusionSave() {
        setUpRecycler()
    }

}