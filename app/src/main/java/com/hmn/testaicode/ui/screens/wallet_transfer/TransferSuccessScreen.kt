package com.hmn.testaicode.ui.screens.wallet_transfer

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.NorthEast
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hmn.testaicode.ui.screens.history_detail.components.DetailRow
import com.hmn.testaicode.ui.theme.TestAICodeTheme
import com.hmn.testaicode.ui.theme.appBackgroundBrush
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Composable
fun TransferSuccessScreen(
    modifier: Modifier = Modifier,
    transfer: TransferSuccessUiModel = TransferSuccessUiModel(),
    onDone: () -> Unit = {},
    onViewReceipt: (() -> Unit)? = null,
) {
    val scheme = MaterialTheme.colorScheme
    val positiveColor = scheme.primary
    val detailBg = scheme.surfaceVariant.copy(alpha = 0.55f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(appBackgroundBrush())
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(horizontal = 18.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center,
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 14.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = scheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(positiveColor.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.NorthEast,
                        contentDescription = null,
                        tint = positiveColor,
                        modifier = Modifier.size(36.dp),
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = transfer.amountDisplay(),
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = positiveColor,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = transfer.status.uppercase(),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = positiveColor,
                )

                Spacer(modifier = Modifier.height(18.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = detailBg,
                    shadowElevation = 0.dp,
                    tonalElevation = 0.dp,
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        DetailRow(label = "Completed at", value = formatCompletedAt(transfer.completedAt))
                        Spacer(modifier = Modifier.height(14.dp))
                        DetailRow(label = "From", value = transfer.fromPhoneNumber)
                        Spacer(modifier = Modifier.height(14.dp))
                        DetailRow(label = "To", value = transfer.toPhoneNumber)

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            color = scheme.outline.copy(alpha = 0.35f),
                        )

                        DetailRow(label = "Description", value = transfer.description.ifBlank { "—" })
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                Button(
                    onClick = onDone,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = scheme.primary,
                        contentColor = scheme.onPrimary,
                    ),
                ) {
                    Text(text = "Done", fontWeight = FontWeight.SemiBold)
                }

                if (onViewReceipt != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = onViewReceipt,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = scheme.surfaceVariant.copy(alpha = 0.55f),
                            contentColor = scheme.onSurface.copy(alpha = 0.85f),
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(text = "View receipt", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

data class TransferSuccessUiModel(
    val amount: Long = 5000,
    val completedAt: String = "2026-05-13T15:00:12.9351279",
    val description: String = "testing",
    val fromPhoneNumber: String = "09123456789",
    val toPhoneNumber: String = "0994849484",
    val status: String = "COMPLETED",
) {
    fun amountDisplay(): String = "$${"%,d".format(amount)}"
}

private fun formatCompletedAt(raw: String): String {
    // Accept either "2026-05-13T15:00:12.9351279" or an OffsetDateTime string.
    val trimmed = raw.trim()
    if (trimmed.isBlank()) return "—"

    val normalized = if (Regex("[zZ]|[+-]\\d{2}:\\d{2}$").containsMatchIn(trimmed)) {
        trimmed
    } else {
        // assume local time, pin to UTC for parsing/formatting
        "${trimmed}Z"
    }

    return try {
        val dt = OffsetDateTime.parse(normalized)
        dt.format(DateTimeFormatter.ofPattern("MMM d, yyyy • h:mm a"))
    } catch (_: DateTimeParseException) {
        raw
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun TransferSuccessPreview() {
    TestAICodeTheme {
        TransferSuccessScreen(
            transfer = TransferSuccessUiModel(
                amount = 5000,
                completedAt = "2026-05-13T15:00:12.9351279",
                description = "testing",
                fromPhoneNumber = "09123456789",
                toPhoneNumber = "0994849484",
                status = "COMPLETED",
            ),
            onViewReceipt = {},
        )
    }
}