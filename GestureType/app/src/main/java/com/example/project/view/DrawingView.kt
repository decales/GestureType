package com.example.project.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project.viewmodel.DrawingVM

@RequiresApi(Build.VERSION_CODES.S)
class DrawingView (
    private val viewModel: DrawingVM,
) {

    @Composable
    fun View() {
        Box {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DrawingSurface()
            }
        }
    }

    @Composable
    fun DrawingSurface() {
        Card (
            modifier = Modifier
                .padding(20.dp)
                .pointerInput(key1 = 1) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        viewModel.addStroke(change, dragAmount)
                    }
                }
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = viewModel.detectedChar, color = Color.Gray, fontSize = 50.sp)
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.45F)
                        .pointerInput(key1 = true) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                viewModel.addStroke(change, dragAmount)
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
}
