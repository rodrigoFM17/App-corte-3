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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appcorte3.core.data.local.Order.entities.OrderEntity

@Composable
fun OrderCard (order: OrderEntity) {

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .background(Color(0xFF353535))
    ){
        Text( text = "$ ${order.total}", fontSize = 30.sp)
        Row (
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = "Cliente"
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text( text = order.clientId.toString())
            }

            Row(
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    Icons.Default.CalendarMonth,
                    contentDescription = "Cliente"
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text( text = order.date.toString(), modifier = Modifier.weight(1f))
            }

        }
    }

}