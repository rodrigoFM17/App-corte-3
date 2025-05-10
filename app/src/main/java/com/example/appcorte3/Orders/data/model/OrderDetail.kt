package com.example.appcorte3.Orders.data.model

data class OrderDetail(
    val name: String,
    val id: String,
    val client_id: String,
    val date: Long,
    val total: Float,
    val completed: Boolean,
    val paid: Boolean
)
