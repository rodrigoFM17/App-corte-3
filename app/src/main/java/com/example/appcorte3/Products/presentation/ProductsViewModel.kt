package com.example.appcorte3.Products.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appcorte3.Products.data.repository.ProductRepository
import com.example.appcorte3.core.data.local.Product.entities.ProductEntity
import com.example.appcorte3.core.data.local.Product.entities.UNIT


class ProductsViewModel (
    context: Context,
    val navigateToAddProduct: () -> Unit,
    val navigateToParticularProduct: () -> Unit,
    val navigateToProducts: () -> Unit
) : ViewModel() {

    private val productRepository = ProductRepository(context)

    private lateinit var productsConst: List<ProductEntity>

    // products (lista)

    private val _products = MutableLiveData<List<ProductEntity>>()
    private val _searchedProduct = MutableLiveData<String>()
    private val _searching = MutableLiveData<Boolean>()
    val searchedProduct : LiveData<String> = _searchedProduct
    val searching : LiveData<Boolean> = _searching

    val products: LiveData<List<ProductEntity>> = _products

    suspend fun getAllProducts () {
        productsConst = productRepository.getAllProducts()
        _products.value = productsConst
    }

    fun onChangeSearchedProduct (value : String) {
        _searchedProduct.value = value

        if (value != "") {
            _searching.value = true
            _products.value = productsConst.filter { it.name.startsWith(value, ignoreCase = true) }
        } else {
            _searching.value = false
            _products.value = productsConst
        }

    }

    // add product

    private val _name = MutableLiveData<String>()
    private val _newProductPrice = MutableLiveData<Float>()

    private val _priceIntegers = MutableLiveData<Int>()
    private val _priceDecimals = MutableLiveData<Int>()
    private val _unit = MutableLiveData<UNIT>()

    val priceIntegers : LiveData<Int> = _priceIntegers
    val priceDecimals: LiveData<Int> = _priceDecimals
    val name: LiveData<String> = _name
    val newProductPrice: LiveData<Float> = _newProductPrice
    val unit : LiveData<UNIT> = _unit

    fun onChangeName(value: String) {
        _name.value = value
    }

    fun onChangePriceIntegers(value: String) {
        val newValue = value.toIntOrNull()

        if(newValue != null) {
            _priceIntegers.value = newValue
            onChangePrice()
        } else {
            _priceIntegers.value = 0
        }
    }

    fun onChangePriceDecimals(value: String) {
        val newValue = value.toIntOrNull()

        if(newValue != null) {
            _priceDecimals.value = newValue
            onChangePrice()
        } else {
            _priceDecimals.value = 0
        }
    }

    fun onChangePrice() {

        val newPrice = "${priceIntegers.value ?: 0}.${priceDecimals.value ?: 0}".toFloatOrNull()

        if(newPrice != null) {
            _newProductPrice.value = newPrice
        }

    }

    fun onChangeUnit(value: UNIT) {
        _unit.value = value
    }

    suspend fun insertProduct(product: ProductEntity) {
        productRepository.insertProduct(product)
        navigateToProducts()
    }

    // particular Product

    private val _selectedProduct = MutableLiveData<ProductEntity>()
    var selectedProduct : LiveData<ProductEntity> = _selectedProduct

    private val _productName = MutableLiveData<String>()
    private val _productPrice = MutableLiveData<Float>()

    private val _productPriceIntegers = MutableLiveData<Int>()
    private val _productPriceDecimals = MutableLiveData<Int>()

    val productPriceIntegers : LiveData<Int> = _productPriceIntegers
    val productPriceDecimals : LiveData<Int> = _productPriceDecimals

    val productName : LiveData<String> = _productName
    val productPrice: LiveData<Float> = _productPrice

    fun onSelectProduct(product: ProductEntity) {
        _selectedProduct.value = product
        _productName.value = product.name
        _productPrice.value = product.price
        val productPrice = product.price.toString().split(".")
        _priceIntegers.value = productPrice[0].toInt()
        _priceDecimals.value = productPrice[1].toInt()
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

        val product = _selectedProduct.value
        product?.price = _newProductPrice.value!!
        productRepository.updateProduct(product!!)
    }


}