package com.hmn.testaicode.ui.screens.wallet_transfer.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun PillField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType,
) {
    val scheme = MaterialTheme.colorScheme
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        singleLine = true,
        shape = RoundedCornerShape(27.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        placeholder = {
            Text(
                text = placeholder,
                color = scheme.onSurface.copy(alpha = 0.35f)
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = scheme.outline.copy(alpha = 0.8f),
            unfocusedBorderColor = scheme.outline.copy(alpha = 0.65f),
            focusedContainerColor = scheme.surface,
            unfocusedContainerColor = scheme.surface,
            cursorColor = scheme.primary,
        )
    )
}