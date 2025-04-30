package com.example.appcorte3.Products.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.example.appcorte3.Products.presentation.components.PriceInput
import com.example.appcorte3.components.ButtonComponent
import com.example.appcorte3.components.TextFieldComponent
import com.example.appcorte3.core.data.local.Product.entities.UNIT
import com.example.appcorte3.core.navigation.ParticularProduct
import kotlinx.coroutines.launch

@Composable
fun ParticularProductScreen(
    productsViewModel: ProductsViewModel
) {

    val product by productsViewModel.selectedProduct.observeAsState()
    var showUnits by remember { mutableStateOf(false) }

    val name by productsViewModel.productName.observeAsState("")
    val integers by productsViewModel.priceIntegers.observeAsState(0)
    val decimals by productsViewModel.priceDecimals.observeAsState(0)

    Container(
        headerTitle = "Edicion de Producto"
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            product?.let {

                TextFieldComponent(
                    placeholder = name,
                    value = name,
                    spacerHeight = 20.dp,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = productsViewModel::onChangeProductName
                )

                PriceInput(
                    integers = integers,
                    decimals = decimals,
                    onChangePriceIntegers = productsViewModel::onChangePriceIntegers,
                    onChangePriceDecimals = productsViewModel::onChangePriceDecimals
                )

                Spacer(modifier = Modifier.height(20.dp))

                Box {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF353535))
                            .padding(10.dp)
                            .clickable { showUnits = !showUnits },
                        horizontalArrangement = Arrangement.SpaceBetween
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
                    ) {
                        if (it.unit == UNIT.INT) {
                            DropdownMenuItem(
                                text = { Text( text = UNIT.FRACC.toString()) },
                                onClick = {
                                    productsViewModel.onChangeProductUnit(UNIT.FRACC)
                                    showUnits = false
                                }
                            )
                        } else {
                            DropdownMenuItem(
                                text = { Text( text = UNIT.INT.toString()) },
                                onClick = {
                                    productsViewModel.onChangeProductUnit(UNIT.INT)
                                    showUnits = false
                                }
                            )
                        }
                    }
                }



                Spacer(modifier = Modifier.height(20.dp))

                ButtonComponent(
                    text = "Guardar cambios",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {productsViewModel.viewModelScope.launch {
                        productsViewModel.onSaveChanges(it)
                    }}
                )
            }
                ?: Text(text = "No existe este producto")

        }
    }
}