package com.example.project.model.old

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat.startActivity
import com.example.project.model.old.BluetoothDeviceReceiver
import com.example.project.model.old.BluetoothStateReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import java.io.IOException
import java.util.UUID

typealias BlueToothClientDevice = BluetoothDevice

sealed interface ConnectionResult {
    object ConnectionEstablished: ConnectionResult
    data class Error(val message: String): ConnectionResult
}

data class BluetoothDevice(
    val name: String?,
    val address: String
)

@RequiresApi(Build.VERSION_CODES.M)
@SuppressLint("MissingPermission")

class BluetoothClient(
    private val context: Context,
    private val adapter: BluetoothAdapter? = context.getSystemService(BluetoothManager::class.java).adapter
) {
    
    // Device
    val pairedDevices: MutableList<BluetoothDevice> = mutableStateListOf()
    val visibleDevices: MutableList<BluetoothDevice> = mutableStateListOf()
    var isConnected: Boolean by mutableStateOf(false)
    var currentDevice: BluetoothDevice? by mutableStateOf(null)
    private val uuid: UUID = UUID.fromString("e646db15-49ef-44c6-9341-b207b21d0ab7")
    
    // Receivers
    private val deviceReceiver = BluetoothDeviceReceiver { device ->
        val newDevice = BluetoothDevice(device.name, device.address)
        if (!visibleDevices.contains(newDevice)) visibleDevices.add(newDevice)
    }
    private val stateReceiver = BluetoothStateReceiver { isConnected, device ->
        if (adapter?.bondedDevices?.contains(device) == true) {
            currentDevice = BluetoothDevice(device.name, device.address)
            this.isConnected = isConnected
        }
        else Log.d("TAG", "cock: ")
    }

    // Sockets
    private var serverSocket: BluetoothServerSocket? = null
    private var clientSocket: BluetoothSocket? = null


    inner class ProxyListener(): BluetoothProfile.ServiceListener {
        override fun onServiceConnected(profile: Int, proxy: BluetoothProfile?) {
            TODO("Not yet implemented")
        }

        override fun onServiceDisconnected(profile: Int) {
            TODO("Not yet implemented")
        }

    }

    private val listener: ProxyListener = ProxyListener()


    @RequiresApi(Build.VERSION_CODES.P)
    val proxy = adapter?.getProfileProxy(context, listener, BluetoothProfile.HID_DEVICE)


    init {
        context.registerReceiver(stateReceiver, IntentFilter().apply {
            addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
            addAction(android.bluetooth.BluetoothDevice.ACTION_ACL_CONNECTED)
            addAction(android.bluetooth.BluetoothDevice.ACTION_ACL_DISCONNECTED)
        })

    }

    private fun hasPermissions(vararg permissions: String): Boolean {
        permissions.forEach { if (context.checkSelfPermission(it) == PackageManager.PERMISSION_DENIED) return false }
        return true
    }

//    fun startDeviceDiscovery() {
//        if (hasPermissions(android.Manifest.permission.BLUETOOTH_SCAN)) {
//            context.registerReceiver(
//                deviceReceiver,
//                IntentFilter(android.bluetooth.BluetoothDevice.ACTION_FOUND)
//            )
//            updatePairedDevices()
//            adapter?.startDiscovery()
//        }
//    }

//    fun stopDeviceDiscovery() {
//        if (hasPermissions(android.Manifest.permission.BLUETOOTH_SCAN)) {
//            adapter?.cancelDiscovery()
//        }
//    }

    fun updatePairedDevices() {
        if (hasPermissions(android.Manifest.permission.BLUETOOTH_CONNECT)) {
            adapter?.bondedDevices?.forEach { device ->
                pairedDevices.add(
                    BluetoothDevice(
                    device.name, device.address)
                )
            }
        }
    }

//    fun freeReceiverData() {
//        context.unregisterReceiver(deviceReceiver)
//        context.unregisterReceiver(stateReceiver)
//        closeConnection()
//    }

//    fun enableDiscoverable() {
//        val discoverableIntent: Intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
//            putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
//        }
//        startActivity(discoverableIntent)
//    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun startDiscoverable() {
        val discoverableIntent: Intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
            putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300) // 300 seconds
        }
        startActivity(context, discoverableIntent, null)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun startServer(): Flow<ConnectionResult> {
        return flow<ConnectionResult> {
            if (hasPermissions(android.Manifest.permission.BLUETOOTH_CONNECT)) {

                startDiscoverable()

                serverSocket = adapter?.listenUsingL2capChannel()
                var serverRunning = true
                while (serverRunning) {
                    try {
                        clientSocket = serverSocket?.accept()
                    } catch (e: IOException) {
                        Log.d("TAG", "startServer: cock")
                        serverRunning = false
                    }


                    Log.d("TAG", "startServer: $clientSocket connected")
                    emit(ConnectionResult.ConnectionEstablished)
                    clientSocket?.let { serverSocket?.close() }
                }
            }
        }.onCompletion {
            closeConnection()
        }.flowOn(Dispatchers.IO)
    }

//    fun connectToDevice(device: BluetoothDevice): Flow<ConnectionResult> {
//        return flow {
//            if (hasPermissions(android.Manifest.permission.BLUETOOTH_CONNECT)) {
//
//                val currentDevice: android.bluetooth.BluetoothDevice? = adapter?.getRemoteDevice(device.address)
//                clientSocket = currentDevice?.createRfcommSocketToServiceRecord(uuid) // Pairing dialogue
//                stopDeviceDiscovery()
//
//                // Socket connection
//                clientSocket?.let { socket ->
//                    try {
//                        socket.connect()
//                        emit(ConnectionResult.ConnectionEstablished)
//                    } catch (e: IOException) {
//                        socket.close()
//                        clientSocket = null
//                        emit(ConnectionResult.Error("Connection was interrupted"))
//                    }
//                }
//            }
//        }.onCompletion {
//            closeConnection()
//        }.flowOn(Dispatchers.IO)
//    }

    fun closeConnection() {
        clientSocket?.close()
        serverSocket?.close()
        clientSocket = null
        serverSocket = null
    }

    fun send(keys: List<Int>) {

    }
}