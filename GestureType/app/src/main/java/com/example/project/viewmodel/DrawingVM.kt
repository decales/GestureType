package com.example.project.viewmodel

import android.os.Build
import android.os.CountDownTimer
import androidx.annotation.RequiresApi
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
import com.example.project.model.processing.OcrClient
import com.example.project.model.processing.StateMachine
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.S)
class DrawingVM(
    private val ocrClient: OcrClient,
    private val stateMachine: StateMachine
): ViewModel() {

    data class Stroke(
        val start: Offset,
        val end: Offset,
    )

    var bitmap: ImageBitmap = ImageBitmap(1000,1000)
    private var canvas: Canvas = Canvas(bitmap)
    private val strokes: MutableList<Stroke> = mutableStateListOf()

    var detectedChar: String by mutableStateOf("")
    private var inputTimer: CountDownTimer? = null
    var timerDuration: Long = 300


    private fun startTimer() {
        inputTimer?.cancel() // Reset timer if already active
        inputTimer = object : CountDownTimer(timerDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) { }
            override fun onFinish() { detectCharacter() }
        }.start()
    }

    fun addStroke(change: PointerInputChange, dragAmount: Offset) {
        detectedChar = ""
        strokes.add(Stroke(change.position - dragAmount, change.position))
        startTimer()
    }

    fun drawStrokes() {
        val style = Paint()
        style.color = Color(0xFFffffff)
        style.strokeWidth = 30F
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
        bitmap = ImageBitmap(size.width.toInt(), size.height.toInt())
        canvas = Canvas(bitmap)
    }


    fun detectCharacter() {
        viewModelScope.launch {
            val output: String = ocrClient.detectFromBitmap(bitmap.asAndroidBitmap())
            if (output.isNotBlank() && output != "AA") { // Only suffix was captured
                detectedChar = output.first().toString()
                stateMachine.input(detectedChar)
            }
            else stateMachine.command = ""
            strokes.clear()
        }
    }
}