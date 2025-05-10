package com.example.appcorte3.Orders.data.model

import com.example.appcorte3.core.data.local.OrderProducts.entitites.OrderProductsEntity
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

data class ParticularDetailedOrder (
    val id: String,
    var clientName: String,
    var client_id: String,
    val total: Float,
    var date: Long,
    var completed: Boolean,
    var paid: Boolean,
    val orderProducts: List<OrderProductDetailed>
    )