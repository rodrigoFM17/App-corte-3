package com.example.appcorte3.Orders.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appcorte3.Clients.data.repository.ClientsRepository
import com.example.appcorte3.Orders.data.model.ProductForOrder
import com.example.appcorte3.Orders.data.repository.OrderRepository
import com.example.appcorte3.Products.data.repository.ProductRepository
import com.example.appcorte3.core.data.local.Client.entities.ClientEntity
import com.example.appcorte3.core.data.local.Order.entities.OrderEntity
import com.example.appcorte3.core.data.local.Product.entities.ProductEntity

class OrdersViewModel(context: Context, val navigateToAddOrder: () -> Unit) : ViewModel() {

    private val orderRepository = OrderRepository(context)
    private val productsRepository = ProductRepository(context)
    private val clientRepository = ClientsRepository(context)

    private val _orders = MutableLiveData<List<OrderEntity>>()

    val orders : LiveData<List<OrderEntity>> = _orders

    suspend fun getAllPendingOrders() {
        _orders.value = orderRepository.getAllPending()
    }

    // add order

    private val _clients = MutableLiveData<List<ClientEntity>>()
    private val _products = MutableLiveData<List<ProductEntity>>()

    private val _clientSelected = MutableLiveData<ClientEntity>()
    private val _productsForOrder = MutableLiveData<List<ProductForOrder>>()
    private val _quantity = MutableLiveData<Int>()
    private val _date = MutableLiveData<Long>()
    private val _total = MutableLiveData<Float>()

    val quantity : LiveData<Int> = _quantity
    val clients : LiveData<List<ClientEntity>> = _clients
    val products : LiveData<List<ProductEntity>> = _products
    val total : LiveData<Float> = _total

    val clientSelected : LiveData<ClientEntity> = _clientSelected
    val date : LiveData<Long> = _date
    val productsForOrder : LiveData<List<ProductForOrder>> = _productsForOrder

    fun onChangeClientId(value: ClientEntity) {
        _clientSelected.value = value
    }

    fun onChangeDate(value: Long){
        _date.value = value
    }

    fun onAddProduct(product: ProductEntity, quantity: Int) {
        val products = _productsForOrder.value?.toMutableList() ?: mutableListOf()

        products.add(ProductForOrder(
            product,
            quantity
        ))

        _productsForOrder.value = products
        refreshTotal()
    }

    private fun refreshTotal() {
        val products = _productsForOrder.value?.toMutableList() ?: mutableListOf()

        var total = 0f
        products.forEach({
            product ->
            run {
                total += product.product.price
            }
        })

        _total.value = total

    }

    fun onIncrementQuantity() {
        val currentQ = _quantity.value ?: 1
        _quantity.value = currentQ + 1

    }

    fun onDecrementQuantity() {
        val currentQ = _quantity.value ?: 1
        if (currentQ > 1) {
            _quantity.value = currentQ - 1
        }
    }

    suspend fun getAllClients() {
        _clients.value = clientRepository.getAllClients()
    }

    suspend fun getAllProducts() {
        _products.value = productsRepository.getAllProducts()
    }

    suspend fun insertOrder(order: OrderEntity) {
        orderRepository.insertOrder(order)
    }


}