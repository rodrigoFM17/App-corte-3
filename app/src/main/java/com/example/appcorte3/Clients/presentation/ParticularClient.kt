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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.appcorte3.components.Modal

@Composable
fun ParticularClientScreen(
    clientsViewModel: ClientsViewModel,
) {

    var showModal by remember { mutableStateOf(false) }
    val name by clientsViewModel.name.observeAsState("")
    val phone by clientsViewModel.phone.observeAsState("")
    val selectedClient by clientsViewModel.selectedClient.observeAsState(null)

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

        ButtonComponent(
            text = "Eliminar cliente",
            modifier = Modifier.fillMaxWidth(),
            negative = true,
            onClick = {showModal = true}
        )

        Modal(
            text = "Esta accion no se puede revertir y eliminara los pedidos a nombre de este cliente ¿Estas seguro que quieres continuar?",
            showModal = showModal,
            dismissAction = {showModal = false},
            confirmAction = {clientsViewModel.viewModelScope.launch {
                clientsViewModel.onDeleteClient(selectedClient!!)
                showModal = false
                clientsViewModel.navigateBack()
            }}
        )
    }
}