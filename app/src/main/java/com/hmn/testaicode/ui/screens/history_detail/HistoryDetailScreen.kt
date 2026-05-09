package com.hmn.testaicode.ui.screens.history_detail

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
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.SouthWest
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hmn.testaicode.ui.screens.history_detail.components.DetailRow
import com.hmn.testaicode.ui.theme.TestAICodeTheme
import com.hmn.testaicode.ui.theme.appBackgroundBrush

data class HistoryDetailUiModel(
    val amountDisplay: String = "+$250.00",
    val counterpartyLabel: String = "Received from",
    val counterpartyName: String = "John Doe",
    val statusLabel: String = "Completed",
    val transactionId: String = "#TXN000000001",
    val dateTime: String = "May 7, 2026 2:30 PM",
    val category: String = "Transfer",
    val paymentMethod: String = "Wallet",
    val note: String = "Payment for dinner last night",
)

@Composable
fun HistoryDetailScreen(
    modifier: Modifier = Modifier,
    detail: HistoryDetailUiModel = HistoryDetailUiModel(),
    onDownload: () -> Unit = {},
    onShare: () -> Unit = {},
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
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding( vertical = 14.dp)
            ,
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = scheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(positiveColor.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.SouthWest,
                        contentDescription = null,
                        tint = positiveColor,
                        modifier = Modifier.size(38.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = detail.amountDisplay,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = positiveColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = detail.counterpartyLabel,
                    fontSize = 13.sp,
                    color = scheme.onSurface.copy(alpha = 0.55f)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = detail.counterpartyName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = scheme.onSurface
                )

                Spacer(modifier = Modifier.height(22.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = detailBg,
                    shadowElevation = 0.dp,
                    tonalElevation = 0.dp,
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        DetailRow(
                            label = "Status",
                            valueContent = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Check,
                                        contentDescription = null,
                                        tint = positiveColor,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = detail.statusLabel,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = positiveColor
                                    )
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        DetailRow(
                            label = "Transaction ID",
                            value = detail.transactionId
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        DetailRow(
                            label = "Date & Time",
                            value = detail.dateTime
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        DetailRow(
                            label = "Category",
                            value = detail.category
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        DetailRow(
                            label = "Payment Method",
                            value = detail.paymentMethod
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            color = scheme.outline.copy(alpha = 0.35f)
                        )

                        Text(
                            text = "Note",
                            fontSize = 13.sp,
                            color = scheme.onSurface.copy(alpha = 0.55f)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = detail.note,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal,
                            color = scheme.onSurface,
                            lineHeight = 22.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDownload,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = scheme.surfaceVariant.copy(alpha = 0.55f),
                            contentColor = scheme.onSurface.copy(alpha = 0.78f)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Download,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(text = "Download", fontWeight = FontWeight.SemiBold)
                    }
                    OutlinedButton(
                        onClick = onShare,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = scheme.surfaceVariant.copy(alpha = 0.55f),
                            contentColor = scheme.onSurface.copy(alpha = 0.78f)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(text = "Share", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HistoryDetailScreenPreview() {
    TestAICodeTheme {
        HistoryDetailScreen()
    }
}
