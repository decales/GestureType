package com.example.project.model.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHidDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.os.Build
import android.os.ParcelUuid
import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.RequiresApi


/** Wrapper for BluetoothHidDevice profile that manages paired HID Host devices.  */
@RequiresApi(Build.VERSION_CODES.P)
class HidDeviceProfile(
    private val context: Context
) {
    /** Used to call back when a profile proxy connection state has changed.  */
    interface ServiceStateListener {
        /**
         * Callback to receive the new profile proxy object.
         *
         * @param proxy Profile proxy object or `null` if the service was disconnected.
         */
        @MainThread
        fun onServiceStateChanged(proxy: BluetoothProfile?)
    }

    private val adapter: BluetoothAdapter? = context.getSystemService(BluetoothManager::class.java).adapter
    private var serviceStateListener: ServiceStateListener? = null
    private var service: BluetoothHidDevice? = null

    // Check if device support HID host profile
    @SuppressLint("MissingPermission")
    fun isProfileSupported(device: BluetoothDevice): Boolean {
        // If a device reports itself as a HID Device, then it isn't a HID Host.
        val uuidArray: Array<ParcelUuid> = device.getUuids()
        for (uuid in uuidArray) {
            if (HID_UUID == uuid || HOGP_UUID == uuid) return false
        }
        return true
    }


    // Connect to profile proxy service
    fun registerServiceListener(listener: ServiceStateListener?) {
        serviceStateListener = checkNotNull(listener)
        adapter?.getProfileProxy(context, ServiceListener(), BluetoothProfile.HID_DEVICE)
    }


    // Close profile service connection
    fun unregisterServiceListener() {
        if (service != null) {
            try {
                adapter?.closeProfileProxy(BluetoothProfile.HID_DEVICE, service)
            } catch (t: Throwable) { Log.w(TAG, "Error cleaning up proxy", t) }
            service = null
        }
        serviceStateListener = null
    }


    // Check device's current connection status
    @SuppressLint("MissingPermission")
    fun getConnectionState(device: BluetoothDevice?): Int {
        return service?.getConnectionState(checkNotNull(device)) ?: BluetoothProfile.STATE_DISCONNECTED
    }


    // Initiate the connection to the remote HID Host device
    @SuppressLint("MissingPermission")
    fun connect(device: BluetoothDevice) { // Removed nullable device
        if (service != null && isProfileSupported(device)) {
            service!!.connect(device)
        }
    }


    // Close the connection with the remote HID Host device
    @SuppressLint("MissingPermission")
    fun disconnect(device: BluetoothDevice) { // Removed nullable device
        if (service != null && isProfileSupported(device)) {
            service!!.disconnect(device)
        }
    }


    // Get all devices that are in the "Connected" state
    val connectedDevices: List<Any>

    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("MissingPermission")
    get() = if (service == null) {
        ArrayList()
    } else service!!.getConnectedDevices()


    // Get all devices that match one of the specified connection states
    @SuppressLint("MissingPermission")
    fun getDevicesMatchingConnectionStates(states: IntArray?): List<BluetoothDevice> {
        return if (service == null) {
            ArrayList()
        } else service!!.getDevicesMatchingConnectionStates(states)
    }


    private inner class ServiceListener : BluetoothProfile.ServiceListener {
        override fun onServiceConnected(profile: Int, proxy: BluetoothProfile) {
            service = proxy as BluetoothHidDevice
            if (serviceStateListener != null) {
                serviceStateListener!!.onServiceStateChanged(service)
            } else {
                adapter?.closeProfileProxy(BluetoothProfile.HID_DEVICE, proxy)
            }
        }


        override fun onServiceDisconnected(profile: Int) {
            service = null
            if (serviceStateListener != null) {
                serviceStateListener!!.onServiceStateChanged(null)
            }
        }
    }

    companion object {
        private const val TAG = "HidDeviceProfile"
        private val HOGP_UUID = ParcelUuid.fromString("00001812-0000-1000-8000-00805f9b34fb")
        private val HID_UUID = ParcelUuid.fromString("00001124-0000-1000-8000-00805f9b34fb")
    }
}
