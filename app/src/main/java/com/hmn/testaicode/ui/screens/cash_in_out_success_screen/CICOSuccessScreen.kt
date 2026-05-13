package com.hmn.testaicode.ui.screens.cash_in_out_success_screen

import android.content.res.Configuration
import android.util.Log
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.NorthEast
import androidx.compose.material.icons.outlined.SouthWest
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hmn.testaicode.navigation.Routes
import com.hmn.testaicode.ui.theme.TestAICodeTheme
import com.hmn.testaicode.ui.theme.appBackgroundBrush

@Composable
fun CICOSuccessScreen(
    modifier: Modifier = Modifier,
    rootNavController: NavController? = null,
    mode: CICMode = CICMode.CASH_IN,
    amountDisplay: String = "$250.00",
    title: String = if (mode == CICMode.CASH_IN) "Cash In Successful" else "Cash Out Successful",
    subtitle: String = if (mode == CICMode.CASH_IN) {
        "Your balance has been topped up."
    } else {
        "Your withdrawal is on the way."
    },
    referenceLabel: String = "Reference",
    referenceValue: String = "#TXN000000001",
    primaryActionLabel: String = "Done",
    secondaryActionLabel: String = "View receipt",
    onPrimaryAction: () -> Unit = {},
    onSecondaryAction: (() -> Unit)? = null,
) {
    val scheme = MaterialTheme.colorScheme
    val accent = scheme.primary
    val iconVector = when (mode) {
        CICMode.CASH_IN -> Icons.Outlined.SouthWest
        CICMode.CASH_OUT -> Icons.Outlined.NorthEast
    }

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
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = scheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp, vertical = 26.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(accent.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CheckCircle,
                        contentDescription = null,
                        tint = accent,
                        modifier = Modifier.size(40.dp),
                    )
                }

                Text(
                    text = amountDisplay,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = accent,
                    textAlign = TextAlign.Center,
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = scheme.onSurface,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = subtitle,
                        fontSize = 13.sp,
                        color = scheme.onSurface.copy(alpha = 0.62f),
                        textAlign = TextAlign.Center,
                    )
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = scheme.surfaceVariant.copy(alpha = 0.50f),
                    tonalElevation = 0.dp,
                    shadowElevation = 0.dp,
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = referenceLabel,
                                fontSize = 12.sp,
                                color = scheme.onSurface.copy(alpha = 0.60f),
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = referenceValue,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = scheme.onSurface,
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(accent.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = iconVector,
                                contentDescription = null,
                                tint = accent,
                                modifier = Modifier.size(22.dp),
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Button(
                    onClick = {
                        if (rootNavController != null) {
                            Log.d("#Hmn", "CICOSuccessScreen: doing ")
                            rootNavController.navigate(Routes.MAIN_MENU_GRAPH) {
                                // Ensure back-press from Home doesn't return to this screen.
                                popUpTo(Routes.MAIN_MENU_GRAPH) { inclusive = true }
                                launchSingleTop = true
                            }
                        } else {
                            Log.d("#Hmn", "CICOSuccessScreen: Null ")
                            onPrimaryAction()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = scheme.primary,
                        contentColor = scheme.onPrimary,
                    ),
                ) {
                    Text(text = primaryActionLabel, fontWeight = FontWeight.SemiBold)
                }

                if (onSecondaryAction != null) {
                    Button(
                        onClick = onSecondaryAction,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = scheme.surfaceVariant.copy(alpha = 0.55f),
                            contentColor = scheme.onSurface.copy(alpha = 0.85f),
                        ),
                    ) {
                        Text(text = secondaryActionLabel, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

enum class CICMode {
    CASH_IN,
    CASH_OUT,
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun CashInSuccessPreview() {
    TestAICodeTheme {
        CICOSuccessScreen(
            mode = CICMode.CASH_IN,
            amountDisplay = "$250.00",
            referenceValue = "#CASHIN-00001",
            onSecondaryAction = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun CashOutSuccessPreview() {
    TestAICodeTheme {
        CICOSuccessScreen(
            mode = CICMode.CASH_OUT,
            amountDisplay = "$120.00",
            referenceValue = "#CASHOUT-00002",
            onSecondaryAction = {},
        )
    }
}