package com.example.project.model

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


data class BluetoothDevice(
    val name: String,
    val address: String
)

class BluetoothClient(
    private val context: Context
) {
    @RequiresApi(Build.VERSION_CODES.M)
    val manager: BluetoothManager = context.getSystemService(BluetoothManager::class.java)
    val scannedDevices: MutableList<BluetoothDevice> = mutableStateListOf()
    var connectedDevice: BluetoothDevice? by mutableStateOf(null)


    fun send(modifier: Int, input: String) {
        for (i in 0.rangeTo(modifier)) {
            // send input to bluetooth module
        }
    }
}