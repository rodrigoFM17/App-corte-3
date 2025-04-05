package com.example.appcorte3.Clients.data.datasource

import com.example.appcorte3.Clients.data.model.ClientBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ClientService {

    @POST("sync/clients/upload")
    suspend fun syncClient(@Body body: List<ClientBody>) : Response<Any>

    @GET("sync/clients")
    suspend fun getNewClients(): Response<Any>

    @PUT("sync/clients/downloaded")
    suspend fun syncDownloadedClients(@Body body: List<Int>): Response<Any>
}