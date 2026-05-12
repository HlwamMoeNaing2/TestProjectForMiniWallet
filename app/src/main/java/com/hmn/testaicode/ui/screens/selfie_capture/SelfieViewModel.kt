package com.hmn.testaicode.ui.screens.selfie_capture

import android.graphics.Bitmap
import android.provider.Contacts
import androidx.camera.core.CameraSelector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmn.testaicode.data.AppStorageProviderRepo
import com.hmn.testaicode.ui.screens.global_constants.ImageType
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
    private val appStorageRepo: AppStorageProviderRepo
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
        imageType: ImageType,
        bitmap: Bitmap
    ) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            runCatching {
                appStorageRepo.saveBitmapToAppStorage(bitmap, imageType)
            }.onSuccess {
                _uiState.value = UiState.Success("")
            }.onFailure {
                _uiState.value = UiState.Error("Something went wrong")
            }

        }
    }

}