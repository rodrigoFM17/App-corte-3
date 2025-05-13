package com.example.appcorte3.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
    value: String,
    onChangeValue: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    spacerHeight: Dp? = null
) {

    TextField(
        value = value,
        onValueChange = onChangeValue,
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "busqueda", tint = Color.Black) },
        placeholder = { Text(text = placeholder) },
        modifier = modifier,
        textStyle = TextStyle(
            fontSize = 15.sp,
        ),
        singleLine = true,

        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedTextColor = Color.Black,
            focusedTextColor = Color.Black,
            unfocusedPlaceholderColor = Color.Black,
            focusedPlaceholderColor = Color.Black,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
        ),
        shape = RoundedCornerShape(5.dp)
    )
    if(spacerHeight != null) {
        Spacer(modifier = Modifier.height(10.dp))
    }

}