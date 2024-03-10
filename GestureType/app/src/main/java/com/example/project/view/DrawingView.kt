package com.example.project.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.project.viewmodel.DrawingVM

class DrawingView (
    private val viewModel: DrawingVM,
) {

    @Composable
    fun View() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { viewModel.detectCharacter() }) {
                Text(text = "Get character")
            }
            DrawingSurface()
            Text(text = "Output: ${viewModel.detectedChar}")
        }
    }

    @Composable
    fun DrawingSurface() {
        Card (
            modifier = Modifier
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
                    .fillMaxWidth()
                    .fillMaxHeight(0.45F)
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
}
