package com.example.appcorte3.Products.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appcorte3.components.TextFieldComponent

@Composable
fun PriceInput(integers: Int, decimals: Int, onChangePriceIntegers: (String) -> Unit, onChangePriceDecimals: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$",
            fontSize = 30.sp
        )
        Spacer(modifier = Modifier.width(10.dp))
        TextFieldComponent(
            value = integers.toString(),
            modifier = Modifier.weight(1f),
            onValueChange = onChangePriceIntegers,
            placeholder = "",
            keyboardOption = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )
        Text(
            text = ".",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            modifier = Modifier
                .padding(horizontal = 10.dp)
        )
        TextFieldComponent(
            value = decimals.toString(),
            modifier = Modifier.weight(1f),
            onValueChange = onChangePriceDecimals,
            placeholder = "",
            keyboardOption = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )
    }
}