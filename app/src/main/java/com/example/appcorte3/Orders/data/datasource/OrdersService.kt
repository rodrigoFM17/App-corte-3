package com.example.appcorte3.Orders.data.datasource

import com.example.appcorte3.Orders.data.model.OrderBody
import com.example.appcorte3.Orders.data.model.OrderResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OrdersService {

    @POST("sync/orders")
    suspend fun insertOrder(@Body body: OrderBody) : Response<Any>

    @GET("sync/orders")
    suspend fun getNewOrders(): Response<List<OrderResponse>>

    @PUT("sync/orders/{orderId}")
    suspend fun markOrderAsSended(@Path("orderId") orderId: String): Response<Any>
}