package com.example.appcorte3.Orders.data.model

import com.example.appcorte3.core.data.local.Product.entities.UNIT

data class OrderProductDetailed(
    val id: String,
    val productId : String,
    val quantity: Float,
    val name: String,
    val price: Float,
    val unit: UNIT
)
