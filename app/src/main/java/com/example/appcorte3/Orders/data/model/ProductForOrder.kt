package com.example.appcorte3.Orders.data.model

import com.example.appcorte3.core.data.local.Product.entities.ProductEntity

data class ProductForOrder(
    var product: ProductEntity,
    var quantity: Float
)
