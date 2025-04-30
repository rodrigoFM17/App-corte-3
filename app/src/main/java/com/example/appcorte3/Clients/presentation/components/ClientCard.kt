package com.example.appcorte3.Clients.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appcorte3.core.data.local.Client.entities.ClientEntity

@Composable
fun ClientCard(client: ClientEntity, navigateParticularClient: () -> Unit) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF353535))
            .padding(15.dp)
            .clickable {
                navigateParticularClient()
            }
    ) {
        Text(
            text = client.name,
            fontSize = 30.sp,
            lineHeight = 30.sp
        )

        Row {
            Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = "contact info",
                tint = Color(0xFF7AB317)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text( text = client.phone)
        }
    }
}