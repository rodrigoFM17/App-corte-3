package com.example.appcorte3.Orders.domain

import android.util.Log
import com.example.appcorte3.Orders.data.model.OrderBody
import com.example.appcorte3.Orders.data.model.OrderResponse
import com.example.appcorte3.Orders.data.repository.OrderRemoteRepository

class GetNewsOrdersUseCase {

    private val orderRemoteRepository = OrderRemoteRepository()

    suspend operator fun invoke(): Result<List<OrderResponse>> {
        val result = orderRemoteRepository.getNewOrders()


        result.onSuccess {
            data ->
            Log.d("API ORDERS", "exito")
        }

        return result
    }


}