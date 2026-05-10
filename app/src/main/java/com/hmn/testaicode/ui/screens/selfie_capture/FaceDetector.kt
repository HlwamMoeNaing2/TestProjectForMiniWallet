package com.hmn.testaicode.ui.screens.selfie_capture

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.compose.ui.geometry.Offset
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector as MlKitFaceDetector
import kotlin.math.abs

class FaceDetector(
    private val ovalCenter: Offset,
    private val ovalRadiusX: Float,
    private val ovalRadiusY: Float,
    private val onFaceDetected: (Boolean) -> Unit,
) : ImageAnalysis.Analyzer {

    companion object {
        private const val MIN_FACE_SIZE = 50f
        private const val FACE_SIZE_MULTIPLIER = 3.5f
        private const val FACE_POSITION_ACCURACY = 0.3f
        private const val DETECT_CONFIRM_FRAMES = 2
        private const val LOST_CONFIRM_FRAMES = 3
        private const val TAG = "FaceDetector"
    }

    private val faceDetector: MlKitFaceDetector = FaceDetection.getClient()
    private var stableDetected = false
    private var detectedFrames = 0
    private var lostFrames = 0
    private var frameCount = 0

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        frameCount += 1
        val mediaImage = imageProxy.image ?: run {
            Log.w(TAG, "Frame#$frameCount has null mediaImage")
            imageProxy.close()
            return
        }

        val inputImage = InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees,
        )

        faceDetector.process(inputImage)
            .addOnSuccessListener { faces ->
                if (frameCount % 10 == 0) {
                    Log.d(
                        TAG,
                        "Frame#$frameCount faces=${faces.size} stable=$stableDetected detectedFrames=$detectedFrames lostFrames=$lostFrames"
                    )
                }
                val detectedInThisFrame = faces.any {
                    isFaceInsideOval(
                        faceCenter = Offset(
                            it.boundingBox.centerX().toFloat(),
                            it.boundingBox.centerY().toFloat()
                        ),
                        faceWidth = it.boundingBox.width().toFloat(),
                        faceHeight = it.boundingBox.height().toFloat(),
                    )
                }
                if (frameCount % 10 == 0) {
                    Log.d(TAG, "Frame#$frameCount detectedInFrame=$detectedInThisFrame")
                }
                emitSmoothedDetection(detectedInThisFrame)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "MLKit process failed on frame#$frameCount: ${exception.message}", exception)
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }

    private fun emitSmoothedDetection(detectedInFrame: Boolean) {
        if (detectedInFrame) {
            detectedFrames += 1
            lostFrames = 0
            if (!stableDetected && detectedFrames >= DETECT_CONFIRM_FRAMES) {
                stableDetected = true
                Log.d(TAG, "Stable state -> DETECTED at frame#$frameCount")
                onFaceDetected(true)
            }
        } else {
            lostFrames += 1
            detectedFrames = 0
            if (stableDetected && lostFrames >= LOST_CONFIRM_FRAMES) {
                stableDetected = false
                Log.d(TAG, "Stable state -> LOST at frame#$frameCount")
                onFaceDetected(false)
            }
        }
    }

    private fun isFaceInsideOval(
        faceCenter: Offset,
        faceWidth: Float,
        faceHeight: Float
    ): Boolean {

        val verticalFaceCenter = Offset(
            faceCenter.x,
            faceCenter.y - ovalRadiusY
        )

        val xInsideOval = abs(verticalFaceCenter.x - ovalCenter.x) <= (ovalRadiusX * FACE_POSITION_ACCURACY)
        val yInsideOval = abs(verticalFaceCenter.y - ovalCenter.y) <= (ovalRadiusY * FACE_POSITION_ACCURACY)

        val isCenterInsideOval = xInsideOval && yInsideOval

        val faceFitsInOval = faceWidth in (MIN_FACE_SIZE)..(ovalRadiusX * FACE_SIZE_MULTIPLIER) &&
                faceHeight in MIN_FACE_SIZE..(ovalRadiusY * FACE_SIZE_MULTIPLIER)

        return isCenterInsideOval && faceFitsInOval
    }

}