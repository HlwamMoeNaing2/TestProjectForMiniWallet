package com.hmn.testaicode.ui.screens.global_constants

enum class ImageType {
SELFIE ,FRONT , BACK
}

fun ImageType.toFolderName(): String {
    return when (this) {

        ImageType.SELFIE -> "com_hmn_selfie_"

        ImageType.FRONT -> "com_hmn_front"

        ImageType.BACK -> "com_hmn_back"

    }

}