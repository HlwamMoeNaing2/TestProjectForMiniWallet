package com.hmn.testaicode.ui.screens.id_matching
/*

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hmn.testaicode.ui.screens.global_constants.ImageType
import com.hmn.testaicode.ui.screens.global_constants.toFolderName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Loads persisted captures from [Application.getFilesDir]/`id_capture`/ using the same base names
 * as [com.hmn.testaicode.ui.screens.id_capture.IDCaptureViewModel.saveBitmapToAppStorage].
 *
 * Each [ImageType] maps to at most one JPEG on disk; flows expose the decoded bitmap or null.
 */
class SelfieMatchingViewModel(application: Application) : AndroidViewModel(application) {

    private val captureDir: File
        get() = File(getApplication<Application>().filesDir, CAPTURE_SUBDIR)

    private val _frontBitmap = MutableStateFlow<Bitmap?>(null)
    val frontBitmap: StateFlow<Bitmap?> = _frontBitmap.asStateFlow()

    private val _backBitmap = MutableStateFlow<Bitmap?>(null)
    val backBitmap: StateFlow<Bitmap?> = _backBitmap.asStateFlow()

    private val _selfieBitmap = MutableStateFlow<Bitmap?>(null)
    val selfieBitmap: StateFlow<Bitmap?> = _selfieBitmap.asStateFlow()

    init {
        refreshSavedImages()
    }

    /** Reload all three types from disk (call after returning from capture screens). */
    fun refreshSavedImages() {
        viewModelScope.launch(Dispatchers.IO) {
            decodeAndEmit(ImageType.FRONT, _frontBitmap)
            decodeAndEmit(ImageType.BACK, _backBitmap)
            decodeAndEmit(ImageType.SELFIE, _selfieBitmap)
        }
    }

    private suspend fun decodeAndEmit(type: ImageType, target: MutableStateFlow<Bitmap?>) {
        val file = File(captureDir, "${type.toFolderName()}.jpg")
        val decoded: Bitmap? = if (file.exists() && file.length() > 0L) {
            runCatching {
                BitmapFactory.Options().apply {
                    inPreferredConfig = Bitmap.Config.ARGB_8888
                }.let { opts ->
                    BitmapFactory.decodeFile(file.absolutePath, opts)
                }
            }.getOrNull()
        } else {
            null
        }

        withContext(Dispatchers.Main.immediate) {
            replaceFlowBitmap(target, decoded)
        }
    }

    private fun replaceFlowBitmap(flow: MutableStateFlow<Bitmap?>, new: Bitmap?) {
        val previous = flow.value
        if (previous != null && previous !== new && !previous.isRecycled) {
            previous.recycle()
        }
        flow.value = new
    }

    override fun onCleared() {
        super.onCleared()
        replaceFlowBitmap(_frontBitmap, null)
        replaceFlowBitmap(_backBitmap, null)
        replaceFlowBitmap(_selfieBitmap, null)
    }

    companion object {
        private const val CAPTURE_SUBDIR = "id_capture"
    }
}

 */


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmn.testaicode.ui.screens.global_constants.ImageType
import com.hmn.testaicode.ui.screens.global_constants.toFolderName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel

class SelfieMatchingViewModel @Inject constructor(
    @ApplicationContext private val context: Context

) : ViewModel() {

    private val captureDir: File
        get() = File(context.filesDir, CAPTURE_SUBDIR)

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
            decodeAndEmit(ImageType.FRONT, _frontBitmap)
            decodeAndEmit(ImageType.BACK, _backBitmap)
            decodeAndEmit(ImageType.SELFIE, _selfieBitmap)
        }
    }

    private suspend fun decodeAndEmit(
        type: ImageType,
        target: MutableStateFlow<Bitmap?>
    ) {
        val file = File(captureDir, "${type.toFolderName()}.jpg")

        val decoded: Bitmap? = if (file.exists() && file.length() > 0L) {
            runCatching {
                BitmapFactory.Options().apply {
                    inPreferredConfig = Bitmap.Config.ARGB_8888
                }.let { opts ->
                    BitmapFactory.decodeFile(file.absolutePath, opts)
                }
            }.getOrNull()
        } else null

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

    companion object {
        private const val CAPTURE_SUBDIR = "id_capture"
    }
}