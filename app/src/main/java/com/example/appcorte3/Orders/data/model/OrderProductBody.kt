package com.example.appcorte3.Orders.data.model


data class OrderProductBody(
    val id: String,
    val order_id: String,
    val product_id: String,
    val quantity: Int,
)
