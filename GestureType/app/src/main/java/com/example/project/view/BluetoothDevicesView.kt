package com.example.project.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.project.R
import com.example.project.viewmodel.BluetoothDevicesVM

class BluetoothDevicesView(
    private val viewModel: BluetoothDevicesVM
) {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun View() {

        IconButton(
            onClick = { viewModel.menuVisible = true },
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.bluetooth),
                contentDescription = "bluetooth",
                //tint = modeColor,
            )
        }

        if (viewModel.menuVisible) {
            Box {
                AlertDialog(onDismissRequest = { viewModel.menuVisible = false }) {
                    Card {
                        Header()

                        Column(
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                        ) {
                            ConnectedDevice()
                            AvailableDevicesList()
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun Header() {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Bluetooth Devices")
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = {viewModel.menuVisible = false }) {
                    Icon(painter = painterResource(id = R.drawable.close), contentDescription = "close")
                }
            }
        }
    }

    @Composable
    fun AvailableDevicesList() {
        Box {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = "Visible devices")


                LazyColumn(
                    modifier = Modifier.border(1.dp, Color.Gray)
                ) {
                    viewModel.visibleDevices.forEach { device ->
                        item {
                            Text(text = device.name)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ConnectedDevice() {
        Box {
            Column {
                Text(text = "Current device")
                if (viewModel.currentDevice == null) Text(text = "No device connected", color = Color.Red)
                else Text(text = viewModel.currentDevice!!.name, color = Color.Green)
            }
        }
    }

}