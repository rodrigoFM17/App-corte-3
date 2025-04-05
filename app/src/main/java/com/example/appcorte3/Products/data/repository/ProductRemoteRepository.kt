package com.example.appcorte3.Products.data.repository

import com.example.appcorte3.Products.data.model.ProductBody
import com.example.appcorte3.core.network.RetrofitHelper

class ProductRemoteRepository {

    private val productRemoteService = RetrofitHelper.productsRemoteService

    suspend fun insertProduct(productBody: ProductBody): Result<Any> {
        return try {
            val response = productRemoteService.insertProduct(productBody)

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