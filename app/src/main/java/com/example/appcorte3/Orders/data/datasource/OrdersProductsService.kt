package com.example.appcorte3.Orders.data.datasource

import com.example.appcorte3.Orders.data.model.OrderProductBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OrdersProductsService {

    @POST("sync/order-products")
    suspend fun insertOrderProduct(@Body orderProductBody: OrderProductBody): Response<Any>

    @GET("sync/order-products")
    suspend fun getNewOrderProducts(): Response<List<OrderProductBody>>

    @PUT("sync/order-products/{orderProductId}")
    suspend fun markOrderProductAsCompleted(@Path("orderProductId") orderProductId: String): Response<Any>

}