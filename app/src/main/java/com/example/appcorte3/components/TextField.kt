package com.example.appcorte3.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
fun TextFieldComponent(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    spacerHeight: Dp? = null,
    keyboardOption: KeyboardOptions = KeyboardOptions.Default
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
            )
        },
        modifier = modifier,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFF525252),
            unfocusedTextColor = Color.White,
            unfocusedIndicatorColor = Color(0xFF7AB317),
            unfocusedPlaceholderColor = Color(0xFFE3E3E3)
        ),
    )
    if(spacerHeight != null) {
        Spacer(modifier = Modifier.height(spacerHeight))
    }
}