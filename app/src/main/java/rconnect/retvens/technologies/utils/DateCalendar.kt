package rconnect.retvens.technologies.utils

import android.app.DatePickerDialog
import android.content.Context
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar
import java.util.Date


 fun utilCreateDatePickerDialog(applicationContext: Context, textDate:TextInputEditText, onDateSetListener: (Date) -> Unit): DatePickerDialog {
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

