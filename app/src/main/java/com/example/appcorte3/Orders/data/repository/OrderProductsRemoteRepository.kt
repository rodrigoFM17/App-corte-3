package com.example.appcorte3.Orders.data.repository

import com.example.appcorte3.Orders.data.model.OrderProductBody
import com.example.appcorte3.core.network.RetrofitHelper

class OrderProductsRemoteRepository {

    private val orderProductsRemoteService = RetrofitHelper.orderProductsRemoteService

    suspend fun insertOrderProduct(orderProductBody: OrderProductBody): Result<Any> {
        return try {
            val response = orderProductsRemoteService.insertOrderProduct(orderProductBody)

            if(response.isSuccessful){
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getNewOrderProducts(): Result<List<OrderProductBody>> {
        return try {
            val response = orderProductsRemoteService.getNewOrderProducts()

            if(response.isSuccessful){
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun markOrderProductsAsSended(orderId: String): Result<Any> {
        return try {
            val response = orderProductsRemoteService.markOrderProductAsCompleted(orderId)

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