package com.hmn.testaicode.ui.screens.wallet_transfer.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
 fun FieldLabel(text: String, scheme: ColorScheme) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth(),
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        color = scheme.onSurface.copy(alpha = 0.85f)
    )
}