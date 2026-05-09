package com.hmn.testaicode.ui.screens.selfie

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmn.testaicode.ui.screens.utils.BitmapEncoder
import com.hmn.testaicode.ui.screens.utils.BitmapUtils
import com.hmn.testaicode.ui.screens.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelfieViewModel @Inject constructor(
    private val bitmapEncoder: BitmapEncoder,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<String>>(UiState.Idle)

    val uiState: StateFlow<UiState<String>> = _uiState


    fun saveBitmap(bitmap: Bitmap, isFromMissingInfoJourney: Boolean) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            // Flip the bitmap horizontally to correct front camera mirroring
            val flippedBitmap = BitmapUtils.flipBitmapHorizontallySafe(bitmap)
            val bitmapEncodeResult = bitmapEncoder.encode("live-photo", flippedBitmap)
            val filePath = bitmapEncodeResult.file.absolutePath
          //  waveKycImagePrefManager.saveFacePhotoPath(filePath)
            _uiState.value = UiState.Success("")
        }
    }

}