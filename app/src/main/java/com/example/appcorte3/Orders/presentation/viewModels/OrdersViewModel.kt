package com.example.appcorte3.Orders.presentation.viewModels

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcorte3.Clients.data.repository.ClientsRepository
import com.example.appcorte3.Orders.data.model.OrderDetail
import com.example.appcorte3.Orders.data.model.ParticularDetailedOrder
import com.example.appcorte3.Orders.data.model.ProductForOrder
import com.example.appcorte3.Orders.data.repository.OrderProductRepository
import com.example.appcorte3.Orders.data.repository.OrderRepository
import com.example.appcorte3.Orders.domain.EditParticularOrderUseCase
import com.example.appcorte3.Orders.domain.GetParticularOrderUseCase
import com.example.appcorte3.Products.data.repository.ProductRepository
import com.example.appcorte3.core.data.local.Client.entities.ClientEntity
import com.example.appcorte3.core.data.local.Order.entities.OrderEntity
import com.example.appcorte3.core.data.local.OrderProducts.entitites.OrderProductsEntity
import com.example.appcorte3.core.data.local.Product.entities.ProductEntity
import com.example.appcorte3.core.data.local.Product.entities.UNIT
import com.example.appcorte3.core.hardware.BluetoothHelper
import com.example.appcorte3.core.storage.StorageManager
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.collections.Set

enum class FRACC_OPTIONS(val label: String) {
    NONE("1/1"),
    QUARTER("1/4"),
    HALF("1/2"),
    THREE_QUARTERS("1/4");

    override fun toString(): String {
        return label
    }
}

enum class FILTER_OPTIONS(val label: String) {
    NONE ("ninguno"),
    COMPLETED ("completado"),
    NO_COMPLETED ("no completado"),
    PAID ("pagado"),
    NO_PAID ("no pagado");
//    CLIENT ("nombre de cliente"),
//    DATE ("fecha")

    override fun toString(): String {
        return label
    }
}

class OrdersViewModel(
    context: Context,
    val navigateToAddOrder: () -> Unit,
    val navigateToParticularOrder: () -> Unit,
    val navigateBack: () -> Unit,
    val navigateToGeneralOrder: () -> Unit,
    val orderStorage: StorageManager<OrderDetail>,
    private val activity: Activity
) : ViewModel() {

    private val getParticularOrderUseCase = GetParticularOrderUseCase(context)
    private val orderRepository = OrderRepository(context)
    private val productsRepository = ProductRepository(context)
    private val clientRepository = ClientsRepository(context)
    private val orderProductRepository = OrderProductRepository(context)

    private val _orders = MutableLiveData<List<OrderDetail>>()
    private val _filter = MutableLiveData<FILTER_OPTIONS>(FILTER_OPTIONS.NONE)
    private val _filtering = MutableLiveData<Boolean>(false)

    val filtering : LiveData<Boolean> = _filtering
    val filter : LiveData<FILTER_OPTIONS> = _filter
    val orders : LiveData<List<OrderDetail>> = _orders

    suspend fun onChangeFilterOption(option: FILTER_OPTIONS) {
        if (option == FILTER_OPTIONS.NONE) {
            _filtering.value = false
        } else {
            _filtering.value = true
        }
        _filter.value = option
        getOrdersFiltered()

    }

    suspend fun getOrdersFiltered() {

        when(_filter.value) {
            FILTER_OPTIONS.NONE -> _orders.value = orderRepository.getAllOrders()
            FILTER_OPTIONS.COMPLETED -> _orders.value = orderRepository.getAllCompleted()
            FILTER_OPTIONS.NO_COMPLETED -> _orders.value = orderRepository.getAllPending()
            FILTER_OPTIONS.PAID -> _orders.value = orderRepository.getAllPaid()
            FILTER_OPTIONS.NO_PAID -> _orders.value = orderRepository.getAllNoPaid()
            else -> _orders.value = emptyList()
        }
    }

    // add order

    private var productsConst = emptyList<ProductEntity>()

    private val _clients = MutableLiveData<List<ClientEntity>>()
    private val _products = MutableLiveData<List<ProductEntity>>()
    private val _selectedProduct = MutableLiveData<ProductEntity>()
    private val _searchedProduct = MutableLiveData<String>()

    private val _clientSelected = MutableLiveData<ClientEntity?>()
    private val _fraccQuantity = MutableLiveData<FRACC_OPTIONS>(FRACC_OPTIONS.NONE)
    private val _fraccString = MutableLiveData<String>()
    private val _fraccDecimal = MutableLiveData<Int>(0)
    private val _productsForOrder = MutableLiveData<List<ProductForOrder>>()
    private val _quantity = MutableLiveData<Int>(1)
    private val _date = MutableLiveData<Long?>()
    private val _total = MutableLiveData<Float>()
    private val _searching = MutableLiveData<Boolean>(false)

    val quantity : LiveData<Int> = _quantity
    val fraccQuantity : LiveData<FRACC_OPTIONS> = _fraccQuantity
    val fraccString : LiveData<String> = _fraccString
    val clients : LiveData<List<ClientEntity>> = _clients
    val selectedProduct : LiveData<ProductEntity> = _selectedProduct
    val products : LiveData<List<ProductEntity>> = _products
    val total : LiveData<Float> = _total

    val clientSelected : LiveData<ClientEntity?> = _clientSelected
    val date : LiveData<Long?> = _date
    val productsForOrder : LiveData<List<ProductForOrder>> = _productsForOrder
    val searchedProduct : LiveData<String> = _searchedProduct
    val searching : LiveData<Boolean> = _searching

    fun onChangeClientId(value: ClientEntity) {
        _clientSelected.value = value
    }

    fun onChangeDate(value: Long){
        _date.value = value
    }

    fun onChangeSearchedProduct(value: String) {
        _searchedProduct.value = value

        if(_searchedProduct.value != "" && _searchedProduct.value != null && _products.value != null){
            val products = _products.value!!.filter { product -> product.name.startsWith(_searchedProduct.value!!, ignoreCase = true) }
            _searching.value = true
            _products.value = products
        } else {
            _searching.value = false
            _products.value = productsConst
        }
    }

    fun onSelectProduct(product: ProductEntity) {
        _selectedProduct.value = product
    }

    fun onSetFraccQuantity(fraccOption: FRACC_OPTIONS) {
        _fraccQuantity.value = fraccOption

        when (fraccOption) {
            FRACC_OPTIONS.NONE -> {
                _fraccString.value = "1/1"
                _fraccDecimal.value = 0
            }
            FRACC_OPTIONS.QUARTER -> {
                _fraccString.value = "1/4"
                _fraccDecimal.value = 25
            }
            FRACC_OPTIONS.HALF -> {
                _fraccString.value = "1/2"
                _fraccDecimal.value = 50
            }
            FRACC_OPTIONS.THREE_QUARTERS -> {
                _fraccString.value = "3/4"
                _fraccDecimal.value = 75
            }
        }
    }

    fun onAddProduct() {

        val products = _productsForOrder.value?.toMutableList() ?: mutableListOf()
        val product = _selectedProduct.value!!
        var quantity = _quantity.value!!.toFloat()
        if(product.unit == UNIT.FRACC) {
            quantity = "${_quantity.value}.${_fraccDecimal.value}".toFloat()
        }

        var isAdded = false

        products.forEach { singleProduct ->
            if (singleProduct.product == product) {
                singleProduct.product = product
                singleProduct.quantity = quantity
                isAdded = true
            }
        }

        if(!isAdded) {
            products.add(ProductForOrder(
                product,
                quantity
            ))
        }

        _productsForOrder.value = products
        refreshTotal()
    }

    private fun refreshTotal() {
        val products = _productsForOrder.value?.toMutableList() ?: mutableListOf()

        var total = 0f
        products.forEach({
            product ->
            run {
                total += product.product.price * product.quantity
            }
        })

        _total.value = total

    }

    fun onDeleteProductForOrder(product: ProductForOrder) {
        var products = _productsForOrder.value?.toMutableList() ?: mutableListOf()
        products.remove(product)
        _productsForOrder.value = products
        refreshTotal()
    }

    fun onIncrementQuantity() {
        val currentQ = _quantity.value ?: 1
        _quantity.value = currentQ + 1

    }

    fun onDecrementQuantity() {
        val currentQ = _quantity.value ?: 1
        if (currentQ > 1) {
            _quantity.value = currentQ - 1
        } else if (currentQ == 1 && selectedProduct.value != null && selectedProduct.value?.unit == UNIT.FRACC && fraccQuantity.value != FRACC_OPTIONS.NONE) {
            _quantity.value = 0
        }
    }

    suspend fun getAllClients() {
        _clients.value = clientRepository.getAllClients()
    }

    suspend fun getAllProducts() {
        productsConst = productsRepository.getAllProducts()
        _products.value = productsConst
    }

    fun resetInputs() {
        _date.value = null
        _clientSelected.value = null
        _total.value = 0f
        _quantity.value = 1
        _productsForOrder.value = emptyList()
        _searching.value = false
        _searchedProduct.value = ""
    }

    suspend fun insertNewOrder() {
        val orderId = UUID.randomUUID().toString()

        orderRepository.insertOrder(OrderEntity(
            id = orderId,
            date = date.value!!,
            clientId = clientSelected.value!!.id,
            total = total.value!!,
            completed = false,
            paid = false
        ))
        for (product in productsForOrder.value!!) {
            orderProductRepository.insertOrder(OrderProductsEntity(
                id = UUID.randomUUID().toString(),
                orderId = orderId,
                quantity = product.quantity,
                productId = product.product.id,
            ))
        }
        navigateBack()
        resetInputs()
    }

    // Particular order

    private val editParticularOrderUseCase = EditParticularOrderUseCase(context)

    fun onSelectParticular(order: OrderDetail) {
        orderStorage.saveInStorage(order)
        navigateToParticularOrder()
    }
}