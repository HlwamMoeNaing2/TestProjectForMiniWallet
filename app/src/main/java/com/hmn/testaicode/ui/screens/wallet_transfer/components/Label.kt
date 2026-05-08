package com.hmn.testaicode.ui.screens.wallet_transfer.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun Label(text: String) {
    val scheme = MaterialTheme.colorScheme
    Text(
        text = text,
        fontSize = 12.sp,
        letterSpacing = 1.sp,
        color = scheme.onSurface.copy(alpha = 0.45f)
    )
}