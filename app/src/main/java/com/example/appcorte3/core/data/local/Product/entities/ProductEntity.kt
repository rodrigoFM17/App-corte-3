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
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "price")
    val price: Float,
    @ColumnInfo(name = "unit")
    val unit: UNIT

)