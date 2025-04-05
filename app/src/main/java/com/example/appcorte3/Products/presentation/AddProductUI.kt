package com.example.appcorte3.Products.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
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
    val price by productsViewModel.price.observeAsState(0f)
    val unit by productsViewModel.unit.observeAsState(null)

    Container(
        headerTitle = "Agregar un producto"
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {

            TextFieldComponent(
                value = name,
                onValueChange = productsViewModel::onChangeName,
                placeholder = "Nombre del producto"
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextFieldComponent(
                value = price.toString(),
                onValueChange = productsViewModel::onChangePrice,
                placeholder = "Precio del producto"
            )
            Spacer(modifier = Modifier.height(20.dp))

            Row {

                Text(
                    text =  if (unit == null) { "seleccione unidad del producto"} else {unit.toString()},
                    modifier = Modifier.clickable {
                        expanded = !expanded
                    }
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text( text = UNIT.INT.toString()) },
                    onClick = {
                        productsViewModel.onChangeUnit(UNIT.INT)
                    }
                )
                DropdownMenuItem(
                    text = { Text( text = UNIT.FRACC.toString()) },
                    onClick = {
                        productsViewModel.onChangeUnit(UNIT.FRACC)
                    }
                )
            }

            ButtonComponent(
                onClick = {
                    productsViewModel.viewModelScope.launch {
                        productsViewModel.insertProduct(
                            ProductEntity(
                                id = UUID.randomUUID().toString(),
                            name = name,
                            price = price,
                            unit = unit!!,
                                sended = false
                        )
                        )
                    }
                },
                text = "Guardar"
            )
        }
    }

}