package rconnect.retvens.technologies.dashboard.configuration.others

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
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
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.billings.PaymentTypeAdapter
import rconnect.retvens.technologies.databinding.FragmentHolidaysBinding
import rconnect.retvens.technologies.databinding.FragmentPaymentTypesBinding
import java.util.Calendar

class HolidaysFragment : Fragment() {

    private lateinit var binding: FragmentHolidaysBinding

    private lateinit var adapter : HolidaysAdapter
    private var list = ArrayList<String>()


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


}