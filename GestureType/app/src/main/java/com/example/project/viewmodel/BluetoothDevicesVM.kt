package com.example.project.viewmodel

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.project.model.bluetooth.BluetoothClient
import kotlinx.coroutines.Job

@SuppressLint("MissingPermission")
@RequiresApi(Build.VERSION_CODES.Q)
class BluetoothDevicesVM(
    val bluetoothClient: BluetoothClient
): ViewModel() {

    var menuVisible: Boolean by mutableStateOf(false)
    var deviceColumn: Int by mutableIntStateOf(0);

    fun toggleDeviceColumn() {
        deviceColumn = if (deviceColumn == 0) 1 else 0
    }

//    fun connectToDevice(device: BluetoothDevice) {
//        isConnecting = true
//        connectionJob = bluetoothClient.connectToDevice(device).listen()
//    }

//    fun disconnectFromDevice() {
//        connectionJob?.cancel()
//        bluetoothClient.closeConnection()
//        isConnected = false
//    }

//    @RequiresApi(Build.VERSION_CODES.Q)
//    fun listenForConnections() {
//
//        awaitingConnection = true
//        connectionJob = bluetoothClient.startServer().listen()
//    }


    fun updateDevices() {
        if (bluetoothClient.hasPermissions(android.Manifest.permission.BLUETOOTH_SCAN)) {
            bluetoothClient.stopDeviceDiscovery()
            bluetoothClient.visibleDevices.clear()
            bluetoothClient.pairedDevices.clear()
            bluetoothClient.startDeviceDiscovery()
        }
    }






//    private fun Flow<ConnectionResult>.listen(): Job {
//        return onEach { result ->
//            Log.d("TAG", "listen: $result")
//
//            when(result) {
//                ConnectionResult.ConnectionEstablished -> {
//                    isConnected = true
//                    awaitingConnection = false
//                    errorMessage = null
//                }
//                is ConnectionResult.Error -> {
//                    isConnected = false
//                    awaitingConnection = false
//                    errorMessage = result.message
//                }
//            }
//        }.catch {
//            bluetoothClient.closeConnection()
//            isConnected = true
//            awaitingConnection = false
//            errorMessage = null
//        }.launchIn(viewModelScope)
//    }
}