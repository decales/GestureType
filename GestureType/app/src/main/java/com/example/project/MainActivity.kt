package com.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.project.model.BluetoothClient
import com.example.project.model.OcrClient
import com.example.project.model.StateMachine
import com.example.project.ui.theme.ProjectTheme
import com.example.project.view.DrawingView
import com.example.project.view.GestureView
import com.example.project.viewmodel.DrawingVM
import com.example.project.viewmodel.GestureVM

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

                    val ocrClient: OcrClient = OcrClient()
                    val bluetoothClient: BluetoothClient = BluetoothClient(this)
                    val stateMachine: StateMachine = StateMachine(bluetoothClient)

                    val drawingVM: DrawingVM  = DrawingVM(ocrClient, stateMachine)
                    val drawingView: DrawingView = DrawingView(drawingVM)

                    val gestureVM: GestureVM =  GestureVM(stateMachine)
                    val gestureView: GestureView = GestureView(gestureVM)

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(30.dp)
                    ) {
                        drawingView.View()
                        gestureView.View()
                    }
                }
            }
        }
    }
}


