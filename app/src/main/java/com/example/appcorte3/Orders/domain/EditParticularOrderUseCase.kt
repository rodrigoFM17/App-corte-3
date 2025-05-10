package com.example.appcorte3.Orders.domain

import android.content.Context
import android.util.Log
import com.example.appcorte3.Orders.data.model.ParticularDetailedOrder
import com.example.appcorte3.Orders.data.model.ProductForOrder
import com.example.appcorte3.Orders.data.repository.OrderProductRepository
import com.example.appcorte3.Orders.data.repository.OrderRepository
import com.example.appcorte3.core.data.local.Order.entities.OrderEntity
import com.example.appcorte3.core.data.local.OrderProducts.entitites.OrderProductsEntity
import java.util.UUID

class EditParticularOrderUseCase(context: Context) {

    private val orderRepository = OrderRepository(context)
    private val orderProductRepository = OrderProductRepository(context)
    suspend operator fun invoke(orderDetail: ParticularDetailedOrder, orderProducts: List<ProductForOrder>, total: Float) {
        orderProductRepository.deleteByOrderId(orderDetail.id)
        Log.d("ELIMINADOS", "ordersProducts eliminados")
        orderRepository.updateOrder(OrderEntity(
            id = orderDetail.id,
            clientId = orderDetail.client_id,
            completed = orderDetail.completed,
            paid = orderDetail.paid,
            total = total,
            date = orderDetail.date
        ))
        Log.d("ORDER", "orderActualizada ${orderDetail.id}")
        for (product in orderProducts) {
            orderProductRepository.insertOrder(OrderProductsEntity(
                id = UUID.randomUUID().toString(),
                quantity = product.quantity,
                orderId = orderDetail.id,
                productId = product.product.id
            ))
            Log.d("ORDERPRODUCT", "order agregada ${product}")
        }
    }
}