package com.example.appcorte3.Orders.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.example.appcorte3.Orders.presentation.components.OrderCard
import com.example.appcorte3.Orders.presentation.viewModels.FILTER_OPTIONS
import com.example.appcorte3.Orders.presentation.viewModels.OrdersViewModel
import com.example.appcorte3.components.ButtonComponent
import com.example.appcorte3.components.DatePickerComponent
import com.example.appcorte3.components.DropdownMenuComponent
import com.example.appcorte3.components.MenuItem
import com.example.appcorte3.layouts.Container
import kotlinx.coroutines.launch

@Composable
fun OrdersScreen(ordersViewModel: OrdersViewModel) {

    val orders by ordersViewModel.orders.observeAsState(emptyList())
    val filter by ordersViewModel.filter.observeAsState(FILTER_OPTIONS.NONE)
    val filtering by ordersViewModel.filtering.observeAsState(false)
    val loading by ordersViewModel.loader.isLoading.observeAsState(true)
    val clients by ordersViewModel.clients.observeAsState(emptyList())
    val filterClient by ordersViewModel.filterClient.observeAsState(null)

    LaunchedEffect(Unit) {
        ordersViewModel.loader.onStartLoadingAction {
            ordersViewModel.viewModelScope.launch {
                ordersViewModel.getOrdersFiltered()
            }
        }
    }

    Container (
        headerTitle = "Pedidos",
    ){
        item {
            if(orders.isEmpty() && !filtering && !loading){
                Text( text = "No tienes pedidos agendados")
                ButtonComponent(
                    icon = Icons.Default.Add,
                    text = "Agregar pedido",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        ordersViewModel.navigateToAddOrder()
                    }
                )
            } else if (orders.isEmpty() && loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    ButtonComponent(
                        icon = Icons.Default.Add,
                        onClick = {
                            ordersViewModel.navigateToAddOrder()
                        },
                        modifier = Modifier.fillMaxHeight()
                    )
                    DropdownMenuComponent(
                        placeholder = filter.toString(),
                        padding = 10.dp,
                        icon = Icons.Default.FilterList,
                        negative = true,
                        contentDescription = "filtros",
                        menuItems = FILTER_OPTIONS.entries.map { option ->
                            MenuItem(
                                text = option.label,
                                onClick = {
                                    ordersViewModel.viewModelScope.launch {
                                        ordersViewModel.onChangeFilterOption(option)
                                    }
                                }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                ButtonComponent(
                    text = "Ver lista de productos por fecha",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = ordersViewModel.navigateToGeneralOrder
                )
                if (filter == FILTER_OPTIONS.BY_CLIENT) {
                    Spacer(modifier = Modifier.height(10.dp))
                    DropdownMenuComponent(
                        icon = Icons.Default.AccountCircle,
                        color = 0xFF525252,
                        iconColor = 0xFF7AB317,
                        contentDescription = "cliente",
                        placeholder = filterClient?.name ?: "seleccione un cliente",
                        menuItems = clients.map { client ->
                            MenuItem(text = client.name, onClick = {
                                ordersViewModel.viewModelScope.launch {
                                    ordersViewModel.onChangeFilterClient(client)
                                }
                            })
                        }
                    )
                }

                if (filter == FILTER_OPTIONS.BY_DATE) {
                    Spacer(modifier = Modifier.height(10.dp))
                    DatePickerComponent(
                        context = LocalContext.current,
                        onChangeDate = {date -> ordersViewModel.viewModelScope.launch {
                                ordersViewModel.onChangeFilterDate(date)
                            }
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        items(
            items = orders,
            key = { order -> order.id},
        ) { order ->

            OrderCard(
                order,
                { ordersViewModel.onSelectParticular(order) }
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

    }
}