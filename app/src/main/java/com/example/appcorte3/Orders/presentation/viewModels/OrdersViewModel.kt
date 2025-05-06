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
import com.example.appcorte3.core.data.local.Product.entities.UNIT
import com.example.appcorte3.core.hardware.BluetoothHelper
import kotlin.collections.Set

enum class FRACC_OPTIONS {
    NONE,
    QUARTER,
    HALF,
    THREE_QUARTERS
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

    private val _clients = MutableLiveData<List<ClientEntity>>()
    private val _products = MutableLiveData<List<ProductEntity>>()
    private val _selectedProduct = MutableLiveData<ProductEntity>()

    private val _clientSelected = MutableLiveData<ClientEntity>()
    private val _fraccQuantity = MutableLiveData<FRACC_OPTIONS>(FRACC_OPTIONS.NONE)
    private val _fraccString = MutableLiveData<String>()
    private val _fraccDecimal = MutableLiveData<Int>(0)
    private val _productsForOrder = MutableLiveData<List<ProductForOrder>>()
    private val _quantity = MutableLiveData<Int>(1)
    private val _date = MutableLiveData<Long>()
    private val _total = MutableLiveData<Float>()

    val quantity : LiveData<Int> = _quantity
    val fraccQuantity : LiveData<FRACC_OPTIONS> = _fraccQuantity
    val fraccString : LiveData<String> = _fraccString
    val clients : LiveData<List<ClientEntity>> = _clients
    val selectedProduct : LiveData<ProductEntity> = _selectedProduct
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

    suspend fun onChangeCompleteStatus () {
        if(_particularOrder.value != null) {
            orderRepository.changeCompleteStatus(_particularOrder.value!!.id, !_particularOrder.value!!.completed)
            getParticularOrder(_particularOrder.value!!.id)
        }
    }

    suspend fun onChangePaidStatus () {
        if(_particularOrder.value != null) {
            orderRepository.changePaidStatus(_particularOrder.value!!.id, !_particularOrder.value!!.paid)
            getParticularOrder(_particularOrder.value!!.id)
        }
    }

    suspend fun onDeleteOrder(orderId: String) {
        orderRepository.deleteOrder(orderId)
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