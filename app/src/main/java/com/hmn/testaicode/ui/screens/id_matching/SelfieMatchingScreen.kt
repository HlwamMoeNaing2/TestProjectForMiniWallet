package com.hmn.testaicode.ui.screens.id_matching

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hmn.testaicode.navigation.Routes
import com.hmn.testaicode.ui.screens.global_constants.ImageType
import com.hmn.testaicode.ui.screens.id_matching.components.IdUploadMiniCard
import com.hmn.testaicode.ui.screens.id_matching.components.IdentityTopBar
import com.hmn.testaicode.ui.screens.id_matching.components.SecurityNoticeCard
import com.hmn.testaicode.ui.screens.enterPhoneNumberScreen.components.GradientContinueButton
import com.hmn.testaicode.ui.theme.TestAICodeTheme
import com.hmn.testaicode.ui.theme.appBackgroundBrush

@Composable
fun SelfieMatchingScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onBack: () -> Unit = {},
    onHelp: () -> Unit = {},
    onValidateIdentity: () -> Unit = {},
) {
    if (LocalInspectionMode.current) {
        SelfieMatchingScreenContent(
            modifier = modifier,
            navController = navController,
            frontPreviewBitmap = null,
            backPreviewBitmap = null,
            selfiePreviewBitmap = null,
            validateEnabled = false,
            onBack = onBack,
            onHelp = onHelp,
            onValidateIdentity = onValidateIdentity,
        )
        return
    }

    val viewModel: SelfieMatchingViewModel = hiltViewModel()
    val frontBitmap by viewModel.frontBitmap.collectAsStateWithLifecycle()
    val backBitmap by viewModel.backBitmap.collectAsStateWithLifecycle()
    val selfieBitmap by viewModel.selfieBitmap.collectAsStateWithLifecycle()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    LaunchedEffect(navBackStackEntry) {
        viewModel.refreshSavedImages()
    }

    val validateEnabled =
        frontBitmap != null && backBitmap != null && selfieBitmap != null

    SelfieMatchingScreenContent(
        modifier = modifier,
        navController = navController,
        frontPreviewBitmap = frontBitmap,
        backPreviewBitmap = backBitmap,
        selfiePreviewBitmap = selfieBitmap,
        validateEnabled = validateEnabled,
        onBack = onBack,
        onHelp = onHelp,
        onValidateIdentity = onValidateIdentity,
    )
}

@Composable
private fun SelfieMatchingScreenContent(
    modifier: Modifier,
    navController: NavController,
    frontPreviewBitmap: Bitmap?,
    backPreviewBitmap: Bitmap?,
    selfiePreviewBitmap: Bitmap?,
    validateEnabled: Boolean,
    onBack: () -> Unit,
    onHelp: () -> Unit,
    onValidateIdentity: () -> Unit,
) {
    val scheme = MaterialTheme.colorScheme
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(appBackgroundBrush())
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        Spacer(Modifier.height(16.dp))
        IdentityTopBar(
            modifier = Modifier.padding(horizontal = 18.dp),
            onBack = onBack,
            onHelp = onHelp,
        )
        Spacer(Modifier.height(8.dp))
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            shape = RoundedCornerShape(28.dp),
            color = scheme.surface.copy(alpha = 0.98f),
            shadowElevation = 6.dp,
            tonalElevation = 0.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 22.dp, vertical = 22.dp),
            ) {
            Text(
                text = "Upload Documents",
                color = scheme.onSurface,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Please provide clear photos of your ID document and a selfie to complete the verification process.",
                color = scheme.onSurface.copy(alpha = 0.72f),
                fontSize = 14.sp,
                lineHeight = 20.sp,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                IdUploadMiniCard(
                    modifier = Modifier.weight(1f),
                    title = "Front ID",
                    previewLabel = "Front ID Preview",
                    done = frontPreviewBitmap != null,
                    previewBitmap = frontPreviewBitmap,
                    onCaptureClick = {
                        navController.navigate(Routes.iDCaptureScreen(ImageType.FRONT))
                    },
                )
                IdUploadMiniCard(
                    modifier = Modifier.weight(1f),
                    title = "Back ID",
                    previewLabel = "Back ID Preview",
                    done = backPreviewBitmap != null,
                    previewBitmap = backPreviewBitmap,
                    onCaptureClick = {
                        navController.navigate(Routes.iDCaptureScreen(ImageType.BACK))
                    },
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val constraintsScope = this
                val slotWidth = (constraintsScope.maxWidth - 12.dp) / 2
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    IdUploadMiniCard(
                        modifier = Modifier.width(slotWidth),
                        title = "Selfie Photo",
                        previewLabel = "Selfie Preview",
                        done = selfiePreviewBitmap != null,
                        previewBitmap = selfiePreviewBitmap,
                        onCaptureClick = {
                            navController.navigate(Routes.faceDetectionScreen(ImageType.SELFIE))
                        },
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            SecurityNoticeCard()
            Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .padding(top = 12.dp, bottom = 20.dp),
        ) {
            GradientContinueButton(
                text = "Validate Identity",
                enabled = validateEnabled,
                onClick = onValidateIdentity,
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SelfieMatchingScreenPreview() {
    val navController = rememberNavController()
    TestAICodeTheme {
        SelfieMatchingScreen(navController = navController)
    }
}
