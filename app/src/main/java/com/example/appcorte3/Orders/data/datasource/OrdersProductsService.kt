package com.example.appcorte3.Orders.data.datasource

import retrofit2.http.POST

interface OrdersProductsService {

    @POST("sync/orders")
    suspend fun syncOrderProduct(): Result<Any>
}