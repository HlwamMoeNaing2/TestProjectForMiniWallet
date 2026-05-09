package com.hmn.testaicode.ui.screens.transaction_history_full_list

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
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hmn.testaicode.ui.screens.home_screen.HomeActivityItem
import com.hmn.testaicode.ui.screens.home_screen.components.ActivityRow
import com.hmn.testaicode.ui.theme.LumenGradientBottom
import com.hmn.testaicode.ui.theme.LumenGradientTop
import com.hmn.testaicode.ui.theme.TestAICodeTheme

@Composable
fun FullTransactionHistoryList(
    modifier: Modifier = Modifier,
    items: List<HomeActivityItem> = demoFullHistory(),
    onBack: () -> Unit = {},
    onFilter: () -> Unit = {},
) {
    val scheme = MaterialTheme.colorScheme
    var query by remember { mutableStateOf("") }

    val filtered = remember(items, query) {
        val q = query.trim()
        if (q.isEmpty()) items
        else items.filter {
            it.title.contains(q, ignoreCase = true) ||
                it.subtitle.contains(q, ignoreCase = true) ||
                it.amount.contains(q, ignoreCase = true)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(LumenGradientTop, LumenGradientBottom)))
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(horizontal = 18.dp, vertical = 14.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // App bar
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(scheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = onBack, modifier = Modifier.size(40.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Back",
                            tint = scheme.onSurface
                        )
                    }
                }
                Text(
                    text = "Transaction History",
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 40.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = scheme.onBackground
                )
            }

            // Search + filter
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    placeholder = {
                        Text(
                            text = "Search transactions",
                            color = scheme.onSurface.copy(alpha = 0.40f),
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = null,
                            tint = scheme.onSurface.copy(alpha = 0.55f)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = scheme.outline.copy(alpha = 0.75f),
                        unfocusedBorderColor = scheme.outline.copy(alpha = 0.55f),
                        focusedContainerColor = scheme.surface,
                        unfocusedContainerColor = scheme.surface,
                        cursorColor = scheme.primary
                    )
                )
                Surface(
                    onClick = onFilter,
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    color = scheme.surface,
                    tonalElevation = 0.dp,
                    shadowElevation = 2.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.FilterList,
                            contentDescription = "Filter",
                            tint = scheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            // List
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = scheme.surface,
                tonalElevation = 0.dp,
                shadowElevation = 0.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    filtered.forEachIndexed { idx, item ->
                        ActivityRow(item = item)
                        if (idx != filtered.lastIndex) {
                            HorizontalDivider(color = scheme.outline.copy(alpha = 0.25f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

private fun demoFullHistory(): List<HomeActivityItem> = listOf(
    HomeActivityItem(
        title = "John Doe",
        subtitle = "Today, 2:30 PM  •  Transfer",
        amount = "+\$250.00",
        isNegative = false
    ),
    HomeActivityItem(
        title = "Coffee Shop",
        subtitle = "Today, 10:15 AM  •  Food",
        amount = "-\$15.50",
        isNegative = true
    ),
    HomeActivityItem(
        title = "Sarah Wilson",
        subtitle = "Yesterday, 5:20 PM  •  Transfer",
        amount = "+\$100.00",
        isNegative = false
    ),
    HomeActivityItem(
        title = "Electricity Bill",
        subtitle = "May 5, 3:45 PM  •  Bills",
        amount = "-\$85.00",
        isNegative = true
    ),
    HomeActivityItem(
        title = "Salary",
        subtitle = "May 1, 9:00 AM  •  Income",
        amount = "+\$2500.00",
        isNegative = false
    ),
    HomeActivityItem(
        title = "Grocery Store",
        subtitle = "Apr 30, 6:15 PM  •  Shopping",
        amount = "-\$125.30",
        isNegative = true
    ),
    HomeActivityItem(
        title = "Netflix Subscription",
        subtitle = "Apr 28, 8:00 AM  •  Subscription",
        amount = "-\$15.99",
        isNegative = true
    ),
    HomeActivityItem(
        title = "Refund - Amazon",
        subtitle = "Apr 27, 3:20 PM  •  Refund",
        amount = "+\$45.00",
        isNegative = false
    ),
    HomeActivityItem(
        title = "Gas Station",
        subtitle = "Apr 26, 7:45 AM  •  Transport",
        amount = "-\$60.00",
        isNegative = true
    ),
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FullTransactionHistoryListPreview() {
    TestAICodeTheme {
        FullTransactionHistoryList()
    }
}