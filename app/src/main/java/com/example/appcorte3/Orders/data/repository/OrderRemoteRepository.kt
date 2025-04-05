package com.example.appcorte3.Orders.data.repository

import com.example.appcorte3.Clients.data.model.ClientBody
import com.example.appcorte3.Orders.data.model.OrderBody
import com.example.appcorte3.core.network.RetrofitHelper

class OrderRemoteRepository {

    private val orderRemoteRepository = RetrofitHelper.orderRemoteService

    suspend fun insertOrder (orderBody: OrderBody):  Result<Any>{
        return try {
            val response = orderRemoteRepository.insertOrder(orderBody)

            if(response.isSuccessful){
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getNewOrders() : Result<Any> {
        return try {
            val response = orderRemoteRepository.getNewOrders()

            if(response.isSuccessful){
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}