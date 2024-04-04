

package com.example.sawitprotest.base.extensions

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Long.toFormattedDateString(): String {
    val sdf = SimpleDateFormat("EEEE, dd MMM yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(this))
}

fun Context.showToast(message: String){
    Toast.makeText(this,message, Toast.LENGTH_LONG).show()
}

fun ViewModel.runScoped(block: suspend () -> Unit) {
    this.viewModelScope.launch {
        block()
    }
}

fun Context.pickDateTime(
    dateObtained : (date : Long) -> Unit
) {
    val currentDateTime = Calendar.getInstance()
    val startYear = currentDateTime.get(Calendar.YEAR)
    val startMonth = currentDateTime.get(Calendar.MONTH)
    val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(this, { _, year, month, day ->
        val selectedDate = Calendar.getInstance()
        selectedDate.set(year, month, day)
        dateObtained(selectedDate.timeInMillis)
    }, startYear, startMonth, startDay).show()
}





