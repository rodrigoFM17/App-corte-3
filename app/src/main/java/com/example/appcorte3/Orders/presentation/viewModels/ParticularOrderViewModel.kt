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
import com.example.appcorte3.MainActivity
import com.example.appcorte3.Orders.data.model.OrderDetail
import com.example.appcorte3.Orders.data.model.ParticularDetailedOrder
import com.example.appcorte3.Orders.data.model.ProductForOrder
import com.example.appcorte3.Orders.data.repository.OrderRepository
import com.example.appcorte3.Orders.domain.EditParticularOrderUseCase
import com.example.appcorte3.Orders.domain.GetParticularOrderUseCase
import com.example.appcorte3.Products.data.repository.ProductRepository
import com.example.appcorte3.core.data.local.Client.entities.ClientEntity
import com.example.appcorte3.core.data.local.Product.entities.ProductEntity
import com.example.appcorte3.core.data.local.Product.entities.UNIT
import com.example.appcorte3.core.hardware.BluetoothHelper
import com.example.appcorte3.core.storage.StorageManager
import kotlinx.coroutines.flow.flow

class ParticularOrderViewModel(
    context: Context,
    val orderStorageManager: StorageManager<OrderDetail>,
    val navigateBack: () -> Unit,
    private val activity: Activity
) : ViewModel() {

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

    private var productConst = emptyList<ProductEntity>()

    private val editParticularOrderUseCase = EditParticularOrderUseCase(context)
    private val getParticularOrderUseCase = GetParticularOrderUseCase(context)
    private val productsRepository = ProductRepository(context)
    private val orderRepository = OrderRepository(context)
    private val clientRepository = ClientsRepository(context)

    private val _clients = MutableLiveData<List<ClientEntity>>(emptyList())
    private var _clientId = MutableLiveData<String>()
    private var _clientName = MutableLiveData<String>()
    private val _completed = MutableLiveData<Boolean>(false)
    private val _paid = MutableLiveData<Boolean>(false)
    private val _particularOrder = MutableLiveData<ParticularDetailedOrder>()
    private val _editing = MutableLiveData<Boolean>(false)
    private val _searchingProducts = MutableLiveData<Boolean>()
    private val _searchedProduct = MutableLiveData<String>()
    private val _products = MutableLiveData<List<ProductEntity>>()
    private val _productsParticularOrder = MutableLiveData<List<ProductForOrder>>()
    private val _quantity = MutableLiveData<Int>(1)
    private val _total = MutableLiveData<Float>()
    private val _selectedProduct = MutableLiveData<ProductEntity>()
    private val _fraccQuantity = MutableLiveData<FRACC_OPTIONS>(FRACC_OPTIONS.NONE)
    private val _fraccDecimal = MutableLiveData<Int>(0)

    val particularDetailedOrder : LiveData<ParticularDetailedOrder> = _particularOrder
    val products : LiveData<List<ProductEntity>> = _products
    val clients : LiveData<List<ClientEntity>> = _clients
    val editing : LiveData<Boolean> = _editing
    val quantity : LiveData<Int> = _quantity
    val total : LiveData<Float> = _total
    val clientName : LiveData<String> = _clientName
    val completed : LiveData<Boolean> = _completed
    val paid : LiveData<Boolean> = _paid
    val selectedProduct : LiveData<ProductEntity> = _selectedProduct
    val searchingProducts : LiveData<Boolean> = _searchingProducts
    val searchedProduct : LiveData<String> = _searchedProduct
    val fraccQuantity : LiveData<FRACC_OPTIONS> = _fraccQuantity
    val productsParticularOrder : LiveData<List<ProductForOrder>> =_productsParticularOrder

    suspend fun getParticularOrder() {
        val order = getParticularOrderUseCase(orderStorageManager.getObjectInStorage().id)
        _particularOrder.value = order
        productConst = productsRepository.getAllProducts()
        _clients.value = clientRepository.getAllClients()
        _clientId.value = order.client_id
        _clientName.value = order.clientName
        _completed.value = order.completed
        _paid.value = order.paid
        _products.value = productConst
        _productsParticularOrder.value = _particularOrder.value!!.orderProducts.map {
                product -> ProductForOrder(
            product = ProductEntity(
                id = product.productId,
                name = product.name,
                price = product.price,
                unit = product.unit
            ),
            quantity = product.quantity
        )
        }
        Log.d("PRODUCTS BD", productConst.toString())
        Log.d("PRODUCTS GETTED", _productsParticularOrder.value.toString())
        refreshTotal()
    }

    private fun refreshTotal() {
        val products = _productsParticularOrder.value?.toMutableList() ?: mutableListOf()

        var total = 0f
        products.forEach({
                product ->
            run {
                total += product.product.price * product.quantity
            }
        })

        _total.value = total
    }

    fun onChangeSearchedProduct(value: String) {
        _searchedProduct.value = value

        if(_searchedProduct.value != "" && _searchedProduct.value != null && _products.value != null){
            val products = _products.value!!.filter { product -> product.name.startsWith(_searchedProduct.value!!, ignoreCase = true) }
            _searchingProducts.value = true
            _products.value = products
        } else {
            _searchingProducts.value = false
            _products.value = productConst
        }
    }

    fun onSetFraccQuantity(fraccOption: FRACC_OPTIONS) {
        _fraccQuantity.value = fraccOption

        when (fraccOption) {
            FRACC_OPTIONS.NONE -> {
                _fraccDecimal.value = 0
            }
            FRACC_OPTIONS.QUARTER -> {
                _fraccDecimal.value = 25
            }
            FRACC_OPTIONS.HALF -> {
                _fraccDecimal.value = 50
            }
            FRACC_OPTIONS.THREE_QUARTERS -> {
                _fraccDecimal.value = 75
            }
        }
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

    fun onSelectProduct(product: ProductEntity) {
        Log.d("PRODUCT", product.name)
        _selectedProduct.value = product
        Log.d("PRODUCT", _selectedProduct.value.toString())
    }

    fun onChangeEditing(value: Boolean) {
        _editing.value = value
    }

    fun onChangeClient(client: ClientEntity) {
        var particularOrder = _particularOrder.value
        if (particularOrder != null) {
            particularOrder.clientName = client.name
            particularOrder.client_id = client.id
            _clientId.value = client.id
            _clientName.value = client.name
            _particularOrder.value = particularOrder
        }
    }

    fun onChangeDateParticular(date : Long) {
        val order = _particularOrder.value
        if(order != null) {
            order.date = date
        }
    }

    fun onAddProductParticular() {

        val products = _productsParticularOrder.value?.toMutableList() ?: mutableListOf()
        val product = _selectedProduct.value!!
        var quantity = _quantity.value!!.toFloat()
        if(product.unit == UNIT.FRACC) {
            quantity = "${_quantity.value}.${_fraccDecimal.value}".toFloat()
        }

        var isAdded = false

        products.forEach { singleProduct ->
            if (singleProduct.product.id == product.id) {
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

        _productsParticularOrder.value = products
        refreshTotal()
    }

    fun onDeleteProductParticularOrder(product: ProductForOrder) {
        var products = _productsParticularOrder.value?.toMutableList() ?: mutableListOf()
        products = products.filter { productS -> productS.product.id != product.product.id  } as MutableList<ProductForOrder>
        _productsParticularOrder.value = products
        refreshTotal()
    }

    suspend fun onChangeCompleteStatus () {
        if(_particularOrder.value != null) {
            orderRepository.changeCompleteStatus(_particularOrder.value!!.id, !_particularOrder.value!!.completed)
            val order = _particularOrder.value
            order!!.completed = !_particularOrder.value!!.completed
            _completed.value = order.completed
            _particularOrder.value = order
        }
    }

    suspend fun onChangePaidStatus () {
        if(_particularOrder.value != null) {
            orderRepository.changePaidStatus(_particularOrder.value!!.id, !_particularOrder.value!!.paid)
            _particularOrder.value!!.paid = !_particularOrder.value!!.paid
            _paid.value = _particularOrder.value!!.paid
        }
    }

    suspend fun onDeleteOrder() {
        orderRepository.deleteOrder(_particularOrder.value!!.id)
        navigateBack()
    }

    suspend fun onEditOrder() {
        if(_particularOrder.value != null && _productsParticularOrder.value != null && _total.value != null) {
            Log.d("products", _productsParticularOrder.value.toString())
            Log.d("particular order", _particularOrder.value!!.id)
            editParticularOrderUseCase(_particularOrder.value!!, _productsParticularOrder.value!!, _total.value!!)
            resetInputs()
        }
    }

    fun printTicket(context: Context, particularDetailedOrder: ParticularDetailedOrder){

        if (socket == null) {
            setPairedDevices(context)
            _showDevices.value = true
        } else {
            BluetoothHelper.printOrderTicket(socket, particularDetailedOrder)
        }
    }

    fun resetInputs() {

    }
}