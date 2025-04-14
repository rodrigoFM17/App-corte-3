package com.example.appcorte3.Products.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appcorte3.Products.data.repository.ProductRepository
import com.example.appcorte3.core.data.local.Product.entities.ProductEntity
import com.example.appcorte3.core.data.local.Product.entities.UNIT


class ProductsViewModel (
    context: Context,
    val navigateToAddProduct: () -> Unit,
    val navigateToParticularProduct: () -> Unit
) : ViewModel() {

    private val productRepository = ProductRepository(context)
    private val _products = MutableLiveData<List<ProductEntity>>()

    val products: LiveData<List<ProductEntity>> = _products

    suspend fun getAllProducts () {
        _products.value = productRepository.getAllProducts()
    }

    private val _name = MutableLiveData<String>()
    private val _price = MutableLiveData<Float>()
    private val _unit = MutableLiveData<UNIT>()

    val name: LiveData<String> = _name
    val price: LiveData<Float> = _price
    val unit : LiveData<UNIT> = _unit

    fun onChangeName(value: String) {
        _name.value = value
    }

    fun onChangePrice(value: String) {

        _price.value = value.toFloat()

    }

    fun onChangeUnit(value: UNIT) {
        _unit.value = value
    }

    suspend fun insertProduct(product: ProductEntity) {
        productRepository.insertProduct(product)
    }

    // particular Product

    private val _selectedProduct = MutableLiveData<ProductEntity>()
    var selectedProduct : LiveData<ProductEntity> = _selectedProduct

    fun onSelectProduct(product: ProductEntity) {
        _selectedProduct.value = product
        navigateToParticularProduct()
    }

    fun onChangeProductUnit(unit: UNIT) {
        var product = _selectedProduct.value

        if (product != null) {
            product.unit = unit
        }
    }


}