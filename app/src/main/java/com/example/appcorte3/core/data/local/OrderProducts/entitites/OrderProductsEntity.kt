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
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "order_id")
    val orderId: String,
    @ColumnInfo(name = "product_id")
    val productId: String,
    @ColumnInfo(name = "quantity")
    val quantity: Int,
    @ColumnInfo(name = "sended")
    val sended: Boolean
)