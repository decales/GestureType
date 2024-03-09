package com.example.project.model

import android.graphics.Bitmap
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

class OcrClient(
    private val ocr: TextRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
) {

    fun detectFromBitmap(bitmap: Bitmap): String {
        val task = ocr.process(InputImage.fromBitmap(bitmap, 0))
        while (!task.isComplete) {} // I hate that this works
        return task.result.text
    }
}