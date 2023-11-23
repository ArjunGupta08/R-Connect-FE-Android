package rconnect.retvens.technologies.dashboard.configuration.guestsAndReservation.reservationType

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
import android.widget.Switch
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.NotificationAdapter
import rconnect.retvens.technologies.databinding.FragmentReservationTypeBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReservationTypeFragment : Fragment(), ReservationTypeAdapter.OnUpdate {

    private lateinit var binding : FragmentReservationTypeBinding

    private lateinit var reservationTypeAdapter: ReservationTypeAdapter

    private lateinit var progressDialog : Dialog
    var list = ArrayList<GetReservationTypeData>()

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
        binding.filterReservation.setOnClickListener{
            openFilterDialog()
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

        val reservationNameLayout = dialog.findViewById<TextInputLayout>(R.id.reservationNameLayout)
        val reservationNameET = dialog.findViewById<TextInputEditText>(R.id.reservationNameET)
        val isConfirmedSwitch = dialog.findViewById<Switch>(R.id.isConfirmedSwitch)

        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        val save = dialog.findViewById<CardView>(R.id.saveBtn)

        cancel.setOnClickListener {
            dialog.dismiss()
        }
        save.setOnClickListener {
            if (reservationNameET.text!!.isEmpty()) {
                shakeAnimation(reservationNameLayout, requireContext())
            } else {
                progressDialog.show()
                saveReservation(
                    dialog, reservationNameET.text.toString(), if (isConfirmedSwitch.isChecked) {
                        "Confirmed"
                    } else {
                        "Unconfirmed"
                    }
                )
            }
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.END)

        dialog.show()

    }
    private fun saveReservation(dialog: Dialog, rName : String, status:String) {
        val create = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).createReservationApi(
            CreateReservationTypeDataClass(UserSessionManager(requireContext()).getUserId().toString(), UserSessionManager(requireContext()).getPropertyId().toString(), rName, status)
        )

        create.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                Log.d( "reservation", "${response.code()} ${response.message()}")
                progressDialog.dismiss()
                dialog.dismiss()
                reservationTypeRecycler()
            }
            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                progressDialog.dismiss()
                Log.d("saveReservationError", "${t.localizedMessage}")
            }
        })
    }
    private fun reservationTypeRecycler() {
        progressDialog = showProgressDialog(requireContext())

        binding.reservationTypeRecycler.layoutManager = LinearLayoutManager(requireContext())

        val reservation = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).getReservationApi(UserSessionManager(requireContext()).getUserId().toString(), UserSessionManager(requireContext()).getPropertyId().toString())
        reservation.enqueue(object : Callback<GetReservationTypeDataClass?> {
            override fun onResponse(
                call: Call<GetReservationTypeDataClass?>,
                response: Response<GetReservationTypeDataClass?>
            ) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    try {
                        val data = response.body()!!.data
                        list = data
                        reservationTypeAdapter = ReservationTypeAdapter(data, requireContext())
                        binding.reservationTypeRecycler.adapter = reservationTypeAdapter
                        reservationTypeAdapter.setOnUpdateListener(this@ReservationTypeFragment)
                        reservationTypeAdapter.notifyDataSetChanged()

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
                                val filterData = data.filter { filtered ->
                                    filtered.reservationName.contains(s.toString(), true)
                                }
                                reservationTypeAdapter.filterList(filterData as ArrayList<GetReservationTypeData>)
                            }

                            override fun afterTextChanged(s: Editable?) {

                            }
                        })
                    } catch ( e : Exception) {
                        e.printStackTrace()
                    }
                } else {
                    Log.d("respons", "${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetReservationTypeDataClass?>, t: Throwable) {
                progressDialog.dismiss()
                Log.d("error", t.localizedMessage)
            }
        })

    }


    private fun openFilterDialog() {
        val dialog =
            Dialog(requireContext()) // Use 'this' as the context, assuming this code is within an Activity
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dialog_reservation_filter)
        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent) // Makes the background transparent
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }


        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.END)

        dialog.show()
        val rName = dialog.findViewById<MaterialCardView>(R.id.card_reservation_name)
        val rStatus = dialog.findViewById<MaterialCardView>(R.id.card_reservation_status)
        val recyclerReservation = dialog.findViewById<RecyclerView>(R.id.recycler_reservation)

        val countName = dialog.findViewById<TextView>(R.id.count_name)
        val countStatus = dialog.findViewById<TextView>(R.id.count_status)

        countName.text = "${list.size}"
        countStatus.text = "${list.size}"
        recyclerReservation.layoutManager = LinearLayoutManager(requireContext())

        rName.setOnClickListener {
            rName.isVisible = false
            rStatus.isVisible = false
            recyclerReservation.isVisible = true
            recyclerReservation.adapter = ReservationTypeDialogAdapter(list,requireContext(),true)
            recyclerReservation.adapter!!.notifyDataSetChanged()
        }
        rStatus.setOnClickListener {
            rName.isVisible = false
            rStatus.isVisible = false
            recyclerReservation.isVisible = true
            recyclerReservation.adapter = ReservationTypeDialogAdapter(list,requireContext(),false)
            recyclerReservation.adapter!!.notifyDataSetChanged()
        }

        dialog.findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            dialog.dismiss() }
    }

    override fun onUpdateReservationType() {
        reservationTypeRecycler()
    }


}