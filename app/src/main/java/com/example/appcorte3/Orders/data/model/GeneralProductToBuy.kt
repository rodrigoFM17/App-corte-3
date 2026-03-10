package com.example.appcorte3.Orders.data.model

data class GeneralProductToBuy (
    val name: String,
    var bought: Boolean,
    var quantity: Float,
    val products: MutableList<ProductToBuy> = mutableListOf()
)