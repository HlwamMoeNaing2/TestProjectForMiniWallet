package com.hmn.testaicode.ui.screens.profile

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hmn.testaicode.ui.screens.profile.components.Divider
import com.hmn.testaicode.ui.screens.profile.components.LogoutRow
import com.hmn.testaicode.ui.screens.profile.components.ProfileHeaderCard
import com.hmn.testaicode.ui.screens.profile.components.SectionLabel
import com.hmn.testaicode.ui.screens.profile.components.SettingsGroup
import com.hmn.testaicode.ui.screens.profile.components.SettingsRow
import com.hmn.testaicode.ui.theme.TestAICodeTheme
import com.hmn.testaicode.ui.theme.appBackgroundBrush

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    initials: String = "AC",
    name: String = "Alex Carter",
    phone: String = "+1 (415) 555 0102",
    appVersion: String = "v1.0.0",
    onPaymentMethods: () -> Unit = {},
    onSecurityPin: () -> Unit = {},
    onNotifications: () -> Unit = {},
    onHelpCenter: () -> Unit = {},
    onPreferences: () -> Unit = {},
    onLogout: () -> Unit = {},
) {
    val scheme = MaterialTheme.colorScheme


    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        Column(
            modifier = modifier
                .weight(1f)
                .fillMaxWidth()
                .background(appBackgroundBrush())
                .windowInsetsPadding(WindowInsets.systemBars)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp)
        ) {
            Spacer(Modifier.height(46.dp))
            Text(
                text = "Profile",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = scheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileHeaderCard(
                initials = initials,
                name = name,
                phone = phone
            )

            Spacer(modifier = Modifier.height(22.dp))

            SectionLabel("ACCOUNT")
            Spacer(modifier = Modifier.height(10.dp))
            SettingsGroup {
                SettingsRow(
                    icon = Icons.Outlined.CreditCard,
                    title = "Payment methods",
                    onClick = onPaymentMethods
                )
                Divider()
                SettingsRow(
                    icon = Icons.Outlined.Lock,
                    title = "Security & PIN",
                    onClick = onSecurityPin
                )
                Divider()
                SettingsRow(
                    icon = Icons.Outlined.NotificationsNone,
                    title = "Notifications",
                    onClick = onNotifications
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            SectionLabel("SUPPORT")
            Spacer(modifier = Modifier.height(10.dp))
            SettingsGroup {
                SettingsRow(
                    icon = Icons.Outlined.HelpOutline,
                    title = "Help center",
                    onClick = onHelpCenter
                )
                Divider()
                SettingsRow(
                    icon = Icons.Outlined.Settings,
                    title = "Preferences",
                    onClick = onPreferences
                )
            }

        }
      LogoutRow(
          modifier  = Modifier.padding(horizontal = 16.dp),
          onClick = onLogout)
        Text(
            text = "Lumen Wallet • $appVersion",
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            fontSize = 12.sp,
            color = scheme.onBackground.copy(alpha = 0.45f)
        )

        Spacer(modifier = Modifier.height(50.dp))
    }


}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProfileScreenPreview() {
    TestAICodeTheme {
        ProfileScreen()
    }
}