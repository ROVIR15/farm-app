package com.vt.vt.utils

import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object PickDatesUtils {
    fun setupDatePicker(context: Context, dateTextView: TextView) {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, date ->
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                calendar.set(year, month, date)
                dateTextView.text = dateFormat.format(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    fun datePickToEdittext(context: Context, textInputEditText: TextInputEditText) {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, date ->
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                calendar.set(year, month, date)
                textInputEditText.setText(dateFormat.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}