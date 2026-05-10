package com.hmn.testaicode.ui.screens.id_capture

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hmn.testaicode.ui.screens.global_constants.ImageType
import com.hmn.testaicode.ui.screens.global_constants.toFolderName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG = "IDCaptureViewModel"

data class IDCaptureUiState(
    val flashEnabled: Boolean = false,
    val isCapturing: Boolean = false,
    val isSubmitting: Boolean = false,
    val capturedBitmap: Bitmap? = null,
    val savedFilePath: String? = null,
    val errorMessage: String? = null,
)

class IDCaptureViewModel(application: Application) : AndroidViewModel(application) {

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
                savedFilePath = null,
                errorMessage = null,
            )
        }
    }

    fun submitCaptured(onSubmitSuccess: (String) -> Unit = {},imageType: ImageType) {
        val bitmap = _uiState.value.capturedBitmap
        if (bitmap == null) {
            onCaptureError("No captured image to submit")
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
            runCatching {
                saveBitmapToAppStorage(bitmap,imageType)
            }.onSuccess { path ->
                Log.d(TAG, "Image saved: $path")
                _uiState.update { it.copy(isSubmitting = false, savedFilePath = path) }
                onSubmitSuccess(path)
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

    private suspend fun saveBitmapToAppStorage(bitmap: Bitmap,imageType: ImageType): String = withContext(Dispatchers.IO) {
        val context = getApplication<Application>()
        val parent = File(context.filesDir, "id_capture")
        if (!parent.exists()) {
            check(parent.mkdirs()) { "Unable to create folder: ${parent.absolutePath}" }
        }

        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val file = File(parent, "${imageType.toFolderName()}.jpg")
        FileOutputStream(file).use { stream ->
            val ok = bitmap.compress(Bitmap.CompressFormat.JPEG, 92, stream)
            check(ok) { "Bitmap compression failed" }
            stream.flush()
            runCatching { stream.fd.sync() }
        }
        file.absolutePath
    }

    override fun onCleared() {
        super.onCleared()
        val bitmap = _uiState.value.capturedBitmap
        if (bitmap != null && !bitmap.isRecycled) {
            bitmap.recycle()
        }
    }
}

