package com.hmn.testaicode.ui.screens.cash_out

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.LocalAtm
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hmn.testaicode.ui.screens.cash_out.components.WithdrawMethodRow
import com.hmn.testaicode.ui.screens.enterPhoneNumberScreen.components.GradientContinueButton
import com.hmn.testaicode.ui.theme.TestAICodeTheme
import com.hmn.testaicode.ui.theme.appBackgroundBrush

@Composable
fun CashOutScreen(
    modifier: Modifier = Modifier,
   // onWithdraw: (String, CashOutMethod) -> Unit = { _, _ -> },
) {
    val scheme = MaterialTheme.colorScheme
    val context = LocalContext.current
    var amount by remember { mutableStateOf("") }
    var selectedMethod by remember { mutableStateOf(CashOutMethod.BankTransfer) }

    val amountValue = amount.toDoubleOrNull() ?: 0.0
    val canWithdraw = amountValue > 0.0

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(appBackgroundBrush())
            .windowInsetsPadding(WindowInsets.systemBars)
            .imePadding()
            .padding(horizontal = 18.dp, vertical = 40.dp),
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
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 22.dp, vertical = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(scheme.primary.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccountBalanceWallet,
                        contentDescription = null,
                        tint = scheme.primary,
                        modifier = Modifier.size(34.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = scheme.surfaceVariant.copy(alpha = 0.45f),
                    border = BorderStroke(1.dp, scheme.outline.copy(alpha = 0.35f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Available Balance",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = scheme.onSurface.copy(alpha = 0.75f)
                        )
                        Text(
                            text = "67898",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = scheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                Text(
                    text = "Amount",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = scheme.onSurface.copy(alpha = 0.75f)
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = { raw ->
                        amount = filterMoneyInput(raw)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    leadingIcon = {
                        Text(
                            text = "$",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = scheme.onSurface.copy(alpha = 0.65f),
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    },
                    placeholder = {
                        Text(
                            text = "0.00",
                            color = scheme.onSurface.copy(alpha = 0.35f),
                            fontSize = 20.sp
                        )
                    },
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = scheme.onSurface.copy(alpha = 0.92f)
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = scheme.primary.copy(alpha = 0.85f),
                        unfocusedBorderColor = scheme.outline.copy(alpha = 0.65f),
                        focusedContainerColor = scheme.surface,
                        unfocusedContainerColor = scheme.surface,
                        cursorColor = scheme.primary,
                        focusedLeadingIconColor = scheme.onSurface.copy(alpha = 0.65f),
                        unfocusedLeadingIconColor = scheme.onSurface.copy(alpha = 0.65f),
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf(20, 50, 100).forEach { preset ->
                        OutlinedButton(
                            onClick = { amount = preset.toString() },
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, scheme.outline.copy(alpha = 0.65f)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = scheme.onSurface
                            )
                        ) {
                            Text(
                                text = "$$preset",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                Text(
                    text = "Select Withdrawal Method",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = scheme.onSurface.copy(alpha = 0.75f)
                )
                Spacer(modifier = Modifier.height(10.dp))

                WithdrawMethodRow(
                    title = "Bank Transfer",
                    subtitle = "1–2 business days • Free",
                    icon = Icons.Outlined.AccountBalance,
                    selected = selectedMethod == CashOutMethod.BankTransfer,
                    onClick = { selectedMethod = CashOutMethod.BankTransfer }
                )
                Spacer(modifier = Modifier.height(12.dp))
                WithdrawMethodRow(
                    title = "ATM Withdrawal",
                    subtitle = "Instant • $2.50 fee",
                    icon = Icons.Outlined.LocalAtm,
                    selected = selectedMethod == CashOutMethod.AtmWithdrawal,
                    onClick = { selectedMethod = CashOutMethod.AtmWithdrawal }
                )

                Spacer(modifier = Modifier.height(28.dp))

                GradientContinueButton(
                    enabled = canWithdraw,
                    onClick = {
                        Toast.makeText(context, "Continue", Toast.LENGTH_SHORT).show()
                        //onWithdraw(amount, selectedMethod)
                    }
                )

/*
                Button(
                    onClick = { onWithdraw(amount, selectedMethod) },
                    enabled = canWithdraw,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = scheme.primary,
                        contentColor = scheme.onPrimary,
                        disabledContainerColor = scheme.onSurface.copy(alpha = 0.12f),
                        disabledContentColor = scheme.onSurface.copy(alpha = 0.38f),
                    )
                ) {
                    Text(
                        text = "Withdraw Money",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
 */

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}


private fun filterMoneyInput(raw: String): String {
    return buildString {
        var dotSeen = false
        var decimals = 0
        for (ch in raw) {
            when {
                ch.isDigit() -> {
                    if (dotSeen) {
                        if (decimals < 2) {
                            append(ch)
                            decimals++
                        }
                    } else append(ch)
                }
                ch == '.' && !dotSeen -> {
                    dotSeen = true
                    append(ch)
                }
            }
            if (length >= 12) break
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CashOutScreenPreview() {
    TestAICodeTheme {
        CashOutScreen()
    }
}
