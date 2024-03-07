package com.example.project.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class DrawingViewModel() {

    data class Stroke(
        val start: Offset,
        val end: Offset,
        val color: Color,
        val weight: Dp
    )

    val strokes: MutableList<Stroke> = mutableStateListOf()

    fun addStroke(change: PointerInputChange, dragAmount: Offset) {
        strokes.add(
            Stroke(
                start = change.position - dragAmount,
                end = change.position,
                color = Color(0xFF03fc2c),
                weight = 8.dp
            )
        )
    }
}