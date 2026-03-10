package com.example.appcorte3.Settings.presentation

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.appcorte3.components.ButtonComponent
import com.example.appcorte3.components.Modal
import com.example.appcorte3.layouts.Container
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel) {

    val context = LocalContext.current

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/zip")
    ) { uri ->
        uri?.let {
            settingsViewModel.exportDB(it, context)
        }
    }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            settingsViewModel.restoreDBFromZip(it, context)
        }
    }

    Container(
        headerTitle = "Configuracion"
    ) {

        ButtonComponent(
            text = "Exportar Base de datos",
            onClick = {
                val dateString = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))
                exportLauncher.launch("respaldo_bd_sgp_${dateString}")
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        ButtonComponent(
            text = "Importar Base de datos",
            onClick = { importLauncher.launch(arrayOf("application/zip"))},
            negative = true,
            modifier = Modifier.fillMaxWidth()
        )
        Text(text = "Esta accion puede causar perdida de datos")

    }

}