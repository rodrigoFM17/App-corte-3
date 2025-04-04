package com.example.appcorte3.core.data.local.OrderProducts.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appcorte3.core.data.local.Order.entities.OrderEntity
import com.example.appcorte3.core.data.local.OrderProducts.entitites.OrderProductsEntity

@Dao
interface OrderProductDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderProduct(orderProduct: OrderProductsEntity)

    @Query("SELECT * FROM OrderProducts where order_id = :orderId")
    suspend fun getProductsByOrderId(orderId: Int) : List<OrderProductsEntity>
}