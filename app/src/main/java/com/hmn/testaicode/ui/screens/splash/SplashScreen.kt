package com.hmn.testaicode.ui.screens.splash

import android.window.SplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hmn.testaicode.ui.theme.appBackgroundBrush

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(appBackgroundBrush())
            .windowInsetsPadding(WindowInsets.systemBars),
        contentAlignment = Alignment.Center,
    ) {
        val scheme = MaterialTheme.colorScheme
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
                modifier = Modifier
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(28.dp),
                color = scheme.surface.copy(alpha = 0.96f),
                tonalElevation = 0.dp,
                shadowElevation = 6.dp,
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 28.dp, vertical = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier = Modifier
                            .size(78.dp)
                            .clip(CircleShape)
                            .background(scheme.primary.copy(alpha = 0.16f))
                            .border(
                                width = 1.dp,
                                color = scheme.primary.copy(alpha = 0.24f),
                                shape = CircleShape,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AccountBalanceWallet,
                            contentDescription = null,
                            tint = scheme.primary,
                            modifier = Modifier.size(34.dp),
                        )
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = "Lumen Wallet",
                        color = scheme.onSurface,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Starting secure session...",
                        color = scheme.onSurface.copy(alpha = 0.62f),
                        fontSize = 14.sp,
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    LinearProgressIndicator(
                        modifier = Modifier
                            .size(width = 210.dp, height = 6.dp)
                            .clip(RoundedCornerShape(999.dp)),
                        color = scheme.primary,
                        trackColor = Color.Transparent,
                    )
                }
            }
        }
    }
    
}