package com.example.appcorte3.Clients.data.datasource

import com.example.appcorte3.Clients.data.model.ClientBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ClientService {

    @POST("clients")
    suspend fun syncClient(@Body body: ClientBody) : Response<Any>
}