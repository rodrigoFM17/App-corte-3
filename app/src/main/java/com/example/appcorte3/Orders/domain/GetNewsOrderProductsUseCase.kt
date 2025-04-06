package com.example.appcorte3.Orders.domain

import com.example.appcorte3.Orders.data.model.OrderBody
import com.example.appcorte3.Orders.data.model.OrderProductBody
import com.example.appcorte3.Orders.data.repository.OrderProductsRemoteRepository

class GetNewsOrderProductsUseCase {

    private val orderProductsRemoteRepository = OrderProductsRemoteRepository()

    suspend operator fun invoke (): List<OrderProductBody> {

        val result = orderProductsRemoteRepository.getNewOrderProducts()

        result.onSuccess {
                data ->
            return data
        }

        return listOf()
    }
}