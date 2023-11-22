package rconnect.retvens.technologies.dashboard.configuration.others.holiday

import android.app.AlertDialog
import android.app.DatePickerDialog
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
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.others.businessSource.GetBusinessSourceData
import rconnect.retvens.technologies.dashboard.configuration.others.seasons.AddSeasonDataClass
import rconnect.retvens.technologies.dashboard.configuration.others.seasons.GetSeasonDataClass
import rconnect.retvens.technologies.dashboard.configuration.others.seasons.SeasonAdapter
import rconnect.retvens.technologies.databinding.FragmentHolidaysBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.generateShortCode
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showProgressDialog
import rconnect.retvens.technologies.utils.utilCreateDatePickerDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.util.Date

class HolidaysFragment : Fragment(), HolidaysAdapter.OnItemUpdate {

    private lateinit var binding: FragmentHolidaysBinding
    lateinit var loader:Dialog
    private lateinit var adapter : HolidaysAdapter
    lateinit var endDatePickerDialog: DatePickerDialog
    lateinit var startDatePickerDialog: DatePickerDialog
    var isDateSelected = false

    var startDate:Date? = null
    var endDate:Date? = null
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

        val shortCode = dialog.findViewById<TextInputEditText>(R.id.shortCodeHoliday)
        val shortCodeLayout = dialog.findViewById<TextInputLayout>(R.id.shortCodeLayoutHoliday)
        val holidayName = dialog.findViewById<TextInputEditText>(R.id.holidayNameHoliday)
        val holidayNameLayout = dialog.findViewById<TextInputLayout>(R.id.holidayNameLayout)
        val toDate = dialog.findViewById<TextView>(R.id.to_date)
        val fromDate = dialog.findViewById<TextView>(R.id.from_date)

        holidayName.doAfterTextChanged {
            if (holidayName.text!!.length>3){
                shortCode.setText(generateShortCode(holidayName.text.toString()))
            }
        }


        startDatePickerDialog = createDatePickerDialog(requireContext(),fromDate) { date->
            startDate = date
        }

        endDatePickerDialog = createDatePickerDialog(requireContext(),toDate){date->

            endDatePickerDialog.datePicker.minDate = startDate!!.time

            if (startDate!=null&&date.before(startDate)){
//                    isRightEndDate = false

                Toast.makeText(requireContext(), "End date cannot be before start date", Toast.LENGTH_SHORT).show()
//                    showToast("End date cannot be before start date")
//                    to_date.text = "--/--/----"
//                    Handler().postDelayed(Runnable {
//                        isRightEndDate = true
//                    },1000)
            }
            else{
                endDate = date
            }
        }


        endDatePickerDialog = toCreateDatePickerDialog(toDate){date->
            if (startDate!=null){
                endDatePickerDialog.datePicker.minDate = startDate!!.time
                endDate = date
            }
        }




        val from_date = dialog.findViewById<TextView>(R.id.from_date)
        val to_date = dialog.findViewById<TextView>(R.id.to_date)

        from_date.setOnClickListener {
            startDatePickerDialog.show()
            dialog.show()
        }

        startDatePickerDialog = createDatePickerDialog(requireContext(),fromDate) { date->
            startDate = date
        }

        endDatePickerDialog = createDatePickerDialog(requireContext(),toDate){date->

            endDatePickerDialog.datePicker.minDate = startDate!!.time

            if (startDate!=null&&date.before(startDate)){
//                    isRightEndDate = false

                Toast.makeText(requireContext(), "End date cannot be before start date", Toast.LENGTH_SHORT).show()
//                    showToast("End date cannot be before start date")
//                    to_date.text = "--/--/----"
//                    Handler().postDelayed(Runnable {
//                        isRightEndDate = true
//                    },1000)
            }
            else{
//                    isRightEndDate = true
//                Toast.makeText(requireContext(), "Karan ji", Toast.LENGTH_SHORT).show()
                endDate = date
                isDateSelected = true
            }
        }
        to_date.setOnClickListener {
//            showCalendarDialog(requireContext(),to_date)
//            dialog.show()

            if (startDate!=null){
                endDatePickerDialog.datePicker.minDate = startDate!!.time
                endDatePickerDialog.show()
                dialog.show()
            }
        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }
        save.setOnClickListener {
            if (holidayName.text!!.isEmpty()){
                shakeAnimation(holidayNameLayout,requireContext())
            }
            else if(shortCode.text!!.isEmpty()){
                shakeAnimation(shortCodeLayout,requireContext())
            }
            else if(!isDateSelected){
                shakeAnimation(fromDate,requireContext())
                shakeAnimation(toDate,requireContext())
            }
            else{
                loader = showProgressDialog(requireContext())
            saveHoliday(requireContext(), dialog, shortCode.text.toString(), holidayName.text.toString(), from_date.text.toString(), to_date.text.toString())
            }
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.END)

        dialog.show()

    }

    private fun setUpRecycler() {
        loader = showProgressDialog(requireContext())

        binding.paymentTypeRecycler.layoutManager = LinearLayoutManager(requireContext())

        val identity = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).getHolidayApi(UserSessionManager(requireContext()).getUserId().toString(), UserSessionManager(requireContext()).getPropertyId().toString())
        identity.enqueue(object : Callback<GetHotelDataClass?> {
            override fun onResponse(
                call: Call<GetHotelDataClass?>,
                response: Response<GetHotelDataClass?>
            ) {
                loader.dismiss()
                if (isAdded) {
                    loader.dismiss()
                    if (response.isSuccessful) {
                        val list = response.body()!!.data
                        adapter = HolidaysAdapter(response.body()!!.data, requireContext())
                        binding.paymentTypeRecycler.adapter = adapter
                        adapter.setOnItemUpdateListener(this@HolidaysFragment)
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
                                val filteredData = list.filter {
                                    it.holidayName.contains(p0.toString())
                                }
                                adapter.filterList(filteredData as ArrayList<GetHotelData>)
                            }

                            override fun afterTextChanged(p0: Editable?) {

                            }
                        })
                    } else {
                        openCreateNewDialog()
                        Log.d("error", "${response.code()} ${response.message()}")
                    }
                }
            }
            override fun onFailure(call: Call<GetHotelDataClass?>, t: Throwable) {
                loader.dismiss()
                Log.d("error", t.localizedMessage)
            }
        })

    }


    private fun saveHoliday(context: Context, dialog: Dialog, shortCodeTxt : String, holidayName:String, startDate:String, endDate:String) {
        val create = OAuthClient<GeneralsAPI>(context).create(GeneralsAPI::class.java).addHolidayApi(
            AddHolidayDataClass(UserSessionManager(context).getUserId().toString(), UserSessionManager(context).getPropertyId().toString(), shortCodeTxt, holidayName, startDate, endDate)
        )
        create.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                loader.dismiss()
                Log.d( "holiday", "${response.code()} ${response.message()}")
                setUpRecycler()
                dialog.dismiss()
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                Log.d("error", t.localizedMessage)
            }
        })
    }
    fun createDatePickerDialog(applicationContext: Context, textDate:TextView, onDateSetListener: (Date) -> Unit): DatePickerDialog {
        val calendar = Calendar.getInstance()
        return DatePickerDialog(
            applicationContext,
            { _, year, month, dayOfMonth ->
                // Create a Date object from the selected date
                calendar.set(year, month, dayOfMonth)
                val selectedDate = calendar.time
                // Invoke the provided listener
                onDateSetListener.invoke(selectedDate)
                // Set the selected date on the EditText
                val selectedDate2 = "$dayOfMonth/${month+1}/$year"
                textDate.setText(selectedDate2)

//                if (textDate==from_date){
//                    startDate = selectedDate
//                }
//                else{
//                    endDate = selectedDate
//                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    private fun toCreateDatePickerDialog(textDate:TextView,onDateSetListener: (Date) -> Unit): DatePickerDialog {
        val calendar = Calendar.getInstance()
        return DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                // Create a Date object from the selected date
                calendar.set(year, month, dayOfMonth)
                val selectedDate = calendar.time
                // Invoke the provided listener
                onDateSetListener.invoke(selectedDate)
                // Set the selected date on the EditText
                val selectedDate2 = "$dayOfMonth/${month+1}/$year"
                textDate.text = selectedDate2

//                if (textDate==from_date){
//                    startDate = selectedDate
//                }
//                else{
//                    endDate = selectedDate
//                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }


    override fun onUpdate() {
        setUpRecycler()
    }

}