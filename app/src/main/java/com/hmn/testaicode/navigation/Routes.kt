package com.hmn.testaicode.navigation

import com.hmn.testaicode.ui.screens.global_constants.ImageType

object Routes {
    const val STARTUP_SCREEN = "STARTUP_SCREEN"
    const val REGISTRATION_GRAPH = "REGISTRATION_GRAPH"
    const val MAIN_MENU_GRAPH = "MAIN_MENU_GRAPH"

    const val ENTER_PHONE_SCREEN = "ENTER_PHONE_SCREEN"

    const val ID_SELFIE_MATCHING_SCREEN = "ID_SELFIE_MATCHING_SCREEN"

    const val FACE_DETECTION_SCREEN = "FACE_DETECTION_SCREEN"

    const val ID_CAPTURING_SCREEN = "ID_CAPTURING_SCREEN"

    const val PERSONAL_INFO_SCREEN = "PERSONAL_INFO_SCREEN"
    const val MAIN_MENU_SCREEN = "MAIN_MENU_SCREEN"

    const val CASH_IN_SCREEN = "CASH_IN_SCREEN"
    const val CASH_OUT_SCREEN = "CASH_OUT_SCREEN"
    const val WALLET_TRANSFER_SCREEN = "WALLET_TRANSFER_SCREEN"
    const val FULL_TRANSACTION_HISTORY_SCREEN = "FULL_TRANSACTION_HISTORY_SCREEN"
    const val RECEIPT_SCREEN = "RECEIPT_SCREEN"


    fun faceDetectionScreen(imageType: ImageType) = "$FACE_DETECTION_SCREEN/${imageType.name}"

    fun iDCaptureScreen(imageType: ImageType) = "$ID_CAPTURING_SCREEN/${imageType.name}"

}