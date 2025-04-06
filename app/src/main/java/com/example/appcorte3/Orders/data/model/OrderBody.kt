package com.example.appcorte3.Orders.data.model

data class OrderBody(
    val id: String,
    val client_id: String,
    val total: Float,
    val date: Long,
    val completed: Boolean,
)
