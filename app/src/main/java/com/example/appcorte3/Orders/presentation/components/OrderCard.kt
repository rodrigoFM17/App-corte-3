package com.example.appcorte3.Orders.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun OrderCard (order: OrderDetail, onSelectOrder: () -> Unit) {

    fun formatTimestamp(timestamp: Long): String {
        val date = Date(timestamp)
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return format.format(date)
    }

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .background(Color(0xFF353535))
            .padding(15.dp)
            .clickable {
                onSelectOrder()
            }
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

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 10.dp, top = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(Color( if (order.completed) 0xFF64B700 else 0xFFFFCC00))
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text( text = if (order.completed) "completado" else "no completado", modifier = Modifier.weight(1f))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 10.dp, top = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(Color( if (order.paid) 0xFF64B700 else 0xFFFFCC00))
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text( text = if (order.paid) "pagado" else "sin pagar", modifier = Modifier.weight(1f))
        }


    }

}