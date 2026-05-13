package com.hmn.testaicode.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hmn.testaicode.ui.screens.cash_in.CashInScreen
import com.hmn.testaicode.ui.screens.cash_out.CashOutScreen
import com.hmn.testaicode.ui.screens.enterPhoneNumberScreen.EnterPhoneNumberScreen
import com.hmn.testaicode.ui.screens.global_constants.ImageType
import com.hmn.testaicode.ui.screens.history_detail.HistoryDetailScreen
import com.hmn.testaicode.ui.screens.id_capture.IDCaptureScreen
import com.hmn.testaicode.ui.screens.id_matching.SelfieMatchingScreen
import com.hmn.testaicode.ui.screens.main_menu.MainMenuScreen
import com.hmn.testaicode.ui.screens.personalInfo.PersonalInfoSubmitScreen
import com.hmn.testaicode.ui.screens.selfie_capture.FaceDetectionScreen
import com.hmn.testaicode.ui.screens.splash.SplashScreen
import com.hmn.testaicode.ui.screens.transaction_history_full_list.FullTransactionHistoryList
import com.hmn.testaicode.ui.screens.wallet_transfer.WalletTransferScreen
import kotlinx.coroutines.delay

@Composable
fun AppNavigation(modifier: Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.STARTUP_SCREEN,
    ) {
        composable(Routes.STARTUP_SCREEN) {
            StartupRoute(
                modifier = modifier,
                onReady = { isLoggedIn ->
                    val destination = if (isLoggedIn) {
                        Routes.MAIN_MENU_GRAPH
                    } else {
                        Routes.REGISTRATION_GRAPH
                    }
                    navController.navigate(Routes.MAIN_MENU_GRAPH) {
                        popUpTo(Routes.STARTUP_SCREEN) { inclusive = true }
                    }
                },
            )
        }

        navigation(
            route = Routes.REGISTRATION_GRAPH,
            startDestination = Routes.ENTER_PHONE_SCREEN,
        ) {
            composable(Routes.ENTER_PHONE_SCREEN) {
                EnterPhoneNumberScreen(
                    modifier = modifier,
                    onContinue = { phoneNumber ->
                        val isNewUser = simulateIsNewUser(phoneNumber)
                        if (isNewUser) {
                            navController.navigate(Routes.ID_SELFIE_MATCHING_SCREEN)
                        } else {
                            navController.navigate(Routes.MAIN_MENU_GRAPH) {
                                popUpTo(Routes.REGISTRATION_GRAPH) { inclusive = true }
                            }
                        }
                    },
                )
            }
            composable(Routes.ID_SELFIE_MATCHING_SCREEN) {
                SelfieMatchingScreen(
                    modifier = modifier,
                    navController = navController,
                    onValidateIdentity = {
                        navController.navigate(Routes.PERSONAL_INFO_SCREEN)
                    },
                )
            }
            composable(
                route = "${Routes.FACE_DETECTION_SCREEN}/{imageType}",
                arguments = listOf(
                    navArgument("imageType") { type = NavType.StringType },
                ),
            ) { backStackEntry ->
                val imageType = runCatching {
                    ImageType.valueOf(backStackEntry.arguments?.getString("imageType") ?: "")
                }.getOrDefault(ImageType.SELFIE)

                FaceDetectionScreen(
                    modifier = modifier,
                )
            }
            composable(
                route = "${Routes.ID_CAPTURING_SCREEN}/{imageType}",
                arguments = listOf(
                    navArgument("imageType") { type = NavType.StringType },
                ),
            ) { backStackEntry ->
                val imageType = runCatching {
                    ImageType.valueOf(backStackEntry.arguments?.getString("imageType") ?: "")
                }.getOrDefault(ImageType.SELFIE)

                IDCaptureScreen(
                    modifier = modifier,
                    imageType = imageType,
                    onClose = { navController.popBackStack() },
                )
            }
            composable(Routes.PERSONAL_INFO_SCREEN) {
                PersonalInfoSubmitScreen(
                    modifier = modifier,
                    onSubmit = { _, _, _, _, _, _ ->
                        navController.navigate(Routes.MAIN_MENU_GRAPH) {
                            popUpTo(Routes.REGISTRATION_GRAPH) { inclusive = true }
                        }
                    },
                )
            }
        }

        navigation(
            route = Routes.MAIN_MENU_GRAPH,
            startDestination = Routes.MAIN_MENU_SCREEN,
        ) {
            composable(Routes.MAIN_MENU_SCREEN) {
                MainMenuScreen(
                    modifier = modifier,
                    onNavigateToWalletTransfer = {
                        navController.navigate(Routes.WALLET_TRANSFER_SCREEN) {
                            launchSingleTop = true
                        }
                    },
                )
            }
        }

        // Non-tab destinations (reachable from anywhere inside Main Menu).
        composable(Routes.CASH_IN_SCREEN) {
            CashInScreen(modifier)
        }

        composable(Routes.CASH_OUT_SCREEN) {
            CashOutScreen(modifier)
        }

        composable(Routes.WALLET_TRANSFER_SCREEN) {
            WalletTransferScreen(modifier,navController)
        }

        composable(Routes.FULL_TRANSACTION_HISTORY_SCREEN) {
            FullTransactionHistoryList()
        }

        composable(Routes.RECEIPT_SCREEN) {
            HistoryDetailScreen(modifier = modifier)
        }
    }
}

@Composable
private fun StartupRoute(
    modifier: Modifier = Modifier,
    onReady: (Boolean) -> Unit,
) {
    LaunchedEffect(Unit) {
        val isAlreadyLoggedIn = false
        delay(350)
        onReady(isAlreadyLoggedIn)
    }

    SplashScreen()
}

private fun simulateIsNewUser(phoneNumber: String): Boolean {
    val lastDigit = phoneNumber.lastOrNull()?.digitToIntOrNull() ?: return true
    return lastDigit % 2 == 0
}
