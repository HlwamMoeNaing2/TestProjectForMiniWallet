package com.hmn.testaicode.ui.screens.profile.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
 fun SectionLabel(text: String) {
    val scheme = MaterialTheme.colorScheme
    Text(
        text = text,
        fontSize = 12.sp,
        letterSpacing = 1.2.sp,
        color = scheme.onBackground.copy(alpha = 0.45f),
        fontWeight = FontWeight.Medium
    )
}