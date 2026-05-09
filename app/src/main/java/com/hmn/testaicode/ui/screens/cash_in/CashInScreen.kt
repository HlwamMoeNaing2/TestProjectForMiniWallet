package com.hmn.testaicode.ui.screens.cash_in

import android.content.res.Configuration
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.CreditCard
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hmn.testaicode.extension.isValidPhone
import com.hmn.testaicode.ui.screens.cash_in.components.PaymentMethodOption
import com.hmn.testaicode.ui.screens.enterPhoneNumberScreen.components.GradientContinueButton
import com.hmn.testaicode.ui.theme.TestAICodeTheme
import com.hmn.testaicode.ui.theme.appBackgroundBrush

@Composable
fun CashInScreen(
    modifier: Modifier = Modifier,
    onAddMoney: (String, CashInMethod) -> Unit = { _, _ -> },
) {

    val context = LocalContext.current
    val scheme = MaterialTheme.colorScheme

    var amount by remember { mutableStateOf("") }
    var selectedMethod by remember { mutableStateOf(CashInMethod.Card) }

    val amountValue = amount.toDoubleOrNull() ?: 0.0
    val canSubmit = amountValue > 0.0

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
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp)),
            color = scheme.surface.copy(alpha = 0.98f),
            shadowElevation = 6.dp,
            tonalElevation = 0.dp,
            shape = RoundedCornerShape(28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 22.dp, vertical = 26.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Deposit icon (arrow down)
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(scheme.tertiary.copy(alpha = 0.16f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowDownward,
                        contentDescription = null,
                        tint = scheme.tertiary,
                        modifier = Modifier.size(34.dp)
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                Text(
                    text = "Amount",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = scheme.onSurface.copy(alpha = 0.85f)
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = filterMoneyInput(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    placeholder = {
                        Text(
                            text = "$ 0.00",
                            color = scheme.onSurface.copy(alpha = 0.40f),
                            fontSize = 18.sp
                        )
                    },
                    leadingIcon = {
                        Text(
                            text = "$",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = scheme.onSurface.copy(alpha = 0.65f),
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = scheme.onSurface.copy(alpha = 0.92f)
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = scheme.primary.copy(alpha = 0.85f),
                        unfocusedBorderColor = scheme.outline.copy(alpha = 0.65f),
                        focusedContainerColor = scheme.surface,
                        unfocusedContainerColor = scheme.surface,
                        cursorColor = scheme.primary,
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf(50, 100, 200).forEach { preset ->
                        OutlinedButton(
                            onClick = { amount = preset.toString() },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, scheme.outline.copy(alpha = 0.65f)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = scheme.onSurface)
                        ) {
                            Text(
                                text = "$$preset",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(26.dp))

                Text(
                    text = "Select Payment Method",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = scheme.onSurface.copy(alpha = 0.85f)
                )
                Spacer(modifier = Modifier.height(12.dp))

                PaymentMethodOption(
                    title = "Credit/Debit Card",
                    subtitle = "Instant transfer",
                    icon = Icons.Outlined.CreditCard,
                    iconContainerColor = scheme.primary.copy(alpha = 0.14f),
                    iconTint = scheme.primary,
                    selected = selectedMethod == CashInMethod.Card,
                    onClick = { selectedMethod = CashInMethod.Card }
                )
                Spacer(modifier = Modifier.height(12.dp))
                PaymentMethodOption(
                    title = "Bank Account",
                    subtitle = "1–2 business days",
                    icon = Icons.Outlined.AccountBalance,
                    iconContainerColor = scheme.secondary.copy(alpha = 0.18f),
                    iconTint = scheme.secondary,
                    selected = selectedMethod == CashInMethod.Bank,
                    onClick = { selectedMethod = CashInMethod.Bank }
                )

                Spacer(modifier = Modifier.height(28.dp))

                val gradient = Brush.horizontalGradient(listOf(scheme.primary, scheme.secondary))
                val disabledGradient = Brush.horizontalGradient(
                    listOf(
                        scheme.primary.copy(alpha = 0.28f),
                        scheme.secondary.copy(alpha = 0.28f),
                    )
                )


                GradientContinueButton(
                    enabled = canSubmit,
                    onClick = {
                        Toast.makeText(context, "Continue", Toast.LENGTH_SHORT).show()
                        onAddMoney(amount, selectedMethod)
                    }
                )


                Spacer(modifier = Modifier.height(12.dp))
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



@Preview(showBackground = true, showSystemUi = true,uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun CashInScreenPreview() {
    TestAICodeTheme {
        CashInScreen()
    }
}
