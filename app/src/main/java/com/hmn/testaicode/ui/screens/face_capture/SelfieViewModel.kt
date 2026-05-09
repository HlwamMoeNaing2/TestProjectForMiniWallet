package com.hmn.testaicode.ui.screens.face_capture

import android.graphics.Bitmap
import androidx.camera.core.CameraSelector
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

    /** Allows emitting another [UiState.Success] after a completed submit ([StateFlow] dedupes equal values). */
    fun consumeSubmitSuccess() {
        if (_uiState.value is UiState.Success) {
            _uiState.value = UiState.Idle
        }
    }

    fun saveBitmap(
        bitmap: Bitmap,
        isFromMissingInfoJourney: Boolean,
        lensFacing: Int = CameraSelector.LENS_FACING_FRONT,
    ) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val orientedBitmap =
                if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
                    BitmapUtils.flipBitmapHorizontallySafe(bitmap)
                } else {
                    bitmap
                }
            try {
                val bitmapEncodeResult = bitmapEncoder.encode("live-photo", orientedBitmap)
                val filePath = bitmapEncodeResult.file.absolutePath
                //  waveKycImagePrefManager.saveFacePhotoPath(filePath)
                _uiState.value = UiState.Success(filePath)
            } finally {
                if (orientedBitmap !== bitmap && !orientedBitmap.isRecycled) {
                    orientedBitmap.recycle()
                }
            }
        }
    }

}