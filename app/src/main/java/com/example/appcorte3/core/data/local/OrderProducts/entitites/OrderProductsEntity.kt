package com.example.appcorte3.core.data.local.OrderProducts.entitites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.appcorte3.core.data.local.Order.entities.OrderEntity
import com.example.appcorte3.core.data.local.Product.entities.ProductEntity

@Entity(
    tableName = "OrderProducts",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["order_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [Index(value = ["order_id"]), Index(value = ["product_id"])]
)
data class OrderProductsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "order_id")
    val orderId: Int,
    @ColumnInfo(name = "product_id")
    val productId: Int,
    @ColumnInfo(name = "quantity")
    val quantity: Int
)