package com.example.appcorte3.Orders.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.example.appcorte3.Orders.presentation.components.OrderCard
import com.example.appcorte3.components.ButtonComponent
import com.example.appcorte3.layouts.Container
import kotlinx.coroutines.launch

@Composable
fun OrdersScreen(ordersViewModel: OrdersViewModel) {

    val orders by ordersViewModel.orders.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        ordersViewModel.viewModelScope.launch {
            ordersViewModel.getAllPendingOrders()
        }
    }

    Container (
        headerTitle = "Pedidos"
    ){
        if(orders.isEmpty()){
            Text( text = "No tienes pedidos agendados")
        }

        ButtonComponent(
            icon = Icons.Default.Add,
            text = "Agregar pedido",
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                ordersViewModel.navigateToAddOrder()
            }

        )

        Column {

            for(order in orders) {
                Spacer(modifier = Modifier.height(20.dp))
                OrderCard(order)
            }
        }

    }
}