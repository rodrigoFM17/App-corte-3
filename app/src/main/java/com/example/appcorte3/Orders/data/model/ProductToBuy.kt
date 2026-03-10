package com.example.appcorte3.Orders.data.model

data class ProductToBuy(
    val orderProductId: String,
    val id: String,
    val name: String,
    val quantity: Float,
    var bought: Boolean,
)
