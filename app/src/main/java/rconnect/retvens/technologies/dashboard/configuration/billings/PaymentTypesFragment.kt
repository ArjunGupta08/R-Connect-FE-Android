package rconnect.retvens.technologies.dashboard.configuration.billings

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.reservation.CreateReservationTypeDataClass
import rconnect.retvens.technologies.dashboard.configuration.reservation.GetReservationTypeDataClass
import rconnect.retvens.technologies.dashboard.configuration.reservation.ReservationTypeAdapter
import rconnect.retvens.technologies.databinding.FragmentPaymentTypesBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PaymentTypesFragment : Fragment() {
    private lateinit var binding : FragmentPaymentTypesBinding

    private lateinit var payTypeAdapter: PaymentTypeAdapter
    private var list = ArrayList<String>()


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

        val paymentMethodName = dialog.findViewById<TextInputEditText>(R.id.paymentMethodName)
        val shortCode = dialog.findViewById<TextInputEditText>(R.id.shortCode)
        val receivedTo = dialog.findViewById<TextInputEditText>(R.id.receivedTo)

        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        val save = dialog.findViewById<CardView>(R.id.saveBtn)

        cancel.setOnClickListener {
            dialog.dismiss()
        }
        save.setOnClickListener {
            savePayment(dialog, shortCode.text.toString(), paymentMethodName.text.toString(), receivedTo.text.toString())
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
                dialog.dismiss()
                setUpRecycler()
            }
            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                Log.d("saveReservationError", "${t.localizedMessage}")
            }
        })
    }
    private fun setUpRecycler() {
        binding.paymentTypeRecycler.layoutManager = LinearLayoutManager(requireContext())

        val reservation = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).getPaymentTypeApi(UserSessionManager(requireContext()).getUserId().toString(), UserSessionManager(requireContext()).getPropertyId().toString())
        reservation.enqueue(object : Callback<GetPaymentTypeDataClass?> {
            override fun onResponse(
                call: Call<GetPaymentTypeDataClass?>,
                response: Response<GetPaymentTypeDataClass?>
            ) {
                if (response.isSuccessful) {
                    payTypeAdapter = PaymentTypeAdapter(response.body()!!.data, requireContext())
                    binding.paymentTypeRecycler.adapter = payTypeAdapter
                    payTypeAdapter.notifyDataSetChanged()
                } else {
                    openCreateNewDialog()
                    Log.d("respons", "${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetPaymentTypeDataClass?>, t: Throwable) {
                Log.d("error", t.localizedMessage)
            }
        })

    }

}