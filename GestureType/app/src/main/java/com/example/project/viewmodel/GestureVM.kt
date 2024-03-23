package com.example.project.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.lifecycle.ViewModel
import com.example.project.model.StateMachine
import kotlin.math.absoluteValue

class GestureVM(
    private val stateMachine: StateMachine
): ViewModel() {

    var detectedGesture: String by mutableStateOf("")
    var pointerCount: Int by mutableIntStateOf(0);

    fun doubleTap() { detectedGesture = "Double tap" }

    fun oneFingerSwipe(change: PointerInputChange, dragAmount: Offset) {
        if (pointerCount == 1) {
            detectedGesture = if (change.position.x + dragAmount.x > change.position.x) "One-finger Swipe right"
            else "One-finger Swipe left"
        }
        else {
            detectedGesture = if (change.position.x + dragAmount.x > change.position.x) "Two-finger Swipe right"
            else "Two-finger Swipe left"
        }
    }
}