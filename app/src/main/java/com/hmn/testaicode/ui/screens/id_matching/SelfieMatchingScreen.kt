package com.hmn.testaicode.ui.screens.id_matching

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hmn.testaicode.ui.screens.id_matching.components.IdUploadMiniCard
import com.hmn.testaicode.ui.screens.id_matching.components.IdentityTopBar
import com.hmn.testaicode.ui.screens.id_matching.components.SecurityNoticeCard
import com.hmn.testaicode.ui.screens.id_matching.components.SelfieSection
import com.hmn.testaicode.ui.screens.id_matching.components.VerificationStepperRow
import com.hmn.testaicode.ui.screens.id_matching.constants.AccentYellow
import com.hmn.testaicode.ui.screens.id_matching.constants.DisabledButtonBg
import com.hmn.testaicode.ui.screens.id_matching.constants.ScreenBg
import com.hmn.testaicode.ui.screens.id_matching.constants.SelfieMatchStatus
import com.hmn.testaicode.ui.screens.id_matching.constants.TextSecondary
import com.hmn.testaicode.ui.screens.id_matching.constants.VerificationStepState
import com.hmn.testaicode.ui.theme.TestAICodeTheme





@Composable
fun SelfieMatchingScreen(
    modifier: Modifier = Modifier,
    stepBasicInfo: VerificationStepState = VerificationStepState.Completed,
    stepDocuments: VerificationStepState = VerificationStepState.Active,
    stepReview: VerificationStepState = VerificationStepState.Upcoming,
    frontIdComplete: Boolean = true,
    backIdComplete: Boolean = false,
    selfieStatus: SelfieMatchStatus = SelfieMatchStatus.Processing,
    validateEnabled: Boolean = false,
    selfieAnalyzeProgress: Float = 0.28f,
    onBack: () -> Unit = {},
    onHelp: () -> Unit = {},
    onFrontIdCapture: () -> Unit = {},
    onBackIdCapture: () -> Unit = {},
    onValidateIdentity: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBg)
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        IdentityTopBar(
            onBack = onBack,
            onHelp = onHelp,
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 16.dp),
        ) {
            Text(
                text = "Upload Documents",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Please provide clear photos of your ID document and a selfie to complete the verification process.",
                color = TextSecondary,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            )
            Spacer(modifier = Modifier.height(24.dp))

            Spacer(modifier = Modifier.height(28.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                IdUploadMiniCard(
                    modifier = Modifier.weight(1f),
                    title = "Front ID",
                    previewLabel = "Front ID Preview",
                    done = false,
                    onCaptureClick = onFrontIdCapture,
                )
                IdUploadMiniCard(
                    modifier = Modifier.weight(1f),
                    title = "Back ID",
                    previewLabel = "Back ID Preview",
                    done = backIdComplete,
                    onCaptureClick = onBackIdCapture,
                )
            }
            Spacer(modifier = Modifier.height(20.dp))


            Box() { }

//            IdUploadMiniCard(
//                modifier = Modifier
//
//                    .height(20.dp)
//                ,
//                title = "Selfie",
//                previewLabel = "Selfie Preview",
//                done = backIdComplete,
//                onCaptureClick = onBackIdCapture,
//            )


//            SelfieSection(
//                status = selfieStatus,
//                progress = selfieAnalyzeProgress.coerceIn(0f, 1f),
//            )
            Spacer(modifier = Modifier.height(20.dp))
            SecurityNoticeCard()
        }
        Button(
            onClick = onValidateIdentity,
            enabled = validateEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp)
                .height(54.dp),
            shape = RoundedCornerShape(27.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (validateEnabled) AccentYellow else DisabledButtonBg,
                disabledContainerColor = DisabledButtonBg,
                contentColor = Color.White,
                disabledContentColor = Color.White.copy(alpha = 0.65f),
            ),
        ) {
            Text(
                text = "Validate Identity",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}














@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SelfieMatchingScreenPreview() {
    TestAICodeTheme {
        SelfieMatchingScreen()
    }
}
