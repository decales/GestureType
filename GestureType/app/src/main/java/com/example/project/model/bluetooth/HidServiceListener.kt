package com.example.project.model.bluetooth

import android.bluetooth.BluetoothProfile

class HidServiceListener(): BluetoothProfile.ServiceListener {
    override fun onServiceConnected(profile: Int, proxy: BluetoothProfile?) {
        TODO("Not yet implemented")
    }

    override fun onServiceDisconnected(profile: Int) {



    }
}