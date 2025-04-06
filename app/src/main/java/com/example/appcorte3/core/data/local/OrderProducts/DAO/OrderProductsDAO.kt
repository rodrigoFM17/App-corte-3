package com.example.appcorte3.core.data.local.OrderProducts.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appcorte3.Orders.data.model.OrderProductDetailed
import com.example.appcorte3.core.data.local.OrderProducts.entitites.OrderProductsEntity

@Dao
interface OrderProductDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderProduct(orderProduct: OrderProductsEntity)

    @Query("SELECT OrderProducts.id, OrderProducts.quantity, Products.name, Products.price FROM OrderProducts INNER JOIN Products on OrderProducts.product_id = Products.id where order_id = :orderId")
    suspend fun getProductsByOrderId(orderId: String) : List<OrderProductDetailed>

    @Query("SELECT * FROM OrderProducts where sended = 0")
    suspend fun getNoSendedOrderProducts(): List<OrderProductsEntity>

    @Query("UPDATE OrderProducts SET sended = 1 WHERE id = :orderProductId")
    suspend fun markOrderProductAsSended(orderProductId: String)
}