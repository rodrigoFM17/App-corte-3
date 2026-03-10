package com.example.appcorte3.Orders.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.appcorte3.Orders.presentation.viewModels.GeneralOrderViewModel
import com.example.appcorte3.components.DatePickerComponent
import com.example.appcorte3.layouts.Container
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.example.appcorte3.Orders.data.model.ProductToBuy
import com.example.appcorte3.Orders.presentation.components.GeneralProductCard
import com.example.appcorte3.Orders.presentation.components.ProductToBuyCard
import kotlinx.coroutines.launch

@Composable
fun GeneralOrderScreen(generalOrderViewModel: GeneralOrderViewModel) {

    val date by generalOrderViewModel.date.observeAsState()
    val productsToBuy by generalOrderViewModel.productsToBuy.observeAsState(emptyList())
    val generalProducts by generalOrderViewModel.generalProductToBuy.observeAsState(emptyList())


    LaunchedEffect(Unit) {
        generalOrderViewModel.viewModelScope.launch {
            date?.let { generalOrderViewModel.onChangeDate(it) }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            generalOrderViewModel.resetInputs()
        }
    }
    Container(
        headerTitle = "Lista de productos a comprar"
    ) {

        DatePickerComponent(
            context = LocalContext.current,
            onChangeDate = {
                generalOrderViewModel.viewModelScope.launch {
                    generalOrderViewModel.onChangeDate(it)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        )
        Spacer(modifier = Modifier.height(20.dp))

        if (date != null) {
//            productsToBuy.forEach { product ->
//                ProductToBuyCard(product, {
//                    generalOrderViewModel.viewModelScope.launch {
//                        generalOrderViewModel.changeBoughtStatus(productId = product.id, bought = product.bought)
//                    }
//                })
//                Spacer(modifier = Modifier.height(10.dp))
//            }
            generalProducts.forEach { generalProduct ->
                GeneralProductCard(generalProduct) { orderProductId, bought ->
                    generalOrderViewModel.viewModelScope.launch {
                        generalOrderViewModel.changeBoughtStatus(
                            orderProductId = orderProductId,
                            bought = bought
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        } else {
            Text(
                text = "Seleccione una fecha para mostrar los productos a comprar",
                fontSize = 20.sp
            )
        }


    }

}