package com.example.appcorte3.core.data.local.Order.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.appcorte3.core.data.local.Client.entities.ClientEntity

@Entity(
    tableName = "Orders",
    foreignKeys = [
        ForeignKey(
            entity = ClientEntity::class,
            parentColumns = ["id"],
            childColumns = ["client_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["client_id"])]
)
data class OrderEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "client_id")
    val clientId: String,
    @ColumnInfo(name = "total")
    val total: Float,
    @ColumnInfo(name = "date")
    val date: Long,
    @ColumnInfo(name = "completed")
    val completed: Boolean,
    @ColumnInfo(name = "sended")
    val sended: Boolean
)