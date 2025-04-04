package com.example.appcorte3.core.data.local.appDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appcorte3.core.data.local.Client.DAO.ClientDAO
import com.example.appcorte3.core.data.local.Client.entities.ClientEntity
import com.example.appcorte3.core.data.local.Order.DAO.OrderDAO
import com.example.appcorte3.core.data.local.Order.entities.OrderEntity
import com.example.appcorte3.core.data.local.OrderProducts.DAO.OrderProductDAO
import com.example.appcorte3.core.data.local.OrderProducts.entitites.OrderProductsEntity
import com.example.appcorte3.core.data.local.Product.DAO.ProductDAO
import com.example.appcorte3.core.data.local.Product.entities.ProductEntity

@Database(
    entities = [
        ClientEntity::class,
        ProductEntity::class,
        OrderEntity::class,
        OrderProductsEntity::class
    ],
    version = 1
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun clientDAO(): ClientDAO

    abstract fun productDAO(): ProductDAO

    abstract fun orderProductDAO(): OrderProductDAO

    abstract fun orderDAO(): OrderDAO
}