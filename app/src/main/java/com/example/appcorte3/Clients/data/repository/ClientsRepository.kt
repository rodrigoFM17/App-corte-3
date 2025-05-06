package com.example.appcorte3.Clients.data.repository

import android.content.Context
import com.example.appcorte3.core.data.local.Client.entities.ClientEntity
import com.example.appcorte3.core.data.local.appDatabase.DatabaseProvider


class ClientsRepository(context: Context) {

    private val clientDAO = DatabaseProvider.getDatabase(context).clientDAO()

    suspend fun insertClient(client: ClientEntity) {
        clientDAO.insertClient(client)
    }

    suspend fun getAllClients(): List<ClientEntity>{
        return clientDAO.getAllClients()
    }

    suspend fun updateClient(client: ClientEntity) {
        clientDAO.updateClient(client)
    }

    suspend fun deleteClient(client: ClientEntity) {
        clientDAO.deleteClient(client)
    }
}