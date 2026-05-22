package com.hmn.testaicode.ui.screens.home_screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowOutward
import androidx.compose.material.icons.outlined.SyncAlt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hmn.testaicode.ui.screens.home_screen.components.ActivityRow
import com.hmn.testaicode.ui.screens.home_screen.components.BalanceCard
import com.hmn.testaicode.ui.screens.home_screen.components.QuickActionTile
import com.hmn.testaicode.ui.screens.home_screen.components.RecentHeader
import com.hmn.testaicode.ui.screens.home_screen.components.TopGreetingRow
import com.hmn.testaicode.ui.theme.TestAICodeTheme
import com.hmn.testaicode.ui.theme.appBackgroundBrush

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    initials: String = "AC",
    phone: String = "+1 (415) 555 0102",
    balanceDisplay: String = "$5,037.50",
    maskedCard: String = "•••• 4421",
    recentCountLabel: String = "11 items",
    recent: List<HomeActivityItem> = demoHomeActivity(),
    onTransfer: () -> Unit = {},
    onCashIn: () -> Unit = {},
    onCashOut: () -> Unit = {},
    onNotifications: () -> Unit = {},
    onToggleBalanceVisibility: () -> Unit = {},
    viewModel:MainMenuViewModel = hiltViewModel()
) {
    val userState by viewModel.userStat.collectAsStateWithLifecycle()
    val scheme = MaterialTheme.colorScheme
//   .windowInsetsPadding(WindowInsets.systemBars)


    when(userState){
        is UserDataState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text((userState as UserDataState.Error).message)
            }
        }
        UserDataState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is UserDataState.Success -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(appBackgroundBrush())

                    .padding(horizontal = 18.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        TopGreetingRow(
                            initials = initials,
                            name =(userState as UserDataState.Success).user.name ,
                            onNotifications = onNotifications
                        )
                    }

                    item {
                        BalanceCard(
                            balanceDisplay = balanceDisplay,
                            maskedCard = maskedCard,
                            phone = phone,
                            onEyeClick = onToggleBalanceVisibility
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            QuickActionTile(
                                modifier = Modifier.weight(1f),
                                icon = Icons.Outlined.SyncAlt,
                                label = "Transfer",
                                onClick = onTransfer,
                            )
                            QuickActionTile(
                                modifier = Modifier.weight(1f),
                                icon = Icons.Outlined.Add,
                                label = "Cash In",
                                onClick = onCashIn
                            )
                            QuickActionTile(
                                modifier = Modifier.weight(1f),
                                icon = Icons.Outlined.ArrowOutward,
                                label = "Cash Out",
                                onClick = onCashOut
                            )
                        }
                    }

                    item {
                        RecentHeader(
                            countLabel = recentCountLabel
                        )
                    }

                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            color = scheme.surface,
                            tonalElevation = 0.dp,
                            shadowElevation = 0.dp,
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                recent.forEachIndexed { idx, it ->
                                    ActivityRow(item = it)
                                    if (idx != recent.lastIndex) {
                                        HorizontalDivider(color = scheme.outline.copy(alpha = 0.25f))
                                    }
                                }
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(10.dp)) }
                }
            }
        }
    }




}

data class HomeActivityItem(
    val title: String,
    val subtitle: String,
    val amount: String,
    val isNegative: Boolean,
)

 fun demoHomeActivity(): List<HomeActivityItem> {
    return List(5) {
        HomeActivityItem(
            title = "Sent to 709809898",
            subtitle = "May 9, 12:19 PM",
            amount = "-$9.00",
            isNegative = true
        )
    }
}










@Preview(showBackground = false, showSystemUi = false,uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun HomeScreenPreview() {
    TestAICodeTheme {
        HomeScreen()
    }
}