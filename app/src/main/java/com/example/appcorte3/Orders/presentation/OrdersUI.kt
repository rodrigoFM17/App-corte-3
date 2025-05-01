package com.example.appcorte3.Orders.presentation

import android.view.Menu
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ModalBottomSheet
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
import com.example.appcorte3.components.DropdownMenuComponent
import com.example.appcorte3.components.MenuItem
import com.example.appcorte3.layouts.Container
import kotlinx.coroutines.launch

@Composable
fun OrdersScreen(ordersViewModel: OrdersViewModel) {

    val orders by ordersViewModel.orders.observeAsState(emptyList())
    val filter by ordersViewModel.filter.observeAsState(FILTER_OPTIONS.NONE)
    val filtering by ordersViewModel.filtering.observeAsState(false)

    LaunchedEffect(Unit) {
        ordersViewModel.viewModelScope.launch {
            ordersViewModel.getOrdersFiltered()
        }
    }

    Container (
        headerTitle = "Pedidos"
    ){
        if(orders.isEmpty() && !filtering){
            Text( text = "No tienes pedidos agendados")
            ButtonComponent(
                icon = Icons.Default.Add,
                text = "Agregar pedido",
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    ordersViewModel.navigateToAddOrder()
                }
            )
        } else {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ){
                ButtonComponent(
                    icon = Icons.Default.Add,
                    onClick = {
                        ordersViewModel.navigateToAddOrder()
                    },
                    modifier = Modifier.fillMaxHeight()
                )
                Spacer(modifier = Modifier.width(10.dp))
                DropdownMenuComponent(
                    placeholder = filter.toString(),
                    icon = Icons.Default.FilterList,
                    negative = true,
                    contentDescription = "filtros",
                    menuItems = listOf(
                        MenuItem(text = FILTER_OPTIONS.NONE.label, {ordersViewModel.viewModelScope.launch { ordersViewModel.onChangeFilterOption(FILTER_OPTIONS.NONE) }}),
                        MenuItem(text = FILTER_OPTIONS.COMPLETED.label, { ordersViewModel.viewModelScope.launch { ordersViewModel.onChangeFilterOption(FILTER_OPTIONS.COMPLETED)}}),
                        MenuItem(text = FILTER_OPTIONS.NO_COMPLETED.label, { ordersViewModel.viewModelScope.launch { ordersViewModel.onChangeFilterOption(FILTER_OPTIONS.NO_COMPLETED)}}),
                        MenuItem(text = FILTER_OPTIONS.PAID.label, { ordersViewModel.viewModelScope.launch {ordersViewModel.onChangeFilterOption(FILTER_OPTIONS.PAID)}}),
                        MenuItem(text = FILTER_OPTIONS.NO_PAID.label, { ordersViewModel.viewModelScope.launch { ordersViewModel.onChangeFilterOption(FILTER_OPTIONS.NO_PAID)}}),
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                )
            }

        }

        Column {

            for(order in orders) {
                Spacer(modifier = Modifier.height(20.dp))
                OrderCard(
                    order,
                    {
                        ordersViewModel.onSelectParticular(order.id)
                        ordersViewModel.navigateToParticularOrder()
                    }

                )
            }
        }

    }
}