package com.hmn.testaicode.ui.screens.id_matching.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hmn.testaicode.ui.screens.id_matching.constants.AccentYellow
import com.hmn.testaicode.ui.screens.id_matching.constants.ConnectorInactive
import com.hmn.testaicode.ui.screens.id_matching.constants.StepInactiveFill
import com.hmn.testaicode.ui.screens.id_matching.constants.SuccessGreen
import com.hmn.testaicode.ui.screens.id_matching.constants.TextSecondary
import com.hmn.testaicode.ui.screens.id_matching.constants.VerificationStepState


@Composable
 fun StepColumn(
    modifier: Modifier,
    state: VerificationStepState,
    stepNumber: Int,
    label: String,
    showCheckWhenDone: Boolean,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val circleColor = when (state) {
            VerificationStepState.Completed -> SuccessGreen
            VerificationStepState.Active -> AccentYellow
            VerificationStepState.Upcoming -> StepInactiveFill
        }
        val ringColor = when (state) {
            VerificationStepState.Completed -> SuccessGreen
            VerificationStepState.Active -> AccentYellow
            VerificationStepState.Upcoming -> ConnectorInactive
        }
        Box(
            modifier = Modifier.size(32.dp),
            contentAlignment = Alignment.Center,
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = CircleShape,
                color = circleColor,
                border = BorderStroke(2.dp, ringColor),
            ) {}
            when {
                state == VerificationStepState.Completed && showCheckWhenDone ->
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp),
                    )

                state != VerificationStepState.Completed || !showCheckWhenDone ->
                    Text(
                        text = stepNumber.toString(),
                        color = when (state) {
                            VerificationStepState.Completed -> Color.White
                            VerificationStepState.Active -> Color.Black
                            VerificationStepState.Upcoming -> TextSecondary
                        },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            color = when (state) {
                VerificationStepState.Completed -> SuccessGreen
                VerificationStepState.Active -> AccentYellow
                VerificationStepState.Upcoming -> TextSecondary
            },
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 2,
            lineHeight = 13.sp,
        )
    }
}