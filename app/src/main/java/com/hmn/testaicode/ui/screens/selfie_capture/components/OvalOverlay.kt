package com.hmn.testaicode.ui.screens.selfie_capture.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

private const val OVAL_WIDTH_DP = 250
private const val OVAL_HEIGHT_DP = 300
private const val OVAL_LEFT_OFFSET_RATIO = 2
private const val OVAL_TOP_OFFSET_RATIO = 3
private const val TAG = "OvalOverlay"
@Composable
 fun OvalOverlay(
    modifier: Modifier = Modifier,
    isFaceDetected: Boolean,
    onCenterCalculated: (Offset) -> Unit = {},
) {
    val ovalColor = if (isFaceDetected) Color.Green else Color.Red
    val density = LocalDensity.current
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    val ovalCenterOffset = remember(canvasSize, density) {
        if (canvasSize == IntSize.Zero) return@remember null
        val ovalWidth = with(density) { OVAL_WIDTH_DP.dp.toPx() }
        val ovalHeight = with(density) { OVAL_HEIGHT_DP.dp.toPx() }
        val ovalLeftOffset = (canvasSize.width - ovalWidth) / OVAL_LEFT_OFFSET_RATIO
        val ovalTopOffset = (canvasSize.height - ovalHeight) / OVAL_TOP_OFFSET_RATIO
        Offset(
            // Keep the original coordinate mapping that matched analyzer space.
            x = ovalLeftOffset + (OVAL_WIDTH_DP / OVAL_LEFT_OFFSET_RATIO.toFloat()),
            y = ovalTopOffset - (OVAL_HEIGHT_DP / OVAL_TOP_OFFSET_RATIO.toFloat()),
        )
    }
    LaunchedEffect(ovalCenterOffset) {
        ovalCenterOffset?.let {
            Log.d(TAG, "Computed ovalCenter=$it canvasSize=$canvasSize")
            onCenterCalculated(it)
        }
    }

    Box(
        modifier = modifier.onSizeChanged {
            canvasSize = it
            Log.d(TAG, "Canvas size changed=$it")
        },
        contentAlignment = Alignment.TopCenter,
    ) {
        Canvas(modifier = modifier) {
            val ovalSize = Size(OVAL_WIDTH_DP.dp.toPx(), OVAL_HEIGHT_DP.dp.toPx())
            val ovalLeftOffset = (size.width - ovalSize.width) / OVAL_LEFT_OFFSET_RATIO
            val ovalTopOffset = (size.height - ovalSize.height) / OVAL_TOP_OFFSET_RATIO

            val ovalRect =
                Rect(
                    ovalLeftOffset,
                    ovalTopOffset,
                    ovalLeftOffset + ovalSize.width,
                    ovalTopOffset + ovalSize.height
                )
            val ovalPath = Path().apply {
                addOval(ovalRect)
            }
            clipPath(ovalPath, clipOp = ClipOp.Difference) {
                drawRect(SolidColor(Color.Black.copy(alpha = 0.95f)))
            }
        }


        Canvas(
            modifier = modifier,
        ) {
            val ovalSize = Size(OVAL_WIDTH_DP.dp.toPx(), OVAL_HEIGHT_DP.dp.toPx())
            val ovalLeft = (size.width - ovalSize.width) / OVAL_LEFT_OFFSET_RATIO
            val ovalTop =
                (size.height - ovalSize.height) / OVAL_TOP_OFFSET_RATIO - ovalSize.height
            drawOval(
                color = ovalColor,
                style = Stroke(width = OVAL_TOP_OFFSET_RATIO.dp.toPx()),
                topLeft = Offset(ovalLeft, ovalTop + ovalSize.height),
                size = ovalSize,
            )
        }

    }
}
