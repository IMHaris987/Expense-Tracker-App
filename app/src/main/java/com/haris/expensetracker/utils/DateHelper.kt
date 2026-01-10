package com.haris.expensetracker.utils

import android.app.DatePickerDialog
import android.content.Context

object DateHelper {
    fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
        val calendar = java.util.Calendar.getInstance()

        DatePickerDialog(
            context,
            { _, year, month, day ->
                // Use String.format to get leading zeros (01, 02, etc.)
                val formattedDate = String.format("%02d/%02d/%04d", day, month + 1, year)
                onDateSelected(formattedDate)
            },
            calendar.get(java.util.Calendar.YEAR),
            calendar.get(java.util.Calendar.MONTH),
            calendar.get(java.util.Calendar.DAY_OF_MONTH)
        ).show()
    }
}