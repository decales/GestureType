package com.example.project.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.lifecycle.ViewModel
import com.example.project.model.StateMachine

class GestureVM(
    val stateMachine: StateMachine
): ViewModel() {

    var detectedGesture: String by mutableStateOf("")
    var pointerCount: Int by mutableIntStateOf(1)
    var dragAmount: Offset by mutableStateOf(Offset(0F, 0.0F))
    var change: PointerInputChange? by mutableStateOf(null)

    fun doubleTap() {
        detectedGesture = "DoubleTap"
        stateMachine.input(detectedGesture)
        pointerCount = 1
    }

    fun swipe() {
        if (pointerCount == 1) { // One-finger swipe
            detectedGesture = if ((change?.position?.x ?: 0F) + dragAmount.x > (change?.position?.x ?: 0F)) "OneSwipeRight"
            else "OneSwipeLeft"
        }
        else if (pointerCount == 2){ // Two-finger swipe
            detectedGesture = if ((change?.position?.x ?: 0F) + dragAmount.x > (change?.position?.x ?: 0F)) "TwoSwipeRight"
            else "TwoSwipeLeft"
            pointerCount = 1
        }
        stateMachine.input(detectedGesture)
    }
}