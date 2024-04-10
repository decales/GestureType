package com.example.project

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.example.project.model.bluetooth.BluetoothClient
import com.example.project.model.processing.OcrClient
import com.example.project.model.processing.StateMachine
import com.example.project.model.processing.TransmissionClient
import com.example.project.ui.theme.ProjectTheme
import com.example.project.view.BluetoothDevicesView
import com.example.project.view.DrawingView
import com.example.project.view.GestureView
import com.example.project.viewmodel.BluetoothDevicesVM
import com.example.project.viewmodel.DrawingVM
import com.example.project.viewmodel.GestureVM

@RequiresApi(Build.VERSION_CODES.S)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            ProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

                    // Model setup
                    val ocrClient = OcrClient()
                    val bluetoothClient = BluetoothClient(this)
                    val transmissionClient = TransmissionClient(bluetoothClient)
                    val stateMachine = StateMachine(transmissionClient)

                    // View and VM setup
                    val drawingVM = DrawingVM(ocrClient, stateMachine)
                    val drawingView = DrawingView(drawingVM)

                    val bluetoothDevicesVM = BluetoothDevicesVM(bluetoothClient)
                    val bluetoothDevicesView = BluetoothDevicesView(bluetoothDevicesVM)

                    val gestureVM = GestureVM(stateMachine)
                    val gestureView = GestureView(gestureVM)

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

    @Composable
    fun MainView(drawingView: DrawingView, gestureView: GestureView, bluetoothDevicesView: BluetoothDevicesView, stateMachine: StateMachine) {
        val modeColor =
            if (stateMachine.mode == StateMachine.StateMachineMode.INSERT) Color.Magenta else Color.Cyan

        Box(
            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, start = 5.dp, end = 5.dp)
        ) {
            Box(
                modifier = Modifier.border(BorderStroke(2.dp, modeColor), RoundedCornerShape(3))
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
}


