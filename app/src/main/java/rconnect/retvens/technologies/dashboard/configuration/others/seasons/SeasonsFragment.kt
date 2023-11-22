package rconnect.retvens.technologies.dashboard.configuration.others.seasons

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
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
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.FragmentSeasonsBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.fetchTargetTimeZoneId
import rconnect.retvens.technologies.utils.generateShortCode
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.util.Date


class SeasonsFragment : Fragment() {
    private lateinit var binding: FragmentSeasonsBinding
    lateinit var loader:Dialog
    private var list = ArrayList<String>()
    private lateinit var robotoMedium : Typeface
    private lateinit var roboto:Typeface
    var isDismissAllowed = true
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
    private var startDate:Date? = null
    private var endDate:Date? = null
    var isRightEndDate = true
    lateinit var to_date:TextView
    lateinit var from_date:TextView

    var selectedDays = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSeasonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        roboto = ResourcesCompat.getFont(requireContext(), R.font.roboto)!!
        robotoMedium = ResourcesCompat.getFont(requireContext(),R.font.roboto_medium)!!

        loader = showProgressDialog(requireContext())
        setUpRecycler()

        binding.createNewBtn.setOnClickListener {
            openCreateNewDialog()
        }

    }

    private fun showToast(s: String) {
        Toast.makeText(requireContext(), "$s", Toast.LENGTH_SHORT).show()
    }

    private fun openCreateNewDialog() {

            val dialog =
                Dialog(requireContext()) // Use 'this' as the context, assuming this code is within an Activity
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(R.layout.dialog_create_season)
            dialog.window?.apply {
                setBackgroundDrawableResource(android.R.color.transparent) // Makes the background transparent
                setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

         to_date = dialog.findViewById<TextView>(R.id.to_date_season)
         from_date = dialog.findViewById<TextView>(R.id.from_date_season)

//        startDatePickerDialog = createDatePickerDialog {date->
//        }
//        endDatePickerDialog = createDatePickerDialog { date->
//        }


        startDatePickerDialog = createDatePickerDialog(requireContext(),from_date) {date->
            startDate = date
        }
        endDatePickerDialog = createDatePickerDialog(requireContext(),to_date){date->

            endDatePickerDialog.datePicker.minDate = startDate!!.time

            if (startDate!=null&&date.before(startDate)){
                isRightEndDate = false

//                Toast.makeText(requireContext(), "cdjn jifnomk", Toast.LENGTH_SHORT).show()
                showToast("End date cannot be before start date")
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
            dialog.show()
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




        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        val save = dialog.findViewById<CardView>(R.id.saveBtn)
         txt_all_days = dialog.findViewById<TextView>(R.id.txt_all_days)
         txt_week_days = dialog.findViewById<TextView>(R.id.txt_week_days)
         txt_weekends = dialog.findViewById<TextView>(R.id.txt_weekends)
         txt_custom = dialog.findViewById<TextView>(R.id.txt_custom)
         sun = dialog.findViewById<TextView>(R.id.sunday)
         mon = dialog.findViewById<TextView>(R.id.monday)
         tues = dialog.findViewById<TextView>(R.id.tuesday)
         wed = dialog.findViewById<TextView>(R.id.wed)
         thur = dialog.findViewById<TextView>(R.id.thursday)
         fri = dialog.findViewById<TextView>(R.id.fri)
         sat = dialog.findViewById<TextView>(R.id.saturday)

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


        val shortCode = dialog.findViewById<TextInputEditText>(R.id.shortCodeText)
        val shortCodeLayout = dialog.findViewById<TextInputLayout>(R.id.short_code_layout_season)
        val seasonText = dialog.findViewById<TextInputEditText>(R.id.seasonText)
        val seasonTextLayout = dialog.findViewById<TextInputLayout>(R.id.seasonLayout)

        seasonText.doAfterTextChanged {
            if (seasonText.text!!.length>3){
                shortCode.setText(generateShortCode(seasonText.text.toString()))
            }
        }

        cancel.setOnClickListener {
            startDate = null
            endDate = null
            dialog.dismiss()
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
                loader.show()
                saveSeason(requireContext(), dialog, shortCode.text.toString(), seasonText.text.toString())
            }
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.END)

        dialog.show()
    }

    private fun saveSeason(context: Context, dialog: Dialog, shortCodeTxt : String, season:String) {
        val create = OAuthClient<GeneralsAPI>(context).create(GeneralsAPI::class.java).addSeasonApi(
            AddSeasonDataClass(UserSessionManager(context).getUserId().toString(), UserSessionManager(context).getPropertyId().toString(), season, shortCodeTxt, from_date.text.toString(), to_date.text.toString(), selectedDays)
        )
        create.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                loader.dismiss()
                Log.d( "season", "${response.code()} ${response.message()}")
                dialog.dismiss()
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                loader.dismiss()
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

    private fun setUpRecycler() {

        binding.paymentTypeRecycler.layoutManager = LinearLayoutManager(requireContext())

        val identity = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).getSeasonApi(UserSessionManager(requireContext()).getUserId().toString(), UserSessionManager(requireContext()).getPropertyId().toString(),
            fetchTargetTimeZoneId()
        )
        identity.enqueue(object : Callback<GetSeasonDataClass?> {
            override fun onResponse(
                call: Call<GetSeasonDataClass?>,
                response: Response<GetSeasonDataClass?>
            ) {
                loader.dismiss()
                if (response.isSuccessful){
                    val adapter = SeasonAdapter(response.body()!!.data, requireContext())
                    binding.paymentTypeRecycler.adapter = adapter
                    adapter.notifyDataSetChanged()
                } else {
                    openCreateNewDialog()
                    Log.d("error" , "${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetSeasonDataClass?>, t: Throwable) {
                Toast.makeText(requireContext(), t.message.toString(), Toast.LENGTH_SHORT).show()
                loader.dismiss()
            }
        })

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

//    private fun allSelected(): Boolean {
//        return isSun && isMon && isTue && isWed && isThu && isFri && isSat
//    }


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



}