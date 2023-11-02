package rconnect.retvens.technologies.dashboard.configuration.others.holiday

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
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
import rconnect.retvens.technologies.dashboard.configuration.others.seasons.AddSeasonDataClass
import rconnect.retvens.technologies.dashboard.configuration.others.seasons.GetSeasonDataClass
import rconnect.retvens.technologies.dashboard.configuration.others.seasons.SeasonAdapter
import rconnect.retvens.technologies.databinding.FragmentHolidaysBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class HolidaysFragment : Fragment(), HolidaysAdapter.OnItemUpdate {

    private lateinit var binding: FragmentHolidaysBinding

    private lateinit var adapter : HolidaysAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHolidaysBinding.inflate(inflater, container, false)
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
        dialog.setContentView(R.layout.dialog_create_holiday)
        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent) // Makes the background transparent
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        val save = dialog.findViewById<CardView>(R.id.saveBtn)

        val shortCode = dialog.findViewById<TextInputEditText>(R.id.shortCode)
        val holidayName = dialog.findViewById<TextInputEditText>(R.id.holidayName)

        val from_date = dialog.findViewById<TextView>(R.id.from_date)
        val to_date = dialog.findViewById<TextView>(R.id.to_date)

        from_date.setOnClickListener {
            showCalendarDialog(requireContext(),from_date)
            dialog.show()
        }
        to_date.setOnClickListener {
            showCalendarDialog(requireContext(),to_date)
            dialog.show()
        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }
        save.setOnClickListener {
            saveHoliday(requireContext(), dialog, shortCode.text.toString(), holidayName.text.toString(), from_date.text.toString(), to_date.text.toString())
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.END)

        dialog.show()

    }

    private fun setUpRecycler() {

        binding.paymentTypeRecycler.layoutManager = LinearLayoutManager(requireContext())

        val identity = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).getHolidayApi(UserSessionManager(requireContext()).getUserId().toString(), UserSessionManager(requireContext()).getPropertyId().toString())
        identity.enqueue(object : Callback<GetHotelDataClass?> {
            override fun onResponse(
                call: Call<GetHotelDataClass?>,
                response: Response<GetHotelDataClass?>
            ) {
                if (isAdded) {
                    if (response.isSuccessful) {
                        adapter = HolidaysAdapter(response.body()!!.data, requireContext())
                        binding.paymentTypeRecycler.adapter = adapter
                        adapter.setOnItemUpdateListener(this@HolidaysFragment)
                        adapter.notifyDataSetChanged()
                    } else {
                        openCreateNewDialog()
                        Log.d("error", "${response.code()} ${response.message()}")
                    }
                }
            }
            override fun onFailure(call: Call<GetHotelDataClass?>, t: Throwable) {
                Log.d("error", t.localizedMessage)
            }
        })

    }

    fun showCalendarDialog(context : Context, textDate: TextView) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                // Set the selected date on the EditText
                val selectedDate = "$dayOfMonth/${month+1}/$year"
                textDate.text = selectedDate
            },
            currentYear,
            currentMonth,
            currentDay
        )
        datePickerDialog.setCancelable(false)

        datePickerDialog.show()
    }

    private fun saveHoliday(context: Context, dialog: Dialog, shortCodeTxt : String, holidayName:String, startDate:String, endDate:String) {
        val create = OAuthClient<GeneralsAPI>(context).create(GeneralsAPI::class.java).addHolidayApi(
            AddHolidayDataClass(UserSessionManager(context).getUserId().toString(), UserSessionManager(context).getPropertyId().toString(), shortCodeTxt, holidayName, startDate, endDate)
        )
        create.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                Log.d( "holiday", "${response.code()} ${response.message()}")
                setUpRecycler()
                dialog.dismiss()
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                Log.d("error", t.localizedMessage)
            }
        })
    }

    override fun onUpdate() {
        setUpRecycler()
    }

}