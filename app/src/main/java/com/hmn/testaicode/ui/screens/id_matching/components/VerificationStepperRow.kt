package com.hmn.testaicode.ui.screens.id_matching.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hmn.testaicode.ui.screens.id_matching.constants.VerificationStepState

@Composable
 fun VerificationStepperRow(
    step1: VerificationStepState,
    step2: VerificationStepState,
    step3: VerificationStepState,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        StepColumn(
            modifier = Modifier.weight(1f),
            state = step1,
            stepNumber = 1,
            label = "Basic Info",
            showCheckWhenDone = true,
        )
        StepConnector(
            modifier = Modifier.weight(0.45f),
            completed = step1 == VerificationStepState.Completed,
        )
        StepColumn(
            modifier = Modifier.weight(1f),
            state = step2,
            stepNumber = 2,
            label = "Documents",
            showCheckWhenDone = false,
        )
        StepConnector(
            modifier = Modifier.weight(0.45f),
            completed = step2 == VerificationStepState.Completed,
        )
        StepColumn(
            modifier = Modifier.weight(1f),
            state = step3,
            stepNumber = 3,
            label = "Review",
            showCheckWhenDone = false,
        )
    }
}
