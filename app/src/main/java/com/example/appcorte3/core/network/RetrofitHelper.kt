package com.example.appcorte3.core.network

import com.example.appcorte3.Clients.data.datasource.ClientService
import com.example.appcorte3.Orders.data.datasource.OrdersService
import com.example.appcorte3.Products.data.datasource.ProductsService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHelper {
    private const val BASE_URL = "http://3.226.75.51/api/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()


    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val clientRemoteService: ClientService by lazy {
        retrofit.create(ClientService::class.java)
    }

    val orderRemoteService: OrdersService by lazy {
        retrofit.create(OrdersService::class.java)
    }

    val productsRemoteService: ProductsService by lazy {
        retrofit.create(ProductsService::class.java)
    }
//
//    val loginService: LoginService by lazy {
//        retrofit.create(LoginService::class.java)
//    }

}