package com.example.appcorte3.Orders.data.datasource

import com.example.appcorte3.Orders.data.model.OrderBody
import com.example.appcorte3.Orders.data.model.OrderResponse
import retrofit2.http.GET
import retrofit2.http.POST

interface OrdersService {

    @POST("orders")
    suspend fun insertOrder(body: OrderBody)

    @GET("sync/orders")
    suspend fun getNewOrders(): List<OrderResponse>
}