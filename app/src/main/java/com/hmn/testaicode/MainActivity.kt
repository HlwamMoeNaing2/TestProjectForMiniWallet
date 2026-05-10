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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hmn.testaicode.ui.screens.face_capture.FaceDetectionScreen
import com.hmn.testaicode.ui.screens.id_matching.SelfieMatchingScreen

import com.hmn.testaicode.ui.theme.TestAICodeTheme
import dagger.hilt.android.AndroidEntryPoint

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
//                    EnterPhoneNumberScreen(
//                        Modifier.padding(innerPadding)
//                    )
                    MyApp ( Modifier.padding(innerPadding))
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
        startDestination = "screenA"
    ) {
        composable("screenA") {
            SelfieMatchingScreen(modifier = modifier, navController = navController)
        }
        composable("screenB") {
            FaceDetectionScreen(modifier)
        }
    }
}