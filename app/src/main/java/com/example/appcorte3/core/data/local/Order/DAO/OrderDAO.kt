package com.example.appcorte3.core.data.local.Order.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appcorte3.core.data.local.Order.entities.OrderEntity

@Dao
interface OrderDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Query("SELECT * FROM Orders where completed = 0")
    suspend fun getAllPendingOrders(): List<OrderEntity>

    @Query("SELECT * FROM Orders where completed = 1")
    suspend fun getAllCompletedOrders(): List<OrderEntity>

    @Query("SELECT * FROM Orders where id = :id")
    suspend fun getOrderById(id: Int): OrderEntity

    @Query("UPDATE Orders set completed = 1 where id = :id")
    suspend fun completeOrderById(id: Int)

    @Query("SELECT * FROM Orders where sended = 0")
    suspend fun getNoSendedOrders(): List<OrderEntity>

}