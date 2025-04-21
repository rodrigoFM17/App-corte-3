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
        val newPrice = value.toFloatOrNull()

        if(newPrice != null) {
            _price.value = newPrice!!
        }

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

    private val _productName = MutableLiveData<String>()
    private val _productPrice = MutableLiveData<Float>()

    val productName : LiveData<String> = _productName
    val productPrice: LiveData<Float> = _productPrice

    fun onSelectProduct(product: ProductEntity) {
        _selectedProduct.value = product
        _productName.value = product.name
        _productPrice.value = product.price
        navigateToParticularProduct()
    }

    fun onChangeProductName(name: String) {
        _productName.value = name
        val newProduct = _selectedProduct.value
        newProduct!!.name = name
        _selectedProduct.value = newProduct!!
    }

    fun onChangeProductPrice(value: String) {
        val newPrice = value.toFloatOrNull()
        if(newPrice != null) {
            _productPrice.value = newPrice!!
            val newProduct = _selectedProduct.value
            newProduct!!.price = newPrice
            _selectedProduct.value = newProduct!!
        }
    }

    fun onChangeProductUnit(unit: UNIT) {
        var product = _selectedProduct.value

        if (product != null) {
            product.unit = unit
        }
    }

    suspend fun onSaveChanges(product: ProductEntity) {
        productRepository.updateProduct(product)
    }


}