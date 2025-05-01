package com.example.appcorte3.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

data class MenuItem (
    val text: String,
    val onClick: () -> Unit
)

@Composable
fun DropdownMenuComponent(
    placeholder: String,
    menuItems: List<MenuItem>,
    modifier: Modifier = Modifier,
    negative: Boolean = false,
    fontSize: TextUnit = TextUnit.Unspecified,
    icon: ImageVector? = null,
    contentDescription: String? = null
) {

    var expanded by remember { mutableStateOf(false) }

    Box {

        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .clip(RoundedCornerShape(5.dp))
                .background(Color( if (!negative) 0xFF7AB317 else 0xFF353535))
                .padding(10.dp)
                .clickable {
                expanded = true
            }
        ){
            if(icon != null && contentDescription != null){
                Icon(icon, contentDescription = contentDescription, tint = Color( if (!negative) 0xFFFFFFFF else 0xFF7AB317))
                Spacer(modifier = Modifier.width(10.dp))
            }
            Text(
                text = placeholder,
                color = Color( if (!negative) 0xFFFFFFFF else 0xFF7AB317),
                fontSize = fontSize,
                fontWeight = FontWeight.Bold
            )
        }


        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false}
        )  {
            menuItems.forEach { menuItem ->
                DropdownMenuItem(
                    text = { Text( text = menuItem.text) },
                    onClick = {
                        menuItem.onClick()
                        expanded = false
                    }
                )
            }
        }
    }
}