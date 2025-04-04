package com.example.appcorte3.Orders.data.model

data class OrderBody(
    val client_id: Int,
    val total: Float,
    val date: Long,
    val completed: Boolean,
)
