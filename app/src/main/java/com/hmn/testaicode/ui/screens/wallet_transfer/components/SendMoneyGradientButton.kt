package com.hmn.testaicode.ui.screens.wallet_transfer.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hmn.testaicode.ui.screens.wallet_transfer.WalletTransferScreen
import com.hmn.testaicode.ui.theme.TestAICodeTheme

@Composable
fun SendMoneyGradientButton(
    modifier: Modifier,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val scheme = MaterialTheme.colorScheme
    val gradient = Brush.horizontalGradient(listOf(scheme.tertiary, scheme.primary))
    val disabledGradient = Brush.horizontalGradient(
        listOf(
            scheme.tertiary.copy(alpha = 0.35f),
            scheme.primary.copy(alpha = 0.35f),
        )
    )

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        contentPadding = PaddingValues()
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(28.dp))
                .background(if (enabled) gradient else disabledGradient),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Send Money",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = if (enabled) scheme.onTertiary else scheme.onSurface.copy(alpha = 0.38f)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun SendMoneyGradientButtonPreview() {
    TestAICodeTheme {

        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            SendMoneyGradientButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                enabled = false
            ) {

            }
        }
    }
}