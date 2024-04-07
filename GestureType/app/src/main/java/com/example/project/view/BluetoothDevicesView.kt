package com.example.project.view

import android.content.res.Resources.Theme
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat.ThemeCompat
import com.example.project.R
import com.example.project.viewmodel.BluetoothDevicesVM

@RequiresApi(Build.VERSION_CODES.M)
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
            )
        }

        if (viewModel.menuVisible) {
            Box {
                AlertDialog(
                    onDismissRequest = {
                        viewModel.menuVisible = false
                        viewModel.bluetoothClient.stopDeviceDiscovery() },
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 100.dp, bottom = 100.dp)
                ) {
                    Card {
                        Header()
                        Column(
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                        ) {
                            CurrentDevice()
                            Row {
                                TabRow(
                                    selectedTabIndex = viewModel.deviceColumn
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
                                IconButton(onClick = { viewModel.updateDevices() }) {
                                    Icon(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "refresh")
                                }
                            }
                            when (viewModel.deviceColumn) {
                                0 -> VisibleDevicesList()
                                1 -> {}
                            }
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
                IconButton(
                    onClick = {
                        viewModel.menuVisible = false
                        viewModel.bluetoothClient.stopDeviceDiscovery() }
                ) {
                    Icon(painter = painterResource(id = R.drawable.close), contentDescription = "close")
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun VisibleDevicesList() {
        Box {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray)
                ) {
                viewModel.bluetoothClient.visibleDevices.forEach { device ->
                    item {
//                        Card(
//                            onClick = { /*TODO*/ },
//                            colors = CardDefaults.cardColors(Color.Gray),
//                            modifier = Modifier.padding(start = 5.dp, end = 5.dp)
//                        ) {
//                            Text(
//                                text = device.name ?: "Unnamed device",
//                                modifier = Modifier.padding(start = 6.dp, end =  6.dp, top = 3.dp, bottom = 3.dp)
//                            )
//                        }
                        Text(
                            text = device.name ?: "Unnamed device",
                            modifier = Modifier.padding(start = 6.dp, end =  6.dp, top = 3.dp, bottom = 3.dp)
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun CurrentDevice() {
        Box {
            Column {
                Text(text = "Current device")
                if (viewModel.currentDevice == null) Text(text = "No device connected", color = Color.Red)
                else Text(text = if (viewModel.currentDevice!!.name == null) "Unnamed device" else viewModel.currentDevice!!.name!!, color = Color.Green)
            }
        }
    }
}