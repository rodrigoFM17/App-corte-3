package com.example.appcorte3.components

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun DatePickerComponent(
    context: Context,
    onChangeDate: (Long) -> Unit,
    modifier: Modifier = Modifier,
    defaultValue: Long? = null,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    color: Long? = null,
){

    fun formatDate(timestamp: Long): String {
        val date = Date(timestamp)
        val formatter = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
        return formatter.format(date)
    }

    var selectedDate by remember { mutableStateOf("seleccione una fecha") }
    var valueChanged by remember { mutableStateOf(false) }
    var showPicker by remember { mutableStateOf(false) }
    val calendar = Calendar.getInstance()

    if (showPicker){
        val datePickerDialog = DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                calendar.set(year, month, dayOfMonth, 0, 0, 0) // Establecer la fecha seleccionada en el calendario
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                onChangeDate(calendar.timeInMillis)
                valueChanged = true
                selectedDate = "$dayOfMonth/${month + 1}/$year"
                showPicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.setOnCancelListener {
            showPicker = false
        }
        datePickerDialog.show()
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalArrangement,
        modifier = modifier.clickable { showPicker = true}
    ) {
        Icon(
            Icons.Default.CalendarMonth,
            contentDescription = "fecha",
            tint = Color(color ?: 0xFF7AB317)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text( text = if (defaultValue != null && !valueChanged) formatDate(defaultValue) else  selectedDate, fontWeight = FontWeight.Bold, color = Color.White)
    }
}