package com.example.appcorte3.Orders.presentation

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewModelScope
import com.example.appcorte3.layouts.Container
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.example.appcorte3.components.ButtonComponent
import com.example.appcorte3.components.StatusIndicator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatTimestamp(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return format.format(date)
}

@Composable
fun ParticularOrderScreen(ordersViewModel: OrdersViewModel) {

    val orderId by ordersViewModel.particularOrderId.observeAsState("")
    val particularOrder by ordersViewModel.particularDetailedOrder.observeAsState()

    val showDevices by ordersViewModel.showDevices.observeAsState(false)
    val bluetoothDevices by ordersViewModel.bluetoothDevices.observeAsState(emptyList())

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        ordersViewModel.viewModelScope.launch {
            ordersViewModel.getParticularOrder(orderId)
        }
    }

    if (particularOrder == null)
        Text( text = "No existe este pedido")
    else {
        Container(
            headerTitle = "Detalles de la orden"
        ) {

            Text(
                text = "$ ${particularOrder!!.total}",
                textAlign = TextAlign.Center,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))
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
                    contentDescription = "fecha de entrega",
                    tint = Color(0xFF7AB317)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text( text = particularOrder!!.clientName)
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text( text = "para cambiar el estado solo toquelo", fontSize = 10.sp)

            StatusIndicator(
                status = particularOrder!!.completed,
                positiveStatusText = "completado",
                negativeStatusText = "no completado",
                spacerHeight = 10.dp,
                changeStatus = {
                    ordersViewModel.viewModelScope.launch {
                        ordersViewModel.onChangeCompleteStatus()
                    }
                }
            )

            StatusIndicator(
                status = particularOrder!!.paid,
                positiveStatusText = "pagado",
                negativeStatusText = "no pagado",
                spacerHeight = 10.dp,
                changeStatus = {
                    ordersViewModel.viewModelScope.launch {
                        ordersViewModel.onChangePaidStatus()
                    }
                }
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
                for( (i, product) in particularOrder!!.orderProducts.withIndex()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (i % 2 == 1) Color(0xFF5B5B5B) else Color(0xFF3C3C3C))
                            .padding(10.dp)
                    ) {
                        Text( text = product.quantity.toString(), modifier = Modifier.weight(1f))
                        Text( text = product.name, modifier = Modifier.weight(2f))
                        Text(text = "$ ${product.price * product.quantity}", modifier = Modifier.weight(1f))
                    }
                }

            }
            Spacer(modifier = Modifier.height(20.dp))

            if(showDevices) {
                Text( text = "Seleccione su impresora bluetooth")
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
                    for( (i, device) in bluetoothDevices.withIndex()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(if (i % 2 == 1) Color(0xFF5B5B5B) else Color(0xFF3C3C3C))
                                .padding(10.dp)
                                .clickable {
                                    ordersViewModel.connectToPrinter(context, device)
                                }
                        ) {
                            Text( text = if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED)
                                    device.name ?: "Desconocido"
                                else "Dispositivo Bluetooth",
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            ButtonComponent(
                icon = Icons.Default.Print,
                negative = true,
                text = "Imprimir recibo",
                onClick = { ordersViewModel.printTicket(context, particularOrder!!) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }


}