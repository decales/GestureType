package com.example.project.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.project.model.BluetoothClient
import com.example.project.model.BluetoothDevice

@RequiresApi(Build.VERSION_CODES.M)
class BluetoothDevicesVM(
    val bluetoothClient: BluetoothClient
): ViewModel() {

    var menuVisible: Boolean by mutableStateOf(false)
    var currentDevice: BluetoothDevice? by mutableStateOf(null)
    var deviceColumn: Int by mutableIntStateOf(0);

    fun updateDevices() {
        bluetoothClient.startDeviceDiscovery()
    }

    fun toggleDeviceColumn() {
        deviceColumn = if (deviceColumn == 0) 1 else 0
    }
}