package com.example.project.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.project.model.BluetoothClient
import com.example.project.model.BluetoothDevice

class BluetoothDevicesVM(
    private val btClient: BluetoothClient
): ViewModel() {

    var menuVisible: Boolean by mutableStateOf(false)

    var pairedDevices: MutableList<BluetoothDevice> = mutableListOf(
        BluetoothDevice("pdevice1", "address"),
        BluetoothDevice("pdevice2", "address"),
        BluetoothDevice("pdevice3", "address"),
    )

    var visibleDevices: MutableList<BluetoothDevice> = mutableListOf(
        BluetoothDevice("device1", "address"),
        BluetoothDevice("device2", "address"),
        BluetoothDevice("device3", "address"),
        BluetoothDevice("device4", "address"),
        BluetoothDevice("device5", "address"),
    )

    var currentDevice: BluetoothDevice? by mutableStateOf(null)
    
}