package com.hmn.testaicode.ui.screens.id_matching.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hmn.testaicode.ui.screens.id_matching.constants.SuccessGreen

@Composable
fun StatusPill(
    text: String,
    done: Boolean,
    showSpinner: Boolean = false,
) {
    val scheme = MaterialTheme.colorScheme
    val accent = scheme.tertiary
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = when {
            showSpinner -> accent.copy(alpha = 0.22f)
            done -> SuccessGreen.copy(alpha = 0.22f)
            else -> scheme.surfaceVariant.copy(alpha = 0.65f)
        },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            if (showSpinner) {
                CircularProgressIndicator(
                    modifier = Modifier.size(12.dp),
                    strokeWidth = 2.dp,
                    color = accent,
                )
            } else if (done) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    tint = SuccessGreen,
                    modifier = Modifier.size(14.dp),
                )
            }
            Text(
                text = text,
                color = when {
                    showSpinner -> accent
                    done -> SuccessGreen
                    else -> scheme.onSurfaceVariant
                },
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                softWrap = false,
            )
        }
    }
}
