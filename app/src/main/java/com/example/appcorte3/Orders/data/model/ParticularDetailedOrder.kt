package com.example.appcorte3.Orders.data.model

import com.example.appcorte3.core.data.local.OrderProducts.entitites.OrderProductsEntity

data class ParticularDetailedOrder (
    val id: String,
    val clientName: String,
    val total: String,
    val date: String,
    val orderProducts: List<OrderProductsEntity>
    )