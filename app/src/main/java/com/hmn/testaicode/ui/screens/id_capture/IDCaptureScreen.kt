package com.hmn.testaicode.ui.screens.id_capture

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.util.Log
import android.view.Surface
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FlashOff
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hmn.testaicode.ui.screens.utils.BitmapUtils
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.math.abs

@Composable
fun IDCaptureScreen(
    modifier: Modifier = Modifier,
    onClose: () -> Unit = {},
    onHelp: () -> Unit = {},
    onSubmitSuccess: (String) -> Unit = {},
) {
    val tag = "IDCaptureScreen"
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val viewModel: IDCaptureViewModel = viewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var hasCameraPermission by remember { mutableStateOf(context.hasCameraPermission()) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        hasCameraPermission = granted
        Log.d(tag, "Camera permission result: granted=$granted")
    }

    val previewView = remember {
        PreviewView(context).apply {
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }

    val previewUseCase = remember {
        Preview.Builder().build()
    }
    val imageCaptureUseCase = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }

    var boundCamera by remember { mutableStateOf<Camera?>(null) }
    var captureAreaSize by remember { mutableStateOf(IntSize.Zero) }
    val capturedBitmap = state.capturedBitmap

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    LaunchedEffect(hasCameraPermission, lifecycleOwner, previewView, capturedBitmap) {
        if (!hasCameraPermission || capturedBitmap != null) return@LaunchedEffect
        runCatching {
            val provider = context.awaitCameraProvider()
            provider.unbindAll()
            previewUseCase.surfaceProvider = previewView.surfaceProvider
            imageCaptureUseCase.targetRotation = previewView.display?.rotation ?: Surface.ROTATION_0
            boundCamera = provider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                previewUseCase,
                imageCaptureUseCase,
            )
            boundCamera?.cameraControl?.enableTorch(state.flashEnabled)
            Log.d(tag, "Camera bound. preview=${previewView.width}x${previewView.height}")
        }.onFailure { error ->
            Log.e(tag, "Failed to bind camera", error)
            viewModel.onCaptureError(error.message ?: "Failed to bind camera")
        }
    }

    LaunchedEffect(state.flashEnabled, boundCamera) {
        boundCamera?.cameraControl?.enableTorch(state.flashEnabled)
        imageCaptureUseCase.flashMode = if (state.flashEnabled) {
            ImageCapture.FLASH_MODE_ON
        } else {
            ImageCapture.FLASH_MODE_OFF
        }
    }

    LaunchedEffect(captureAreaSize) {
        if (captureAreaSize != IntSize.Zero) {
            imageCaptureUseCase.targetRotation = previewView.display?.rotation ?: Surface.ROTATION_0
            Log.d(tag, "Capture area size=${captureAreaSize.width}x${captureAreaSize.height}")
        }
    }

    LaunchedEffect(state.errorMessage) {
        val message = state.errorMessage ?: return@LaunchedEffect
        scope.launch { snackbarHostState.showSnackbar(message) }
        viewModel.clearError()
    }

    DisposableEffect(Unit) {
        onDispose {
            runCatching {
                val provider = ProcessCameraProvider.getInstance(context).get()
                provider.unbindAll()
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        if (!hasCameraPermission) {
            PermissionStateContent(
                onRequestPermission = { permissionLauncher.launch(Manifest.permission.CAMERA) },
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                HeaderRow(
                    title = "မှတ်ပုံတင်ကတ် အရှေ့ဘက်",
                    onClose = onClose,
                    onHelp = onHelp,
                )

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.88f)
                                .widthIn(max = 420.dp)
                                .aspectRatio(1.58f)
                                .clip(RoundedCornerShape(22.dp))
                                .background(Color(0xFF161616))
                                .onSizeChanged { captureAreaSize = it },
                        ) {
                            if (capturedBitmap != null) {
                                androidx.compose.foundation.Image(
                                    bitmap = capturedBitmap.asImageBitmap(),
                                    contentDescription = "Captured ID",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                )
                            } else {
                                // PreviewView is intentionally constrained to this rectangle.
                                AndroidView(
                                    modifier = Modifier.fillMaxSize(),
                                    factory = { previewView },
                                    update = {
                                        if (it.controller == null) {
                                            previewUseCase.setSurfaceProvider(it.surfaceProvider)
                                        }
                                    },
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        InstructionCard()
                    }
                }

                BottomActionSection(
                    flashEnabled = state.flashEnabled,
                    isBusy = state.isCapturing || state.isSubmitting,
                    hasCapturedBitmap = capturedBitmap != null,
                    onToggleFlash = { viewModel.setFlashEnabled(!state.flashEnabled) },
                    onCapture = {
                        if (state.isCapturing || state.isSubmitting) return@BottomActionSection
                        if (captureAreaSize == IntSize.Zero) {
                            viewModel.onCaptureError("Capture area is not ready yet")
                            return@BottomActionSection
                        }
                        viewModel.startCapture()
                        imageCaptureUseCase.takePicture(
                            ContextCompat.getMainExecutor(context),
                            object : ImageCapture.OnImageCapturedCallback() {
                                override fun onCaptureSuccess(image: androidx.camera.core.ImageProxy) {
                                    try {
                                        val source = BitmapUtils.bitmapFromJpegImageProxy(image)
                                        val cropped = cropBitmapToPreviewRect(
                                            sourceBitmap = source,
                                            previewWidth = captureAreaSize.width,
                                            previewHeight = captureAreaSize.height,
                                        )
                                        if (cropped !== source && !source.isRecycled) {
                                            source.recycle()
                                        }
                                        Log.d(
                                            tag,
                                            "Capture complete source=${source.width}x${source.height}, " +
                                                "cropped=${cropped.width}x${cropped.height}"
                                        )
                                        viewModel.onCaptureSuccess(cropped)
                                    } catch (error: Throwable) {
                                        Log.e(tag, "Capture processing failed", error)
                                        viewModel.onCaptureError(error.message ?: "Image processing failed")
                                    } finally {
                                        image.close()
                                    }
                                }

                                override fun onError(exception: ImageCaptureException) {
                                    Log.e(tag, "CameraX capture failed", exception)
                                    viewModel.onCaptureError(exception.message ?: "Camera capture failed")
                                }
                            },
                        )
                    },
                    onSubmit = {
                        viewModel.submitCaptured { path ->
                            onSubmitSuccess(path)
                        }
                    },
                    onRetake = { viewModel.retake() },
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
        )
    }
}

@Composable
private fun HeaderRow(
    title: String,
    onClose: () -> Unit,
    onHelp: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.35f)),
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "Close",
                tint = Color.White,
            )
        }
        Text(
            text = title,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 52.dp),
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )
        IconButton(
            onClick = onHelp,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.35f)),
        ) {
            Icon(
                imageVector = Icons.Outlined.HelpOutline,
                contentDescription = "Help",
                tint = Color.White,
            )
        }
    }
}

@Composable
private fun InstructionCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.88f)
            .widthIn(max = 420.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF2A2C30))
            .padding(horizontal = 20.dp, vertical = 18.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "◻",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = "အမည်ကတ်ကိုကတ်ပြင် အတွင်း အောင်အောင်မြင်မြင် ထည့်ပေးပါ။",
                color = Color.White,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
private fun BottomActionSection(
    flashEnabled: Boolean,
    isBusy: Boolean,
    hasCapturedBitmap: Boolean,
    onToggleFlash: () -> Unit,
    onCapture: () -> Unit,
    onSubmit: () -> Unit,
    onRetake: () -> Unit,
) {
    if (!hasCapturedBitmap) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = onToggleFlash,
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF232529)),
            ) {
                Icon(
                    imageVector = if (flashEnabled) Icons.Outlined.FlashOn else Icons.Outlined.FlashOff,
                    contentDescription = "Toggle flash",
                    tint = Color.White,
                )
            }

            Spacer(modifier = Modifier.size(28.dp))

            Box(
                modifier = Modifier
                    .size(84.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(6.dp)
                    .clickable(enabled = !isBusy, onClick = onCapture),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color.Black),
                    contentAlignment = Alignment.Center,
                ) {
                    if (isBusy) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(28.dp),
                            strokeWidth = 3.dp,
                            color = Color.White,
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.PhotoCamera,
                            contentDescription = "Capture",
                            tint = Color.White,
                        )
                    }
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Button(
                onClick = onSubmit,
                enabled = !isBusy,
                modifier = Modifier
                    .fillMaxWidth(0.88f)
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5E62FF),
                    contentColor = Color.White,
                ),
                contentPadding = PaddingValues(horizontal = 16.dp),
            ) {
                if (isBusy) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.White,
                    )
                } else {
                    Text(text = "Submit", style = MaterialTheme.typography.titleMedium)
                }
            }
            Text(
                text = "Retake",
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier
                    .wrapContentSize()
                    .clickable(enabled = !isBusy, onClick = onRetake)
                    .padding(6.dp),
            )
        }
    }
}

@Composable
private fun PermissionStateContent(onRequestPermission: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Camera permission is required to capture ID.",
            color = Color.White,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRequestPermission) {
            Text("Grant Camera Permission")
        }
    }
}

private suspend fun Context.awaitCameraProvider(): ProcessCameraProvider =
    suspendCancellableCoroutine { continuation ->
        val future = ProcessCameraProvider.getInstance(this)
        future.addListener(
            {
                runCatching { future.get() }
                    .onSuccess { continuation.resume(it) }
                    .onFailure { error -> continuation.resumeWithException(error) }
            },
            ContextCompat.getMainExecutor(this),
        )
    }

/**
 * Crops the upright captured bitmap to match the on-screen preview rectangle's aspect ratio.
 *
 * This removes hidden camera regions introduced by center-crop scaling and keeps only what
 * users visually saw in the rounded preview area.
 */
private fun cropBitmapToPreviewRect(
    sourceBitmap: Bitmap,
    previewWidth: Int,
    previewHeight: Int,
): Bitmap {
    if (sourceBitmap.width <= 1 || sourceBitmap.height <= 1) return sourceBitmap
    if (previewWidth <= 1 || previewHeight <= 1) return sourceBitmap

    val sourceRatio = sourceBitmap.width.toFloat() / sourceBitmap.height.toFloat()
    val previewRatio = previewWidth.toFloat() / previewHeight.toFloat()

    if (abs(sourceRatio - previewRatio) < 0.0001f) return sourceBitmap

    val cropWidth: Int
    val cropHeight: Int
    val offsetX: Int
    val offsetY: Int

    if (sourceRatio > previewRatio) {
        // Source is wider than preview => crop left/right.
        cropHeight = sourceBitmap.height
        cropWidth = (cropHeight * previewRatio).toInt().coerceIn(1, sourceBitmap.width)
        offsetX = ((sourceBitmap.width - cropWidth) / 2).coerceAtLeast(0)
        offsetY = 0
    } else {
        // Source is taller than preview => crop top/bottom.
        cropWidth = sourceBitmap.width
        cropHeight = (cropWidth / previewRatio).toInt().coerceIn(1, sourceBitmap.height)
        offsetX = 0
        offsetY = ((sourceBitmap.height - cropHeight) / 2).coerceAtLeast(0)
    }

    return try {
        Bitmap.createBitmap(
            sourceBitmap,
            offsetX,
            offsetY,
            cropWidth,
            cropHeight,
        )
    } catch (_: Throwable) {
        sourceBitmap
    }
}

private fun Context.hasCameraPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.CAMERA,
    ) == PackageManager.PERMISSION_GRANTED
}