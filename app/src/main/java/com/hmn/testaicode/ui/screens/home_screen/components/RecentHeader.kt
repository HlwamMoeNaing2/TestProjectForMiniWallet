package com.hmn.testaicode.ui.screens.home_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun RecentHeader(countLabel: String) {
    val scheme = MaterialTheme.colorScheme
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Recent activity",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = scheme.onBackground
        )
        Text(
            text = countLabel,
            fontSize = 13.sp,
            color = scheme.onBackground.copy(alpha = 0.55f)
        )
    }
}