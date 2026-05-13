package com.hmn.testaicode.ui.screens.error

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun ErrorDialogScreen(
    visible: Boolean,
    modifier: Modifier = Modifier,
    title: String = "Something went wrong",
    message: String = "We couldn’t complete your request. Please try again.",
    retryLabel: String = "Retry",
    cancelLabel: String = "Cancel",
    onRetry: () -> Unit,
    onCancel: () -> Unit,
    onDismissRequest: () -> Unit = onCancel,
) {
    if (!visible) return

    val scheme = MaterialTheme.colorScheme

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        icon = {
            Icon(
                imageVector = Icons.Outlined.ErrorOutline,
                contentDescription = null,
                tint = scheme.error,
            )
        },
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        },
        text = {
            Text(
                text = message,
                color = scheme.onSurface.copy(alpha = 0.80f),
            )
        },
        confirmButton = {
            Button(
                onClick = onRetry,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = scheme.primary,
                    contentColor = scheme.onPrimary,
                ),
            ) {
                Text(text = retryLabel, fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            Button(
                onClick = onCancel,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = scheme.surfaceVariant.copy(alpha = 0.55f),
                    contentColor = scheme.onSurface.copy(alpha = 0.85f),
                ),
            ) {
                Text(text = cancelLabel, fontWeight = FontWeight.SemiBold)
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = scheme.surface,
        tonalElevation = 6.dp,
    )
}