package com.example.appcorte3.Products.data.repository

import android.content.Context
import com.example.appcorte3.core.data.local.Product.entities.ProductEntity
import com.example.appcorte3.core.data.local.appDatabase.DatabaseProvider

class ProductRepository(context: Context) {

    private val productDAO = DatabaseProvider.getDatabase(context).productDAO()

    suspend fun insertProduct(product: ProductEntity){
        productDAO.insertProduct(product)
    }

    suspend fun getAllProducts() : List<ProductEntity> {
        return productDAO.getAllProduct()
    }

}