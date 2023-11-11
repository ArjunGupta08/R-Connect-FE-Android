package rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.databinding.FragmentAddReservationBinding
import rconnect.retvens.technologies.utils.utilCreateDatePickerDialog
import java.util.Date
import java.util.concurrent.TimeUnit

class AddReservationFragment : Fragment() {
    lateinit var binding: FragmentAddReservationBinding
    var rList = ArrayList<String>()
    var startDate: Date? = null
    var endDate: Date? = null
    lateinit var startDatePickerDialog: DatePickerDialog
    lateinit var endDatePickerDialog: DatePickerDialog
    var nights = ""

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
        rList.addAll(List(1) { "" })

        binding.recyclerRoomDetails.layoutManager = LinearLayoutManager(requireContext())
        val addReservationAdapter = AddReservationAdapter(requireContext(), rList)
        binding.recyclerRoomDetails.adapter = addReservationAdapter

        binding.roomCount.text = "(${rList.size} Rooms Added)"

        binding.llAddRoom.setOnClickListener {
            val position = rList.size
            rList.add("")
//            addReservationAdapter.notifyDataSetChanged()
            addReservationAdapter.notifyItemInserted(position)
            binding.roomCount.text = "(${rList.size} Rooms Added)"
        }

        startDatePickerDialog =
            utilCreateDatePickerDialog(requireContext(), binding.checkIn) { date ->
                startDate = date
            }
        endDatePickerDialog =
            utilCreateDatePickerDialog(requireContext(), binding.checkOut) { date ->
                endDate = date
                nights = getDaysBetween(startDate!!,endDate!!).toString()
                Toast.makeText(requireContext(), "$nights night", Toast.LENGTH_SHORT).show()
                binding.countNight.text = nights.toString()
                binding.countNight2.text = nights.toString()
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
                Toast.makeText(requireContext(), "$nights night", Toast.LENGTH_SHORT).show()
                binding.countNight.text = nights.toString()
                binding.countNight2.text = nights.toString()
            }
        }

        val options = arrayOf("Deluxe", "Premium", "Elite")


        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)

        // Set up the TextInputLayout with an end icon
        binding.bookingSourceLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM
//        binding.bookingSourceLayout.setEndIconDrawable(R.drawable.ic_dropdown)

        // Set a click listener for the end icon
        binding.spin.setOnClickListener {
            // Show dropdown menu
            showDropdownMenu(adapter,it)
        }
    }

    private fun showDropdownMenu(adapter: ArrayAdapter<String>, anchorView: View) {
        val listPopupWindow = ListPopupWindow(requireContext())
        listPopupWindow.setAdapter(adapter)
        listPopupWindow.anchorView = anchorView
        listPopupWindow.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = adapter.getItem(position)
            binding.bookingSourceLayout.editText?.setText(selectedItem)
            listPopupWindow.dismiss()
        }


        listPopupWindow.show()
    }
    fun getDaysBetween(startDate: Date, endDate: Date): Long {
        val diffInMillies = endDate.time - startDate.time
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)
    }

    }
