package com.hmn.testaicode.ui.screens.face_capture

/*

import android.Manifest
import android.graphics.Bitmap
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Cameraswitch
import androidx.compose.material.icons.outlined.FaceRetouchingNatural
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.hmn.testaicode.R
import com.hmn.testaicode.ui.screens.utils.BitmapUtils
import com.hmn.testaicode.ui.screens.utils.UiState
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private val SelfieGreen = Color(0xFF4ADE80)
private val InstructionCardBg = Color(0xFF2C2C2C)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun SelfieCameraScreen(
    modifier: Modifier = Modifier,
    isFromMissingInfoJourney: Boolean = false,
    onClose: () -> Unit = {},
    onCaptureSaved: () -> Unit = {},
) {
    val viewModel: SelfieViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val previewView =
        remember(context) {
            PreviewView(context).apply {
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }
        }

    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_FRONT) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var showHelpDialog by remember { mutableStateOf(false) }

    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)

    LaunchedEffect(Unit) {
        if (!cameraPermission.status.isGranted) {
            cameraPermission.launchPermissionRequest()
        }
    }

    LaunchedEffect(uiState) {
        when (val s = uiState) {
            is UiState.Error -> snackbarHostState.showSnackbar(s.message)
            is UiState.Success -> {
                onCaptureSaved()
                viewModel.consumeSubmitSuccess()
            }
            else -> Unit
        }
    }

    LaunchedEffect(lensFacing, cameraPermission.status.isGranted) {
        if (!cameraPermission.status.isGranted) return@LaunchedEffect

        val provider =
            try {
                context.getCameraProviderSuspended()
            } catch (_: Exception) {
                snackbarHostState.showSnackbar(context.getString(R.string.selfie_capture_failed))
                return@LaunchedEffect
            }

        val preview =
            Preview.Builder().build().apply {
                setSurfaceProvider(previewView.surfaceProvider)
            }
        val capture =
            ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

        val selector =
            CameraSelector.Builder().requireLensFacing(lensFacing).build()

        try {
            provider.unbindAll()
            provider.bindToLifecycle(lifecycleOwner, selector, preview, capture)
            imageCapture = capture
        } catch (_: Exception) {
            imageCapture = null
            snackbarHostState.showSnackbar(context.getString(R.string.selfie_capture_failed))
        }

        try {
            awaitCancellation()
        } finally {
            imageCapture = null
            provider.unbindAll()
        }
    }

    val executor = remember { ContextCompat.getMainExecutor(context) }

    fun capturePhoto() {
        val capture = imageCapture ?: return
        capture.takePicture(
            executor,
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    try {
                        val bmp = BitmapUtils.bitmapFromJpegImageProxy(image)
                        capturedBitmap?.recycle()
                        capturedBitmap = bmp
                    } catch (_: Exception) {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                context.getString(R.string.selfie_capture_failed),
                            )
                        }
                    } finally {
                        image.close()
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            exception.localizedMessage
                                ?: context.getString(R.string.selfie_capture_failed),
                        )
                    }
                }
            },
        )
    }

    fun submitPhoto() {
        val bmp = capturedBitmap
        if (bmp == null) {
            scope.launch {
                snackbarHostState.showSnackbar(context.getString(R.string.selfie_submit_no_photo))
            }
            return
        }
        viewModel.saveBitmap(bmp, isFromMissingInfoJourney, lensFacing)
    }

    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            confirmButton = {
                TextButton(onClick = { showHelpDialog = false }) {
                    Text(stringResource(android.R.string.ok))
                }
            },
            title = { Text(stringResource(R.string.selfie_help_title)) },
            text = { Text(stringResource(R.string.selfie_help_message)) },
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Black,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.selfie_screen_title),
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = stringResource(android.R.string.cancel),
                            tint = Color.White,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showHelpDialog = true }) {
                        Icon(
                            Icons.Outlined.HelpOutline,
                            contentDescription = stringResource(R.string.selfie_help_title),
                            tint = Color.White,
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Black,
                        scrolledContainerColor = Color.Black,
                        navigationIconContentColor = Color.White,
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White,
                    ),
            )
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                when {
                    cameraPermission.status.isGranted -> {
                        CameraPreviewSection(
                            previewView = previewView,
                            onFlipCamera = {
                                lensFacing =
                                    if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
                                        CameraSelector.LENS_FACING_BACK
                                    } else {
                                        CameraSelector.LENS_FACING_FRONT
                                    }
                                capturedBitmap?.recycle()
                                capturedBitmap = null
                            },
                        )

                        InstructionCard(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Column(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            CaptureControl(onCapture = { capturePhoto() })

                            Button(
                                onClick = { submitPhoto() },
                                enabled = capturedBitmap != null && uiState !is UiState.Loading,
                                modifier =
                                    Modifier
                                        .fillMaxWidth(0.85f)
                                        .height(48.dp),
                                colors =
                                    ButtonDefaults.buttonColors(
                                        containerColor = InstructionCardBg,
                                        contentColor = Color.White,
                                        disabledContainerColor =
                                            InstructionCardBg.copy(alpha = 0.4f),
                                        disabledContentColor = Color.White.copy(alpha = 0.5f),
                                    ),
                                shape = RoundedCornerShape(24.dp),
                            ) {
                                Text(text = stringResource(R.string.selfie_submit))
                            }
                        }
                    }

                    else -> {
                        Column(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = stringResource(R.string.selfie_camera_permission_required),
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { cameraPermission.launchPermissionRequest() },
                            ) {
                                Text(stringResource(R.string.selfie_grant_permission))
                            }
                            if (cameraPermission.status.shouldShowRationale) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = stringResource(R.string.selfie_camera_permission_required),
                                    color = Color.White.copy(alpha = 0.7f),
                                    style = MaterialTheme.typography.bodySmall,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }
                }
            }

            if (uiState is UiState.Loading) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.45f)),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = SelfieGreen)
                }
            }
        }
    }
}

@Composable
private fun CameraPreviewSection(
    previewView: PreviewView,
    onFlipCamera: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .height(340.dp),
    ) {
        AndroidView(
            factory = { _ -> previewView },
            modifier = Modifier.fillMaxSize(),
        )

        OvalFaceGuideOverlay(modifier = Modifier.fillMaxSize())

        IconButton(
            onClick = onFlipCamera,
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
        ) {
            Surface(
                shape = CircleShape,
                color = Color.Black.copy(alpha = 0.45f),
            ) {
                Icon(
                    Icons.Outlined.Cameraswitch,
                    contentDescription = stringResource(R.string.selfie_flip_camera),
                    tint = Color.White,
                    modifier = Modifier.padding(10.dp),
                )
            }
        }
    }
}

@Composable
private fun OvalFaceGuideOverlay(modifier: Modifier = Modifier) {
    Canvas(
        modifier =
            modifier.graphicsLayer {
                compositingStrategy = CompositingStrategy.Offscreen
            },
    ) {
        val ovalW = size.width * 0.82f
        val ovalH = size.height * 0.74f
        val left = (size.width - ovalW) / 2f
        val top = (size.height - ovalH) / 2f

        drawRect(color = Color.Black)

        drawOval(
            color = Color.Black,
            topLeft = Offset(left, top),
            size = Size(ovalW, ovalH),
            blendMode = BlendMode.Clear,
        )

        drawOval(
            color = SelfieGreen,
            topLeft = Offset(left, top),
            size = Size(ovalW, ovalH),
            style =
                Stroke(
                    width = 4.dp.toPx(),
                    cap = StrokeCap.Round,
                ),
        )
    }
}

@Composable
private fun InstructionCard(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = InstructionCardBg,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(
                Icons.Outlined.FaceRetouchingNatural,
                contentDescription = null,
                tint = SelfieGreen,
                modifier = Modifier.size(36.dp),
            )
            Text(
                text = stringResource(R.string.selfie_instruction_title),
                color = Color.White,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(R.string.selfie_instruction_body),
                color = Color.White.copy(alpha = 0.92f),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun CaptureControl(onCapture: () -> Unit) {
    Box(
        modifier = Modifier.size(92.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .matchParentSize()
                    .border(width = 2.dp, color = Color.White, shape = CircleShape),
        )
        Box(
            modifier =
                Modifier
                    .size(74.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable(onClick = onCapture),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Outlined.PhotoCamera,
                contentDescription = stringResource(R.string.selfie_capture_preview_hint),
                tint = Color.Black,
                modifier = Modifier.size(34.dp),
            )
        }
    }
}

private suspend fun android.content.Context.getCameraProviderSuspended(): ProcessCameraProvider =
    suspendCoroutine { cont ->
        val executor = ContextCompat.getMainExecutor(this)
        val future = ProcessCameraProvider.getInstance(this)
        future.addListener(
            {
                try {
                    cont.resume(future.get())
                } catch (e: Exception) {
                    cont.resumeWith(Result.failure(e))
                }
            },
            executor,
        )
    }

 */