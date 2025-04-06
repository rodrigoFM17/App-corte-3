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

    fun printOrderTicket(socket: BluetoothSocket?, goal: GoalDTO) {
        try {
            val outputStream: OutputStream? = socket?.outputStream
            if (outputStream == null) return

            val ESC = 0x1B.toByte()
            val GS = 0x1D.toByte()

            outputStream.write(byteArrayOf(ESC, '@'.code.toByte()))


            outputStream.write(byteArrayOf(GS, '!' .code.toByte(), 0x11))
            outputStream.write("${goal.title}\n".toByteArray(Charsets.UTF_8))
            outputStream.write(byteArrayOf(GS, '!' .code.toByte(), 0x00)) // Reset tamaño

            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            val fechaActual = dateFormat.format(Date())
            outputStream.write("Enhorabuena!! has completado tu meta, continua asi y lograras todo lo que te propongas\ncomplecion: $fechaActual\n".toByteArray(Charsets.UTF_8))

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