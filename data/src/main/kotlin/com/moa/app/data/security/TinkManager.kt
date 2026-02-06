package com.moa.app.data.security

import android.content.Context
import android.util.Base64
import com.google.crypto.tink.Aead
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.AesGcmKeyManager
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TinkManager @Inject constructor(
    private val context: Context,
) {
    private val aead: Aead by lazy {
        try {
            AeadConfig.register()

            val keysetHandle = AndroidKeysetManager.Builder()
                .withSharedPref(context, KEYSET_NAME, PREF_FILE_NAME)
                .withKeyTemplate(AesGcmKeyManager.aes256GcmTemplate())
                .withMasterKeyUri(MASTER_KEY_URI)
                .build()
                .keysetHandle

            keysetHandle.getPrimitive(Aead::class.java)
        } catch (e: Exception) {
            throw SecurityException("Failed to initialize TinkManager", e)
        }
    }

    fun encrypt(plaintext: String): String {
        val plaintextBytes = plaintext.toByteArray(Charsets.UTF_8)
        val ciphertext = aead.encrypt(plaintextBytes, null)
        return Base64.encodeToString(ciphertext, Base64.NO_WRAP)
    }

    fun decrypt(ciphertext: String): String {
        val ciphertextBytes = Base64.decode(ciphertext, Base64.NO_WRAP)
        val plaintext = aead.decrypt(ciphertextBytes, null)
        return String(plaintext, Charsets.UTF_8)
    }

    companion object {
        private const val KEYSET_NAME = "moa_keyset"
        private const val PREF_FILE_NAME = "moa_crypto_pref"
        private const val MASTER_KEY_URI = "android-keystore://moa_master_key"
    }
}
