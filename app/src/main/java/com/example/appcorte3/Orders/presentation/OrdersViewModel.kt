package com.example.appcorte3.Orders.presentation

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
import com.example.appcorte3.Clients.data.repository.ClientsRepository
import com.example.appcorte3.Orders.data.model.OrderDetail
import com.example.appcorte3.Orders.data.model.ParticularDetailedOrder
import com.example.appcorte3.Orders.data.model.ProductForOrder
import com.example.appcorte3.Orders.data.repository.OrderProductRepository
import com.example.appcorte3.Orders.data.repository.OrderRepository
import com.example.appcorte3.Orders.domain.GetParticularOrderUseCase
import com.example.appcorte3.Products.data.repository.ProductRepository
import com.example.appcorte3.core.data.local.Client.entities.ClientEntity
import com.example.appcorte3.core.data.local.Order.entities.OrderEntity
import com.example.appcorte3.core.data.local.OrderProducts.entitites.OrderProductsEntity
import com.example.appcorte3.core.data.local.Product.entities.ProductEntity
import com.example.appcorte3.core.hardware.BluetoothHelper
import kotlin.collections.Set

class OrdersViewModel(
    context: Context,
    val navigateToAddOrder: () -> Unit,
    val navigateToParticularOrder: () -> Unit,
    private val activity: Activity
) : ViewModel() {

    private val getParticularOrderUseCase = GetParticularOrderUseCase(context)
    private val orderRepository = OrderRepository(context)
    private val productsRepository = ProductRepository(context)
    private val clientRepository = ClientsRepository(context)
    private val orderProductRepository = OrderProductRepository(context)

    private val _orders = MutableLiveData<List<OrderDetail>>()

    val orders : LiveData<List<OrderDetail>> = _orders

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
                total += product.product.price * product.quantity
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

    suspend fun insertOrderProduct(orderProductsEntity: OrderProductsEntity) {
        orderProductRepository.insertOrder(orderProductsEntity)
    }

    // Particular order

    var socket: BluetoothSocket? = null

    private val _bluetoothDevices = MutableLiveData<Set<BluetoothDevice>>(emptySet<BluetoothDevice>())
    private val _showDevices = MutableLiveData<Boolean>()

    val showDevices : LiveData<Boolean> = _showDevices
    val bluetoothDevices: LiveData<Set<BluetoothDevice>> = _bluetoothDevices

    fun setPairedDevices (context: Context) {
        if(BluetoothHelper.checkAndRequestPermissions(activity)){
            _bluetoothDevices.value = BluetoothHelper.getPairedDevices(context)
            Log.d("Bluetooth", _bluetoothDevices.value?.size.toString())
        }
    }

    fun connectToPrinter (context: Context, device: BluetoothDevice) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            socket = BluetoothHelper.connectToPrinter(device, context)
            Toast.makeText(context, "Conectado a ${device.name}", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permiso de Bluetooth no concedido", Toast.LENGTH_SHORT).show()
        }
    }

    private val _particularOrderId = MutableLiveData<String>()
    private val _particularOrder = MutableLiveData<ParticularDetailedOrder>()

    val particularDetailedOrder : LiveData<ParticularDetailedOrder> = _particularOrder
    val particularOrderId : LiveData<String> = _particularOrderId

    fun onSelectParticular(orderId: String) {
        _particularOrderId.value = orderId
    }

    suspend fun getParticularOrder(orderId: String) {
        val particularOrder = getParticularOrderUseCase(orderId)
        _particularOrder.value = particularOrder
    }

    fun printTicket(context: Context, particularDetailedOrder: ParticularDetailedOrder){

        if (socket == null) {
             setPairedDevices(context)
            _showDevices.value = true
        } else {
            BluetoothHelper.printOrderTicket(socket, particularDetailedOrder)
        }
    }

}