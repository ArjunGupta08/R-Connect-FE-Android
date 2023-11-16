package rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.AddReservationApis
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.RatesAndInventoryInterface
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.FragmentAddReservationBinding
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.utilCreateDatePickerDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.ref.SoftReference
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class AddReservationFragment : Fragment(), AddReservationAdapter.OnItemClick {
    lateinit var binding: FragmentAddReservationBinding
    var rList = ArrayList<String>()
    var startDate: Date? = null
    var endDate: Date? = null
    lateinit var startDatePickerDialog: DatePickerDialog
    lateinit var endDatePickerDialog: DatePickerDialog
    lateinit var checkInDate:String
    lateinit var checkOutDate:String
    var rateTypeId:String = ""
    var bookingTypeId:String = ""
    var bookingSourceId:String = ""
    var nights = ""
    private var rateTypeList:ArrayList<RateType> = ArrayList()
    private var reservationList:ArrayList<ReservationItem> = ArrayList()
    private var bookingSource:ArrayList<BookingItem> = ArrayList()
    private var roomDetailsList:ArrayList<RoomDetail> = ArrayList()
    private lateinit var addReservationAdapter:AddReservationAdapter
    private  var userId = ""
    private var propertyId = ""
    private  var  listRoom:ArrayList<RoomDetail> = ArrayList()
    private var availableList:ArrayList<AvailableRoomType> = ArrayList()
    private var apiCheckInDate:String = ""
    private var apiCheckOutDate:String = ""
    private var barRateReservation:ArrayList<BarRateReservation> = ArrayList()
    private var reservationSummary:ArrayList<ReservationSummary> = ArrayList()
    private var guestInfo:ArrayList<GuestInfo> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddReservationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = UserSessionManager(requireContext()).getUserId().toString()
        propertyId = UserSessionManager(requireContext()).getPropertyId().toString()

        getRateType()
        getBookingSource()
        getReservationType()

        guestInfo.add(GuestInfo("Shubham Singh","88464","shub@gmail.com","kujqwhdukbs","kujqhubd","India","MP","INdore","501401"))

        binding.recyclerRoomDetails.layoutManager = LinearLayoutManager(requireContext())
        addReservationAdapter = AddReservationAdapter(requireContext(), roomDetailsList,availableList)
        binding.recyclerRoomDetails.adapter = addReservationAdapter

        addReservationAdapter.setOnClickListener(this)

        addReservationAdapter.addItem()
        listRoom = addReservationAdapter.reservationList

        binding.roomCount.text = "1 Room Added"

        binding.llAddRoom.setOnClickListener {
            addReservationAdapter.addItem()

            binding.roomCount.text = "(${listRoom.size} Rooms Added)"

        }

        startDatePickerDialog =
            utilCreateDatePickerDialog(requireContext(), binding.checkIn) { date ->
                startDate = date
                checkInDate = formatDate(date.toString())
                binding.dateFrom.text = checkInDate
                apiCheckInDate = ApiformatDate(date.toString())
                getAvailableRoom()
            }
        endDatePickerDialog =
            utilCreateDatePickerDialog(requireContext(), binding.checkOut) { date ->
                endDate = date
                checkOutDate = formatDate(date.toString())
                nights = getDaysBetween(startDate!!,endDate!!).toString()
                binding.dateTo.text = checkOutDate
                binding.countNight.text = nights.toString()
                binding.countNight2.text = nights.toString()
                apiCheckOutDate = ApiformatDate(date.toString())
                getAvailableRoom()

            }

        binding.CheckInLayout.setEndIconOnClickListener {
            endDate = null

            // Set the minimum date for the start date picker to be the current date
            startDatePickerDialog.datePicker.minDate = System.currentTimeMillis()
            startDatePickerDialog.show()
        }
        binding.CheckOutLayout.setEndIconOnClickListener {
            if (startDate != null) {
                endDatePickerDialog.datePicker.minDate = startDate!!.time
                endDatePickerDialog.show()
                if (endDate!=null){
                    nights = getDaysBetween(startDate!!,endDate!!).toString()
                }
                binding.countNight.text = nights.toString()
                binding.countNight2.text = nights.toString()
            }
        }

        val options = arrayOf("Deluxe", "Premium", "Elite")


        val adapter = ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, bookingSource)

        // Set up the TextInputLayout with an end icon
        binding.bookingSourceLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM
//        binding.bookingSourceLayout.setEndIconDrawable(R.drawable.ic_dropdown)

        // Set a click listener for the end icon
        binding.spin.setOnClickListener {
            // Show dropdown menu
            showBookingDropDown(adapter,it)
        }

        val rateAdapter = ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, rateTypeList)

        binding.rateTypeText.setOnClickListener {
            showRateDropDown(rateAdapter, it)
        }


        val bookingTypeAdapter = ArrayAdapter(requireContext(),R.layout.simple_dropdown_item_1line,reservationList)

        binding.bookingTypeText.setOnClickListener {
            showBookingTypeDropDown(bookingTypeAdapter,it)
        }

        binding.submitCard.setOnClickListener {
            generateBooking()
        }

    }

    private fun generateBooking() {



        barRateReservation.add(BarRateReservation(bookingTypeId,bookingSourceId))

        val booking = ReservationDataClass(
            userId,
            propertyId,
            apiCheckInDate,
            apiCheckOutDate,
            nights,
            rateTypeId,
            guestInfo,
            barRateReservation,
            roomDetailsList,
            emptyList(),
            reservationSummary,
            "",
            emptyList(),
            emptyList(),
            emptyList(),
        )

        val generateBooking = OAuthClient<AddReservationApis>(requireContext()).create(AddReservationApis::class.java).generateBooking(booking)

        Log.e("generateBooking",booking.toString())

        generateBooking.enqueue(object : Callback<BookingResponse?> {
            override fun onResponse(
                call: Call<BookingResponse?>,
                response: Response<BookingResponse?>
            ) {
                if(response.isSuccessful){
                    val response = response.body()!!
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                    Log.e("response",response.message.toString())
                }else{
                    Log.e("error",response.code().toString())
                }
            }

            override fun onFailure(call: Call<BookingResponse?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }
        })
    }

    private fun getAvailableRoom() {
        Log.e("apiCheckInDate",apiCheckInDate)
        Log.e("apiCheckOutDate",apiCheckOutDate)
        val getAvailableRoom = OAuthClient<AddReservationApis>(requireContext()).create(AddReservationApis::class.java).getAvailableRoom(userId,propertyId,apiCheckInDate,apiCheckOutDate)
        getAvailableRoom.enqueue(object : Callback<AvailableRoomDataClass?> {
            override fun onResponse(
                call: Call<AvailableRoomDataClass?>,
                response: Response<AvailableRoomDataClass?>
            ) {
                if (response.isSuccessful){
                    val responses = response.body()!!
                    availableList.addAll(responses.data)
                    addReservationAdapter.notifyDataSetChanged()
                }else{
                    Log.e("error",response.code().toString())
                }
            }

            override fun onFailure(call: Call<AvailableRoomDataClass?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }
        })
    }


    private fun showRateDropDown(adapter: ArrayAdapter<RateType>, it: View?) {
        val listPopupWindow = ListPopupWindow(requireContext())

        // Create a custom adapter to display only the rateType property
        val customAdapter = object : ArrayAdapter<RateType>(requireContext(), R.layout.simple_dropdown_item_1line, rateTypeList) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val rateType = getItem(position)?.rateType
                (view as TextView).text = rateType
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val rateType = getItem(position)?.rateType
                (view as TextView).text = rateType
                return view
            }
        }

        listPopupWindow.setAdapter(customAdapter)

        listPopupWindow.anchorView = it
        listPopupWindow.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = customAdapter.getItem(position)
            binding.rateTypeLayout.editText?.setText(selectedItem?.rateType)
            rateTypeId = selectedItem?.rateTypeId.toString()
            listPopupWindow.dismiss()
        }

        listPopupWindow.show()
    }

    private fun showBookingTypeDropDown(adapter: ArrayAdapter<ReservationItem>, it: View?) {
        val listPopupWindow = ListPopupWindow(requireContext())

        // Create a custom adapter to display only the rateType property
        val customAdapter = object : ArrayAdapter<ReservationItem>(requireContext(), R.layout.simple_dropdown_item_1line, reservationList) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val bookingType = getItem(position)?.reservationName
                (view as TextView).text = bookingType
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val bookingType = getItem(position)?.reservationName
                (view as TextView).text = bookingType
                return view
            }
        }

        listPopupWindow.setAdapter(customAdapter)

        listPopupWindow.anchorView = it
        listPopupWindow.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = customAdapter.getItem(position)
            binding.bookingTypeLayout.editText?.setText(selectedItem?.reservationName)
            bookingTypeId = selectedItem?.reservationTypeId.toString()
            listPopupWindow.dismiss()
        }

        listPopupWindow.show()
    }


    private fun showBookingDropDown(adapter: ArrayAdapter<BookingItem>, it: View?) {
        val listPopupWindow = ListPopupWindow(requireContext())

        // Create a custom adapter to display only the rateType property
        val customAdapter = object : ArrayAdapter<BookingItem>(requireContext(), R.layout.simple_dropdown_item_1line, bookingSource) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val bookingSource = getItem(position)?.bookingSource
                (view as TextView).text = bookingSource
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val bookingSource = getItem(position)?.bookingSource
                (view as TextView).text = bookingSource
                return view
            }
        }

        listPopupWindow.setAdapter(customAdapter)

        listPopupWindow.anchorView = it
        listPopupWindow.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = customAdapter.getItem(position)
            binding.bookingSourceLayout.editText?.setText(selectedItem?.bookingSource)
            bookingSourceId = selectedItem?.bookingSourceId.toString()
            listPopupWindow.dismiss()
        }

        listPopupWindow.show()
    }
    fun getDaysBetween(startDate: Date, endDate: Date): Long {
        val diffInMillies = endDate.time - startDate.time
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)
    }

    fun getRateType(){
        val getRateType = RetrofitObject.reservationApis.getRateType()

        getRateType.enqueue(object : Callback<RateTypeDataClass?> {
            override fun onResponse(
                call: Call<RateTypeDataClass?>,
                response: Response<RateTypeDataClass?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    rateTypeList.addAll(response.data)
                }else{
                    Log.e("error",response.code().toString())
                }
            }

            override fun onFailure(call: Call<RateTypeDataClass?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }
        })
    }

    fun getBookingSource(){
        val getBookingSource =  OAuthClient<AddReservationApis>(requireContext()).create(AddReservationApis::class.java).getBookingSource(userId,propertyId)

        getBookingSource.enqueue(object : Callback<BookingSorceDataClass?> {
            override fun onResponse(
                call: Call<BookingSorceDataClass?>,
                response: Response<BookingSorceDataClass?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    bookingSource.addAll(response.data)
                }else{
                    Log.e("error",response.code().toString())
                }
            }

            override fun onFailure(call: Call<BookingSorceDataClass?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }
        })

    }

    fun getReservationType(){

        Log.e("userId",userId)
        Log.e("property",propertyId)

        val getReservationType = OAuthClient<AddReservationApis>(requireContext()).create(AddReservationApis::class.java).getReservationType(userId,propertyId)
        getReservationType.enqueue(object : Callback<ReservationTypeDataClass?> {
            override fun onResponse(
                call: Call<ReservationTypeDataClass?>,
                response: Response<ReservationTypeDataClass?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    reservationList.addAll(response.data)
                        Log.e("res",response.toString())
                }else{
                    Log.e("error",response.code().toString())
                }
            }

            override fun onFailure(call: Call<ReservationTypeDataClass?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }
        })
    }

    fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)

        try {
            val date = inputFormat.parse(inputDate)
            return outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return inputDate // Return the original date if parsing fails
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

    override fun onItemDelete(count: String) {
        binding.roomCount.text = "(${count} Rooms Added)"
    }

    override fun updateRates() {

        val totalCharge = listRoom.sumBy { it.charge.toIntOrNull() ?: 0 }

        val grandTotal = listRoom.sumBy {
            val charge = it.charge.toIntOrNull() ?: 0
            val extras = it.extraInclusion.toIntOrNull() ?: 0
            val totalWithoutGST = charge + extras
            val gst = 0.16 // 16% GST

            // Calculate the total amount with GST
            val totalWithGST = totalWithoutGST + (totalWithoutGST * gst).toInt()

            totalWithGST
        }
        binding.txtRoomCharges.text = "₹ ${totalCharge.toString()}"
        Log.e("listRoom",listRoom.toString())

        binding.txtGrandTotal.text = "₹ ${grandTotal.toString()}"

        reservationSummary.clear()

        reservationSummary.add(ReservationSummary(totalCharge.toString(),"","",apiCheckInDate,apiCheckOutDate,grandTotal.toString()))
    }
}
