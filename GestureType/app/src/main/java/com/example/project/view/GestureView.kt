package com.example.project.view

import android.util.Log
import androidx.compose.foundation.gestures.awaitDragOrCancellation
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitHorizontalDragOrCancellation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.project.viewmodel.GestureVM
import kotlinx.coroutines.awaitAll
import kotlin.math.log

class GestureView(
    private val viewModel: GestureVM
): ViewModel() {

    @Composable
    fun View() {
        Box {
            GestureSurface()
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun GestureSurface() {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9F)
                .padding(top = 10.dp, bottom = 10.dp)
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

        ) {

        }
    }
}