package com.example.appcorte3.core.data.local.Order.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appcorte3.Orders.data.model.OrderDetail
import com.example.appcorte3.core.data.local.Order.entities.OrderEntity

@Dao
interface OrderDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Query("SELECT Orders.id, client_id, Orders.total, Orders.date, Clients.name FROM Orders inner join Clients on Orders.client_id = Clients.id WHERE completed = 0")
    suspend fun getAllPendingOrders(): List<OrderDetail>

    @Query("SELECT * FROM Orders where completed = 1")
    suspend fun getAllCompletedOrders(): List<OrderEntity>

    @Query("SELECT Orders.id, client_id, Orders.total, Orders.date, Clients.name FROM Orders inner join Clients on Orders.client_id = Clients.id WHERE Orders.id = :id")
    suspend fun getOrderById(id: String): OrderDetail

    @Query("UPDATE Orders set completed = 1 where id = :id")
    suspend fun completeOrderById(id: Int)

    @Query("SELECT * FROM Orders where sended = 0")
    suspend fun getNoSendedOrders(): List<OrderEntity>

    @Query("UPDATE Orders set sended = 1 WHERE id = :orderId")
    suspend fun markOrderAsSended(orderId: String)

}