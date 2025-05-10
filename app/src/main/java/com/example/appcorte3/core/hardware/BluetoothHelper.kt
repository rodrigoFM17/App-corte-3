package com.example.appcorte3.core.hardware

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.appcorte3.Orders.data.model.ParticularDetailedOrder
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

object BluetoothHelper {
    private const val REQUEST_BLUETOOTH_PERMISSIONS = 1

    fun checkAndRequestPermissions(activity: Activity): Boolean {
        val permissions = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN
        )

        val missingPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }

        return if (missingPermissions.isEmpty()) {
            true
        } else {
            ActivityCompat.requestPermissions(activity, missingPermissions.toTypedArray(), REQUEST_BLUETOOTH_PERMISSIONS)
            false
        }
    }

    fun getBluetoothAdapter(context: Context): BluetoothAdapter? {
        val bluetoothManager = context.getSystemService(BluetoothManager::class.java)
        return bluetoothManager?.adapter
    }

    fun getPairedDevices(context: Context): Set<BluetoothDevice>? {
        val bluetoothAdapter = getBluetoothAdapter(context)
        Log.d("Bluetooth", bluetoothAdapter.toString())

        return if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            bluetoothAdapter?.bondedDevices
        } else {
            Log.d("Bluetooth", "No tiene permisos")
            null
        }
    }

    fun connectToPrinter(device: BluetoothDevice, context: Context): BluetoothSocket? {
        return try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Permiso Bluetooth no concedido", Toast.LENGTH_SHORT).show()
                return null
            }

            val socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
            socket.connect()
            socket
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun printOrderTicket(socket: BluetoothSocket?, particularDetailedOrder: ParticularDetailedOrder) {
        try {
            val outputStream: OutputStream? = socket?.outputStream
            if (outputStream == null) return

            val ESC = 0x1B.toByte()
            val GS = 0x1D.toByte()

            outputStream.write(byteArrayOf(ESC, '@'.code.toByte()))


            outputStream.write(byteArrayOf(ESC, 'a' .code.toByte(), 1)) // centrar texto
            outputStream.write(byteArrayOf(ESC, '!' .code.toByte(), 0x01)) // doble ancho de texto
            outputStream.write("TICKET DE PEDIDO\n\n".toByteArray(Charsets.UTF_8))
            outputStream.write(byteArrayOf(GS, '!' .code.toByte(), 0x00)) // Reset tamaño

            outputStream.write(byteArrayOf(ESC, 'a' .code.toByte(), 0)) // alinear a la izquierda
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            val fechaActual = dateFormat.format(Date())
            outputStream.write("Cliente: ${particularDetailedOrder.clientName}\nFecha: $fechaActual\n".toByteArray(Charsets.UTF_8))

            outputStream.write("Producto      Cant Precio Subtotal\n".toByteArray(Charsets.UTF_8))
            outputStream.write("--------------------------------\n".toByteArray(Charsets.UTF_8))

            for (product in particularDetailedOrder.orderProducts) {
                val name = product.name.take(13).padEnd(13) // Ajustamos nombre para dejar espacio
                val qty = product.quantity.toString().padStart(4)
                val price = "%.2f".format(product.price).padStart(7)
                val subtotal = "%.2f".format(product.quantity * product.price).padStart(9)
                outputStream.write("$name$qty$price$subtotal\n".toByteArray(Charsets.UTF_8))
            }

            outputStream.write("--------------------------------\n".toByteArray(Charsets.UTF_8))

            // Total en negrita
            outputStream.write(byteArrayOf(ESC, 'E'.code.toByte(), 1)) // Negrita ON
            outputStream.write("TOTAL: $%.2f\n".format(particularDetailedOrder.total).toByteArray(Charsets.UTF_8))
            outputStream.write(byteArrayOf(ESC, 'E'.code.toByte(), 0)) // Negrita OFF

            // Mensaje final centrado
            outputStream.write(byteArrayOf(ESC, 'a'.code.toByte(), 1)) // Centrar
            outputStream.write("\n¡Gracias por su compra!\n\n".toByteArray(Charsets.UTF_8))

            outputStream.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun closeConnection(socket: BluetoothSocket?) {
        try {
            socket?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}