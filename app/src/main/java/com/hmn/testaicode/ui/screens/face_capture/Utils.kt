package com.hmn.testaicode.ui.screens.face_capture

import android.graphics.Bitmap
import android.graphics.Matrix

fun Bitmap.rotateBitmap(rotationDegrees: Int): Bitmap {
    if (rotationDegrees == 0) return this
    val matrix = Matrix().apply {
        postRotate(rotationDegrees.toFloat())
    }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}