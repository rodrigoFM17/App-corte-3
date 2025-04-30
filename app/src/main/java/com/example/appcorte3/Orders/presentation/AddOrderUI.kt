package com.example.appcorte3.Orders.presentation

import android.R
import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.example.appcorte3.components.ButtonComponent
import com.example.appcorte3.components.DropdownMenuComponent
import com.example.appcorte3.components.MenuItem
import com.example.appcorte3.core.data.local.Order.entities.OrderEntity
import com.example.appcorte3.core.data.local.OrderProducts.entitites.OrderProductsEntity
import com.example.appcorte3.core.data.local.Product.entities.UNIT
import com.example.appcorte3.layouts.Container
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent
import java.util.Calendar
import java.util.UUID

@Composable
fun AddOrderScreen(ordersViewModel: OrdersViewModel){

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var selectedDate by remember { mutableStateOf("seleccione una fecha") }
    var timestamp by remember { mutableStateOf<Long?>(null) } // Guarda el timestamp

    var showDialog by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    val clients by ordersViewModel.clients.observeAsState(emptyList())
    val products by ordersViewModel.products.observeAsState(emptyList())
    val selectedProduct by ordersViewModel.selectedProduct.observeAsState()
    val clientSelected by ordersViewModel.clientSelected.observeAsState(null)
    val quantity by ordersViewModel.quantity.observeAsState(1)
    val fraccQuantity by ordersViewModel.fraccString.observeAsState("1/1")
    val total by ordersViewModel.total.observeAsState(0f)
    val date by ordersViewModel.date.observeAsState(0)

    val productsForOrder by ordersViewModel.productsForOrder.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        ordersViewModel.viewModelScope.launch {
            ordersViewModel.getAllClients()
            ordersViewModel.getAllProducts()
        }
    }

    Container(
        headerTitle = "Agregar Pedido"
    ) {

        Column {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                ButtonComponent(
                    icon = Icons.Default.AccountCircle,
                    onClick = {showMenu = !showMenu}
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text( text = if (clientSelected == null) {"seleccione un cliente"} else {clientSelected!!.name})
            }
        }

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            clients.forEach {client ->
                DropdownMenuItem(
                    text = { Text(text = client.name)},
                    onClick = {
                        ordersViewModel.onChangeClientId(client)
                    }
                )
            }

        }

        if (showDialog){
            DatePickerDialog(
                context,
                { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                    calendar.set(year, month, dayOfMonth, 0, 0, 0) // Establecer la fecha seleccionada en el calendario
                    timestamp = calendar.timeInMillis // Convertir a timestamp
                    ordersViewModel.onChangeDate(calendar.timeInMillis)
                    selectedDate = "$dayOfMonth/${month + 1}/$year"
                    showDialog = false
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            ButtonComponent(
                icon = Icons.Default.CalendarMonth,
                onClick = { showDialog = true}
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text( text = selectedDate)
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (products.isEmpty()) {
            Text( text = "Aun no tienes ningun producto registrado, registra uno para empezar a registrar pedidos.")
        } else {
            Text(
                text = "Selecciona un producto",
                fontSize = 10.sp,
                lineHeight = 12.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color(0xFF353535))
                    .verticalScroll(rememberScrollState())
            ) {

                Spacer(modifier = Modifier
                    .background(Color(0xFF7AB317))
                    .height(10.dp)
                    .fillMaxWidth()
                )

                for ((i, product) in products.withIndex()) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (i % 2 == 1) Color(0xFF5B5B5B) else Color(0xFF3C3C3C))
                            .padding(10.dp)
                            .clickable {
                            ordersViewModel.onSelectProduct(product)
                        }
                    ) {
                        Text( text = product.name, modifier = Modifier.weight(2f))
                        Text(text = product.price.toString(), modifier = Modifier.weight(1f))
                    }
                }

            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = selectedProduct?.name ?: "",
                color = Color(0xFF7AB317),
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Ingresa la cantidad deseada",
                fontSize = 10.sp,
                lineHeight = 12.sp
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ButtonComponent(
                    icon = Icons.Default.Add,
                    onClick = ordersViewModel::onIncrementQuantity,
                    modifier = Modifier.weight(1f),
                    negative = true
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(2f)
                ) {
                    Text(
                        text = quantity.toString(),
                        textAlign = TextAlign.Center,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    )

                    if(selectedProduct?.unit == UNIT.FRACC) {
                        DropdownMenuComponent(
                            placeholder = fraccQuantity.toString(),
                            fontSize = 15.sp,
                            menuItems = listOf(
                                MenuItem("1/1", { ordersViewModel.onSetFraccQuantity(FRACC_OPTIONS.NONE)}),
                                MenuItem("1/4", { ordersViewModel.onSetFraccQuantity(FRACC_OPTIONS.QUARTER)}),
                                MenuItem("1/2", { ordersViewModel.onSetFraccQuantity(FRACC_OPTIONS.HALF)}),
                                MenuItem("3/4", { ordersViewModel.onSetFraccQuantity(FRACC_OPTIONS.THREE_QUARTERS)}),
                            )
                        )
                    }
                }

                ButtonComponent(
                    icon = Icons.Default.Remove,
                    onClick = ordersViewModel::onDecrementQuantity,
                    modifier = Modifier.weight(1f),
                    negative = true
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        ButtonComponent(
            text = "Agregar producto",
            onClick = ordersViewModel::onAddProduct,
            modifier = Modifier.fillMaxWidth()
        )

        if (productsForOrder.isNotEmpty()) {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color(0xFF353535))
                    .verticalScroll(rememberScrollState())

            ) {
                Spacer(modifier = Modifier
                    .background(Color(0xFF7AB317))
                    .height(10.dp)
                    .fillMaxWidth()
                )
                for( (i, product) in productsForOrder.withIndex()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (i % 2 == 1) Color(0xFF5B5B5B) else Color(0xFF3C3C3C))
                            .padding(10.dp)
                    ) {
                        Text( text = product.product.name, modifier = Modifier.weight(2f))
                        Text(text = product.quantity.toString(), modifier = Modifier.weight(1f))
                    }
                }

            }
            Spacer(modifier = Modifier.height(20.dp))

            Text( text = total.toString())

        }

        ButtonComponent(
            text = "Guardar Pedido",
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                val orderId = UUID.randomUUID().toString()
                ordersViewModel.viewModelScope.launch {
                    ordersViewModel.insertOrder(OrderEntity(
                        id = orderId,
                        date = date,
                        clientId = clientSelected!!.id,
                        total = total,
                        completed = false,
                        paid = false
                    ))
                    for (product in productsForOrder) {
                        ordersViewModel.insertOrderProduct(OrderProductsEntity(
                            id = UUID.randomUUID().toString(),
                            orderId = orderId,
                            quantity = product.quantity,
                            productId = product.product.id,
                        ))
                    }
                }
            }
        )
    }
}