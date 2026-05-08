package com.hmn.testaicode.ui.screens.wallet_transfer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SendMoneyButton(
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val scheme = MaterialTheme.colorScheme
    val disabledBrush = Brush.horizontalGradient(
        listOf(
            scheme.primary.copy(alpha = 0.30f),
            scheme.secondary.copy(alpha = 0.30f),
        )
    )
    val enabledSolid = Color(0xFF6E60FF)

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
        shape = RoundedCornerShape(29.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        contentPadding = ButtonDefaults.ContentPadding
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(29.dp))
                .background(if (enabled) enabledSolid else Color.Transparent)
                .then(if (!enabled) Modifier.background(disabledBrush) else Modifier),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Send Money",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (enabled) Color.White else scheme.onSurface.copy(alpha = 0.45f)
            )
        }
    }
}