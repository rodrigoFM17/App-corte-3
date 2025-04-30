package com.example.appcorte3.Clients.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.example.appcorte3.components.ButtonComponent
import com.example.appcorte3.components.TextFieldComponent
import com.example.appcorte3.core.data.local.Client.entities.ClientEntity
import com.example.appcorte3.layouts.Container
import kotlinx.coroutines.launch
import java.util.UUID
import androidx.compose.runtime.getValue

@Composable
fun ParticularClientScreen(
    clientsViewModel: ClientsViewModel,
) {

    val name by clientsViewModel.name.observeAsState("")
    val phone by clientsViewModel.phone.observeAsState("")

    Container(
        headerTitle = "Editar Cliente"
    ) {

        TextFieldComponent(
            value = name,
            onValueChange = clientsViewModel::onNameChange,
            placeholder = "Nombre",
            spacerHeight = 20.dp,
            modifier = Modifier.fillMaxWidth()
        )

        TextFieldComponent(
            value = phone,
            onValueChange = clientsViewModel::onPhoneChange,
            placeholder = "Numero de telefono",
            spacerHeight = 20.dp,
            modifier = Modifier.fillMaxWidth()
        )

        ButtonComponent(
            text = "Guardar",
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                clientsViewModel.viewModelScope.launch {
                    clientsViewModel.updateClient()
                }
            }
        )
    }
}