package com.example.appcorte3.Products.data.model

import com.example.appcorte3.core.data.local.Product.entities.UNIT

data class ProductBody(
    val name: String,
    val price: Float,
    val unit: UNIT
)
