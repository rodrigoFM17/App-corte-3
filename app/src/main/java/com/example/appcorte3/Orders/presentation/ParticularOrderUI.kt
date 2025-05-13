package com.example.appcorte3.Orders.presentation

import android.Manifest
import android.content.pm.PackageManager
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
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewModelScope
import com.example.appcorte3.layouts.Container
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.example.appcorte3.Orders.presentation.viewModels.FRACC_OPTIONS
import com.example.appcorte3.Orders.presentation.viewModels.OrdersViewModel
import com.example.appcorte3.Orders.presentation.viewModels.ParticularOrderViewModel
import com.example.appcorte3.components.ButtonComponent
import com.example.appcorte3.components.DatePickerComponent
import com.example.appcorte3.components.DropdownMenuComponent
import com.example.appcorte3.components.MenuItem
import com.example.appcorte3.components.Modal
import com.example.appcorte3.components.ProductQuantityInputs
import com.example.appcorte3.components.SearchField
import com.example.appcorte3.components.StatusIndicator
import com.example.appcorte3.components.Table
import com.example.appcorte3.components.TableColumn
import com.example.appcorte3.components.TableRow
import com.example.appcorte3.core.data.local.Product.entities.UNIT
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatTimestamp(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return format.format(date)
}

@Composable
fun ParticularOrderScreen(particularOrderViewModel: ParticularOrderViewModel) {

    val particularOrder by particularOrderViewModel.particularDetailedOrder.observeAsState()
    val editing by particularOrderViewModel.editing.observeAsState(false)
    val products by particularOrderViewModel.products.observeAsState(emptyList())
    val quantity by particularOrderViewModel.quantity.observeAsState(1)
    val selectedProduct by particularOrderViewModel.selectedProduct.observeAsState(null)
    val clientName by particularOrderViewModel.clientName.observeAsState("")
    val completed by particularOrderViewModel.completed.observeAsState(false)
    val paid by particularOrderViewModel.paid.observeAsState(false)
    val fraccQuantity by particularOrderViewModel.fraccQuantity.observeAsState(FRACC_OPTIONS.NONE)
    val productsParticularOrder by particularOrderViewModel.productsParticularOrder.observeAsState(emptyList())
    val clients by particularOrderViewModel.clients.observeAsState(emptyList())
    val total by particularOrderViewModel.total.observeAsState(0f)
    val searchedProduct by particularOrderViewModel.searchedProduct.observeAsState("")

    val showDevices by particularOrderViewModel.showDevices.observeAsState(false)
    val bluetoothDevices by particularOrderViewModel.bluetoothDevices.observeAsState(emptyList())

    val context = LocalContext.current
    var showModal by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        particularOrderViewModel.viewModelScope.launch {
            particularOrderViewModel.getParticularOrder()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            particularOrderViewModel.resetInputs()
        }
    }

    if (particularOrder == null)
        Text( text = "No existe este pedido")
    else {
        Container(
            headerTitle = "Detalles de la orden"
        ) {

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                ButtonComponent(
                    icon = if (!editing) Icons.Default.Edit else Icons.Default.Cancel,
                    negative = true,
                    onClick = { particularOrderViewModel.viewModelScope.launch {
                        particularOrderViewModel.onChangeEditing(!editing)
                    }},
                    modifier = Modifier.padding(end = 10.dp)
                )

                Text(
                    text = "$%.2f".format(particularOrder!!.total),
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                )

            }

            Spacer(modifier = Modifier.height(20.dp))

            if (editing) {
                DatePickerComponent(
                    context = LocalContext.current,
                    defaultValue = particularOrder!!.date,
                    onChangeDate = particularOrderViewModel::onChangeDateParticular
                )
                Spacer(modifier = Modifier.height(10.dp))
                DropdownMenuComponent(
                    icon = Icons.Default.AccountCircle,
                    color = 0xFF525252,
                    iconColor = 0xFF7AB317,
                    contentDescription = "cliente",
                    placeholder = clientName,
                    menuItems = clients.map { client ->
                        MenuItem(text = client.name, onClick = {particularOrderViewModel.onChangeClient(client)})
                    }
                )
            } else {
                Row {
                    Icon(
                        Icons.Default.CalendarMonth,
                        contentDescription = "fecha de entrega",
                        tint = Color(0xFF7AB317)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text( text = formatTimestamp(particularOrder!!.date))
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = "cliente",
                        tint = Color(0xFF7AB317)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text( text = particularOrder!!.clientName)
                }
            }


            Spacer(modifier = Modifier.height(10.dp))
            Text( text = "para cambiar el estado solo presionelo", fontSize = 10.sp)

            StatusIndicator(
                status = completed,
                positiveStatusText = "completado",
                negativeStatusText = "no completado",
                spacerHeight = 10.dp,
                changeStatus = {
                    particularOrderViewModel.viewModelScope.launch {
                        particularOrderViewModel.onChangeCompleteStatus()
                    }
                }
            )

            StatusIndicator(
                status = paid,
                positiveStatusText = "pagado",
                negativeStatusText = "sin pagar",
                spacerHeight = 10.dp,
                changeStatus = {
                    particularOrderViewModel.viewModelScope.launch {
                        particularOrderViewModel.onChangePaidStatus()
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (editing) {
                SearchField(
                    placeholder = "nombre del producto",
                    value = searchedProduct,
                    onChangeValue = particularOrderViewModel::onChangeSearchedProduct,
                    modifier = Modifier.fillMaxWidth(),
                    spacerHeight = 10.dp
                )
                Table(
                    height = 200.dp,
                    tableContent = products.map { product ->
                        TableRow(
                            columns = listOf(
                                TableColumn( text = product.name, weight = 2f),
                                TableColumn( text = product.price.toString(), weight = 1f)
                            ),
                            onClick = { particularOrderViewModel.onSelectProduct(product) }
                        )
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                ProductQuantityInputs(
                    incrementQuantity = particularOrderViewModel::onIncrementQuantity,
                    decrementQuantity = particularOrderViewModel::onDecrementQuantity,
                    addProduct = particularOrderViewModel::onAddProductParticular,
                    quantity = quantity,
                    total = total,
                    fracc = fraccQuantity,
                    setQuantity = particularOrderViewModel::onSetFraccQuantity,
                    selectedProduct = selectedProduct
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

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
                if(editing) {
                    for( (i, product) in productsParticularOrder.withIndex()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(if (i % 2 == 1) Color(0xFF5B5B5B) else Color(0xFF3C3C3C))
                                .padding(10.dp)
                        ) {
                            Text( text = product.product.name, modifier = Modifier.weight(2f))
                            Text( text = product.quantity.toString(), modifier = Modifier.weight(1f))
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "eliminar producto",
                                modifier = Modifier.clickable {
                                    particularOrderViewModel.onDeleteProductParticularOrder(product)
                                }
                            )
                        }
                    }

                } else {
                    for( (i, product) in particularOrder!!.orderProducts.withIndex()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(if (i % 2 == 1) Color(0xFF5B5B5B) else Color(0xFF3C3C3C))
                                .padding(10.dp)
                        ) {
                            Text( text = product.quantity.toString(), modifier = Modifier.weight(1f))
                            Text( text = product.name, modifier = Modifier.weight(2f))
                            Text(text = "$%.2f".format(product.price * product.quantity), modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            if (editing){
                Spacer(modifier = Modifier.height(20.dp))
                Text( text = "total: ", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text( text = "$%.2f".format(total), fontSize = 50.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(20.dp))

                ButtonComponent(
                    text = "Guardar Cambios",
                    modifier = Modifier.fillMaxWidth(),
                    spacerForIcon = 10.dp,
                    onClick = {particularOrderViewModel.viewModelScope.launch {
                        particularOrderViewModel.onEditOrder()
                    }},
                    icon = Icons.Default.Save
                )
            } else {
                ButtonComponent(
                    icon = Icons.Default.Print,
                    text = "Imprimir recibo",
                    spacerForIcon = 10.dp,
                    onClick = { particularOrderViewModel.printTicket(context, particularOrder!!) },
                    modifier = Modifier.fillMaxWidth()
                )

                ButtonComponent(
                    text = "Eliminar pedido",
                    negative = true,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {showModal = true}
                )
            }



            if(showDevices) {
                Text( text = "Seleccione su impresora bluetooth")
                Spacer(modifier = Modifier.height(20.dp))

                Table(
                    height = 150.dp,
                    tableContent = bluetoothDevices.map {
                        bDevice -> TableRow(
                            columns = listOf(TableColumn(
                                text = if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) bDevice.name ?: "Desconocido" else "Dispositivo Bluetooth",
                                weight = 1f,
                                onClick = {
                                    particularOrderViewModel.connectToPrinter(context, bDevice)
                                }
                            ))
                        )
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
            }

            Modal(
                text = "Esta accion no se puede revertir ¿Estas seguro que quieres continuar?",
                showModal = showModal,
                dismissAction = {showModal = false},
                confirmAction = {particularOrderViewModel.viewModelScope.launch {
                    particularOrderViewModel.onDeleteOrder()
                    showModal = false
                }}
            )

        }
    }


}