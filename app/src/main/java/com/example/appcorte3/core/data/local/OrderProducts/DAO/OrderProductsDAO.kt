package com.example.appcorte3.core.data.local.OrderProducts.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appcorte3.Orders.data.model.OrderProductDetailed
import com.example.appcorte3.Orders.data.model.ProductToBuy
import com.example.appcorte3.core.data.local.OrderProducts.entitites.OrderProductsEntity

@Dao
interface OrderProductDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderProduct(orderProduct: OrderProductsEntity)

    @Query("SELECT OrderProducts.id, OrderProducts.quantity, Products.name, Products.price FROM OrderProducts INNER JOIN Products on OrderProducts.product_id = Products.id where order_id = :orderId")
    suspend fun getProductsByOrderId(orderId: String) : List<OrderProductDetailed>

    @Query("SELECT Products.name, sum(quantity) as quantity, 0 as bought \n" +
            "FROM Orders inner join OrderProducts on Orders.id = OrderProducts.order_id inner join Products on OrderProducts.product_id = Products.id " +
            "where Orders.date = :date group by OrderProducts.product_id")
    suspend fun getProductsToBuyByDate(date: Long): List<ProductToBuy>

    @Delete
    suspend fun deleteOrderProduct(orderProduct: OrderProductsEntity)
}