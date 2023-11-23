package rconnect.retvens.technologies.dashboard.configuration.billings

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
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.guestsAndReservation.reservationType.GetReservationTypeData
import rconnect.retvens.technologies.dashboard.configuration.guestsAndReservation.reservationType.ReservationTypeDialogAdapter
import rconnect.retvens.technologies.databinding.FragmentPaymentTypesBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.fetchTargetTimeZoneId
import rconnect.retvens.technologies.utils.generateShortCode
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PaymentTypesFragment : Fragment(), PaymentTypeAdapter.MealUpdatedListener {
    private lateinit var binding : FragmentPaymentTypesBinding

    private lateinit var payTypeAdapter: PaymentTypeAdapter
    private var list = ArrayList<GetPaymentTypeData>()

    private lateinit var progressDialog : Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPaymentTypesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecycler()

        binding.createNewBtn.setOnClickListener {
            openCreateNewDialog()
        }
        binding.filterPayment.setOnClickListener{
            openFilterDialog()
        }

    }

    private fun openCreateNewDialog() {
        val dialog = Dialog(requireContext()) // Use 'this' as the context, assuming this code is within an Activity
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dialog_create_payment_type)
        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent) // Makes the background transparent
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        val paymentMethodNameLayout = dialog.findViewById<TextInputLayout>(R.id.paymentMethodNameLayout)
        val shortCodeLayout = dialog.findViewById<TextInputLayout>(R.id.shortCodeLayout)
        val receivedToLayout = dialog.findViewById<TextInputLayout>(R.id.receivedToLayout)

        val paymentMethodName = dialog.findViewById<TextInputEditText>(R.id.paymentMethodName)
        val shortCode = dialog.findViewById<TextInputEditText>(R.id.shortCode)
        val receivedTo = dialog.findViewById<TextInputEditText>(R.id.receivedTo)

        paymentMethodName.doAfterTextChanged {
            if (paymentMethodName.text!!.length > 2){
                shortCode.setText(generateShortCode(paymentMethodName.text.toString()))
            }
        }
        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        val save = dialog.findViewById<CardView>(R.id.saveBtn)

        cancel.setOnClickListener {
            dialog.dismiss()
        }
        save.setOnClickListener {
            if (paymentMethodName.text!!.isEmpty()){
                shakeAnimation(paymentMethodNameLayout, requireContext())
            } else if (shortCode.text!!.isEmpty()){
                shakeAnimation(shortCodeLayout, requireContext())
            } else if (receivedTo.text!!.isEmpty()){
                shakeAnimation(receivedToLayout, requireContext())
            } else {
                progressDialog.show()
                savePayment(
                    dialog,
                    shortCode.text.toString(),
                    paymentMethodName.text.toString(),
                    receivedTo.text.toString()
                )
            }
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.END)

        dialog.show()

    }
    private fun savePayment(dialog: Dialog, shortCode : String, paymentMethodName:String, receivedTo:String) {
        val create = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).addPaymentTypeApi(UserSessionManager(requireContext()).getUserId().toString(),
            AddPaymentTypeDataClass(UserSessionManager(requireContext()).getUserId().toString(), UserSessionManager(requireContext()).getPropertyId().toString(), shortCode, paymentMethodName, receivedTo)
        )

        create.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                Log.d( "reservation", "${response.code()} ${response.message()}")
                progressDialog.dismiss()
                setUpRecycler()
                dialog.dismiss()
            }
            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                progressDialog.dismiss()
                Log.d("saveReservationError", "${t.localizedMessage}")
            }
        })
    }
    private fun setUpRecycler() {
        progressDialog = showProgressDialog(requireContext())
        binding.paymentTypeRecycler.layoutManager = LinearLayoutManager(requireContext())

        val reservation = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).getPaymentTypeApi(UserSessionManager(requireContext()).getUserId().toString(), UserSessionManager(requireContext()).getPropertyId().toString(), fetchTargetTimeZoneId())
        reservation.enqueue(object : Callback<GetPaymentTypeDataClass?> {
            override fun onResponse(
                call: Call<GetPaymentTypeDataClass?>,
                response: Response<GetPaymentTypeDataClass?>
            ) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    try {
                        val data = response.body()!!.data
                        list = data
                        payTypeAdapter = PaymentTypeAdapter(data, requireContext())
                        binding.paymentTypeRecycler.adapter = payTypeAdapter
                        payTypeAdapter.setOnMealUpdateListener(this@PaymentTypesFragment)
                        payTypeAdapter.notifyDataSetChanged()

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
                                val filterData = data.filter {searchInData->
                                    searchInData.paymentMethodName.contains(s.toString(),ignoreCase = true)
                                }
                                payTypeAdapter.filterList(filterData as ArrayList<GetPaymentTypeData>)
                            }

                            override fun afterTextChanged(s: Editable?) {

                            }
                        })

                    } catch (e : Exception){
                        e.printStackTrace()
                    }
                } else {
                    openCreateNewDialog()
                }
                Log.d("respons", "${response.code()} ${response.message()}")
            }

            override fun onFailure(call: Call<GetPaymentTypeDataClass?>, t: Throwable) {
                progressDialog.dismiss()
                Log.d("error", t.localizedMessage)
            }
        })

    }

    override fun onMealUpdated() {
        setUpRecycler()
    }

    private fun openFilterDialog() {
        val dialog =
            Dialog(requireContext()) // Use 'this' as the context, assuming this code is within an Activity
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dialog_payment_type_filter)
        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent) // Makes the background transparent
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            val rName = dialog.findViewById<MaterialCardView>(R.id.card_received_payment)
            val rStatus = dialog.findViewById<MaterialCardView>(R.id.card_paymentType)
            val recyclerReservation = dialog.findViewById<RecyclerView>(R.id.recycler_payment)

            val countName = dialog.findViewById<TextView>(R.id.count_received)
            val countStatus = dialog.findViewById<TextView>(R.id.count_payment)

            countName.text = "${list.size}"
            countStatus.text = "${list.size}"
            recyclerReservation.layoutManager = LinearLayoutManager(requireContext())

            rName.setOnClickListener {
                rName.isVisible = false
                rStatus.isVisible = false
                recyclerReservation.isVisible = true
                recyclerReservation.adapter = PaymentTypeDialogAdapter(list,requireContext(),false)
                recyclerReservation.adapter!!.notifyDataSetChanged()
            }
            rStatus.setOnClickListener {
                rName.isVisible = false
                rStatus.isVisible = false
                recyclerReservation.isVisible = true
                recyclerReservation.adapter = PaymentTypeDialogAdapter(list,requireContext(),true)
                recyclerReservation.adapter!!.notifyDataSetChanged()
            }

        }



        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.END)

        dialog.show()

        dialog.findViewById<ImageView>(R.id.iv_back).setOnClickListener { dialog.dismiss() }
    }
}