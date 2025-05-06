package com.example.appcorte3.Products.presentation

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.example.appcorte3.Products.presentation.components.ProductCard
import com.example.appcorte3.components.ButtonComponent
import com.example.appcorte3.components.SearchField
import com.example.appcorte3.layouts.Container
import kotlinx.coroutines.launch

@Composable
fun ProductsScreen(productsViewModel: ProductsViewModel) {

    val products by productsViewModel.products.observeAsState(emptyList())
    val searchedProduct by productsViewModel.searchedProduct.observeAsState("")
    val searching by productsViewModel.searching.observeAsState(false)

    LaunchedEffect(Unit) {
        productsViewModel.viewModelScope.launch {
            productsViewModel.getAllProducts()
        }
    }
    Container(
        headerTitle = "Productos"
    ) {

        if(products.isEmpty() && !searching){
            Text( text = "Aun no ha registrado ningun producto")

            ButtonComponent(
                onClick = productsViewModel.navigateToAddProduct,
                icon = Icons.Default.Add,
                text = "Agregar un producto",
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ){
                ButtonComponent(
                    onClick = productsViewModel.navigateToAddProduct,
                    icon = Icons.Default.Add,
                    modifier = Modifier.fillMaxHeight()
                )

                Spacer(modifier = Modifier.width(10.dp))

                SearchField(
                    value = searchedProduct,
                    onChangeValue = productsViewModel::onChangeSearchedProduct,
                    placeholder = "nombre del producto"
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column {
                for(product in products) {
                    ProductCard(
                        product,
                        {productsViewModel.onSelectProduct(product)}
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

    }
}