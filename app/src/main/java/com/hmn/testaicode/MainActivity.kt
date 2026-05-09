package com.hmn.testaicode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.hmn.testaicode.ui.screens.cash_in.CashInScreen
import com.hmn.testaicode.ui.screens.cash_out.CashOutScreen
import com.hmn.testaicode.ui.screens.enterPhoneNumberScreen.EnterPhoneNumberScreen
import com.hmn.testaicode.ui.screens.history_detail.HistoryDetailScreen
import com.hmn.testaicode.ui.screens.home_screen.HomeScreen
import com.hmn.testaicode.ui.screens.idSubmitScreen.IDSubmitScreen
import com.hmn.testaicode.ui.screens.personalInfo.PersonalInfoSubmitScreen
import com.hmn.testaicode.ui.screens.profile.ProfileScreen
import com.hmn.testaicode.ui.screens.selfieSubmitScreen.SelfieSubmitScreen
import com.hmn.testaicode.ui.screens.transaction_history_full_list.FullTransactionHistoryList
import com.hmn.testaicode.ui.screens.wallet_transfer.WalletTransferScreen
import com.hmn.testaicode.ui.theme.TestAICodeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

// Configure the behavior: transient bars appear on swipe and hide again
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

// Hide the status bar specifically
        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())

        setContent {
            TestAICodeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    EnterPhoneNumberScreen(
//                        Modifier.padding(innerPadding)
//                    )
                    FullTransactionHistoryList( Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TestAICodeTheme {
        Greeting("Android")
    }
}