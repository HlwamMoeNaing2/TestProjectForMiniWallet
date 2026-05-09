package com.hmn.testaicode.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush

@Composable
fun appBackgroundBrush(): Brush {
    val colors = if (isSystemInDarkTheme()) {
        listOf(LumenDarkGradientTop, LumenDarkGradientBottom)
    } else {
        listOf(LumenGradientTop, LumenGradientBottom)
    }
    return Brush.verticalGradient(colors)
}

