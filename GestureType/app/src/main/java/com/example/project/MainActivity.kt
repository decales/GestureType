package com.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.project.model.OcrClient
import com.example.project.ui.theme.ProjectTheme
import com.example.project.view.DrawingView
import com.example.project.viewmodel.DrawingViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

                    val ocrClient: OcrClient = OcrClient()
                    val drawingViewModel: DrawingViewModel  = DrawingViewModel(ocrClient)
                    val drawingView: DrawingView = DrawingView(drawingViewModel)

                    drawingView.View()
                }
            }
        }
    }
}


