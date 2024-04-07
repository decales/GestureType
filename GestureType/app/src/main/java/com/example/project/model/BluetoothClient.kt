package com.example.project.model

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf

typealias BlueToothClientDevice = BluetoothDevice

data class BluetoothDevice(
    val name: String?,
    val address: String
)

@RequiresApi(Build.VERSION_CODES.M)
@SuppressLint("MissingPermission")
class BluetoothClient(
    private val context: Context
) {
    private val manager: BluetoothManager = context.getSystemService(BluetoothManager::class.java)
    val pairedDevices: MutableList<BluetoothDevice> = mutableStateListOf()
    val visibleDevices: MutableList<BluetoothDevice> = mutableStateListOf()
    private val deviceReceiver = BluetoothDeviceReceiver { device ->
        val newDevice = BluetoothDevice(device.name, device.address)
        if (!visibleDevices.contains(newDevice)) visibleDevices.add(newDevice)
    }

    private fun hasPermissions(vararg permissions: String): Boolean {
        permissions.forEach { if (context.checkSelfPermission(it) == PackageManager.PERMISSION_DENIED) return false }
        return true
    }

    fun startDeviceDiscovery() {
        if (hasPermissions(android.Manifest.permission.BLUETOOTH_SCAN)) {
            context.registerReceiver(
                deviceReceiver,
                IntentFilter(android.bluetooth.BluetoothDevice.ACTION_FOUND)
            )
            updatePairedDevices()
            manager.adapter?.startDiscovery()
        }
    }

    fun stopDeviceDiscovery() {
        if (hasPermissions(android.Manifest.permission.BLUETOOTH_SCAN)) {
            manager.adapter?.cancelDiscovery()
        }
    }

    fun updatePairedDevices() {
        if (hasPermissions(android.Manifest.permission.BLUETOOTH_CONNECT)) {
            manager.adapter?.bondedDevices?.forEach { device ->
                pairedDevices.add(BluetoothDevice(device.name, device.address))
            }
        }
    }

    fun freeReceiverData() {
        context.unregisterReceiver(deviceReceiver)
    }

    fun send(keys: List<Int>) {

    }
}