package com.example.appcorte3.Clients.data.datasource

import com.example.appcorte3.Clients.data.model.ClientBody
import com.example.appcorte3.Clients.data.model.ClientResponse
import retrofit2.http.GET
import retrofit2.http.POST

interface ClientService {

    @POST("clients")
    suspend fun insertClient(body: ClientBody)

    @GET("sync/clients")
    suspend fun getNewClients(): List<ClientResponse>
}