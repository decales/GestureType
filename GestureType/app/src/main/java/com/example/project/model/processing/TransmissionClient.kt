package com.example.project.model.processing

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.project.model.bluetooth.BluetoothClient
import com.example.project.model.bluetooth.KeyboardReport

@RequiresApi(Build.VERSION_CODES.S)
@SuppressLint("MissingPermission")
class TransmissionClient(
    private val bluetoothClient: BluetoothClient
) {

    fun sendKeys(key: Int, controlDown: Boolean, shiftDown: Boolean, modifier: Int) {
        if (bluetoothClient.isConnected) {
            repeat(modifier) {
                val reportKey = KeyboardReport.keyReportMap[key]
                if (reportKey != null) {

                    // Alter report bytes
                    KeyboardReport.key1 = reportKey.toByte()
                    if (controlDown) KeyboardReport.leftControl = true
                    if (shiftDown) KeyboardReport.leftShift = true

                    // Send bytes
                    bluetoothClient.hidDevice?.sendReport(bluetoothClient.hostDevice, KeyboardReport.id, KeyboardReport.inputBytes)!!
                    sendNull()
                }
            }
        }
    }

    fun sendNull() {
        KeyboardReport.inputBytes.fill(0)
        bluetoothClient.hidDevice?.sendReport(bluetoothClient.hostDevice, KeyboardReport.id, KeyboardReport.inputBytes)
    }
}