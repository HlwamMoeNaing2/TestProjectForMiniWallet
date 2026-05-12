package com.hmn.testaicode.ui.screens.id_matching

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmn.testaicode.data.AppStorageProviderRepo
import com.hmn.testaicode.data.SimpleResponseHandler
import com.hmn.testaicode.ui.screens.global_constants.ImageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel

class SelfieMatchingViewModel @Inject constructor(
    private val appStorageProviderRepo: AppStorageProviderRepo,
) : ViewModel() {

    private val _frontBitmap = MutableStateFlow<Bitmap?>(null)
    val frontBitmap: StateFlow<Bitmap?> = _frontBitmap.asStateFlow()

    private val _backBitmap = MutableStateFlow<Bitmap?>(null)
    val backBitmap: StateFlow<Bitmap?> = _backBitmap.asStateFlow()

    private val _selfieBitmap = MutableStateFlow<Bitmap?>(null)
    val selfieBitmap: StateFlow<Bitmap?> = _selfieBitmap.asStateFlow()

    init {
        refreshSavedImages()
    }

    /** Reload all saved images from disk */
    fun refreshSavedImages() {
        viewModelScope.launch(Dispatchers.IO) {
            retrieveAndEmit(ImageType.FRONT, _frontBitmap)
            retrieveAndEmit(ImageType.BACK, _backBitmap)
            retrieveAndEmit(ImageType.SELFIE, _selfieBitmap)
        }
    }

    private suspend fun retrieveAndEmit(
        type: ImageType,
        target: MutableStateFlow<Bitmap?>,
    ) {
        val decoded = when (val response = appStorageProviderRepo.retrieveBitmapFromAppStorage(type)) {
            is SimpleResponseHandler.Success -> response.data
            is SimpleResponseHandler.Error -> null
        }

        withContext(Dispatchers.Main.immediate) {
            replaceFlowBitmap(target, decoded)
        }
    }

    private fun replaceFlowBitmap(
        flow: MutableStateFlow<Bitmap?>,
        new: Bitmap?
    ) {
        val previous = flow.value
        if (previous != null && previous !== new && !previous.isRecycled) {
            previous.recycle()
        }
        flow.value = new
    }
}