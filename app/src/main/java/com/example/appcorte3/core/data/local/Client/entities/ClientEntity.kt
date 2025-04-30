package com.example.appcorte3.core.data.local.Client.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Clients")
data class ClientEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "phone")
    var phone: String,
    @ColumnInfo(name = "sended")
    val sended: Boolean
)
