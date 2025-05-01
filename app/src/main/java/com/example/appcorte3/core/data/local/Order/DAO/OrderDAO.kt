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

    @Query("SELECT Orders.id, client_id, Orders.total, Orders.date, Clients.name, completed, paid FROM Orders inner join Clients on Orders.client_id = Clients.id")
    suspend fun getAllOrders(): List<OrderDetail>

    @Query("SELECT Orders.id, client_id, Orders.total, Orders.date, Clients.name, completed, paid FROM Orders inner join Clients on Orders.client_id = Clients.id WHERE completed = 0")
    suspend fun getAllPendingOrders(): List<OrderDetail>

    @Query("SELECT Orders.id, client_id, Orders.total, Orders.date, Clients.name, completed, paid FROM Orders inner join Clients on Orders.client_id = Clients.id WHERE completed = 1")
    suspend fun getAllCompletedOrders(): List<OrderDetail>

    @Query("SELECT Orders.id, client_id, Orders.total, Orders.date, Clients.name, completed, paid FROM Orders inner join Clients on Orders.client_id = Clients.id WHERE paid = 1")
    suspend fun getAllPaidOrders(): List<OrderDetail>

    @Query("SELECT Orders.id, client_id, Orders.total, Orders.date, Clients.name, completed, paid FROM Orders inner join Clients on Orders.client_id = Clients.id WHERE paid = 0")
    suspend fun getAllNoPaidOrders(): List<OrderDetail>

    @Query("SELECT Orders.id, client_id, Orders.total, Orders.date, Clients.name, completed, paid FROM Orders inner join Clients on Orders.client_id = Clients.id WHERE Orders.id = :id")
    suspend fun getOrderById(id: String): OrderDetail

    @Query("UPDATE Orders set completed = :status where id = :id")
    suspend fun changeCompleteOrderStatusById(id: String, status: Int)

    @Query("UPDATE Orders set paid = :status where id = :id")
    suspend fun changePaidOrderStatusById(id: String, status: Int)



}