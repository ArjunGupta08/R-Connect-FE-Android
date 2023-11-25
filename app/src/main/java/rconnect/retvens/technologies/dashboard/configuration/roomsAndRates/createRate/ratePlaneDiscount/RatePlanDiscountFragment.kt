package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlaneDiscount

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.card.MaterialCardView
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.configurationApi.SingleConfiguration
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.CreateRateData
import rconnect.retvens.technologies.databinding.FragmentRatePlanDiscountBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showProgressDialog
import rconnect.retvens.technologies.utils.utilCreateDatePickerDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RatePlanDiscountFragment : Fragment(), CreateRatePlanAdapter.OnRateTypeListChangeListener, BlackOutDateAdapter.OnBlackOutDateListChangeListener {
    private lateinit var binding : FragmentRatePlanDiscountBinding

    var startDate: Date?= null
    var endDate: Date? = null
    lateinit var startDatePickerDialog: DatePickerDialog
    lateinit var endDatePickerDialog: DatePickerDialog
    private lateinit var progressDialog: Dialog
    private var getBarRate:ArrayList<GetBarRate> = ArrayList()
    private lateinit var createRatePlanAdapter:CreateRatePlanAdapter
    private var applicableOn:ArrayList<ApplicableOn> = ArrayList()
    var discountPercentages = ""
    var discountAmounts = ""
    var apiFromDate = ""
    var apiToDate = ""
    var discountType = ""
    var discountPrice = ""


    private var dateArray = ArrayList<String>()
    private lateinit var blackOutDateAdapter: BlackOutDateAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRatePlanDiscountBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = showProgressDialog(requireContext())
        getBarRatePlan()

//    private val discountDta = RatePlanDiscountData()



        binding.recycler.layoutManager = LinearLayoutManager(requireContext())

        createRatePlanAdapter = CreateRatePlanAdapter(requireContext(),getBarRate,"","")
        binding.recycler.adapter = createRatePlanAdapter
        createRatePlanAdapter.notifyDataSetChanged()
        createRatePlanAdapter.setOnListUpdateListener(this)

        binding.dateRecycler.layoutManager = GridLayoutManager(requireContext(), 2)

        binding.addDate.setOnClickListener {
            showDatePicker()
        }

        binding.amountCard.setOnClickListener {
            discountType = "Amount"
            binding.discountTextLayout.hint = "Discount ₹"
            cardSelected(binding.amountCard, binding.amountText)
            binding.discountText.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val discountAmount = p0.toString()
                    Log.e("char",p0.toString())
                    val percentage = ""
                    discountPrice = p0.toString()

                    discountAmounts = discountAmount
                    discountPercentages = percentage
                    createRatePlanAdapter = CreateRatePlanAdapter(requireContext(),getBarRate,discountAmount,percentage)
                    binding.recycler.adapter = createRatePlanAdapter
                    createRatePlanAdapter.notifyDataSetChanged()
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })

        }
        binding.percentCard.setOnClickListener {
            discountType = "Percentage"
            binding.discountTextLayout.hint = "Discount %"
            cardSelected(binding.percentCard, binding.percentText)
            binding.discountText.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val discountPercentage = p0.toString()
                    val discountAmount = ""
                    discountPrice = p0.toString()
                    discountPercentages = discountPercentage
                    discountAmounts = discountAmount
                    createRatePlanAdapter = CreateRatePlanAdapter(requireContext(),getBarRate,discountAmount,discountPercentage)
                    binding.recycler.adapter = createRatePlanAdapter
                    createRatePlanAdapter.notifyDataSetChanged()
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })

        }

        startDatePickerDialog = utilCreateDatePickerDialog(requireContext(),binding.from) {date->
            startDate = date
            apiFromDate = ApiformatDate(date.toString())
        }
        endDatePickerDialog = utilCreateDatePickerDialog(requireContext(),binding.to){date->
            endDate = date
            apiToDate = ApiformatDate(date.toString())
        }


        binding.fromLayout.setEndIconOnClickListener {
            endDate = null

            // Set the minimum date for the start date picker to be the current date
            startDatePickerDialog.datePicker.minDate = System.currentTimeMillis()
            startDatePickerDialog.show()
        }
        binding.toLayout.setEndIconOnClickListener {
            if (startDate!=null){
                endDatePickerDialog.datePicker.minDate = startDate!!.time
                endDatePickerDialog.show()
            }
        }

        binding.ratePlanName.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val code = createShortCode(p0.toString())
                binding.shortCodeText.setText(code)
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


        discountType = "Percentage"
        binding.discountTextLayout.hint = "Discount %"
        cardSelected(binding.percentCard, binding.percentText)
        binding.discountText.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val discountPercentage = p0.toString()
                val discountAmount = ""
                discountPrice = p0.toString()
                discountPercentages = discountPercentage
                discountAmounts = discountAmount
                createRatePlanAdapter = CreateRatePlanAdapter(requireContext(),getBarRate,discountAmount,discountPercentage)
                binding.recycler.adapter = createRatePlanAdapter
                createRatePlanAdapter.notifyDataSetChanged()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        val continueBtn = requireActivity().findViewById<CardView>(R.id.continueBtnRate)
        continueBtn?.setOnClickListener {
            if (binding.ratePlanName.text!!.isEmpty()){
                shakeAnimation(binding.ratePlanLayout,requireContext())
            }else if (binding.shortCodeText.text!!.isEmpty()){
                shakeAnimation(binding.shortCode,requireContext())
            }else if (binding.discountText.text!!.isEmpty()){
                shakeAnimation(binding.discountTextLayout,requireContext())
            }else if (startDate == null || endDate == null) {
                Toast.makeText(requireContext(), "Please Select Proper Date", Toast.LENGTH_SHORT).show()
            }else{
                if (createRatePlanAdapter.applicableList.size == 0){
                    Toast.makeText(requireContext(), "Select Plan", Toast.LENGTH_SHORT).show()
                }else{
                    progressDialog = showProgressDialog(requireContext())
                    setDiscount()
                }

            }
        }

    }

    private fun setDiscount() {

        val discountData = AddDiscountDataClass(
            "Android",
            UserSessionManager(requireContext()).getPropertyId().toString(),
            binding.ratePlanName.text.toString(),
            binding.shortCodeText.text.toString(),
            discountType,
            discountPercentages,
            discountPrice,
            apiFromDate,
            apiToDate,
            emptyList(),
            createRatePlanAdapter.applicableList

        )

        val sendData = OAuthClient<SingleConfiguration>(requireContext()).create(SingleConfiguration::class.java).addDiscount(
            UserSessionManager(requireContext()).getUserId().toString(),
            discountData
        )

        sendData.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }else{
                    Log.e("error",response.code().toString())
                    progressDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                Log.e("error",t.message.toString())
                progressDialog.dismiss()
            }
        })

    }

    private fun getBarRatePlan() {
        val userId  = UserSessionManager(requireContext()).getUserId()!!
        val propertyId = UserSessionManager(requireContext()).getPropertyId()!!
        val getBarRatePlan = OAuthClient<SingleConfiguration>(requireContext()).create(SingleConfiguration::class.java).getRoomWithPlans(userId,propertyId)

        getBarRatePlan.enqueue(object : Callback<GetBarRatePlanForDiscountDataClass?> {
            override fun onResponse(
                call: Call<GetBarRatePlanForDiscountDataClass?>,
                response: Response<GetBarRatePlanForDiscountDataClass?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()?.data!!
                    response.forEach {
                        getBarRate.add(it)
                        progressDialog.dismiss()
                    }
                    Log.e("res",response.toString())
                    createRatePlanAdapter.notifyDataSetChanged()
                }else{
                    Log.e("error",response.code().toString())
                    progressDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<GetBarRatePlanForDiscountDataClass?>, t: Throwable) {
                Log.e("error",t.message.toString())
                progressDialog.dismiss()
            }
        })
    }

    private fun cardSelected(card : MaterialCardView, text : TextView){
        binding.amountCard.strokeColor = ContextCompat.getColor(requireContext(), R.color.textInputStrokeColor)
        binding.percentCard.strokeColor = ContextCompat.getColor(requireContext(), R.color.textInputStrokeColor)
        binding.amountText.setTextColor(ContextCompat.getColor(requireContext(), R.color.subtitle))
        binding.percentText.setTextColor(ContextCompat.getColor(requireContext(), R.color.subtitle))

        card.strokeColor = ContextCompat.getColor(requireContext(), R.color.secondary)
        text.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))
    }

    fun createShortCode(input: String): String {
        // Convert the input string to uppercase
        val upperCaseInput = input.toUpperCase()

        // Take the first three characters or the entire string if its length is less than 3
        return upperCaseInput.take(3)
    }


    override fun onRateTypeList() {
        Log.e("list",createRatePlanAdapter.applicableList.toString())
    }

    fun ApiformatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

        try {
            val date = inputFormat.parse(inputDate)
            return outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return inputDate // Return the original date if parsing fails
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view, selectedYear, selectedMonth, selectedDay ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay)

                val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedCalendar.time)

                dateArray.add(formattedDate)
//                binding.addDate.text = formattedDate

                blackOutDateAdapter = BlackOutDateAdapter(requireContext(), dateArray)
                binding.dateRecycler.adapter = blackOutDateAdapter
                blackOutDateAdapter.setOnBlackOutDateListUpdateListener(this)
            },
            year,
            month,
            dayOfMonth
        )

        // Set a minimum date if needed
        // datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000

        datePickerDialog.show()
    }

    override fun onBlackOutDateDelete(position: Int) {
        dateArray.removeAt(position)
    }

}