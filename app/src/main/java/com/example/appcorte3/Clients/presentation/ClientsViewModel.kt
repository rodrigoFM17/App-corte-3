package com.example.appcorte3.Clients.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appcorte3.Clients.data.repository.ClientsRepository
import com.example.appcorte3.core.data.local.Client.entities.ClientEntity

class ClientsViewModel(
    context: Context,
    val navigateToParticularClient: () -> Unit,
    val navigateToAddClient: () -> Unit,
    val navigateBack: () -> Unit,
) : ViewModel() {

    private val clientsRepository = ClientsRepository(context)
    private lateinit var clientsConst : List<ClientEntity>

    // clients (lista)
    private val _clients = MutableLiveData<List<ClientEntity>>()
    private val _searchedClient = MutableLiveData<String>()
    private val _searching = MutableLiveData<Boolean>()

    val clients: LiveData<List<ClientEntity>> = _clients
    val searchedClient : LiveData<String> = _searchedClient
    val searching: LiveData<Boolean> = _searching

    suspend fun getAllClients() {
        clientsConst = clientsRepository.getAllClients()
        _clients.value = clientsConst
    }

    fun onChangeSearchedClient(value: String) {

        _searchedClient.value = value
        if(value != "") {
            _searching.value = true
            _clients.value = clientsConst.filter { it.name.startsWith(value, ignoreCase = true) }
        } else {
            _searching.value = false
            _clients.value = clientsConst
        }
    }

    // add client

    private val _name = MutableLiveData<String>()
    private val _phone = MutableLiveData<String>()

    val name: LiveData<String> = _name
    val phone : LiveData<String> = _phone

    fun onNameChange(value: String) {
        _name.value = value
    }

    fun onPhoneChange(value: String){
        _phone.value = value
    }

    fun onSelectClient(client: ClientEntity) {
        _selectedClient.value = client
        _name.value = client.name
        _phone.value = client.phone
        navigateToParticularClient()
    }

    suspend fun insertClient(client: ClientEntity) {
        clientsRepository.insertClient(client)
        _name.value = ""
        _phone.value = ""
        navigateBack()
    }

    fun resetInputs(){
        _name.value = ""
        _phone.value = ""
    }

    // particular client

    private val _selectedClient = MutableLiveData<ClientEntity>()
    val selectedClient : LiveData<ClientEntity> = _selectedClient

    suspend fun updateClient() {
        var newClient = _selectedClient.value

        if (newClient != null){
            newClient.name = _name.value!!
            newClient.phone = _phone.value!!

            clientsRepository.updateClient(newClient)
            navigateBack()
        }
    }

    suspend fun onDeleteClient(client: ClientEntity) {
        clientsRepository.deleteClient(client)
    }
}