package com.hmn.testaicode.ui.screens.selfieSubmitScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun SelfieFrame(
    modifier: Modifier,
    accent: Color,
) {
    val dashed = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

    Surface(
        modifier = modifier,
        color = Color.Transparent,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Outer dashed ring
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .drawBehind {
                        drawCircle(
                            color = accent.copy(alpha = 0.45f),
                            style = Stroke(width = 3.dp.toPx(), pathEffect = dashed)
                        )
                    }
            )

            // Inner solid ring
            Box(
                modifier = Modifier
                    .size(170.dp)
                    .drawBehind {
                        drawCircle(
                            color = accent.copy(alpha = 0.55f),
                            style = Stroke(width = 2.dp.toPx())
                        )
                    }
            )

            // Center avatar circle
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(accent.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.PersonOutline,
                    contentDescription = null,
                    tint = accent.copy(alpha = 0.55f),
                    modifier = Modifier.size(64.dp)
                )
            }
        }
    }
}