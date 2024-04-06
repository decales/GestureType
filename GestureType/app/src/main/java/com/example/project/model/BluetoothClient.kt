package com.example.project.model

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.project.Manifest
import java.security.Permission

typealias BlueToothClientDevice = BluetoothDevice

data class BluetoothDevice(
    val name: String,
    val address: String
)

@RequiresApi(Build.VERSION_CODES.M)
@SuppressLint("MissingPermission")
class BluetoothClient(
    private val context: Context
) {
    val manager: BluetoothManager = context.getSystemService(BluetoothManager::class.java)
    val scannedDevices: MutableList<BluetoothDevice> = mutableStateListOf()
    var connectedDevice: BluetoothDevice? by mutableStateOf(null)
    val deviceReceiver = BluetoothDeviceReceiver { device ->
        val newDevice: BluetoothDevice = BluetoothDevice(device.name, device.address)


    }

    fun hasPermission(permission: String): Boolean {
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    fun startDeviceDiscovery() {
        manager.adapter.startDiscovery()
        context.registerReceiver(
            deviceReceiver,
            IntentFilter(android.bluetooth.BluetoothDevice.ACTION_FOUND)
        )
    }

    fun stopDeviceDiscovery() {
        manager.adapter.cancelDiscovery()
    }

    fun freeReceiverData() {
        context.unregisterReceiver(deviceReceiver)
    }

    fun send(keys: List<Int>) {

    }
}