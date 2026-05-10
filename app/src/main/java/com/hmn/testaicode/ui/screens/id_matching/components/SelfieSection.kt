package com.hmn.testaicode.ui.screens.id_matching.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
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
import com.hmn.testaicode.ui.screens.id_matching.constants.SuccessGreen
import com.hmn.testaicode.ui.screens.id_matching.constants.SurfaceCard
import com.hmn.testaicode.ui.screens.id_matching.constants.TextSecondary
import com.hmn.testaicode.ui.screens.id_matching.constants.SelfieMatchStatus

@Composable
 fun SelfieSection(
    status: SelfieMatchStatus,
    progress: Float,
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Selfie Photo",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
            )
            when (status) {
                SelfieMatchStatus.Required ->
                    StatusPill(text = "Required", done = false)

                SelfieMatchStatus.Processing ->
                    StatusPill(text = "Processing", done = false, showSpinner = true)

                SelfieMatchStatus.Done ->
                    StatusPill(text = "Done", done = true)
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = SurfaceCard,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 28.dp, horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                when (status) {
                    SelfieMatchStatus.Required -> {
                        Icon(
                            imageVector = Icons.Outlined.CameraAlt,
                            contentDescription = null,
                            tint = AccentYellow,
                            modifier = Modifier.size(40.dp),
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Capture a selfie to verify your identity",
                            color = TextSecondary,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        )
                    }

                    SelfieMatchStatus.Processing -> {
                        SelfieAnalyzingRing(progress = progress)
                        Spacer(modifier = Modifier.height(18.dp))
                        Text(
                            text = "Analyzing face data...",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Please keep the app open",
                            color = TextSecondary,
                            fontSize = 13.sp,
                        )
                    }

                    SelfieMatchStatus.Done -> {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = null,
                            tint = SuccessGreen,
                            modifier = Modifier.size(48.dp),
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Selfie verified",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}