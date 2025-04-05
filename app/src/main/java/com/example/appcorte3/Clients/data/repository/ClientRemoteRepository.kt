package com.example.appcorte3.Clients.data.repository

import android.util.Log
import com.example.appcorte3.Clients.data.model.ClientBody
import com.example.appcorte3.core.network.RetrofitHelper

class ClientRemoteRepository {

    private val clientRemoteService = RetrofitHelper.clientRemoteService

    suspend fun syncClient(clientBody: ClientBody):  Result<Any>{
        return try {
            val response = clientRemoteService.insertClient(clientBody)

            if(response.isSuccessful){
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun




}