package com.example.appcorte3.core.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.appcorte3.Clients.data.repository.ClientRemoteRepository
import com.example.appcorte3.Orders.data.repository.OrderRemoteRepository
import com.example.appcorte3.Products.data.repository.ProductRemoteRepository
import com.example.appcorte3.core.data.local.appDatabase.AppDataBase
import com.example.appcorte3.core.data.local.appDatabase.DatabaseProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SyncService : Service() {

    private val scope = CoroutineScope(Dispatchers.IO)
    private lateinit var db: AppDataBase

    private val clientRemoteRepository = ClientRemoteRepository()
    private val orderRemoteRepository = OrderRemoteRepository()
    private val productRemoteRepository = ProductRemoteRepository()

    override fun onCreate() {
        super.onCreate()
        db = DatabaseProvider.getDatabase(this)

        startForeground(1, createNotification())
        startSync()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startSync() {
        scope.launch {
            while (true) {
                syncClients()
                syncProducts()
                downloadNewOrders()
                uploadPendingOrders()
                delay(10000) // Sincroniza cada 30 segundos
            }
        }
    }

    private suspend fun syncClients() {
        val clientsToSend = db.clientDAO().getNoSendedClients()

        if(clientsToSend.isNotEmpty()){
            clientRemoteRepository

        }

        try {


            val response = fetchFromServer("$serverUrl/clients")
            val clientsArray = JSONArray(response)

            for (i in 0 until clientsArray.length()) {
                val clientJson = clientsArray.getJSONObject(i)

            }
        } catch (e: Exception) {
            Log.e("SyncService", "Error sincronizando clientes: ${e.message}")
        }
    }

    private fun syncProducts() {
        try {
            val response = fetchFromServer("$serverUrl/products")
            val productsArray = JSONArray(response)

            for (i in 0 until productsArray.length()) {
                val productJson = productsArray.getJSONObject(i)
                db.productDao().insertOrUpdate(productJson.getInt("id"), productJson.getString("name"), productJson.getDouble("price").toFloat(), productJson.getString("unit"))
            }
        } catch (e: Exception) {
            Log.e("SyncService", "Error sincronizando productos: ${e.message}")
        }
    }

    private fun downloadNewOrders() {
        try {
            val response = fetchFromServer("$serverUrl/orders")
            val ordersArray = JSONArray(response)

            for (i in 0 until ordersArray.length()) {
                val orderJson = ordersArray.getJSONObject(i)
                val orderId = orderJson.getInt("id")

                if (db.orderDao().getOrderById(orderId) == null) {
                    val order = OrderEntity(
                        id = orderId,
                        clientId = orderJson.getInt("client_id"),
                        total = orderJson.getDouble("total").toFloat(),
                        date = orderJson.getLong("date"),
                        completed = orderJson.getBoolean("completed"),
                        sended = true
                    )
                    db.orderDao().insert(order)

                    val productsArray = orderJson.getJSONArray("products")
                    for (j in 0 until productsArray.length()) {
                        val productJson = productsArray.getJSONObject(j)
                        val orderProduct = OrderProductsEntity(
                            orderId = orderId,
                            productId = productJson.getInt("product_id"),
                            quantity = productJson.getInt("quantity"),
                            sended = true
                        )
                        db.orderProductsDao().insert(orderProduct)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("SyncService", "Error descargando pedidos: ${e.message}")
        }
    }

    private fun uploadPendingOrders() {
        val pendingOrders = db.orderDao().getUnsendedOrders()

        for (order in pendingOrders) {
            val orderJson = JSONObject()
            orderJson.put("client_id", order.clientId)
            orderJson.put("total", order.total)
            orderJson.put("date", order.date)
            orderJson.put("completed", order.completed)

            val productsArray = JSONArray()
            val orderProducts = db.orderProductsDao().getByOrderId(order.id)
            for (product in orderProducts) {
                val productJson = JSONObject()
                productJson.put("product_id", product.productId)
                productJson.put("quantity", product.quantity)
                productsArray.put(productJson)
            }

            orderJson.put("products", productsArray)

            try {
                postToServer("$serverUrl/orders", orderJson.toString())
                db.orderDao().markAsSended(order.id)
            } catch (e: Exception) {
                Log.e("SyncService", "Error subiendo pedido: ${e.message}")
            }
        }
    }

    private fun createNotification(): Notification {
        val channelId = "sync_service"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Sync Service", NotificationManager.IMPORTANCE_LOW)
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Sincronización en segundo plano")
            .setContentText("Manteniendo los datos actualizados...")
            .setSmallIcon(R.drawable.ic_sync)
            .build()
    }

    private fun fetchFromServer(url: String): String {
        val connection = URL(url).openConnection() as HttpURLConnection
        return connection.inputStream.bufferedReader().readText()
    }

    private fun postToServer(url: String, jsonBody: String) {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true
        connection.outputStream.write(jsonBody.toByteArray())
        connection.inputStream.bufferedReader().readText()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}