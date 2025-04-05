package com.example.appcorte3.Products.data.datasource

import com.example.appcorte3.Products.data.model.ProductBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductsService {

    @POST("products")
    suspend fun insertProduct(@Body body: ProductBody): Response<Any>

    @GET("sync/products")
    suspend fun getNewProducts(): Response<Any>
}