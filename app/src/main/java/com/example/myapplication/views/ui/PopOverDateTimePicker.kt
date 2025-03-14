package com.example.myapplication.views.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.util.Calendar

@Composable
fun PopOverDateTimePicker(
    showDatePicker: Boolean,
    showTimePicker: Boolean,
    initialDate: String? = null, // Default date value
    initialTime: String? = null, // Default time value
    minDate: Long? = null, // Minimum selectable date in milliseconds
    maxDate: Long? = null, // Maximum selectable date in milliseconds
    onDateTimeSelected: (String?) -> Unit,
    onDismissRequest: () -> Unit = {},
    primaryColor: Color = Color.Blue, // Customizable primary color
    secondaryColor: Color = Color.Gray // Customizable secondary color
) {
    val selectedDate = remember { mutableStateOf(initialDate) }
    val selectedTime = remember { mutableStateOf(initialTime) }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
        ) {
            if (showDatePicker) {
                Text("Select Date", color = primaryColor)
                DatePicker(minDate, maxDate, selectedDate)
            }
            if (showTimePicker) {
                Text("Select Time", color = primaryColor)
                TimePicker(selectedTime)
            }

            Button(
                onClick = { onDateTimeSelected("${selectedDate.value} ${selectedTime.value}") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
            ) {
                Text("Confirm", color = Color.White)
            }

            Box(modifier = Modifier.height(6.dp))

            Button(
                onClick = { onDismissRequest() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = secondaryColor)
            ) {
                Text("Cancel", color = Color.White)
            }
        }
    }
}

@Composable
fun DatePicker(minDate: Long?, maxDate: Long?, selectedDate: MutableState<String?>) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    val minCal = minDate?.let { Calendar.getInstance().apply { timeInMillis = it } }
    val maxCal = maxDate?.let { Calendar.getInstance().apply { timeInMillis = it } }

    val onDateSelected: (Int, Int, Int) -> Unit = { year, month, day ->
        selectedDate.value = "$year-${(month + 1).toString().padStart(2, '0')}-${
            day.toString().padStart(2, '0')
        }"
    }

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                onDateSelected(year, monthOfYear, dayOfMonth)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            minCal?.let { datePicker.minDate = it.timeInMillis }
            maxCal?.let { datePicker.maxDate = it.timeInMillis }
        }
    }

    Button(onClick = { datePickerDialog.show() }) {
        Text(text = "Pick Date")
    }
}

@Composable
fun TimePicker(selectedTime: MutableState<String?>) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    val onTimeSelected: (Int, Int) -> Unit = { hour, minute ->
        selectedTime.value = "$hour:${minute.toString().padStart(2, '0')}"
    }

    val timePickerDialog = remember {
        TimePickerDialog(
            context,
            { _: TimePicker, hourOfDay: Int, minute: Int ->
                onTimeSelected(hourOfDay, minute)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
    }

    Button(onClick = { timePickerDialog.show() }) {
        Text(text = "Pick Time")
    }
}

//
//@Preview(showBackground = true)
//@Composable
//fun PreviewPopOverDateTimePicker() {
//    PopOverDateTimePicker(
//        showDatePicker = true,
//        showTimePicker = true,
//        initialDate = "2025-03-14",
//        initialTime = "12:00",
//        onDateTimeSelected = { selectedDateTime -> println(selectedDateTime) }
//    )
//}
