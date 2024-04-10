package com.example.project.view

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project.R
import com.example.project.viewmodel.BluetoothDevicesVM

@SuppressLint("MissingPermission")
@RequiresApi(Build.VERSION_CODES.S)
class BluetoothDevicesView(
    private val viewModel: BluetoothDevicesVM
) {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun View() {
        IconButton(
            onClick = {
                viewModel.menuVisible = true
                viewModel.updateDevices() },
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.bluetooth),
                contentDescription = "bluetooth",
                tint = (if (viewModel.bluetoothClient.isConnected) Color.Green else Color.Red)
            )
        }
        if (viewModel.menuVisible) {
            Box {
                AlertDialog(
                    onDismissRequest = {
                        viewModel.menuVisible = false
                        viewModel.bluetoothClient.stopDeviceDiscovery()
                                       },
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 200.dp, bottom = 200.dp)
                ) {
                    Card {
                        Header()
                        Column(
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                                .fillMaxSize()
                        ) {
                            
                            if (viewModel.bluetoothClient.adapter != null) { // Bluetooth supported
                                if (viewModel.bluetoothClient.adapter.isEnabled) { // Bluetooth enabled
                                    if (viewModel.bluetoothClient.hasPermissions(android.Manifest.permission.BLUETOOTH_SCAN, android.Manifest.permission.BLUETOOTH_SCAN)) { // Permissions granted
                                        if (viewModel.bluetoothClient.isConnecting) ConnectingToDevice()
                                        else if (viewModel.bluetoothClient.isDisconnecting) DisconnectingFromDevice()
                                        else ConnectedDevice()
                                        DeviceLists()
                                    }
                                    else { // Permissions not granted
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.spacedBy(20.dp)
                                            ) {
                                                Text(text = "Bluetooth permissions not granted")
                                                Button(onClick = { viewModel.bluetoothClient.requestPermissions() }) {
                                                    Text(text = "Grant")
                                                }
                                                Button(onClick = { repeat(2) {viewModel.menuVisible = !viewModel.menuVisible} }) {
                                                    Text(text = "Refresh")
                                                }
                                            }
                                        }
                                    }
                                }
                                else { // Bluetooth disabled
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(20.dp)
                                        ) {
                                            Text(text = "Bluetooth is disabled")
                                            Button(onClick = { viewModel.bluetoothClient.requestBluetoothEnable() }) {
                                                Text(text = "Enable")
                                            }
                                            Button(onClick = { repeat(2) {viewModel.menuVisible = !viewModel.menuVisible} }) {
                                                Text(text = "Refresh")
                                            }
                                        }
                                    }
                                }
                            }
                            else { // Bluetooth not supported
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(text = "Bluetooth is not supported on this device :(")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun Header() {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
        ) {
            Text(
                text = "Bluetooth Devices",
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
            IconButton(
                onClick = {
                    viewModel.menuVisible = false
                    viewModel.bluetoothClient.stopDeviceDiscovery() },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) { Icon(painter = painterResource(id = R.drawable.close), contentDescription = "close") }
        }
    }

    @Composable
    fun DeviceLists() {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TabRow(
                selectedTabIndex = viewModel.deviceColumn,
            ) {
                listOf("Visible", "Paired").forEachIndexed { index, tab ->
                    Tab(selected = viewModel.deviceColumn == index, onClick = { viewModel.toggleDeviceColumn() }) {
                        Text(
                            text = tab,
                            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                        )
                    }
                }
            }
            LazyColumn(
                contentPadding = PaddingValues(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .padding(4.dp)
                    .weight(1F)
            ) {
                val list = if (viewModel.deviceColumn == 0) viewModel.bluetoothClient.visibleDevices
                else viewModel.bluetoothClient.pairedDevices

                list.forEachIndexed { index, device ->
                    item {
                        Column {
                            Box (
                                contentAlignment = Alignment.CenterStart,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.bluetoothClient.connectToDevice(device) }
                            ) {
                                Text(
                                    text = device.name ?: "Unnamed device",
                                    modifier = Modifier.padding(top = if (index == 0) 0.dp else 6.dp, bottom = if (index == list.lastIndex) 0.dp else 6.dp)
                                )
                            }
                            if (index != list.lastIndex) Divider(thickness = 1.dp, color = Color.Gray)
                        }
                    }
                }
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(modifier = Modifier.clickable { viewModel.updateDevices() }) {
                    Text(text = "Rescan")
                    Icon(
                        painter = painterResource(id = R.drawable.refresh),
                        contentDescription = "refresh",
                        modifier = Modifier
                            .padding(start = 2.dp)
                            .size(22.dp)
                    )
                }
            }
        }
    }

    @Composable
    fun ConnectedDevice() {
        Box (modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                Text(text = "Connected device")
                if (!viewModel.bluetoothClient.isConnected) Text(text = "No device connected", color = Color.Red)
                else Text(text = if (viewModel.bluetoothClient.hostDevice!!.name == null) "Unnamed device" else viewModel.bluetoothClient.hostDevice!!.name!!, color = Color.Green)
            }
            if (viewModel.bluetoothClient.isConnected) {
                OutlinedButton(
                    onClick = { viewModel.bluetoothClient.disconnectFromDevice()},
                    contentPadding = PaddingValues(12.dp),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                ) { Text(text = "Disconnect") }
            }
        }
    }

    @Composable
    fun ConnectingToDevice() {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                Text(text = "Connecting to device")
                Text(
                    text = (if (viewModel.bluetoothClient.hostDevice!!.name == null) "Unnamed device" else viewModel.bluetoothClient.hostDevice!!.name)!!,
                    color = Color.Yellow
                )
            }
            CircularProgressIndicator(
                strokeWidth = 2.dp,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(30.dp))
        }
    }

    @Composable
    fun DisconnectingFromDevice() {
        Box (modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                Text(text = "Disconnecting from device")
                Text(
                    text = (if (viewModel.bluetoothClient.hostDevice!!.name == null) "Unnamed device" else viewModel.bluetoothClient.hostDevice!!.name)!!,
                    color = Color.Blue
                )
            }
            CircularProgressIndicator(
                strokeWidth = 2.dp,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(30.dp))
        }
    }
}