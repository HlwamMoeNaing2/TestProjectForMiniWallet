package com.hmn.testaicode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hmn.testaicode.navigation.Routes
import com.hmn.testaicode.ui.screens.enterPhoneNumberScreen.EnterPhoneNumberScreen
import com.hmn.testaicode.ui.screens.global_constants.ImageType
import com.hmn.testaicode.ui.screens.main_menu.MainMenuScreen
import com.hmn.testaicode.ui.screens.personalInfo.PersonalInfoSubmitScreen
import com.hmn.testaicode.ui.screens.id_capture.IDCaptureScreen
import com.hmn.testaicode.ui.screens.id_matching.SelfieMatchingScreen
import com.hmn.testaicode.ui.screens.selfie_capture.FaceDetectionScreen
import com.hmn.testaicode.ui.theme.TestAICodeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
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
                    MyApp(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MyApp(modifier: Modifier) {
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
                    navController.navigate(destination) {
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
                    navArgument("imageType") { type = NavType.StringType }
                ),
            ) { backStackEntry ->
                val imageType = runCatching {
                    ImageType.valueOf(backStackEntry.arguments?.getString("imageType") ?: "")
                }.getOrDefault(ImageType.SELFIE)

                FaceDetectionScreen(
                    modifier = modifier,
                    imageType = imageType,
                    onSubmitSuccess = { navController.popBackStack() },
                )
            }
            composable(
                route = "${Routes.ID_CAPTURING_SCREEN}/{imageType}",
                arguments = listOf(
                    navArgument("imageType") { type = NavType.StringType }
                ),
            ) { backStackEntry ->
                val imageType = runCatching {
                    ImageType.valueOf(backStackEntry.arguments?.getString("imageType") ?: "")
                }.getOrDefault(ImageType.SELFIE)

                IDCaptureScreen(
                    modifier = modifier,
                    imageType = imageType,
                    onClose = { navController.popBackStack() },
                    onSubmitSuccess = { navController.popBackStack() },
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
                MainMenuScreen(modifier = modifier)
            }
        }
    }
}

@Composable
private fun StartupRoute(
    modifier: Modifier = Modifier,
    onReady: (Boolean) -> Unit,
) {
    LaunchedEffect(Unit) {
        // Simulated local session flag until real auth state is wired.
        val isAlreadyLoggedIn = false
        delay(350)
        onReady(isAlreadyLoggedIn)
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

private fun simulateIsNewUser(phoneNumber: String): Boolean {
    // Simulated API result: even last digit => new user, odd => existing user.
    val lastDigit = phoneNumber.lastOrNull()?.digitToIntOrNull() ?: return true
    return lastDigit % 2 == 0
}
