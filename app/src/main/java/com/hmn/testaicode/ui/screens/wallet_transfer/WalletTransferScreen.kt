package com.hmn.testaicode.ui.screens.wallet_transfer

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ColorScheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hmn.testaicode.extension.isValidPhone
import com.hmn.testaicode.navigation.Routes
import com.hmn.testaicode.ui.screens.enterPhoneNumberScreen.components.GradientContinueButton
import com.hmn.testaicode.ui.screens.wallet_transfer.components.FieldLabel
import com.hmn.testaicode.ui.screens.wallet_transfer.components.SendMoneyGradientButton
import com.hmn.testaicode.ui.theme.TestAICodeTheme
import com.hmn.testaicode.ui.theme.appBackgroundBrush

@Composable
fun WalletTransferScreen(
    modifier: Modifier = Modifier,
    //availableBalanceDisplay: String = "$5,234.50",
   // transferFeeDisplay: String = "Free",
    //onSendMoney: (recipient: String, amount: String, note: String) -> Unit = { _, _, _ -> },
    navController: NavController
) {
    val scheme = MaterialTheme.colorScheme
    val context = LocalContext.current
    var recipient by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    val amountValue = amount.toDoubleOrNull() ?: 0.0
    val canSend = recipient.trim().isNotEmpty() && amountValue > 0.0

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(appBackgroundBrush())
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(top = 26.dp)
            .imePadding()
          ,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp)
            ,horizontalAlignment = Alignment.CenterHorizontally

        ){
            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(scheme.primary.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Send,
                        contentDescription = null,
                        tint = scheme.tertiary,
                        modifier = Modifier.size(34.dp)
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                FieldLabel(text = "Recipient", scheme = scheme)
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = recipient,
                    onValueChange = { recipient = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    placeholder = {
                        Text(
                            text = "Enter name or phone number",
                            color = scheme.onSurface.copy(alpha = 0.40f),
                            fontSize = 15.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null,
                            tint = scheme.onSurface.copy(alpha = 0.55f)
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
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

                Spacer(modifier = Modifier.height(18.dp))

                FieldLabel(text = "Amount", scheme = scheme)
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
                            text = "0.00",
                            color = scheme.onSurface.copy(alpha = 0.40f),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    leadingIcon = {
                        Text(
                            text = "$",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = scheme.onSurface.copy(alpha = 0.65f),
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
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

                Spacer(modifier = Modifier.height(18.dp))

                FieldLabel(text = "Note (Optional)", scheme = scheme)
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    minLines = 4,
                    maxLines = 6,
                    shape = RoundedCornerShape(14.dp),
                    placeholder = {
                        Text(
                            text = "Add a note",
                            color = scheme.onSurface.copy(alpha = 0.40f),
                            fontSize = 15.sp
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
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

                Spacer(modifier = Modifier.height(22.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = scheme.primary.copy(alpha = 0.12f)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Available Balance",
                                fontSize = 14.sp,
                                color = scheme.onSurface.copy(alpha = 0.72f)
                            )
                            Text(
                                text = "43454",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = scheme.onSurface
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Transfer Fee",
                                fontSize = 14.sp,
                                color = scheme.onSurface.copy(alpha = 0.72f)
                            )
                            Text(
                                text = "4544",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = scheme.primary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))


            }
            Spacer(modifier = Modifier.height(10.dp))

            GradientContinueButton(
                enabled = canSend,
                onClick = {
                    Toast.makeText(context, "Continue", Toast.LENGTH_SHORT).show()
                    //onSendMoney(recipient.trim(), amount, note.trim())
                    navController.navigate(Routes.RECEIPT_SCREEN)
                }
            )

            Spacer(modifier = Modifier.height(10.dp))


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

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun WalletTransferScreenPreview() {
    TestAICodeTheme {
        WalletTransferScreen(navController = rememberNavController())
    }
}
