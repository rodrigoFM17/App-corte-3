package com.example.appcorte3.Orders.presentation

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
            onClick = {
                ordersViewModel.navigateToAddOrder()
            }

        )

        LazyColumn {
            items(orders) { order ->
                OrderCard(order)
            }
        }

    }
}