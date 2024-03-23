package com.example.project.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project.model.OcrClient
import com.example.project.model.StateMachine
import kotlinx.coroutines.launch

class DrawingVM(
    private val ocrClient: OcrClient,
    private val stateMachine: StateMachine
): ViewModel() {

    data class Stroke(
        val start: Offset,
        val end: Offset,
    )

    private val strokes: MutableList<Stroke> = mutableStateListOf()
    var canvasBitmap: ImageBitmap = ImageBitmap(1000,1000)
    private var canvas: Canvas = Canvas(canvasBitmap)
    var detectedChar: String by mutableStateOf("")

    fun addStroke(change: PointerInputChange, dragAmount: Offset) {
        strokes.add(Stroke(change.position - dragAmount, change.position))
    }

    fun drawStrokes() {
        val style: Paint = Paint()
        style.color = Color(0xFFffffff)
        style.strokeWidth = 25F
        style.strokeCap = StrokeCap.Round
        strokes.forEach {
            canvas.drawLine(
                p1 = it.start,
                p2 = it.end,
                paint = style
            )
        }
    }

    fun prepareCanvas(size: Size) {
        canvasBitmap = ImageBitmap(size.width.toInt(), size.height.toInt())
        canvas = Canvas(canvasBitmap)
    }

    fun detectCharacter() {
        viewModelScope.launch {
            detectedChar = ocrClient.detectFromBitmap(canvasBitmap.asAndroidBitmap())
            if (detectedChar.isNotBlank()) stateMachine.input(detectedChar)
            strokes.clear()
        }
    }
}