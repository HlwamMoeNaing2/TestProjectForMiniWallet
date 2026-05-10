package com.hmn.testaicode.ui.screens.id_capture

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hmn.testaicode.ui.screens.id_capture.components.BottomActionSection
import com.hmn.testaicode.ui.screens.id_capture.components.HeaderRow
import com.hmn.testaicode.ui.screens.id_capture.components.InstructionCard
import com.hmn.testaicode.ui.screens.utils.BitmapUtils
import com.hmn.testaicode.ui.screens.utils.cropBitmapToPreviewRect
import com.hmn.testaicode.ui.theme.TestAICodeTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

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



private fun Context.hasCameraPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.CAMERA,
    ) == PackageManager.PERMISSION_GRANTED
}



@androidx.compose.ui.tooling.preview.Preview(showBackground = true, showSystemUi = true,uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun IDCaptureScreenPreview() {
    TestAICodeTheme {
        IDCaptureScreen()
    }
}