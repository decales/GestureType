package com.example.project.model.processing

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class OcrClient(
    val context: Context
) {

    private val ocr: TextRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private var suffixBitmap: Bitmap? by mutableStateOf(null)
    var mergedBitmap: Bitmap? by mutableStateOf(null)


    init {
        initSuffixBitmap()
    }

    private fun initSuffixBitmap() {
        val drawable = ContextCompat.getDrawable(context, com.example.project.R.drawable.suffix)
        val bitmap = Bitmap.createBitmap(drawable!!.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        suffixBitmap = bitmap
    }

    fun detectFromBitmap(bitmap: Bitmap): String {
        if (suffixBitmap != null) {
            // Process bitmap
            val boundingBox = getBoundingBox(bitmap)
            val croppedBitmap = cropBitmap(bitmap, boundingBox)
            val mergedBitmap = mergeBitmap(croppedBitmap)

            val task = ocr.process(InputImage.fromBitmap(mergedBitmap, 0))
            while (!task.isComplete) { /* lol */ }
            Log.d("TAG", "detectFromBitmap: ${task.result.text}")
            return task.result.text
        }
        return ""
    }

    private fun getBoundingBox(bitmap: Bitmap): Rect {
        var minX = bitmap.width
        var minY = bitmap.height
        var maxX = -1
        var maxY = -1

        for (y in 0 until bitmap.height) {
            for (x in 0 until bitmap.width) {
                val pixel = bitmap.getPixel(x, y)
                if (pixel != 0) { // Not transparent
                    if (x < minX) minX = x
                    if (x > maxX) maxX = x
                    if (y < minY) minY = y
                    if (y > maxY) maxY = y
                }
            }
        }
        return Rect(minX, minY, maxX, maxY)
    }

    private fun cropBitmap(bitmap: Bitmap, boundingBox: Rect): Bitmap {
//        val padX = (boundingBox.width() * 0.5).toInt()
//        val padY = (boundingBox.height() * 0.5).toInt()
        val padX = 0
        val padY = 0

        val left = (boundingBox.left - padX).coerceAtLeast(0)
        val top = (boundingBox.top - padY).coerceAtLeast(0)
        val right = (boundingBox.right + padX).coerceAtMost(bitmap.width)
        val bottom = (boundingBox.bottom + padY).coerceAtMost(bitmap.height)

        return Bitmap.createBitmap(bitmap, left, top, right - left, bottom - top)
    }

    private fun mergeBitmap(bitmap: Bitmap): Bitmap {
        val scaledBitmap = scaleBitmapHeight(bitmap)
        suffixBitmap = scaleBitmapHeight(suffixBitmap!!)

        val mergedBitmap = Bitmap.createBitmap(scaledBitmap.width + suffixBitmap!!.width, 100, Bitmap.Config.ARGB_8888)
        Canvas(mergedBitmap).apply {
            drawBitmap(scaledBitmap, 0f, 0f, null)
            drawBitmap(suffixBitmap!!, scaledBitmap.width.toFloat(), 0f, null)
        }
        this.mergedBitmap = mergedBitmap
        return mergedBitmap
    }

    private fun scaleBitmapHeight(bitmap: Bitmap): Bitmap {
        val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
        val targetWidth = (100 * aspectRatio).toInt()
        return Bitmap.createScaledBitmap(bitmap, targetWidth, 100, false)
    }
}