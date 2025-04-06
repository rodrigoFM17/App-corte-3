package com.example.appcorte3.Orders.data.model

data class OrderResponse(
    val id: String,
    val client_id: String,
    val total: Float,
    val date: Long,
    val completed: Int,
    val sended: Int
)
