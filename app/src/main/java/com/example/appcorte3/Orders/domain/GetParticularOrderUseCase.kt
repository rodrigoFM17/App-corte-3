package com.example.appcorte3.Orders.domain

import android.content.Context
import com.example.appcorte3.Orders.data.model.ParticularDetailedOrder
import com.example.appcorte3.core.data.local.appDatabase.DatabaseProvider

class GetParticularOrderUseCase(context: Context) {

    private val orderDAO = DatabaseProvider.getDatabase(context).orderDAO()
    private val orderProductDAO = DatabaseProvider.getDatabase(context).orderProductDAO()

    suspend operator fun invoke(orderId: String): ParticularDetailedOrder{
        val order = orderDAO.getOrderById(orderId)
        val orderProducts = orderProductDAO.getProductsByOrderId(orderId)

        return ParticularDetailedOrder(
            id = order.id,
            clientName = order.name,
            date = order.date,
            total = order.total,
            orderProducts = orderProducts
        )
    }
}