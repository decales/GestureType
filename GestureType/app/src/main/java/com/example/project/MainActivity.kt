package com.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import com.example.project.model.BluetoothClient
import com.example.project.model.OcrClient
import com.example.project.model.StateMachine
import com.example.project.model.StateMachineMode
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

                    MainView(
                        drawingView = drawingView,
                        gestureView = gestureView,
                        stateMachine = stateMachine
                    )
                }
            }
        }
    }
}

@Composable
fun MainView(drawingView: DrawingView,gestureView: GestureView, stateMachine: StateMachine) {
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
                Text(text = "${stateMachine.mode} MODE", color = modeColor)
                Column(
                    modifier = Modifier

                        .padding(20.dp)
                ) {
                    drawingView.View()
                    gestureView.View()
                }
                Text(text = "${stateMachine.command}")
            }
        }

    }

}


