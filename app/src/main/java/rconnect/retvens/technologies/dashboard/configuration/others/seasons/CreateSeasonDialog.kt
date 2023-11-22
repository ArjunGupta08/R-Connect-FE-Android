package rconnect.retvens.technologies.dashboard.configuration.others.seasons

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.convertStringToDate
import rconnect.retvens.technologies.utils.generateShortCode
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.util.Date

class CreateSeasonDialog( private val getSeasonData: GetSeasonData ?=
    GetSeasonData(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        arrayListOf<String>()
    )
    ) : DialogFragment() {

    private var mListener: OnSeasonSave? = null
    fun setOnSeasonDialogListener(listener: OnSeasonSave) {
        mListener = listener
    }

    interface OnSeasonSave {
        fun onSeasonSave()
    }

    companion object {
        const val TAG = "AddRecipientWalletSheet"
    }

    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    lateinit var progressDialog: Dialog

    private lateinit var robotoMedium : Typeface
    private lateinit var roboto: Typeface
    lateinit var fri:TextView
    lateinit var sat:TextView
    lateinit var sun:TextView
    lateinit var mon:TextView
    lateinit var tues:TextView
    lateinit var wed:TextView
    lateinit var thur:TextView
    lateinit var txt_all_days:TextView
    lateinit var txt_week_days:TextView
    lateinit var txt_weekends:TextView
    lateinit var txt_custom:TextView
    var isAllDay = false
    var isweekend = false
    var isWeekDay = false
    var isCustom = true

    private var isDateSelected = false

    var isMon = false
    var isTues = false
    var isWed = false
    var isThur = false
    var isFri = false
    var isSat = true
    var isSun = false


    lateinit var startDatePickerDialog: DatePickerDialog
    lateinit var endDatePickerDialog: DatePickerDialog
    private var startDate: Date? = null
    private var endDate: Date? = null
    var isRightEndDate = true
    lateinit var to_date: TextView
    lateinit var from_date: TextView

    private var selectedDays = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_create_season, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        roboto = ResourcesCompat.getFont(requireContext(), R.font.roboto)!!
        robotoMedium = ResourcesCompat.getFont(requireContext(),R.font.roboto_medium)!!

        to_date = view.findViewById(R.id.to_date_season)
        from_date = view.findViewById(R.id.from_date_season)

        txt_all_days = view.findViewById<TextView>(R.id.txt_all_days)
        txt_week_days = view.findViewById<TextView>(R.id.txt_week_days)
        txt_weekends = view.findViewById<TextView>(R.id.txt_weekends)
        txt_custom = view.findViewById<TextView>(R.id.txt_custom)
        sun = view.findViewById<TextView>(R.id.sunday)
        mon = view.findViewById<TextView>(R.id.monday)
        tues = view.findViewById<TextView>(R.id.tuesday)
        wed = view.findViewById<TextView>(R.id.wed)
        thur = view.findViewById<TextView>(R.id.thursday)
        fri = view.findViewById<TextView>(R.id.fri)
        sat = view.findViewById<TextView>(R.id.saturday)

        val shortCode = view.findViewById<TextInputEditText>(R.id.shortCodeText)
        val shortCodeLayout = view.findViewById<TextInputLayout>(R.id.short_code_layout_season)
        val seasonText = view.findViewById<TextInputEditText>(R.id.seasonText)
        val seasonTextLayout = view.findViewById<TextInputLayout>(R.id.seasonLayout)

        seasonText.doAfterTextChanged {
            if (seasonText.text!!.length>3){
                shortCode.setText(generateShortCode(seasonText.text.toString()))
            }
        }

        if (getSeasonData!!.seasonId != "") {
            from_date.text = getSeasonData.startDate
            to_date.text = getSeasonData.endDate
            seasonText.setText(getSeasonData.seasonName)
            shortCode.setText(getSeasonData.shortCode)

            startDate = convertStringToDate(getSeasonData.startDate)

            getSeasonData.days.forEach {
                when (it) {
                    "Monday" -> {
                        isMon = true
                        selectedDays.add("Monday")
                        selectCard(mon)
                    }
                    "Tuesday" -> {
                        isTues = true
                        selectedDays.add("Tuesday")
                        selectCard(tues)
                    }
                    "Wednesday" -> {
                        isWed = true
                        selectedDays.add("Wednesday")
                        selectCard(wed)
                    }
                    "Thursday" -> {
                        isThur = true
                        selectedDays.add("Thursday")
                        selectCard(thur)
                    }
                    "Friday" -> {
                        isFri = true
                        selectedDays.add("Friday")
                        selectCard(fri)
                    }
                    "Saturday" -> {
                        isSat = true
                        selectedDays.add("Saturday")
                        selectCard(sat)
                    }
                    "Sunday" -> {
                        isSun = true
                        selectedDays.add("Sunday")
                        selectCard(sun)
                    }
                }
            }

        }

        startDatePickerDialog = createDatePickerDialog(requireContext(),from_date) {date->
                startDate = date
        }
        endDatePickerDialog = createDatePickerDialog(requireContext(),to_date){date->

            endDatePickerDialog.datePicker.minDate = startDate!!.time

            if (startDate!=null&&date.before(startDate)){
                isRightEndDate = false

//                Toast.makeText(requireContext(), "cdjn jifnomk", Toast.LENGTH_SHORT).show()
                Toast.makeText(requireContext(), "End date cannot be before start date", Toast.LENGTH_SHORT).show()
                to_date.text = "--/--/----"
                Handler().postDelayed(Runnable {
                    isRightEndDate = true
                },1000)
            }
            else{
                isRightEndDate = true
//                Toast.makeText(requireContext(), "Karan ji", Toast.LENGTH_SHORT).show()
                endDate = date
                isDateSelected = true
            }
        }


        from_date.setOnClickListener {
            startDatePickerDialog.show()
        }
        to_date.setOnClickListener {
//            showCalendarDialog(requireContext(),to_date)
//            dialog.show()

            if (startDate!=null){
                endDatePickerDialog.datePicker.minDate = startDate!!.time
                endDatePickerDialog.show()
            }
        }


        val cancel = view.findViewById<TextView>(R.id.cancel)
        val save = view.findViewById<CardView>(R.id.saveBtn)

        txt_all_days.setBackgroundResource(R.drawable.rounded_border_light_black)
        txt_weekends.setBackgroundResource(R.drawable.rounded_border_light_black)
        txt_week_days.setBackgroundResource(R.drawable.rounded_border_light_black)
        selectCard(txt_custom)
        txt_custom.setBackgroundResource(R.drawable.rounded_border_black)

        fri.setOnClickListener {
            if (!isFri) {
                selectCard(fri)
                isFri = true
            } else {
                unSelectCard(fri)
                isFri = false
            }
        }
        sat.setOnClickListener {
            if (!isSat) {
                selectCard(sat)
                isSat = true
            } else {
                unSelectCard(sat)
                isSat = false
            }
        }
        sun.setOnClickListener {
            if (!isSun) {
                selectCard(sun)
                isSun = true
            } else {
                unSelectCard(sun)
                isSun = false
            }
        }
        mon.setOnClickListener {
            if (!isMon) {
                selectCard(mon)
                isMon = true
            } else {
                unSelectCard(mon)
                isMon = false
            }
        }
        tues.setOnClickListener {
            if (!isTues) {
                selectCard(tues)
                isTues = true
            } else {
                unSelectCard(tues)
                isTues = false
            }
        }
        wed.setOnClickListener {
            if (!isWed) {
                selectCard(wed)
                isWed = true
            } else {
                unSelectCard(wed)
                isWed = false
            }
        }
        thur.setOnClickListener {
            if (!isThur) {
                selectCard(thur)
                isThur = true
            } else {
                unSelectCard(thur)
                isThur = false
            }
        }


        txt_all_days.setOnClickListener {

            selectHead(txt_all_days)
            selectCard(sun)
            selectCard(mon)
            selectCard(tues)
            selectCard(wed)
            selectCard(thur)
            selectCard(fri)
            selectCard(sat)
            notClickable()
            isAllDay = true

        }
        txt_weekends.setOnClickListener {

            selectHead(txt_weekends)
            unSelectAllDays()
            selectCard(sat)
            selectCard(sun)
            isweekend = true
            notClickable()

        }
        txt_week_days.setOnClickListener {

            selectHead(txt_week_days)
            unSelectAllDays()

            selectCard(mon)
            selectCard(tues)
            selectCard(wed)
            selectCard(thur)
            selectCard(fri)
            isWeekDay = true

        }
        txt_custom.setOnClickListener {

            selectHead(txt_custom)
            unSelectAllDays()
            allClickable()
            isCustom = true
        }

        cancel.setOnClickListener {
            startDate = null
            endDate = null
            dismiss()
        }
        save.setOnClickListener {
            if (seasonText.text!!.isEmpty()){
                shakeAnimation(seasonTextLayout,requireContext())
            }
            else if(shortCode.text!!.isEmpty()){
                shakeAnimation(shortCodeLayout,requireContext())
            }
            else if (!isDateSelected){
                shakeAnimation(from_date,requireContext())
                shakeAnimation(to_date,requireContext())
            }
            else{
                progressDialog = showProgressDialog(requireContext())
                if (getSeasonData.seasonId != "") {
                    updateSeason(shortCode.text.toString(), seasonText.text.toString())
                } else {
                    saveSeason(
                        requireContext(),
                        shortCode.text.toString(),
                        seasonText.text.toString()
                    )
                }
            }
        }

    }

    private fun saveSeason(context: Context, shortCodeTxt : String, season:String) {
        val create = OAuthClient<GeneralsAPI>(context).create(GeneralsAPI::class.java).addSeasonApi(
            AddSeasonDataClass(UserSessionManager(context).getUserId().toString(), UserSessionManager(context).getPropertyId().toString(), season, shortCodeTxt, from_date.text.toString(), to_date.text.toString(), selectedDays)
        )
        create.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                progressDialog.dismiss()
                Log.d( "season", "${response.code()} ${response.message()}")
                dismiss()
                mListener?.onSeasonSave()
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                progressDialog.dismiss()
                Log.e("error", t.localizedMessage)
            }
        })
    }

    private fun allClickable() {
        sun.isClickable = true
        mon.isClickable = true
        tues.isClickable = true
        wed.isClickable = true
        thur.isClickable = true
        fri.isClickable = true
        sat.isClickable = true
    }

    private fun unSelectAllDays() {
        selectedDays.clear()
        unSelectCard(sun)
        unSelectCard(mon)
        unSelectCard(tues)
        unSelectCard(wed)
        unSelectCard(thur)
        unSelectCard(fri)
        unSelectCard(sat)
    }

    private fun notClickable() {
        sun.isClickable = false
        mon.isClickable = false
        tues.isClickable = false
        wed.isClickable = false
        thur.isClickable = false
        fri.isClickable = false
        sat.isClickable = false
    }

    private fun selectHead(head: TextView?) {
        unSelectCard(txt_all_days)
        unSelectCard(txt_week_days)
        unSelectCard(txt_weekends)
        unSelectCard(txt_custom)

        head?.typeface = robotoMedium
        selectCard(head)
    }

    private fun selectCard(day: TextView?) {
        val days = day?.text.toString()
        if (!selectedDays.contains(days) && days != "All Days" && days != "Weekdays" && days != "Weekends" && days != "Custom"){
            when (days) {
                "Mon" -> {
                    isMon = true
                    selectedDays.add("Monday")
                }
                "Tue" -> {
                    isTues = true
                    selectedDays.add("Tuesday")
                }
                "Wed" -> {
                    isWed = true
                    selectedDays.add("Wednesday")
                }
                "Thu" -> {
                    isThur = true
                    selectedDays.add("Thursday")
                }
                "Fri" -> {
                    isFri = true
                    selectedDays.add("Friday")
                }
                "Sat" -> {
                    isSat = true
                    selectedDays.add("Saturday")
                }
                else -> {
                    isSun = true
                    selectedDays.add("Sunday")
                }
            }
        }
        day?.setBackgroundResource(R.drawable.rounded_border_black)
        day?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
    }

    private fun unSelectCard(day: TextView?) {
        val days = day?.text.toString()
        if (selectedDays.contains(day?.text.toString()) && days != "All Days" && days != "Weekdays" && days != "Weekends" && days != "Custom"){

            when (days) {
                "Mon" -> {
                    isMon = false
                    selectedDays.remove("Monday")
                }
                "Tue" -> {
                    isTues = false
                    selectedDays.remove("Tuesday")
                }
                "Wed" -> {
                    isWed = false
                    selectedDays.remove("Wednesday")
                }
                "Thu" -> {
                    isThur = false
                    selectedDays.remove("Thursday")
                }
                "Fri" -> {
                    isFri = false
                    selectedDays.remove("Friday")
                }
                "Sat" -> {
                    isSat = false
                    selectedDays.remove("Saturday")
                }
                else -> {
                    isSun = false
                    selectedDays.remove("Sunday")
                }
            }

        }
        day?.setBackgroundResource(R.drawable.rounded_border_light_black)
        day?.setTextColor(ContextCompat.getColor(requireContext(), R.color.lightBlack))
        day?.typeface = roboto
    }

    private fun createDatePickerDialog(applicationContext: Context, textDate:TextView, onDateSetListener: (Date) -> Unit): DatePickerDialog {
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

    private fun updateSeason(shortCodeTxt : String, season:String) {
        val create = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).updateSeasonTypeApi(
            UserSessionManager(requireContext()).getUserId().toString(),
            getSeasonData!!.seasonId,
            UpdateSeasonDataClass( shortCodeTxt, season, from_date.text.toString(), to_date.text.toString(), selectedDays)
        )
        create.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                progressDialog.dismiss()
                if (response.isSuccessful){
                    response.body()
                }
                mListener?.onSeasonSave()
                Log.d( "season", "${response.code()} ${response.message()}")
                dismiss()
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                progressDialog.dismiss()
                dismiss()
                Log.e("error", t.localizedMessage)
            }
        })
    }

}