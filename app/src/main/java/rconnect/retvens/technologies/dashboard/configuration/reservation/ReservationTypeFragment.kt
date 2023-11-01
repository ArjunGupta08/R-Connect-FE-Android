package rconnect.retvens.technologies.dashboard.configuration.reservation

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
import android.widget.Switch
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.FragmentReservationTypeBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReservationTypeFragment : Fragment() {

    private lateinit var binding : FragmentReservationTypeBinding

    private lateinit var reservationTypeAdapter: ReservationTypeAdapter
    private var reservationTypeList = ArrayList<GetReservationTypeDataClass>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReservationTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        reservationTypeRecycler()

        binding.createNewBtn.setOnClickListener {
            openCreateNewDialog()
        }

    }

    private fun openCreateNewDialog() {
        val dialog = Dialog(requireContext()) // Use 'this' as the context, assuming this code is within an Activity
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dialog_create_reservation_type)
        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent) // Makes the background transparent
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        val reservationNameET = dialog.findViewById<TextInputEditText>(R.id.reservationNameET)
        val isConfirmedSwitch = dialog.findViewById<Switch>(R.id.isConfirmedSwitch)

        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        val save = dialog.findViewById<CardView>(R.id.saveBtn)

        cancel.setOnClickListener {
            dialog.dismiss()
        }
        save.setOnClickListener {
            saveReservation(  dialog,reservationNameET.text.toString(), if (isConfirmedSwitch.isChecked){
                "Confirmed"
            } else {
                "Unconfirmed"
            })
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.END)

        dialog.show()

    }
    private fun saveReservation(dialog: Dialog, rName : String, status:String) {
        UserSessionManager(requireContext()).savePropertyId("Cg4vWWtt")
        val create = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).createReservationApi(
            CreateReservationTypeDataClass(UserSessionManager(requireContext()).getUserId().toString(), UserSessionManager(requireContext()).getPropertyId().toString(), rName, status))

        create.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                Log.d( "reservation", "${response.code()} ${response.message()}")
                dialog.dismiss()
                reservationTypeRecycler()
            }
            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                Log.d("saveReservationError", "${t.localizedMessage}")
            }
        })
    }

    private fun reservationTypeRecycler() {
        binding.reservationTypeRecycler.layoutManager = LinearLayoutManager(requireContext())

        val reservation = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).getReservationApi(UserSessionManager(requireContext()).getUserId().toString(), UserSessionManager(requireContext()).getPropertyId().toString())
        reservation.enqueue(object : Callback<GetReservationTypeDataClass?> {
            override fun onResponse(
                call: Call<GetReservationTypeDataClass?>,
                response: Response<GetReservationTypeDataClass?>
            ) {
                if (response.isSuccessful) {
                    reservationTypeAdapter = ReservationTypeAdapter(response.body()!!.data, requireContext())
                    binding.reservationTypeRecycler.adapter = reservationTypeAdapter
                    reservationTypeAdapter.notifyDataSetChanged()
                } else {
                    Log.d("respons", "${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetReservationTypeDataClass?>, t: Throwable) {
                Log.d("error", t.localizedMessage)
            }
        })

    }



}