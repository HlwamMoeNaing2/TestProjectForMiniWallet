package com.hmn.testaicode.ui.screens.wallet_transfer

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hmn.testaicode.ui.screens.wallet_transfer.components.Label
import com.hmn.testaicode.ui.screens.wallet_transfer.components.PillField
import com.hmn.testaicode.ui.screens.wallet_transfer.components.SendMoneyButton
import com.hmn.testaicode.ui.theme.TestAICodeTheme
import com.hmn.testaicode.ui.theme.appBackgroundBrush

@Composable
fun WalletTransferScreen(
    modifier: Modifier = Modifier,
    availableBalance: String = "$4286.50",
    onBack: () -> Unit = {},
    onSend: (amount: String, recipientPhone: String, note: String) -> Unit = { _, _, _ -> },
) {
    val scheme = MaterialTheme.colorScheme

    var amount by remember { mutableStateOf("") }
    var recipientPhone by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    val amountValue = amount.toDoubleOrNull() ?: 0.0
    val canSend = amountValue > 0.0 && recipientPhone.trim().isNotEmpty()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(appBackgroundBrush())
            .windowInsetsPadding(WindowInsets.systemBars)
            .imePadding()
            .padding(horizontal = 18.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(34.dp)),
            color = scheme.surface.copy(alpha = 0.98f),
            shadowElevation = 6.dp,
            tonalElevation = 0.dp,
            shape = RoundedCornerShape(34.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 22.dp, vertical = 18.dp)
            ) {
                // Top bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(scheme.onSurface.copy(alpha = 0.06f)),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = onBack, modifier = Modifier.size(40.dp)) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBack,
                                contentDescription = "Back",
                                tint = scheme.onSurface
                            )
                        }
                    }
                    Text(
                        text = "Transfer",
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 40.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = scheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(26.dp))

                // Amount section
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "AMOUNT",
                        fontSize = 12.sp,
                        letterSpacing = 1.sp,
                        color = scheme.onSurface.copy(alpha = 0.45f)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "$",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Medium,
                            color = scheme.onSurface.copy(alpha = 0.55f),
                            modifier = Modifier.padding(bottom = 10.dp, end = 12.dp)
                        )
                        BasicTextField(
                            value = amount,
                            onValueChange = { raw ->
                                // digits + max one dot, max 2 decimals, max length guard
                                val filtered = buildString {
                                    var dotSeen = false
                                    var decimals = 0
                                    for (ch in raw) {
                                        when {
                                            ch.isDigit() -> {
                                                if (dotSeen) {
                                                    if (decimals < 2) {
                                                        append(ch); decimals++
                                                    }
                                                } else {
                                                    append(ch)
                                                }
                                            }
                                            ch == '.' && !dotSeen -> {
                                                dotSeen = true
                                                append(ch)
                                            }
                                        }
                                        if (length >= 12) break
                                    }
                                }
                                amount = filtered
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            textStyle = TextStyle(
                                fontSize = 56.sp,
                                fontWeight = FontWeight.Bold,
                                color = scheme.onSurface.copy(alpha = if (amount.isBlank()) 0.28f else 0.55f),
                                letterSpacing = (-1).sp,
                                textAlign = TextAlign.Center
                            ),
                            decorationBox = { inner ->
                                Box(contentAlignment = Alignment.CenterStart) {
                                    if (amount.isBlank()) {
                                        Text(
                                            text = "0.00",
                                            fontSize = 56.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = scheme.onSurface.copy(alpha = 0.28f),
                                            letterSpacing = (-1).sp
                                        )
                                    }
                                    inner()
                                }
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Available $availableBalance",
                        fontSize = 13.sp,
                        color = scheme.onSurface.copy(alpha = 0.45f)
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                Label("RECIPIENT PHONE")
                Spacer(modifier = Modifier.height(10.dp))
                PillField(
                    value = recipientPhone,
                    onValueChange = {
                        // keep digits + + and spaces (simple)
                        recipientPhone = it.take(20)
                    },
                    placeholder = "+1 415 555 0000",
                    keyboardType = KeyboardType.Phone
                )

                Spacer(modifier = Modifier.height(18.dp))

                Label("NOTE (OPTIONAL)")
                Spacer(modifier = Modifier.height(10.dp))
                PillField(
                    value = note,
                    onValueChange = { note = it.take(60) },
                    placeholder = "Dinner, rent...",
                    keyboardType = KeyboardType.Text
                )

                Spacer(modifier = Modifier.weight(1f))

                SendMoneyButton(
                    enabled = canSend,
                    onClick = { onSend(amount, recipientPhone, note) }
                )

                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }
}







@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun WalletTransferScreenPreview() {
    TestAICodeTheme {
        WalletTransferScreen()
    }
}