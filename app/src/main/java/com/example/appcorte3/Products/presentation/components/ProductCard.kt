package com.example.appcorte3.Products.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appcorte3.core.data.local.Product.entities.ProductEntity
import java.nio.file.WatchEvent

@Composable
fun ProductCard(
    product: ProductEntity,
    onSelect: () -> Unit
    ) {

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .background(Color(0xFF353535))
            .padding(15.dp)
            .clickable {
                onSelect()
            }
    ){
        Text(
            text = product.name,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "$ ${product.price}",
            modifier = Modifier.padding(start = 10.dp)
        )

    }
}