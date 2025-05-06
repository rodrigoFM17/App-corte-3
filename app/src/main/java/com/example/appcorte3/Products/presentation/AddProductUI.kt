package com.example.appcorte3.Products.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewModelScope
import com.example.appcorte3.Products.presentation.components.PriceInput
import com.example.appcorte3.components.ButtonComponent
import com.example.appcorte3.components.TextFieldComponent
import com.example.appcorte3.core.data.local.Product.entities.ProductEntity
import com.example.appcorte3.core.data.local.Product.entities.UNIT
import com.example.appcorte3.layouts.Container
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun AddProductScreen( productsViewModel: ProductsViewModel) {

    var expanded by remember { mutableStateOf(false) }

    val name by productsViewModel.name.observeAsState("")
    val price by productsViewModel.newProductPrice.observeAsState(0f)
    val unit by productsViewModel.unit.observeAsState(null)

    val priceIntegers by productsViewModel.priceIntegers.observeAsState(0)
    val priceDecimals by productsViewModel.priceDecimals.observeAsState(0)

    Container(
        headerTitle = "Agregar un producto"
    ) {

        TextFieldComponent(
            value = name,
            onValueChange = productsViewModel::onChangeName,
            placeholder = "Nombre del producto",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        PriceInput(
            integers = priceIntegers,
            decimals = priceDecimals,
            onChangePriceIntegers = productsViewModel::onChangePriceIntegers,
            onChangePriceDecimals = productsViewModel::onChangePriceDecimals
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(),
        ) {

            Row {

                Text(
                    text =  if (unit == null) { "seleccione unidad del producto"} else {unit.toString()},
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color(0xFF353535))
                        .padding(15.dp)
                        .clickable {
                            expanded = !expanded
                        }
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.zIndex(1f)
            ) {
                DropdownMenuItem(
                    text = { Text( text = UNIT.INT.label) },
                    onClick = {
                        productsViewModel.onChangeUnit(UNIT.INT)
                    }
                )
                DropdownMenuItem(
                    text = { Text( text = UNIT.FRACC.label) },
                    onClick = {
                        productsViewModel.onChangeUnit(UNIT.FRACC)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        ButtonComponent(
            onClick = {
                productsViewModel.viewModelScope.launch {
                    productsViewModel.insertProduct(
                        ProductEntity(
                            id = UUID.randomUUID().toString(),
                            name = name,
                            price = price,
                            unit = unit!!,
                        )
                    )
                }
            },
            text = "Guardar",
            enabled = name != "" && price != 0f && unit != null,
            modifier = Modifier.fillMaxWidth()
        )
    }

}