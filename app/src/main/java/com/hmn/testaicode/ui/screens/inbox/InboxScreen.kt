package com.hmn.testaicode.ui.screens.inbox

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hmn.testaicode.ui.theme.TestAICodeTheme
import com.hmn.testaicode.ui.theme.appBackgroundBrush

@Composable
fun InboxScreen(modifier: Modifier = Modifier) {
    val scheme = MaterialTheme.colorScheme
    val notifications = demoInboxItems()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(appBackgroundBrush())
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 14.dp),
        ) {
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Inbox",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = scheme.onBackground,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Your notifications and updates",
                fontSize = 16.sp,
                color = scheme.onBackground.copy(alpha = 0.58f),
            )
            Spacer(modifier = Modifier.height(20.dp))

            notifications.forEach { item ->
                InboxNotificationCard(item = item)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun InboxNotificationCard(
    item: InboxNotificationItem,
    modifier: Modifier = Modifier,
) {
    val scheme = MaterialTheme.colorScheme
    val iconTint = Color(0xFF2642B8)
    val iconBg = Color(0xFFEFF1FF)

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(22.dp),
        shadowElevation = 1.dp,
        tonalElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(iconBg, CircleShape),
                contentAlignment = androidx.compose.ui.Alignment.Center,
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp),
                )
            }

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = item.title,
                            color = scheme.onSurface,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                        if (item.isUnread) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 9.dp)
                                    .size(7.dp)
                                    .background(Color(0xFF8B83FF), CircleShape),
                            )
                        }
                    }
                    Text(
                        text = item.timeLabel,
                        color = scheme.onSurface.copy(alpha = 0.52f),
                        fontSize = 15.sp,
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.message,
                    color = scheme.onSurface.copy(alpha = 0.62f),
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                )
            }
        }
    }
}

private data class InboxNotificationItem(
    val title: String,
    val message: String,
    val timeLabel: String,
    val isUnread: Boolean,
    val icon: ImageVector,
)

private fun demoInboxItems(): List<InboxNotificationItem> = listOf(
    InboxNotificationItem(
        title = "Verification approved",
        message = "Your identity has been verified.",
        timeLabel = "Just now",
        isUnread = true,
        icon = Icons.Outlined.Security,
    ),
    InboxNotificationItem(
        title = "Welcome bonus",
        message = "You earned \$5 for joining Lumen.",
        timeLabel = "2h ago",
        isUnread = true,
        icon = Icons.Outlined.Star,
    ),
    InboxNotificationItem(
        title = "Spending insight",
        message = "You spent 18% less this week.",
        timeLabel = "Yesterday",
        isUnread = false,
        icon = Icons.Outlined.TrendingUp,
    ),
    InboxNotificationItem(
        title = "Security tip",
        message = "Enable Face ID for faster login.",
        timeLabel = "Mon",
        isUnread = false,
        icon = Icons.Outlined.Notifications,
    ),
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun InboxScreenPreview() {
    TestAICodeTheme {
        InboxScreen()
    }
}