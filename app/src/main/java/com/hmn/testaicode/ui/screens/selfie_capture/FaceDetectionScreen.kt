package com.hmn.testaicode.ui.screens.selfie_capture

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.ImageAnalysis
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.hmn.testaicode.ui.screens.LifecycleLogger
import com.hmn.testaicode.ui.screens.global_constants.ImageType
import com.hmn.testaicode.ui.screens.selfie_capture.components.CameraView
import com.hmn.testaicode.ui.screens.selfie_capture.components.CapturePhotoButton
import com.hmn.testaicode.ui.screens.selfie_capture.components.CapturedPhotoView
import com.hmn.testaicode.ui.screens.selfie_capture.components.OvalOverlay
import com.hmn.testaicode.ui.screens.selfie_capture.components.SubmitPhotoButton
import com.hmn.testaicode.ui.theme.TestAICodeTheme
import com.hmn.testaicode.ui.theme.appBackgroundBrush
import java.util.concurrent.Executor

private const val OVAL_WIDTH_DP = 250
private const val OVAL_HEIGHT_DP = 300
private const val FACE_CAPTURE_TAG = "FaceDetectionScreen"


@SuppressLint("RememberReturnType")
@Composable
fun FaceDetectionScreen(modifier: Modifier = Modifier, imageType: ImageType) {
    if (LocalInspectionMode.current) {
        FaceDetectionPreviewContent(modifier)
        return
    }

    LifecycleLogger("FaceDetectionScreen")
    val context: Context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    var isCameraShown by remember { mutableStateOf(true) }
    var isFaceDetected by remember { mutableStateOf(false) }

    var capturedPhoto by remember { mutableStateOf<ImageBitmap?>(null) }
    var ovalCenter by remember { mutableStateOf<Offset?>(null) }

    val cameraController: LifecycleCameraController = remember {
        LifecycleCameraController(context).apply {
            Log.d(FACE_CAPTURE_TAG, "Creating LifecycleCameraController")
            cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
            imageAnalysisTargetSize = CameraController.OutputSize(AspectRatio.RATIO_16_9)
            imageAnalysisBackpressureStrategy = ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE or
                    CameraController.IMAGE_ANALYSIS
            )
        }
    }

    val cameraPreviewView = remember {
        mutableStateOf(PreviewView(context))
    }

    LaunchedEffect(lifecycleOwner) {
        Log.d(FACE_CAPTURE_TAG, "Binding camera controller to lifecycle")
        cameraController.bindToLifecycle(lifecycleOwner)
    }
    LaunchedEffect(cameraPreviewView.value, isCameraShown) {
        if (isCameraShown) {
            Log.d(FACE_CAPTURE_TAG, "Attaching controller to PreviewView")
            cameraPreviewView.value.controller = cameraController
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            Log.d(FACE_CAPTURE_TAG, "Clearing analyzer on dispose")
            cameraController.clearImageAnalysisAnalyzer()
        }
    }
    LaunchedEffect(ovalCenter) {
        val center = ovalCenter ?: return@LaunchedEffect
        Log.d(FACE_CAPTURE_TAG, "Setting analyzer with ovalCenter=$center")
        cameraController.setImageAnalysisAnalyzer(
            ContextCompat.getMainExecutor(context),
            FaceDetector(
                onFaceDetected = { detected ->
                    if (isFaceDetected != detected) {
                        Log.d(FACE_CAPTURE_TAG, "onFaceDetected changed: $isFaceDetected -> $detected")
                        isFaceDetected = detected
                    }
                },
                ovalCenter = center,
                ovalRadiusX = OVAL_WIDTH_DP / 2f,
                ovalRadiusY = OVAL_HEIGHT_DP / 2f,
            ),
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(appBackgroundBrush())
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (isCameraShown) {
                CameraView(PaddingValues(), cameraPreviewView)
            } else {
                capturedPhoto?.let { photo ->
                    CapturedPhotoView(photo)
                }
            }

            OvalOverlay(
                modifier = Modifier.fillMaxSize(),
                isFaceDetected = isFaceDetected,
                onCenterCalculated = { center ->
                    if (ovalCenter != center) {
                        Log.d(FACE_CAPTURE_TAG, "Oval center calculated=$center")
                        ovalCenter = center
                    }
                }
            )

            if (isCameraShown) {
                CapturePhotoButton(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 50.dp),
                    isFaceDetected = isFaceDetected,
                    onButtonClicked = {
                        Log.d(FACE_CAPTURE_TAG, "Capture clicked. isFaceDetected=$isFaceDetected")
                        capturePhotoAndReplaceBackground(
                            context,
                            cameraController,
                            isFaceDetected
                        ) { capturedBitmap ->
                            Log.d(FACE_CAPTURE_TAG, "Capture success. bitmap=${capturedBitmap.width}x${capturedBitmap.height}")
                            capturedPhoto = capturedBitmap.asImageBitmap()
                            if (isFaceDetected)
                                isCameraShown = false
                        }
                    },
                )
            } else {
                SubmitPhotoButton(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 32.dp, vertical = 50.dp),
                    onSubmit = {
                        Log.d(FACE_CAPTURE_TAG, "Submit clicked. capturedPhotoExists=${capturedPhoto != null}")

                        val mText = if (capturedPhoto == null) "Null" else "Is value exist"

                        Toast.makeText(
                            context,
                            mText,
                            Toast.LENGTH_SHORT,
                        ).show()
                    },
                )
            }
        }
    }
}

@Composable
private fun FaceDetectionPreviewContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(appBackgroundBrush()),
        contentAlignment = Alignment.Center,
    ) {
        OvalOverlay(
            modifier = Modifier.fillMaxSize(),
            isFaceDetected = false,
        )
    }
}


private fun capturePhotoAndReplaceBackground(
    context: Context,
    cameraController: LifecycleCameraController,
    isFaceDetected: Boolean,
    onBackgroundReplaced: (Bitmap) -> Unit,
) {
    val mainExecutor: Executor = ContextCompat.getMainExecutor(context)
    if (isFaceDetected) {
        cameraController.takePicture(
            mainExecutor,
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    Log.d(FACE_CAPTURE_TAG, "onCaptureSuccess rotation=${image.imageInfo.rotationDegrees}")
                    val capturedBitmap: Bitmap =
                        image.toBitmap().rotateBitmap(image.imageInfo.rotationDegrees)

                    processCapturedPhotoAndReplaceBackground(capturedBitmap) { processedBitmap ->
                        onBackgroundReplaced(processedBitmap)
                    }

                    image.close()
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(FACE_CAPTURE_TAG, "onCaptureError: ${exception.message}", exception)
                }
            },
        )
    }
}

private fun processCapturedPhotoAndReplaceBackground(
    capturedBitmap: Bitmap,
    onBackgroundReplaced: (Bitmap) -> Unit,
) {
    onBackgroundReplaced(capturedBitmap)
}

@Preview(showBackground = true, showSystemUi = true,uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun FaceDetectionScreenPreview() {
    TestAICodeTheme {
        FaceDetectionPreviewContent(Modifier.fillMaxSize())
    }
}

