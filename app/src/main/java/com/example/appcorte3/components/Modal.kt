package com.example.appcorte3.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun Modal(
    text: String,
    confirmAction: () -> Unit,
    dismissAction: () -> Unit,
    showModal: Boolean
) {

    if(showModal) {
        Dialog(
            onDismissRequest = dismissAction
        ) {
            Column(
                modifier = Modifier
                    .padding(15.dp)
                    .background(color = Color(0xFF525252))
                    .padding(15.dp)
                    .clip(RoundedCornerShape(5.dp))
            ) {
                Text(text = text)
                ButtonComponent(
                    text = "Aceptar",
                    onClick = confirmAction,
                    modifier = Modifier.fillMaxWidth()
                )
                ButtonComponent(
                    negative = true,
                    text = "Cancelar",
                    onClick = dismissAction,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}