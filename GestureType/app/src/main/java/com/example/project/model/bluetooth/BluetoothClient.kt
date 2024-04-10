package com.example.project.model.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHidDevice
import android.bluetooth.BluetoothHidDeviceAppSdpSettings
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.project.model.old.BluetoothDeviceReceiver


@RequiresApi(Build.VERSION_CODES.Q)
@SuppressLint("MissingPermission")

class BluetoothClient(
    private val context: Context,
) {

    // Main bluetooth adapter
    val adapter: BluetoothAdapter? = context.getSystemService(BluetoothManager::class.java).adapter

    // Device variables
    var hidDevice: BluetoothHidDevice? = null // The android device emulating an hid device
    var hostDevice: BluetoothDevice? by mutableStateOf(null) // The device connected to the hid emulation
    val visibleDevices: MutableList<BluetoothDevice> = mutableStateListOf()
    val pairedDevices: MutableList<BluetoothDevice> = mutableStateListOf()

    // Connection state
    var isConnected by mutableStateOf(false)
    var isConnecting by mutableStateOf(false)
    var isDisconnecting by mutableStateOf(false)

    // Listeners
    private val serviceListener: HidServiceListener = HidServiceListener()
    val deviceCallback: HidDeviceCallback = HidDeviceCallback()

    private val deviceReceiver = BluetoothDeviceReceiver { device ->
        if (!visibleDevices.contains(device)) visibleDevices.add(device)
    }


    init {
        requestBluetoothEnable()
        //requestPermissions()
        initProxy()
    }


    fun hasPermissions(vararg permissions: String): Boolean {
        permissions.forEach { if (context.checkSelfPermission(it) == PackageManager.PERMISSION_DENIED) return false }
        return true
    }

//    fun requestPermissions() {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            bluetoothPermissionsLauncher.launch(
//                arrayOf(
//                    android.Manifest.permission.BLUETOOTH_CONNECT,
//                    android.Manifest.permission.BLUETOOTH_SCAN
//                )
//            )
//        }
//    }


    fun requestBluetoothEnable() {
        if (adapter != null) {
            if (!adapter.isEnabled) context.startActivity(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
        }
        else Toast.makeText(context, "Bluetooth is not supported on this device", Toast.LENGTH_LONG).show()
    }


    fun initProxy() {
        if (hasPermissions(android.Manifest.permission.BLUETOOTH_CONNECT)) {
            adapter?.getProfileProxy(context, serviceListener, BluetoothProfile.HID_DEVICE)
        }
    }


    fun updatePairedDevices() {
        if (hasPermissions(android.Manifest.permission.BLUETOOTH_CONNECT)) {
            adapter?.bondedDevices?.forEach { device ->
                pairedDevices.add(device)
            }
        }
    }


    fun startDeviceDiscovery() {
        if (hasPermissions(android.Manifest.permission.BLUETOOTH_SCAN)) {
            context.registerReceiver(
                deviceReceiver,
                IntentFilter(BluetoothDevice.ACTION_FOUND)
            )
            updatePairedDevices()
            adapter?.startDiscovery()
        }
    }


    fun connectToDevice(device: BluetoothDevice) {
        if (hasPermissions(android.Manifest.permission.BLUETOOTH_CONNECT)) {
            hidDevice?.connect(device)
        }
    }


    fun disconnectFromDevice()  {
        if (hasPermissions(android.Manifest.permission.BLUETOOTH_CONNECT)) {
            hidDevice?.disconnect(hostDevice)
        }
    }


    fun stopDeviceDiscovery() {
        if (hasPermissions(android.Manifest.permission.BLUETOOTH_SCAN)) {
            adapter?.cancelDiscovery()
        }
    }


    inner class HidServiceListener(): BluetoothProfile.ServiceListener {
        override fun onServiceConnected(profile: Int, proxy: BluetoothProfile?) {
            hidDevice = proxy as? BluetoothHidDevice
            hidDevice?.registerApp(
                BluetoothHidDeviceAppSdpSettings(
                    "GestureType device",
                    "Gesture-based input device application",
                    "CMPT 481",
                    BluetoothHidDevice.SUBCLASS1_KEYBOARD,
                    KeyboardDescriptors.KEYBOARD_MODIFIED
                ),
                null, null, {it.run()}, deviceCallback
            )
        }

        override fun onServiceDisconnected(profile: Int) {
            hidDevice = null
        }

    }


    inner class HidDeviceCallback: BluetoothHidDevice.Callback() {

        override fun onConnectionStateChanged(device: BluetoothDevice?, state: Int) {
            super.onConnectionStateChanged(device, state)

            when(state) {
                BluetoothProfile.STATE_CONNECTED -> {
                    hostDevice = device
                    isConnected = true
                    isConnecting = false
                    isDisconnecting = false
                }
                BluetoothProfile.STATE_CONNECTING -> {
                    hostDevice = device
                    isConnected = false
                    isConnecting = true
                    isDisconnecting = false
                }
                BluetoothProfile.STATE_DISCONNECTING -> {
                    hostDevice = device
                    isConnected = true
                    isConnecting = false
                    isDisconnecting = true
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    hostDevice = null
                    isConnected = false
                    isConnecting = false
                    isDisconnecting = false
                }
            }
        }


        override fun onAppStatusChanged(pluggedDevice: BluetoothDevice?, registered: Boolean) {
            super.onAppStatusChanged(pluggedDevice, registered)
            if (registered) {
                // ?
            }
            else {
                initProxy() // Attempt to (re)create proxy after bluetooth settings are enabled
            }
        }
    }
}