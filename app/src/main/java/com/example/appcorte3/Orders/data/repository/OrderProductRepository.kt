package com.example.appcorte3.Orders.data.repository

import android.content.Context
import android.util.Log
import com.example.appcorte3.Orders.data.model.GeneralProductToBuy
import com.example.appcorte3.Orders.data.model.ProductToBuy
import com.example.appcorte3.core.data.local.Order.entities.OrderEntity
import com.example.appcorte3.core.data.local.OrderProducts.entitites.OrderProductsEntity
import com.example.appcorte3.core.data.local.appDatabase.DatabaseProvider

class OrderProductRepository(context: Context) {
    private val orderProductDAO = DatabaseProvider.getDatabase(context).orderProductDAO()

    suspend fun insertOrder(order: OrderProductsEntity) {
        orderProductDAO.insertOrderProduct(order)
    }

    suspend fun getProductsToBuyByDate(date: Long): List<GeneralProductToBuy> {

        val orderProducts = orderProductDAO.getProductsToBuyByDate(date)
        val generalProducts = mutableListOf<GeneralProductToBuy>()
        for (product in orderProducts) {
            val generalProduct = generalProducts.find {it.name == product.name}
            if ( generalProduct == null) {
                generalProducts.add(GeneralProductToBuy(product.name, product.bought, product.quantity, mutableListOf(product)))
            } else {
                generalProduct.quantity += product.quantity
                generalProduct.bought = generalProduct.bought && product.bought
                generalProduct.products.add(product)
            }
        }
        Log.d("GENERAL PRODUCTS", generalProducts.toString())
        return generalProducts
    }

    suspend fun deleteByOrderId(orderId: String) {
        orderProductDAO.deleteByOrderId(orderId)
    }

    suspend fun changeBoughtStatus(orderProductId: String, status: Int) {
        orderProductDAO.changeBoughtStatusByProductId(orderProductId, status)
    }


}