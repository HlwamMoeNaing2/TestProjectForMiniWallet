package com.hmn.testaicode.ui.screens.id_matching.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.hmn.testaicode.ui.screens.id_matching.constants.ConnectorInactive
import com.hmn.testaicode.ui.screens.id_matching.constants.SuccessGreen

@Composable
fun StepConnector(
    modifier: Modifier = Modifier,
    completed: Boolean,
) {
    Box(
        modifier = modifier
            .padding(top = 14.dp)
            .height(4.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(if (completed) SuccessGreen else ConnectorInactive),
    )
}
