package com.hmn.testaicode.ui.screens.utils

import android.graphics.Bitmap
import kotlin.math.abs

/**
 * Crops the upright captured bitmap to match the on-screen preview rectangle's aspect ratio.
 *
 * This removes hidden camera regions introduced by center-crop scaling and keeps only what
 * users visually saw in the rounded preview area.
 */
fun cropBitmapToPreviewRect(
    sourceBitmap: Bitmap,
    previewWidth: Int,
    previewHeight: Int,
): Bitmap {
    if (sourceBitmap.width <= 1 || sourceBitmap.height <= 1) return sourceBitmap
    if (previewWidth <= 1 || previewHeight <= 1) return sourceBitmap

    val sourceRatio = sourceBitmap.width.toFloat() / sourceBitmap.height.toFloat()
    val previewRatio = previewWidth.toFloat() / previewHeight.toFloat()

    if (abs(sourceRatio - previewRatio) < 0.0001f) return sourceBitmap

    val cropWidth: Int
    val cropHeight: Int
    val offsetX: Int
    val offsetY: Int

    if (sourceRatio > previewRatio) {
        // Source is wider than preview => crop left/right.
        cropHeight = sourceBitmap.height
        cropWidth = (cropHeight * previewRatio).toInt().coerceIn(1, sourceBitmap.width)
        offsetX = ((sourceBitmap.width - cropWidth) / 2).coerceAtLeast(0)
        offsetY = 0
    } else {
        // Source is taller than preview => crop top/bottom.
        cropWidth = sourceBitmap.width
        cropHeight = (cropWidth / previewRatio).toInt().coerceIn(1, sourceBitmap.height)
        offsetX = 0
        offsetY = ((sourceBitmap.height - cropHeight) / 2).coerceAtLeast(0)
    }

    return try {
        Bitmap.createBitmap(
            sourceBitmap,
            offsetX,
            offsetY,
            cropWidth,
            cropHeight,
        )
    } catch (_: Throwable) {
        sourceBitmap
    }
}