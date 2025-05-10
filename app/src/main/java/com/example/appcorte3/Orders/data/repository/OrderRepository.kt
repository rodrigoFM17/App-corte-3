package com.example.appcorte3.Orders.data.repository

import android.content.Context
import com.example.appcorte3.Orders.data.model.OrderDetail
import com.example.appcorte3.Products.domain.UpdateOrderTotalByProductId
import com.example.appcorte3.core.data.local.Order.entities.OrderEntity
import com.example.appcorte3.core.data.local.appDatabase.DatabaseProvider


class OrderRepository(context: Context) {

    private val orderDAO = DatabaseProvider.getDatabase(context).orderDAO()

    suspend fun insertOrder(order: OrderEntity) {
        orderDAO.insertOrder(order)
    }

    suspend fun getAllOrders(): List<OrderDetail> {
        return orderDAO.getAllOrders()
    }

    suspend fun getAllPending(): List<OrderDetail> {
        return orderDAO.getAllPendingOrders()
    }

    suspend fun getAllCompleted() : List<OrderDetail> {
        return orderDAO.getAllCompletedOrders()
    }

    suspend fun getOrderById(id: String): OrderDetail {
        return orderDAO.getOrderById(id)
    }

    suspend fun getAllPaid(): List<OrderDetail> {
        return orderDAO.getAllPaidOrders()
    }

    suspend fun getAllNoPaid() : List<OrderDetail> {
        return orderDAO.getAllNoPaidOrders()
    }

    suspend fun getOrdersByProductId(productId: String): List<String> {
        return orderDAO.getOrdersByProductId(productId)
    }

    suspend fun updateOrder(order: OrderEntity) {
        orderDAO.updateOrder(order)
    }

    suspend fun updateTotalByOrderId(orderId: String) {
        orderDAO.updateOrderTotalById(orderId)
    }

    suspend fun changeCompleteStatus(id: String, status: Boolean) {
        orderDAO.changeCompleteOrderStatusById(id, if (status) 1 else 0)
    }

    suspend fun changePaidStatus(id: String, status: Boolean) {
        orderDAO.changePaidOrderStatusById(id, if (status) 1 else 0)
    }

    suspend fun deleteOrder(orderId: String){
        orderDAO.deleteOrderById(orderId)
    }
}