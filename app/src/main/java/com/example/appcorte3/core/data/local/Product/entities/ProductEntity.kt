package com.example.appcorte3.core.data.local.Product.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UNIT {
    INT,
    FRACC
}

@Entity( tableName = "Products")
data class ProductEntity (
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "price")
    val price: Float,
    @ColumnInfo(name = "unit")
    var unit: UNIT,
    @ColumnInfo(name = "sended")
    val sended: Boolean
)