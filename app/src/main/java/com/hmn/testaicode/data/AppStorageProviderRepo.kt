package com.hmn.testaicode.data

import android.graphics.Bitmap
import com.hmn.testaicode.ui.screens.global_constants.ImageType

interface AppStorageProviderRepo {
   suspend fun saveBitmapToAppStorage(bitmap: Bitmap, imageType: ImageType):SimpleResponseHandler<Unit>
   suspend fun retrieveBitmapFromAppStorage(imageType: ImageType): SimpleResponseHandler<Bitmap?>
}