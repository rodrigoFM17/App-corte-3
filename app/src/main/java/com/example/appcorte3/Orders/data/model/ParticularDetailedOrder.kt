package com.example.appcorte3.Orders.data.model

import com.example.appcorte3.core.data.local.OrderProducts.entitites.OrderProductsEntity

data class ParticularDetailedOrder (
    val id: String,
    val clientName: String,
    val total: Float,
    val date: Long,
    val orderProducts: List<OrderProductDetailed>
    )