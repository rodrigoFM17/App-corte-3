package com.example.appcorte3.components

import androidx.annotation.Size
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun StatusIndicator(
    status: Boolean,
    positiveStatusText: String,
    negativeStatusText: String,
    changeStatus: () -> Unit,
    modifier: Modifier = Modifier,
    positiveColor: Long = 0xFF64B700,
    negativeColor: Long = 0xFFFFCC00,
    spacerHeight: Dp? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable {
                changeStatus()
            }
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(
                    Color( if (status) positiveColor else negativeColor)
                )
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = if (status) positiveStatusText else negativeStatusText,
            fontWeight = FontWeight.Bold
        )
    }

    if(spacerHeight != null) {
        Spacer(modifier = Modifier.height(spacerHeight))
    }
}