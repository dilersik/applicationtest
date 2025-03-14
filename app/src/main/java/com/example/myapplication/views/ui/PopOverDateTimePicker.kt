package com.example.myapplication.views.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.util.Calendar

@Composable
fun PopOverDateTimePicker(
    showDatePicker: Boolean,
    showTimePicker: Boolean,
    initialDate: String? = null,
    initialTime: String? = null,
    minDate: Long? = null,
    maxDate: Long? = null,
    onDateTimeSelected: (String?) -> Unit,
    onDismissRequest: () -> Unit = {},
    primaryColor: Color = Color(0xFF6200EE),
    secondaryColor: Color = Color(0xFFBDBDBD)
) {
    val selectedDate = remember { mutableStateOf(initialDate) }
    val selectedTime = remember { mutableStateOf(initialTime) }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select Date & Time",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                if (showDatePicker) {
                    DatePicker(minDate, maxDate, selectedDate)
                }
                if (showTimePicker) {
                    TimePicker(selectedTime)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { onDateTimeSelected("${selectedDate.value} ${selectedTime.value}") },
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Confirm", color = Color.White)
                    }

                    Button(
                        onClick = { onDismissRequest() },
                        colors = ButtonDefaults.buttonColors(containerColor = secondaryColor),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Cancel", color = Color.White)
                    }
                }
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
