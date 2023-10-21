package rconnect.retvens.technologies.dashboard.configuration.others.seasons

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.others.HolidaysAdapter
import rconnect.retvens.technologies.databinding.FragmentSeasonsBinding
import java.util.Calendar


class SeasonsFragment : Fragment() {
    private lateinit var binding: FragmentSeasonsBinding

    private lateinit var adapter: HolidaysAdapter
    private var list = ArrayList<String>()
    lateinit var roboto: Typeface
    var isSun = false
    var isMon = false
    var isTue = false
    var isWed = false
    var isThu = false
    var isFri = false
    var isSat = false
    var isAllDay = false


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

        setUpRecycler()

        binding.createNewBtn.setOnClickListener {
            openCreateNewDialog()
        }

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

        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        val save = dialog.findViewById<CardView>(R.id.saveBtn)
        val allDays = dialog.findViewById<TextView>(R.id.allDays)
        val sunday = dialog.findViewById<TextView>(R.id.sunday)
        val monday = dialog.findViewById<TextView>(R.id.monday)
        val tuesday = dialog.findViewById<TextView>(R.id.tuesday)
        val wednesday = dialog.findViewById<TextView>(R.id.wednesday)
        val thursday = dialog.findViewById<TextView>(R.id.thursday)
        val friday = dialog.findViewById<TextView>(R.id.friday)
        val saturday = dialog.findViewById<TextView>(R.id.saturday)
        val to_date = dialog.findViewById<TextView>(R.id.to_date)
        val from_date = dialog.findViewById<TextView>(R.id.from_date)

        to_date.setOnClickListener {
            showCalendarDialog(requireContext(),to_date)
            dialog.show()
        }

        from_date.setOnClickListener {
            showCalendarDialog(requireContext(),from_date)
            dialog.show()
        }

        allDays.setOnClickListener {
            if (!isAllDay){
                selectCard(sunday)
                selectCard(monday)
                selectCard(tuesday)
                selectCard(wednesday)
                selectCard(thursday)
                selectCard(friday)
                selectCard(saturday)
                selectCard(allDays)
                makeDays(true)
                isAllDay = true
            }
            else{
                unSelectCard(sunday)
                unSelectCard(monday)
                unSelectCard(tuesday)
                unSelectCard(wednesday)
                unSelectCard(thursday)
                unSelectCard(friday)
                unSelectCard(saturday)
                unSelectCard(allDays)
                isAllDay = false
                makeDays(false)

            }

        }
        monday.setOnClickListener {
            if (!isMon) {
                isMon = true
                selectCard(monday)
                if (allSelected()) {
                    selectCard(allDays)
                    isAllDay = true
                }
            } else {
                isMon = false
                unSelectCard(monday)
                unSelectCard(allDays)
                isAllDay = false
            }
        }
        tuesday.setOnClickListener {
            if (!isTue) {
                isTue = true
                selectCard(tuesday)
                if (allSelected()) {
                    selectCard(allDays)
                    isAllDay = true
                }
            } else {
                isTue = false
                unSelectCard(tuesday)
                unSelectCard(allDays)
                isAllDay = false
            }
        }
        wednesday.setOnClickListener {
            if (!isWed) {
                isWed = true
                selectCard(wednesday)
                if (allSelected()) {
                    selectCard(allDays)
                    isAllDay = true
                }
            } else {
                isWed = false
                unSelectCard(wednesday)
                unSelectCard(allDays)
                isAllDay = false
            }
        }
        thursday.setOnClickListener {
            if (!isThu) {
                isThu = true
                selectCard(thursday)
                if (allSelected()) {
                    selectCard(allDays)
                    isAllDay = true
                }
            } else {
                isThu = false
                unSelectCard(thursday)
                unSelectCard(allDays)
                isAllDay = false
            }
        }
        friday.setOnClickListener {
            if (!isFri) {
                isFri = true
                selectCard(friday)
                if (allSelected()) {
                    selectCard(allDays)
                    isAllDay = true
                }
            } else {
                isFri = false
                unSelectCard(friday)
                unSelectCard(allDays)
                isAllDay = false
            }
        }
        saturday.setOnClickListener {
            if (!isSat) {
                isSat = true
                selectCard(saturday)
                if (allSelected()) {
                    selectCard(allDays)
                    isAllDay = true
                }
            } else {
                isSat = false
                unSelectCard(saturday)
                unSelectCard(allDays)
                isAllDay = false
            }
        }
        sunday.setOnClickListener {
            if (!isSun) {
                isSun = true
                selectCard(sunday)
                if (allSelected()) {
                    selectCard(allDays)
                    isAllDay = true
                }
            } else {
                isSun = false
                unSelectCard(sunday)
                    unSelectCard(allDays)
                    isAllDay = false
            }
        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }
        save.setOnClickListener {
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.END)

        dialog.show()

    }

    private fun setUpRecycler() {

        binding.paymentTypeRecycler.layoutManager = LinearLayoutManager(requireContext())

        list.add("4")
        list.add("4")
        list.add("4")
        list.add("4")
        list.add("4")
        list.add("4")
        list.add("4")

        adapter = HolidaysAdapter(list, requireContext())
        binding.paymentTypeRecycler.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun selectCard(day: TextView?) {
        day?.setBackgroundResource(R.drawable.rounded_border_black)
        day?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
    }

    private fun unSelectCard(day: TextView?) {
        day?.setBackgroundResource(R.drawable.rounded_border_light_black)
        day?.setTextColor(ContextCompat.getColor(requireContext(), R.color.lightBlack))
        day?.typeface = roboto
    }

    private fun allSelected(): Boolean {
        return isSun && isMon && isTue && isWed && isThu && isFri && isSat
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

    private fun makeDays(x:Boolean){
        isSun = x
        isMon = x
        isTue = x
        isWed = x
        isThu = x
        isFri = x
        isSat = x
    }


}