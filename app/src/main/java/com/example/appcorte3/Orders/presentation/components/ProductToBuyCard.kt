package com.example.appcorte3.Orders.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appcorte3.Orders.data.model.ProductToBuy
import java.nio.file.WatchEvent

@Composable
fun ProductToBuyCard(productToBuy: ProductToBuy) {

    var bought by remember { mutableStateOf(productToBuy.bought) }

    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .background(Color(if(bought) 0xFF353535 else 0xFF252525))
            .padding(10.dp)
            .clickable {
                productToBuy.bought = !productToBuy.bought
                bought = !bought
            }
    ) {
        Text(
            text = productToBuy.name,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            textDecoration = if(bought) TextDecoration.LineThrough else TextDecoration.None
        )
        Text(
            text = productToBuy.quantity.toString(),
        )
    }
}