package com.example.appcorte3.Orders.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appcorte3.Orders.data.model.OrderDetail
import com.example.appcorte3.core.data.local.Order.entities.OrderEntity
import java.nio.file.WatchEvent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OrderCard (order: OrderDetail) {

    fun formatTimestamp(timestamp: Long): String {
        val date = Date(timestamp)
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return format.format(date)
    }

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF353535))
            .padding(15.dp)
    ){
        Text(
            text = "$ ${order.total}",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
        )

        Row(
            modifier = Modifier
                .padding(start = 10.dp, top = 10.dp)
        ) {
            Icon(
                Icons.Default.AccountCircle,
                contentDescription = "Cliente",
                tint = Color(0xFF7AB317)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text( text = order.name)
        }

        Row(
            modifier = Modifier
                .padding(start = 10.dp, top = 10.dp)
        ) {
            Icon(
                Icons.Default.CalendarMonth,
                contentDescription = "Cliente",
                tint = Color(0xFF7AB317)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text( text = formatTimestamp(order.date), modifier = Modifier.weight(1f))
        }
    }

}