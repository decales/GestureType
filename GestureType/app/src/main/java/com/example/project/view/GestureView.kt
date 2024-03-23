package com.example.project.view

import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitVerticalDragOrCancellation
import androidx.compose.foundation.gestures.awaitVerticalTouchSlopOrCancellation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.gestures.transformable
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.project.viewmodel.GestureVM
import kotlin.math.absoluteValue

class GestureView(
    private val viewModel: GestureVM
): ViewModel() {

    @Composable
    fun View() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GestureSurface()
            Text(text = "Command: ${viewModel.stateMachine.command}")
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
                .pointerInput(Unit) {

                    detectDragGestures(
                        onDrag = {change, dragAmount ->
                            viewModel.change = change
                            viewModel.dragAmount = dragAmount
                        },
                        onDragEnd = { viewModel.swipe() }

                    )
                    //detectDragGestures { change, dragAmount -> viewModel.swipe(change, dragAmount) }
                }
                .pointerInput(Unit) {
                    detectTapGestures(onDoubleTap = { viewModel.doubleTap() })
                }
//                .pointerInteropFilter { event ->
//                    viewModel.pointerCount = event.pointerCount
//                    true
//                }
        ) {}
    }
}