package com.hmn.testaicode.ui.screens.profile.components

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
 fun Divider() {
    val scheme = MaterialTheme.colorScheme
    HorizontalDivider(color = scheme.outline.copy(alpha = 0.35f))
}
