package com.example.appcorte3.Products.data.datasource

import retrofit2.http.POST

interface ProductsService {

    @POST("products")
    suspend fun insertProduct()
}