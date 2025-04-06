package com.example.appcorte3.core.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.compose.runtime.traceEventEnd
import androidx.core.app.NotificationCompat
import com.example.appcorte3.Clients.data.model.ClientBody
import com.example.appcorte3.Clients.data.repository.ClientRemoteRepository
import com.example.appcorte3.Orders.data.model.OrderBody
import com.example.appcorte3.Orders.data.model.OrderProductBody
import com.example.appcorte3.Orders.data.model.OrderResponse
import com.example.appcorte3.Orders.data.repository.OrderProductsRemoteRepository
import com.example.appcorte3.Orders.data.repository.OrderRemoteRepository
import com.example.appcorte3.Orders.domain.GetNewsOrderProductsUseCase
import com.example.appcorte3.Orders.domain.GetNewsOrdersUseCase
import com.example.appcorte3.Products.data.model.ProductBody
import com.example.appcorte3.Products.data.repository.ProductRemoteRepository
import com.example.appcorte3.R
import com.example.appcorte3.core.data.local.Client.DAO.ClientDAO
import com.example.appcorte3.core.data.local.Order.DAO.OrderDAO
import com.example.appcorte3.core.data.local.Order.entities.OrderEntity
import com.example.appcorte3.core.data.local.OrderProducts.DAO.OrderProductDAO
import com.example.appcorte3.core.data.local.OrderProducts.entitites.OrderProductsEntity
import com.example.appcorte3.core.data.local.Product.DAO.ProductDAO
import com.example.appcorte3.core.data.local.appDatabase.AppDataBase
import com.example.appcorte3.core.data.local.appDatabase.DatabaseProvider
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SyncService : Service() {

    private val scope = CoroutineScope(Dispatchers.IO)
    private lateinit var db: AppDataBase

    private val clientRemoteRepository = ClientRemoteRepository()
    private val orderRemoteRepository = OrderRemoteRepository()
    private val productRemoteRepository = ProductRemoteRepository()
    private val orderProductsRemoteRepository = OrderProductsRemoteRepository()

    private lateinit var clientDAO: ClientDAO
    private lateinit var productDAO: ProductDAO
    private lateinit var orderDAO: OrderDAO
    private lateinit var orderProductDAO: OrderProductDAO

    override fun onCreate() {
        super.onCreate()
        db = DatabaseProvider.getDatabase(this)

        clientDAO = db.clientDAO()
        productDAO = db.productDAO()
        orderDAO = db.orderDAO()
        orderProductDAO = db.orderProductDAO()

        startForeground(1, createNotification())
        startSync()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }


    private fun startSync() {
        scope.launch {
            FirebaseMessaging.getInstance().subscribeToTopic("new_orders")
            while (true) {
                if(isNetworkAvailable()){
                    syncClients()
                    syncProducts()
                    syncNewOrders()
                    syncNewOrderProducts()
                }
                delay(10000) // Sincroniza cada 30 segundos
            }
        }
    }

    private suspend fun syncClients() {
        val clientsToSend = clientDAO.getNoSendedClients()

        for (clientToSend in clientsToSend) {
            clientRemoteRepository.syncClient(ClientBody(
                id = clientToSend.id,
                name = clientToSend.name,
                phone = clientToSend.phone
            ))
            clientDAO.markClientAsSended(clientToSend.id)
        }

    }

    private suspend fun syncProducts() {

        val productsToSend = productDAO.getNoSendedProducts()

        for (productToSend in productsToSend) {
            productRemoteRepository.insertProduct(ProductBody(
                id = productToSend.id,
                name = productToSend.name,
                price = productToSend.price,
                unit = productToSend.unit
            ))
            productDAO.markProductAsSended(productToSend.id)
        }
    }

    private suspend fun syncNewOrders() {
        val getNewsOrdersUseCase = GetNewsOrdersUseCase()
        val result = getNewsOrdersUseCase()
        var ordersToDownload = emptyList<OrderResponse>()

        result.onSuccess { data ->
            ordersToDownload = data
        }

        Log.d("API ORDERS", ordersToDownload.toString())

        for (order in ordersToDownload) {
            orderDAO.insertOrder(OrderEntity(
                id = order.id,
                date = order.date,
                total = order.total,
                clientId = order.client_id,
                completed = order.completed != 0,
                sended = true
            ))
            orderRemoteRepository.markOrderAsSended(order.id)
        }

        val ordersToSend = orderDAO.getNoSendedOrders()

        for (orderToSend in ordersToSend) {
            orderRemoteRepository.insertOrder(OrderBody(
                id = orderToSend.id,
                client_id = orderToSend.clientId,
                total = orderToSend.total,
                completed = orderToSend.completed,
                date = orderToSend.date
            ))
            orderDAO.markOrderAsSended(orderToSend.id)
        }
    }

    private suspend fun syncNewOrderProducts() {
        val getNewsOrderProductsUseCase = GetNewsOrderProductsUseCase()
        val orderProductsToDownload = getNewsOrderProductsUseCase()

        for (orderProduct in orderProductsToDownload) {
            orderProductDAO.insertOrderProduct(OrderProductsEntity(
                id = orderProduct.id,
                productId = orderProduct.product_id,
                orderId = orderProduct.order_id,
                quantity = orderProduct.quantity,
                sended = true
            ))
            orderProductsRemoteRepository.markOrderProductsAsSended(orderProduct.id)
        }

        val orderProductsToSend = orderProductDAO.getNoSendedOrderProducts()

        for(orderProductToSend in orderProductsToSend) {
            orderProductsRemoteRepository.insertOrderProduct(OrderProductBody(
                id = orderProductToSend.id,
                order_id = orderProductToSend.orderId,
                product_id = orderProductToSend.productId,
                quantity = orderProductToSend.quantity
            ))
            orderProductDAO.markOrderProductAsSended(orderProductToSend.id)
        }
    }

    private fun createNotification(): Notification {
        val channelId = "sync_service"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Sync Service", NotificationManager.IMPORTANCE_HIGH)
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Sincronización en segundo plano")
            .setContentText("Manteniendo los datos actualizados...")
            .setSmallIcon(R.drawable.logo)
            .build()
    }


    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}