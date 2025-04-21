package com.example.appcorte3.core.data.local.Product.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.appcorte3.core.data.local.Product.entities.ProductEntity

@Dao
interface ProductDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Query("SELECT * FROM Products")
    suspend fun getAllProduct(): List<ProductEntity>

    @Query("SELECT * FROM Products where sended = 0")
    suspend fun getNoSendedProducts(): List<ProductEntity>

    @Query("UPDATE Products set sended = 1 where id = :productId")
    suspend fun markProductAsSended(productId: String)

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Delete
    suspend fun deleteProduct(product: ProductEntity)
}