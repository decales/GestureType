package com.example.project.model

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class OcrClient(
    private val ocr: TextRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
) {

    fun detectFromBitmap(bitmap: Bitmap): String {
        val task = ocr.process(InputImage.fromBitmap(bitmap, 0))
        while (!task.isComplete) { /**/ }
        return task.result.text
    }
}