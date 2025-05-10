package com.example.appcorte3.Orders.data.repository

import android.content.Context
import com.example.appcorte3.Orders.data.model.ProductToBuy
import com.example.appcorte3.core.data.local.Order.entities.OrderEntity
import com.example.appcorte3.core.data.local.OrderProducts.entitites.OrderProductsEntity
import com.example.appcorte3.core.data.local.appDatabase.DatabaseProvider

class OrderProductRepository(context: Context) {
    private val orderProductDAO = DatabaseProvider.getDatabase(context).orderProductDAO()

    suspend fun insertOrder(order: OrderProductsEntity) {
        orderProductDAO.insertOrderProduct(order)
    }

    suspend fun getProductsToBuyByDate(date: Long): List<ProductToBuy> {
        return orderProductDAO.getProductsToBuyByDate(date)
    }

    suspend fun deleteByOrderId(orderId: String) {
        orderProductDAO.deleteByOrderId(orderId)
    }


}