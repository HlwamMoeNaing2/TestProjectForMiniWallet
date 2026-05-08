package com.hmn.testaicode.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.hmn.testaicode.ui.screens.LabelColor

@Composable
fun TermsFooter() {
    Text(
        text = "By continuing you agree to our Terms & Privacy.",
        fontSize = 12.sp,
        color = LabelColor,
        lineHeight = 18.sp,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}