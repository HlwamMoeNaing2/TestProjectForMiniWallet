package com.hmn.testaicode.ui.screens.main_menu

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hmn.testaicode.ui.screens.home_screen.HomeScreen
import com.hmn.testaicode.ui.screens.inbox.InboxScreen
import com.hmn.testaicode.ui.screens.profile.ProfileScreen

private data class MainTab(
    val route: String,
    val label: String,
    val icon: ImageVector,
)

private object MainMenuTabRoutes {
    const val HOME = "main_menu_home"
    const val INBOX = "main_menu_inbox"
    const val PROFILE = "main_menu_profile"
}

@Composable
fun MainMenuScreen(
    modifier: Modifier = Modifier,
    onNavigateToWalletTransfer: () -> Unit = {},
) {
    val tabNavController = rememberNavController()
    val currentBackStackEntry = tabNavController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route
    val tabs = listOf(
        MainTab(MainMenuTabRoutes.HOME, "Home", Icons.Outlined.Home),
        MainTab(MainMenuTabRoutes.INBOX, "Inbox", Icons.Outlined.Inbox),
        MainTab(MainMenuTabRoutes.PROFILE, "Profile", Icons.Outlined.Person),
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                tabs.forEach { tab ->
                    NavigationBarItem(
                        selected = currentRoute == tab.route,
                        onClick = {
                            if (currentRoute == tab.route) return@NavigationBarItem
                            tabNavController.navigate(tab.route) {
                                popUpTo(tabNavController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { androidx.compose.material3.Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) },
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = tabNavController,
            startDestination = MainMenuTabRoutes.HOME,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(MainMenuTabRoutes.HOME) {
                HomeScreen(onTransfer = onNavigateToWalletTransfer)
            }
            composable(MainMenuTabRoutes.INBOX) {
                InboxScreen()
            }
            composable(MainMenuTabRoutes.PROFILE) {
                ProfileScreen()
            }
        }
    }
}
