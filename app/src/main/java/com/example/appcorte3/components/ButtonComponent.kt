package com.example.appcorte3.components

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ButtonComponent (
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "",
    negative: Boolean = false,
    icon: ImageVector? = null,
    contentDescription: String? = null,
    enabled: Boolean = true,
    spacerForIcon : Dp? = null
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(color = if (!negative) 0xFF7AB317 else 0xFF353535),
        ),
        contentPadding = PaddingValues(0.dp),
        modifier = modifier,
        shape = RoundedCornerShape(5.dp),
        enabled = enabled
    ) {
        Row (
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            if(icon != null) {
                Icon(
                    imageVector = icon ,
                    contentDescription = contentDescription,
                    tint = if (negative) { Color(0xFF7AB317) } else { Color.White} )
            }
            if(spacerForIcon != null) {
                Spacer(modifier = Modifier.width(spacerForIcon))
            }
            Text(
                text = text,
                color = if(!negative){ Color.White } else { Color(0xFF7AB317) }
                )
        }
    }
}