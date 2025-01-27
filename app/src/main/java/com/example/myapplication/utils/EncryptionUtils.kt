package com.example.myapplication.utils

import java.security.Key
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import android.util.Base64
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.security.SecureRandom
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

private const val ALGORITHM = "AES"
private const val ALGORITHM_PADDING = "AES/CBC/PKCS7Padding"

class EncryptionUtils {

    private val encryptionKey: Key by lazy {
        KeyGenerator.getInstance(ALGORITHM).apply { init(256) }.generateKey()
    }

    fun encrypt(data: String): String {
        val cipher = Cipher.getInstance(ALGORITHM_PADDING) // Use PKCS7Padding for better compatibility
        val iv = ByteArray(16) // AES block size is 16 bytes
        SecureRandom().nextBytes(iv)
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.ENCRYPT_MODE, encryptionKey, ivSpec)
        val encryptedData = cipher.doFinal(data.toByteArray())
        return Base64.encodeToString(iv + encryptedData, Base64.DEFAULT) // Prepend IV to encrypted data
    }

    fun decrypt(data: String): String {
        val decodedData = Base64.decode(data, Base64.DEFAULT)
        val iv = decodedData.copyOfRange(0, 16) // Extract the IV
        val encryptedData = decodedData.copyOfRange(16, decodedData.size)
        val ivSpec = IvParameterSpec(iv)
        val cipher = Cipher.getInstance(ALGORITHM_PADDING)
        cipher.init(Cipher.DECRYPT_MODE, encryptionKey, ivSpec)
        return String(cipher.doFinal(encryptedData))
    }

    inline fun <reified T> encryptObject(obj: T, encryptionKey: ByteArray, serializer: KSerializer<T>): String {
        // Serialize the object to JSON
        val jsonString = Json.encodeToString(serializer, obj)

        // Generate IV
        val iv = ByteArray(16)
        SecureRandom().nextBytes(iv)
        val ivSpec = IvParameterSpec(iv)

        // Encrypt the JSON string
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val secretKey = SecretKeySpec(encryptionKey, "AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
        val encryptedData = cipher.doFinal(jsonString.toByteArray())

        // Combine IV and encrypted data, then encode to Base64
        val combinedData = iv + encryptedData
        return Base64.encodeToString(combinedData, Base64.DEFAULT)
    }

    inline fun <reified T> decryptObject(encryptedData: String, encryptionKey: ByteArray, serializer: KSerializer<T>): T {
        // Decode Base64
        val combinedData = Base64.decode(encryptedData, Base64.DEFAULT)

        // Extract IV (first 16 bytes) and encrypted data (remaining bytes)
        val iv = combinedData.copyOfRange(0, 16)
        val encryptedBytes = combinedData.copyOfRange(16, combinedData.size)
        val ivSpec = IvParameterSpec(iv)

        // Decrypt the data
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val secretKey = SecretKeySpec(encryptionKey, "AES")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
        val decryptedData = cipher.doFinal(encryptedBytes)

        // Deserialize the JSON back to the object
        val jsonString = String(decryptedData)
        return Json.decodeFromString(serializer, jsonString)
    }


}