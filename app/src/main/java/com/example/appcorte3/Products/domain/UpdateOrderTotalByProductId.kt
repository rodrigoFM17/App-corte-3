
package com.example.appcorte3.Products.domain

import android.content.Context
import com.example.appcorte3.Orders.data.repository.OrderRepository
import com.example.appcorte3.core.data.local.Product.entities.ProductEntity

class UpdateOrderTotalByProductId(context: Context) {

    val orderRepository = OrderRepository(context)

    suspend operator fun invoke(product: ProductEntity) {
        val ordersIds = orderRepository.getOrdersByProductId(product.id)

        ordersIds.forEach { id -> orderRepository.updateTotalByOrderId(id) }
    }
}