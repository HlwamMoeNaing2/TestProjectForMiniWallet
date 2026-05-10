package com.hmn.testaicode.ui.screens.selfie_capture.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hmn.testaicode.R

@Composable
 fun CapturePhotoButton(
    modifier: Modifier,
    isFaceDetected: Boolean,
    onButtonClicked: ()->Unit
) {

    Image(
        modifier = modifier
            .padding(top = 20.dp)
            .size(92.dp)
            .clickable {
                onButtonClicked()

            },
        painter = painterResource(
            id =
                if (isFaceDetected)
                    R.drawable.camera_button_enabled
                else
                    R.drawable.camera_button_disabled,
        ),
        contentDescription = null,
    )

}