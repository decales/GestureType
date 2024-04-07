package com.example.project

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.project.model.BluetoothClient
import com.example.project.model.OcrClient
import com.example.project.model.StateMachine
import com.example.project.model.StateMachineMode
import com.example.project.ui.theme.ProjectTheme
import com.example.project.view.BluetoothDevicesView
import com.example.project.view.DrawingView
import com.example.project.view.GestureView
import com.example.project.viewmodel.BluetoothDevicesVM
import com.example.project.viewmodel.DrawingVM
import com.example.project.viewmodel.GestureVM

@RequiresApi(Build.VERSION_CODES.M)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        // Bluetooth setup
        val manager: BluetoothManager = applicationContext.getSystemService(BluetoothManager::class.java)
        val bluetoothEnabled: Boolean = manager.adapter.isEnabled

        val enableBluetoothLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {}

        val bluetoothPermissionsLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val canEnableBluetooth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                permissions[android.Manifest.permission.BLUETOOTH_CONNECT] == true
            } else true

            if (canEnableBluetooth && !bluetoothEnabled) {
                enableBluetoothLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            }
        }

        // Launch permission check
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            bluetoothPermissionsLauncher.launch(
                arrayOf(
                    android.Manifest.permission.BLUETOOTH_CONNECT,
                    android.Manifest.permission.BLUETOOTH_SCAN
                )
            )
        }

        super.onCreate(savedInstanceState)
        setContent {
            ProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

                    // Model setup
                    val ocrClient: OcrClient = OcrClient()
                    val bluetoothClient: BluetoothClient = BluetoothClient(this)
                    val stateMachine: StateMachine = StateMachine(bluetoothClient)

                    // View and VM setup
                    val drawingVM: DrawingVM  = DrawingVM(ocrClient, stateMachine)
                    val drawingView: DrawingView = DrawingView(drawingVM)

                    val bluetoothDevicesVM: BluetoothDevicesVM = BluetoothDevicesVM(bluetoothClient)
                    val bluetoothDevicesView: BluetoothDevicesView = BluetoothDevicesView(bluetoothDevicesVM)

                    val gestureVM: GestureVM =  GestureVM(stateMachine)
                    val gestureView: GestureView = GestureView(gestureVM)

                    MainView(
                        drawingView = drawingView,
                        gestureView = gestureView,
                        bluetoothDevicesView = bluetoothDevicesView,
                        stateMachine = stateMachine
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun MainView(drawingView: DrawingView, gestureView: GestureView, bluetoothDevicesView: BluetoothDevicesView, stateMachine: StateMachine) {
    val modeColor = if (stateMachine.mode == StateMachineMode.INSERT) Color.Magenta else Color.Cyan

    Box(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, start = 5.dp, end = 5.dp)
    ) {
        Box(modifier = Modifier.border(BorderStroke(2.dp, modeColor), RoundedCornerShape(3))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                ) {

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box {
                        Text(text = "${stateMachine.mode} MODE", color = modeColor,)
                    }
                    Box(
                        contentAlignment = Alignment.CenterEnd,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        bluetoothDevicesView.View()
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                ) {
                    drawingView.View()
                    gestureView.View()
                }
                Text(text = stateMachine.command)
            }
        }
    }
}


