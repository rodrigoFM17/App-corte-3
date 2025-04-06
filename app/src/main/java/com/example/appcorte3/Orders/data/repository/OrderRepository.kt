package com.example.appcorte3.Orders.data.repository

import android.content.Context
import com.example.appcorte3.Orders.data.model.OrderDetail
import com.example.appcorte3.core.data.local.Order.entities.OrderEntity
import com.example.appcorte3.core.data.local.appDatabase.DatabaseProvider


class OrderRepository(context: Context) {

    private val orderDAO = DatabaseProvider.getDatabase(context).orderDAO()

    suspend fun insertOrder(order: OrderEntity) {
        orderDAO.insertOrder(order)
    }

    suspend fun getAllPending(): List<OrderDetail> {
        return orderDAO.getAllPendingOrders()
    }

    suspend fun getOrderById(id: String): OrderDetail {
        return orderDAO.getOrderById(id)
    }
}