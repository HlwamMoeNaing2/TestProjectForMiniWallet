package com.hmn.testaicode.ui.screens.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Encryption manager for securing sensitive files using Android Keystore.
 * Uses AES-256-GCM encryption for strong security.
 *
 * This is suitable for encrypting sensitive user data like:
 * - ID card photos
 * - Selfie photos
 * - Other personal documents
 *
 * IMPORTANT: Android 7-9 KeyStore GCM Issues
 * ==========================================
 * Due to known bugs in Android KeyStore implementation on Android 7, 8, and 9:
 * - AEADBadTagException can occur during decryption
 * - "Signature/MAC verification failed" errors on certain devices
 * - Hardware-specific issues with GCM mode operations
 *
 * References:
 * - https://issuetracker.google.com/issues/147039506
 *
 * Callers should handle encryption failures gracefully and consider
 * skipping encryption on Android < 10 (API 29).
 */
@Singleton
class EncryptionManager @Inject constructor() {

    companion object {
        private const val KEYSTORE_PROVIDER = "AndroidKeyStore"
        private const val KEY_ALIAS = "WavePaySecureKey"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val GCM_TAG_LENGTH = 128
        private const val IV_SIZE = 12 // GCM standard IV size
    }

    private val keyStore: KeyStore = KeyStore.getInstance(KEYSTORE_PROVIDER).apply {
        load(null)
    }

    /**
     * Get or create encryption key from Android Keystore
     */
    private fun getOrCreateSecretKey(): SecretKey {
        // Check if key already exists
        if (keyStore.containsAlias(KEY_ALIAS)) {
            val entry = keyStore.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry
            if (entry != null) {
                return entry.secretKey
            }
        }

        // Generate new key
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            KEYSTORE_PROVIDER
        )

        val builder = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .setRandomizedEncryptionRequired(true)

        // For Android M and above, set user authentication requirement
        builder.setUserAuthenticationRequired(false) // Set to true if you want biometric auth

        keyGenerator.init(builder.build())
        return keyGenerator.generateKey()
    }

    /**
     * Encrypt data and write to file
     * Format: [IV_SIZE (1 byte)][IV (12 bytes)][ENCRYPTED_DATA]
     *
     * Note: On Android 7-9, this may fail due to KeyStore GCM bugs.
     * Callers should check return value and implement fallback strategy.
     *
     * @return true if encryption succeeded, false if failed
     */
    fun encryptToFile(data: ByteArray, file: File): Boolean {
        return try {
            // Ensure parent directory exists
            file.parentFile?.mkdirs()

            val cipher = Cipher.getInstance(TRANSFORMATION)
            val secretKey = getOrCreateSecretKey()

            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            val iv = cipher.iv // Get the generated IV
            val encryptedData = cipher.doFinal(data)

            // Write to temp file first, then rename for atomicity
            val tempFile = File(file.parent, "${file.name}.tmp")
            try {
                FileOutputStream(tempFile).use { outputStream ->
                    // Write IV length (for future compatibility)
                    outputStream.write(iv.size)
                    // Write IV
                    outputStream.write(iv)
                    // Write encrypted data
                    outputStream.write(encryptedData)
                    outputStream.flush()
                    // Ensure data is on disk before rename (fixes empty/corrupt reads on Android 7/8/9)
                    try {
                        outputStream.fd.sync()
                    } catch (_: java.io.SyncFailedException) { /* ignore */
                    }
                }

                // Atomic rename
                if (!tempFile.renameTo(file)) {
                    tempFile.delete()
                    return false
                }

                true
            } catch (e: Exception) {
                // Cleanup temp file
                tempFile.delete()
                throw e
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Clean up any partial files
            file.delete()
            false
        }
    }

    /**
     * Decrypt data from file
     *
     * Note: On Android 7-9, this may throw AEADBadTagException due to KeyStore bugs.
     * Returns null on failure - caller should handle gracefully.
     *
     * @return Decrypted data, or null if decryption fails
     */
    fun decryptFromFile(file: File): ByteArray? {
        return try {
            if (!file.exists()) {
                return null
            }

            if (file.length() < IV_SIZE + 1) {
                return null
            }

            FileInputStream(file).use { inputStream ->
                // Read IV size
                val ivSize = inputStream.read()
                if (ivSize == -1 || ivSize != IV_SIZE) {
                    return null
                }

                // Read IV
                val iv = ByteArray(ivSize)
                val ivBytesRead = inputStream.read(iv)
                if (ivBytesRead != ivSize) {
                    return null
                }

                // Read encrypted data
                val encryptedData = inputStream.readBytes()
                if (encryptedData.isEmpty()) {
                    return null
                }

                // Decrypt
                val cipher = Cipher.getInstance(TRANSFORMATION)
                val secretKey = getOrCreateSecretKey()
                val spec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
                cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)
                cipher.doFinal(encryptedData)
            }
        } catch (e: Exception) {
            // Log error but return null - caller will handle fallback
            e.printStackTrace()
            null
        }
    }

    /**
     * Check if file is encrypted (has proper header)
     */
    fun isEncryptedFile(file: File): Boolean {
        if (!file.exists() || file.length() < IV_SIZE + 1) {
            return false
        }
        return try {
            FileInputStream(file).use { inputStream ->
                val ivSize = inputStream.read()
                ivSize == IV_SIZE
            }
        } catch (e: Exception) {
            false
        }
    }
}
