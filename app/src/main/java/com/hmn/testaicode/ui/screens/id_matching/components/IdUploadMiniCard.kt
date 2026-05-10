package com.hmn.testaicode.ui.screens.id_matching.components

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hmn.testaicode.ui.screens.id_matching.constants.AccentYellow
import com.hmn.testaicode.ui.screens.id_matching.constants.ConnectorInactive
import com.hmn.testaicode.ui.screens.id_matching.constants.SuccessGreen
import com.hmn.testaicode.ui.screens.id_matching.constants.SurfaceCard
import com.hmn.testaicode.ui.screens.id_matching.constants.TextSecondary
import com.hmn.testaicode.ui.theme.TestAICodeTheme

@Composable
fun IdUploadMiniCard(
    modifier: Modifier,
    title: String,
    previewLabel: String,
    done: Boolean,
    previewBitmap: Bitmap? = null,
    onCaptureClick: () -> Unit,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
            )
            StatusPill(
                text = if (done) "Done" else "Required",
                done = done,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.82f),
        ) {
            if (done) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(14.dp)),
                    shape = RoundedCornerShape(14.dp),
                    color = SurfaceCard,
                    border = BorderStroke(2.dp, SuccessGreen),
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        val safeBitmap = previewBitmap?.takeIf { !it.isRecycled }
                        if (safeBitmap != null) {
                            Image(
                                bitmap = safeBitmap.asImageBitmap(),
                                contentDescription = "ID Preview",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(12.dp))
                                    .padding(8.dp),
                                contentScale = ContentScale.Crop,
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = previewLabel,
                                    color = TextSecondary,
                                    fontSize = 12.sp,
                                )
                            }
                        }
                        Surface(
                            onClick = onCaptureClick,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                                .size(36.dp),
                            shape = CircleShape,
                            color = Color(0xFF2A2A2A),
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Icon(
                                    imageVector = Icons.Outlined.Replay,
                                    contentDescription = "Retake",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp),
                                )
                            }
                        }
                    }
                }
            } else {
                val dash = floatArrayOf(10f, 8f)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(14.dp))
                        .background(SurfaceCard)
                        .drawBehind {
                            drawRoundRect(
                                color = ConnectorInactive,
                                style = Stroke(
                                    width = 2.dp.toPx(),
                                    pathEffect = PathEffect.dashPathEffect(dash, 0f),
                                ),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                                    14.dp.toPx(),
                                    14.dp.toPx(),
                                ),
                            )
                        }
                        .clickable(onClick = onCaptureClick),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Outlined.CameraAlt,
                            contentDescription = null,
                            tint = AccentYellow,
                            modifier = Modifier.size(32.dp),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tap to capture",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                        )
                        Text(
                            text = "Clear details",
                            color = TextSecondary,
                            fontSize = 11.sp,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SelfieMatchingScreenPreview() {
    TestAICodeTheme {

        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            IdUploadMiniCard(
                modifier = Modifier.fillMaxSize(),
                title = "Test",
                previewLabel = "Preview",
                done = true,
                onCaptureClick = {},
            )
        }


    }
}