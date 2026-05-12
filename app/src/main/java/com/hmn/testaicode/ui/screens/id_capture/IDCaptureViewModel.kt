package com.hmn.testaicode.ui.screens.id_capture

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmn.testaicode.data.AppStorageProviderRepo
import com.hmn.testaicode.ui.screens.global_constants.ImageType
import com.hmn.testaicode.ui.screens.global_constants.toFolderName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

private const val TAG = "IDCaptureViewModel"

data class IDCaptureUiState(
    val flashEnabled: Boolean = false,
    val isCapturing: Boolean = false,
    val isSubmitting: Boolean = false,
    val capturedBitmap: Bitmap? = null,
    val errorMessage: String? = null,
)

@HiltViewModel
class IDCaptureViewModel @Inject constructor(private val appStorageRepo: AppStorageProviderRepo) : ViewModel() {

    private val _uiState = MutableStateFlow(IDCaptureUiState())
    val uiState: StateFlow<IDCaptureUiState> = _uiState.asStateFlow()

    fun setFlashEnabled(enabled: Boolean) {
        _uiState.update { it.copy(flashEnabled = enabled) }
    }

    fun startCapture() {
        _uiState.update { it.copy(isCapturing = true, errorMessage = null) }
    }

    fun onCaptureSuccess(bitmap: Bitmap) {
        val previous = _uiState.value.capturedBitmap
        if (previous != null && previous !== bitmap && !previous.isRecycled) {
            previous.recycle()
        }
        Log.d(TAG, "Capture success bitmap=${bitmap.width}x${bitmap.height}")
        _uiState.update {
            it.copy(
                isCapturing = false,
                capturedBitmap = bitmap,
                errorMessage = null,
            )
        }
    }

    fun onCaptureError(message: String) {
        Log.e(TAG, "Capture error: $message")
        _uiState.update { it.copy(isCapturing = false, errorMessage = message) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun retake() {
        val current = _uiState.value.capturedBitmap
        if (current != null && !current.isRecycled) current.recycle()
        _uiState.update {
            it.copy(
                isCapturing = false,
                capturedBitmap = null,
                errorMessage = null,
            )
        }
    }

    fun submitCaptured(imageType: ImageType) {
        val bitmap = _uiState.value.capturedBitmap
        if (bitmap == null) {
            onCaptureError("No captured image to submit")
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
            runCatching {
                appStorageRepo.saveBitmapToAppStorage(bitmap, imageType)
            }.onSuccess { path ->
                Log.d(TAG, "Image saved: $path")
                _uiState.update { it.copy(isSubmitting = false) }
               // onSubmitSuccess(path)
            }.onFailure { error ->
                Log.e(TAG, "Submit failed", error)
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage = error.message ?: "Failed to save image",
                    )
                }
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        val bitmap = _uiState.value.capturedBitmap
        if (bitmap != null && !bitmap.isRecycled) {
            bitmap.recycle()
        }
    }
}

