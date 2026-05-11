package com.hmn.testaicode.ui.screens.selfie_capture.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
 fun SubmitPhotoButton(
    modifier: Modifier,
    onSubmit: () -> Unit,
) {
    val scheme = MaterialTheme.colorScheme
    Button(
        onClick = onSubmit,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = scheme.primary,
            contentColor = scheme.onPrimary,
        ),
    ) {
        Text(
            text = "Submit",
            style = MaterialTheme.typography.titleMedium,
        )
    }
}