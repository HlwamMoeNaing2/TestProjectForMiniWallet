package com.hmn.testaicode.navigation

import com.hmn.testaicode.ui.screens.global_constants.ImageType

object Routes {

    const val ID_SELFIE_MATCHING_SCREEN = "ID_SELFIE_MATCHING_SCREEN"

    const val FACE_DETECTION_SCREEN = "FACE_DETECTION_SCREEN"

    const val ID_CAPTURING_SCREEN = "ID_CAPTURING_SCREEN"

    fun faceDetectionScreen(imageType: ImageType) = "$FACE_DETECTION_SCREEN/${imageType.name}"

    fun iDCaptureScreen(imageType: ImageType) = "$ID_CAPTURING_SCREEN/${imageType.name}"

}