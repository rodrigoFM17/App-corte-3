package com.example.appcorte3.core.data.local.Product.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UNIT(val label: String) {
    INT("Por Pieza"),
    FRACC("Por Peso");

    override fun toString(): String {
        return label
    }
}

@Entity( tableName = "Products")
data class ProductEntity (
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "price")
    var price: Float,
    @ColumnInfo(name = "unit")
    var unit: UNIT,
)