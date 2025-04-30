package com.example.appcorte3.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

data class MenuItem (
    val text: String,
    val onClick: () -> Unit
)

@Composable
fun DropdownMenuComponent(
    placeholder: String,
    menuItems: List<MenuItem>,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified
) {

    var expanded by remember { mutableStateOf(false) }

    Box {
        Text(
            text = placeholder,
            fontSize = fontSize,
            modifier = modifier.clickable {
                expanded = true
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false}
        )  {
            menuItems.forEach { menuItem ->
                DropdownMenuItem(
                    text = { Text( text = menuItem.text) },
                    onClick = menuItem.onClick
                )
            }
        }
    }
}