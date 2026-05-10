package com.hmn.testaicode.ui.screens.face_capture

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.view.ViewGroup.LayoutParams
import android.widget.LinearLayout
import android.widget.Toast
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.hmn.testaicode.R
import com.hmn.testaicode.ui.screens.LifecycleLogger
import com.hmn.testaicode.ui.screens.cash_in.CashInScreen
import com.hmn.testaicode.ui.screens.face_capture.components.CameraView
import com.hmn.testaicode.ui.screens.face_capture.components.CapturePhotoButton
import com.hmn.testaicode.ui.screens.face_capture.components.CapturedPhotoView
import com.hmn.testaicode.ui.screens.face_capture.components.OvalOverlay
import com.hmn.testaicode.ui.screens.face_capture.components.SubmitPhotoButton
import com.hmn.testaicode.ui.theme.TestAICodeTheme
import java.util.concurrent.Executor

private const val OVAL_WIDTH_DP = 250
private const val OVAL_HEIGHT_DP = 300


@SuppressLint("RememberReturnType")
@Composable
fun FaceDetectionScreen(modifier: Modifier) {
    LifecycleLogger("FaceDetectionScreen")
    val context: Context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    var isCameraShown by remember { mutableStateOf(true) }
    var isFaceDetected by remember { mutableStateOf(false) }

    var capturedPhoto by remember { mutableStateOf<ImageBitmap?>(null) }
    var ovalCenter by remember { mutableStateOf<Offset?>(null) }

    val cameraController: LifecycleCameraController =
        remember { LifecycleCameraController(context) }
    cameraController.cameraSelector =
        CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build()

    val cameraPreviewView = remember {
        mutableStateOf(PreviewView(context))
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .statusBarsPadding(),
    ) { paddingValues: PaddingValues ->
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (isCameraShown) {
                CameraView(paddingValues, cameraPreviewView)
            } else {
                capturedPhoto?.let { photo ->
                    CapturedPhotoView(photo)
                }
            }

            OvalOverlay(
                modifier = Modifier.fillMaxSize(),
                isFaceDetected = isFaceDetected,
                onCenterCalculated = { ovalCenter = it }
            )
            ovalCenter?.let {
                startFaceDetection(
                    context = context,
                    cameraController = cameraController,
                    lifecycleOwner = lifecycleOwner,
                    previewView = cameraPreviewView.value,
                    onFaceDetected = { detected ->
                        isFaceDetected = detected
                    },
                    it,
                )
            }

            if (isCameraShown) {
                CapturePhotoButton(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 50.dp),
                    isFaceDetected = isFaceDetected,
                    onButtonClicked = {
                        capturePhotoAndReplaceBackground(
                            context,
                            cameraController,
                            isFaceDetected
                        ) { capturedBitmap ->
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
                    val capturedBitmap: Bitmap =
                        image.toBitmap().rotateBitmap(image.imageInfo.rotationDegrees)

                    processCapturedPhotoAndReplaceBackground(capturedBitmap) { processedBitmap ->
                        onBackgroundReplaced(processedBitmap)
                    }

                    image.close()
                }

                override fun onError(exception: ImageCaptureException) {
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

private fun startFaceDetection(
    context: Context,
    cameraController: LifecycleCameraController,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    onFaceDetected: (Boolean) -> Unit,
    ovalRect: Offset,
) {

    cameraController.imageAnalysisTargetSize = CameraController.OutputSize(AspectRatio.RATIO_16_9)
    cameraController.setImageAnalysisAnalyzer(
        ContextCompat.getMainExecutor(context),
        FaceDetector(
            onFaceDetected = onFaceDetected,
            ovalCenter = ovalRect,
            ovalRadiusX = OVAL_WIDTH_DP / 2f,
            ovalRadiusY = OVAL_HEIGHT_DP / 2f,
        ),
    )

    cameraController.bindToLifecycle(lifecycleOwner)
    previewView.controller = cameraController
}





@Preview(showBackground = true, showSystemUi = true,uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun FaceDetectionScreenPreview() {
    TestAICodeTheme {
        FaceDetectionScreen(Modifier.fillMaxSize())
    }
}

