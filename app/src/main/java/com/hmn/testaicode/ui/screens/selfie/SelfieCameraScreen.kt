package com.hmn.testaicode.ui.screens.selfie

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hmn.testaicode.ui.screens.utils.UiState

@Composable
fun SelfieCameraScreen(modifier: Modifier = Modifier) {
    val viewModel: SelfieViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Box(modifier = modifier) {
        when (uiState) {
            is UiState.Idle,
            is UiState.Loading,
            is UiState.Success,
            is UiState.Error -> Unit
        }
    }
}