package rconnect.retvens.technologies.dashboard.configuration.others.transportationTypes

import android.app.Dialog
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
import androidx.cardview.widget.CardView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.billings.AddPaymentTypeDataClass
import rconnect.retvens.technologies.dashboard.configuration.billings.GetPaymentTypeDataClass
import rconnect.retvens.technologies.dashboard.configuration.billings.PaymentTypeAdapter
import rconnect.retvens.technologies.databinding.FragmentTransportationTypesBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.generateShortCode
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransportationTypesFragment : Fragment(), TransportationTypeAdapter.OnUpdate {
    private lateinit var binding : FragmentTransportationTypesBinding
    lateinit var loader:Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTransportationTypesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loader = showProgressDialog(requireContext())
        setUpRecycler()

        binding.createNewBtn.setOnClickListener {
            openCreateNewDialog()
        }

    }
    private fun setUpRecycler() {
        binding.reservationTypeRecycler.layoutManager = LinearLayoutManager(requireContext())

        val reservation = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).getTransportationModeTypeApi(UserSessionManager(requireContext()).getUserId().toString(), UserSessionManager(requireContext()).getPropertyId().toString())
        reservation.enqueue(object : Callback<GetTransportationTypeDataClass?> {
            override fun onResponse(
                call: Call<GetTransportationTypeDataClass?>,
                response: Response<GetTransportationTypeDataClass?>
            ) {
                if (response.isSuccessful) {
                    loader.dismiss()
                    if (isAdded){
                    try {
                        val data = response.body()!!.data
                        val adapter =
                            TransportationTypeAdapter(response.body()!!.data, requireContext())
                        binding.reservationTypeRecycler.adapter = adapter
                        adapter.setOnUpdateListener(this@TransportationTypesFragment)
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
                                    it.transportationModeName.contains(p0.toString())
                                }
                                adapter.filterList(filteredData as ArrayList<GetTransportationTypeData>)
                            }

                            override fun afterTextChanged(p0: Editable?) {

                            }
                        })
                    } catch (e:NullPointerException){
                        e.printStackTrace()
                    }
                } else {
                    loader.dismiss()
                    openCreateNewDialog()
                    Log.d("respons", "${response.code()} ${response.message()}")
                }
            }
            }

            override fun onFailure(call: Call<GetTransportationTypeDataClass?>, t: Throwable) {
                openCreateNewDialog()
                Log.d("error", t.localizedMessage)
            }
        })
    }

    private fun openCreateNewDialog() {
        val dialog = Dialog(requireContext()) // Use 'this' as the context, assuming this code is within an Activity
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dialog_create_transportation_type)
        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent) // Makes the background transparent
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        val shortCode = dialog.findViewById<TextInputEditText>(R.id.shortCode)
        val transportationModeText = dialog.findViewById<TextInputEditText>(R.id.transportationModeText)
        val transportationModeLayout = dialog.findViewById<TextInputLayout>(R.id.propertyChainLayout_transport)
        val short_code_layout_tranport = dialog.findViewById<TextInputLayout>(R.id.short_code_layout_tranport)

        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        val save = dialog.findViewById<CardView>(R.id.saveBtn)

        transportationModeText.doAfterTextChanged {
            if (transportationModeText.text!!.length>3){
                shortCode.setText(generateShortCode(transportationModeText.text.toString()))
            }
        }


        cancel.setOnClickListener {
            dialog.dismiss()
        }
        save.setOnClickListener {

            if (transportationModeText.text!!.isEmpty()){
                shakeAnimation(transportationModeLayout,requireContext())
            }
            else if (shortCode.text!!.isEmpty()){
                shakeAnimation(short_code_layout_tranport,requireContext())
            }
            else {
                loader.show()
                saveTMode(dialog, shortCode.text.toString(), transportationModeText.text.toString())
            }
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.END)

        dialog.show()

    }
    private fun saveTMode(dialog: Dialog, shortCodeTxt:String , transportationModeTxt:String) {
        val create = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).addTransportationModeTypeApi(
            AddTransportationTypeDataClass(UserSessionManager(requireContext()).getUserId().toString(), UserSessionManager(requireContext()).getPropertyId().toString(), transportationModeTxt, shortCodeTxt)
        )

        create.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                loader.dismiss()
                Log.d( "transport", "${response.code()} ${response.message()}")
                dialog.dismiss()
                setUpRecycler()
            }
            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                loader.dismiss()
                Log.d("saveReservationError", "${t.localizedMessage}")
            }
        })
    }

    override fun onUpdateTransportationType() {
        setUpRecycler()
    }

}