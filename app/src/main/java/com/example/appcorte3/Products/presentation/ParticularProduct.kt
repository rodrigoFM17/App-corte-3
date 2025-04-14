package com.example.appcorte3.Products.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.appcorte3.layouts.Container
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appcorte3.components.ButtonComponent
import com.example.appcorte3.core.data.local.Product.entities.UNIT
import kotlin.math.exp

@Composable
fun ParticularOrderScreen(
    productsViewModel: ProductsViewModel
) {

    val product by productsViewModel.selectedProduct.observeAsState()
    var showUnits by remember { mutableStateOf(false) }

    Container(
        headerTitle = "Product"
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            product?.let {
                Text(
                    text = it.name,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = it.price.toString(),
                    fontSize = 20.sp,
                )
                Spacer(modifier = Modifier.height(20.dp))

                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF353535))
                        .padding(10.dp)
                ) {
                    Text(
                        text = it.unit.toString(),
                    )
                    Icon(Icons.Default.ArrowDropDown,
                        contentDescription = "desplegar")
                }

                DropdownMenu(
                    expanded = showUnits,
                    onDismissRequest = {showUnits = false},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (it.unit == UNIT.INT) {
                        DropdownMenuItem(
                            text = { Text( text = UNIT.FRACC.toString()) },
                            onClick = {productsViewModel.onChangeProductUnit(UNIT.FRACC)}
                        )
                    } else {
                        DropdownMenuItem(
                            text = { Text( text = UNIT.INT.toString()) },
                            onClick = {productsViewModel.onChangeProductUnit(UNIT.INT)}
                        )
                    }
                }


                ButtonComponent(
                    text = "Guardar cambios",
                    onClick =
                )



            }
                ?: Text(text = "No existe este producto")

        }
    }
}