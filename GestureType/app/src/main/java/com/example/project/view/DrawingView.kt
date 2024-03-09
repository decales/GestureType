package com.example.project.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.example.project.viewmodel.DrawingViewModel
import kotlinx.coroutines.launch

class DrawingView (
    private val viewModel: DrawingViewModel,
) {

    @Composable
    fun View() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Row {
                Button(onClick = { viewModel.detectCharacter() }) {
                    Text(text = "Get character")
                }
            }
            DrawingSurface()
            Text(text = "Output: ${viewModel.detectedChar}")
            GestureSurface()
        }
    }

    @Composable
    fun DrawingSurface() {
        Card (modifier = Modifier
            .fillMaxHeight(0.5F)
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp)
            .pointerInput(key1 = 1) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    viewModel.addStroke(change, dragAmount)
                }
            }
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(key1 = true) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            viewModel.addStroke(change, dragAmount);
                        }
                    }
            ) {
                viewModel.prepareCanvas(size)
                viewModel.drawStrokes()
                drawImage(viewModel.canvasBitmap)
            }
        }
    }

    @Composable
    fun GestureSurface() {
        Card(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxHeight(1F)
                .fillMaxWidth()
        ) {
        }
    }
}
