package com.example.project.view

import android.view.GestureDetector
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.project.viewmodel.DrawingViewModel

class DrawingView (
    private val viewModel: DrawingViewModel,
) {

    @Composable
    fun View() {
        DrawingSurface()
    }

    @Composable
    fun DrawingSurface() {
        Card (modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
        ) {
            //val lines = remember { mutableStateListOf<DrawingViewModel.Stroke>() }
            //val lines = remember { mutableStateOf(viewModel.strokes) }

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
                viewModel.strokes.forEach { stroke ->
                    drawLine(
                        color = stroke.color,
                        start = stroke.start,
                        end = stroke.end,
                        strokeWidth = stroke.weight.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }

    // eoc
}
