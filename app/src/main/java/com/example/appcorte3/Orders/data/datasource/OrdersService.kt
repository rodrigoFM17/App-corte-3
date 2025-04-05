package com.example.appcorte3.Orders.data.datasource

import com.example.appcorte3.Orders.data.model.OrderBody
import com.example.appcorte3.Orders.data.model.OrderResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface OrdersService {

    @POST("sync/orders/upload")
    suspend fun insertOrder(@Body body: OrderBody) : Response<Any>

    @GET("sync/orders")
    suspend fun getNewOrders(): Response<Any>
}