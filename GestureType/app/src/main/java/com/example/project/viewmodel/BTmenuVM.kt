package com.example.project.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class BTmenuVM: ViewModel() {

    var menuVisible: Boolean by mutableStateOf(false)


    var availableDevices: MutableList<String> = mutableListOf(
        "device1", "device2, device3", "device4", "device5", "device6"
    )

    var connectedDevice: String? by mutableStateOf(null)
    
}