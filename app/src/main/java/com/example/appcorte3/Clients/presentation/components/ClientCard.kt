package com.example.appcorte3.Clients.presentation.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appcorte3.core.data.local.Client.entities.ClientEntity
import java.net.URLEncoder

@Composable
fun ClientCard(client: ClientEntity, navigateParticularClient: () -> Unit) {

    fun openWhatsapp(context: Context, phoneNumber: String) {
        val link = "https://wa.me/$phoneNumber"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))

        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "WhatsApp no está instalado", Toast.LENGTH_SHORT).show()
        }
    }

    val context = LocalContext.current

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
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
            Text(
                text = client.phone,
                modifier = Modifier
                    .padding(5.dp)
                    .clickable {
                        openWhatsapp(context, client.phone)
                    }
            )
        }
    }
}