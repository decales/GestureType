package com.example.project.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.project.viewmodel.GestureVM

@RequiresApi(Build.VERSION_CODES.S)
class GestureView(
    private val viewModel: GestureVM
): ViewModel() {

    @Composable
    fun View() {
        Box {
            GestureSurface()
        }
    }

    @Composable
    fun GestureSurface() {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9F)
                .padding(20.dp)
                .pointerInput(Unit) { detectTapGestures(onDoubleTap = { viewModel.doubleTap() }) }
                .pointerInput(Unit){
                    awaitEachGesture {
                        awaitFirstDown()
                        do {
                            val event = awaitPointerEvent()
                            if (event.changes.size == 2 && viewModel.pointerCount != 2) viewModel.pointerCount = 2
                        } while (event.changes.any { it.pressed })
                    }
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            viewModel.change = change
                            viewModel.dragAmount = dragAmount
                        },
                        onDragEnd = { viewModel.swipe() }
                    )
                }
        ) { }
    }
}