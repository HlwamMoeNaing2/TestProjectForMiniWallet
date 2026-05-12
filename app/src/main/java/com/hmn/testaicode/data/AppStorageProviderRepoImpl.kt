package com.hmn.testaicode.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.hmn.testaicode.ui.screens.global_constants.ImageType
import com.hmn.testaicode.ui.screens.global_constants.toFolderName
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class AppStorageProviderRepoImpl @Inject constructor( @ApplicationContext private val context: Context): AppStorageProviderRepo{
    override suspend fun saveBitmapToAppStorage(
        bitmap: Bitmap,
        imageType: ImageType
    ) :SimpleResponseHandler<Unit>{
        return try {
            withContext(Dispatchers.IO) {
                val parent = File(context.filesDir, "id_capture")
                if (!parent.exists()) {
                    check(parent.mkdirs()) { "Unable to create folder: ${parent.absolutePath}" }
                }

                val baseName = imageType.toFolderName()
                val file = File(parent, "$baseName.jpg")

                if (file.exists()) {
                    check(file.delete()) {
                        "Unable to delete existing file for replace: ${file.absolutePath}"
                    }
                }

                FileOutputStream(file).use { stream ->
                    val ok = bitmap.compress(Bitmap.CompressFormat.JPEG, 92, stream)
                    check(ok) { "Bitmap compression failed" }
                    stream.flush()
                    runCatching { stream.fd.sync() }
                }

                file.absolutePath
            }

            SimpleResponseHandler.Success(Unit)
        }catch (e: Exception){
            SimpleResponseHandler.Error(e)
        }
    }

    override suspend fun retrieveBitmapFromAppStorage(imageType: ImageType): SimpleResponseHandler<Bitmap?> {
        return try {
            val decoded = withContext(Dispatchers.IO) {
                val file = File(File(context.filesDir, "id_capture"), "${imageType.toFolderName()}.jpg")
                if (!file.exists() || file.length() <= 0L) {
                    return@withContext null
                }

                val options = BitmapFactory.Options().apply {
                    inPreferredConfig = Bitmap.Config.ARGB_8888
                }
                BitmapFactory.decodeFile(file.absolutePath, options)
            }
            SimpleResponseHandler.Success(decoded)
        } catch (e: Exception) {
            SimpleResponseHandler.Error(e)
        }
    }
}