package com.example.project.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange

class GestureVM {

    var detectedGesture: String by mutableStateOf("")

    fun detectDoubleTap() { detectedGesture = "Double tap" }

    fun detectSwipe(change: PointerInputChange, dragAmount: Offset) {
        if (change.position.x + dragAmount.x > change.position.x) detectedGesture = "One-finger Swipe right"
        else detectedGesture = "One-finger Swipe left"
    }


}