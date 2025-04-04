package com.example.appcorte3.Products.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.appcorte3.core.data.local.Product.entities.ProductEntity

@Composable
fun ProductCard(product: ProductEntity) {

    Row (
        modifier = Modifier.fillMaxWidth()
    ){
        Text(
            text = product.name,
            modifier = Modifier.weight(2f)
        )
        Text(
            text = "$ ${product.price}",
            modifier = Modifier.weight(1f)
        )

    }
}