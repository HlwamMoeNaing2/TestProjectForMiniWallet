package com.hmn.testaicode.ui.screens.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

interface BitmapEncoder {
    suspend fun encode(name: String, bitmap: Bitmap): BitmapEncodeResult
}

data class BitmapEncodeResult(
    val file: File,
    val encodedValue: String
)


@Singleton
class BitmapEncoderImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val encryptionManager: EncryptionManager,
) : BitmapEncoder {

    companion object {
        private const val ENCRYPTED_FILE_EXT = ".enc"
        private const val UNENCRYPTED_FILE_EXT = ".jpg"
    }

    override suspend fun encode(name: String, bitmap: Bitmap): BitmapEncodeResult {
        // Use obfuscated UUID-based filename instead of descriptive names
        val uuid = UUID.randomUUID()
        val parent = File(context.filesDir, "poi")

        // Ensure directory exists
        if (!parent.exists() && !parent.mkdirs()) {
            throw java.io.IOException("Failed to create directory: ${parent.absolutePath}")
        }

        // First, compress bitmap to byte array
        val byteArrayOutputStream = ByteArrayOutputStream()
        val compressionSuccess =
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream)
        if (!compressionSuccess) {
            byteArrayOutputStream.close()
            throw java.io.IOException("Bitmap compression failed")
        }

        val bitmapData = byteArrayOutputStream.toByteArray()
        byteArrayOutputStream.close()

        // SECURITY NOTE: Skip encryption on Android < 10 (API 29) due to known KeyStore GCM reliability issues.
        // References:
        // - Android Issue Tracker: https://issuetracker.google.com/issues/168407869
        // - KeyStore GCM operations on Android 7-9 can fail with AEADBadTagException
        // - "Signature/MAC verification failed" errors due to hardware-specific KeyStore bugs
        //
        // On Android 7-9, files are stored unencrypted as a workaround, but remain protected by:
        // - App-private directory (filesDir) - accessible only by this app
        // - Restrictive file permissions (owner read/write only)
        // - Android sandbox security model
        val shouldEncrypt = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q

        if (!shouldEncrypt) {
            // Save as unencrypted .jpg for Android < 10
            val fallbackFileName = "$uuid$UNENCRYPTED_FILE_EXT"
            val fallbackFile = File(parent, fallbackFileName)

            if (fallbackFile.exists()) {
                fallbackFile.delete()
            }

            // Write unencrypted data and sync to disk
            FileOutputStream(fallbackFile).use { outputStream ->
                outputStream.write(bitmapData)
                outputStream.flush()
                try {
                    outputStream.fd.sync()
                } catch (_: java.io.SyncFailedException) { /* ignore */
                }
            }

            // Set restrictive file permissions
            fallbackFile.setReadable(false, false)
            fallbackFile.setReadable(true, true)
            fallbackFile.setWritable(false, false)
            fallbackFile.setWritable(true, true)

            val encodedValue = Base64.encodeToString(bitmapData, Base64.DEFAULT)
            return BitmapEncodeResult(fallbackFile, encodedValue)
        }

        // Try encryption (Android 10+)
        val encryptedFileName = "$uuid$ENCRYPTED_FILE_EXT"
        val encryptedFile = File(parent, encryptedFileName)

        if (encryptedFile.exists()) {
            encryptedFile.delete()
        }

        val encryptionSuccess = encryptionManager.encryptToFile(bitmapData, encryptedFile)

        if (encryptionSuccess && encryptedFile.exists() && encryptedFile.length() > 0) {
            // Encryption succeeded - use encrypted file.
            // Use in-memory bitmapData for base64 to avoid decryptFromFile() here; on Android 7/8/9
            // immediate decrypt-after-encrypt can throw AEADBadTagException (KeyStore/cipher timing).
            val encodedValue = Base64.encodeToString(bitmapData, Base64.DEFAULT)

            // Set restrictive file permissions
            encryptedFile.setReadable(false, false)
            encryptedFile.setReadable(true, true)
            encryptedFile.setWritable(false, false)
            encryptedFile.setWritable(true, true)

            return BitmapEncodeResult(encryptedFile, encodedValue)
        } else {
            // Encryption failed - fallback to unencrypted .jpg
            encryptedFile.delete() // Clean up any partial encrypted file

            val fallbackFileName = "$uuid$UNENCRYPTED_FILE_EXT"
            val fallbackFile = File(parent, fallbackFileName)

            if (fallbackFile.exists()) {
                fallbackFile.delete()
            }

            // Write unencrypted data and sync to disk (helps reliable read on Android 7/8/9)
            FileOutputStream(fallbackFile).use { outputStream ->
                outputStream.write(bitmapData)
                outputStream.flush()
                try {
                    outputStream.fd.sync()
                } catch (_: java.io.SyncFailedException) { /* ignore */
                }
            }

            // Set restrictive file permissions
            fallbackFile.setReadable(false, false)
            fallbackFile.setReadable(true, true)
            fallbackFile.setWritable(false, false)
            fallbackFile.setWritable(true, true)

            val encodedValue = Base64.encodeToString(bitmapData, Base64.DEFAULT)

            return BitmapEncodeResult(fallbackFile, encodedValue)
        }
    }

    /**
     * Decrypt file and encode to base64
     * Handles both encrypted and unencrypted files
     */
    private fun File.encodeToBase64Encrypted(): String {
        // Try to decrypt first (for .enc files)
        val decryptedData = encryptionManager.decryptFromFile(this)

        if (decryptedData != null) {
            // Successfully decrypted
            return Base64.encodeToString(decryptedData, Base64.DEFAULT)
        } else {
            // Not encrypted or decryption failed - read as regular file
            val rawData = this.readBytes()
            return Base64.encodeToString(rawData, Base64.DEFAULT)
        }
    }
}