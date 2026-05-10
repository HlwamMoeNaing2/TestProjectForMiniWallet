package com.hmn.testaicode.ui.screens.selfie_capture.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
    Button(
        onClick = onSubmit,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
    ) {
        Text(
            text ="Submit",
            style = MaterialTheme.typography.titleMedium,
        )
    }
}