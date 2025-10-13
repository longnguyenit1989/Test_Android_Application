package com.example.testapplication.utils


import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

object SecurePrefs {
    private const val PREF_NAME = "secure_auth"
    private const val KEY_PIN = "user_pin"
    private const val KEY_PATTERN = "user_pattern"
    private const val KEY_AUTH_TYPE = "auth_type"

    private fun getPrefs(context: Context) = EncryptedSharedPreferences.create(
        PREF_NAME,
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun hasAnyAuth(context: Context): Boolean {
        return hasPin(context) || hasPattern(context)
    }

    fun hasPin(context: Context): Boolean {
        return getPrefs(context).contains(KEY_PIN)
    }

    fun hasPattern(context: Context): Boolean {
        return getPrefs(context).contains(KEY_PATTERN)
    }

    fun savePin(context: Context, pin: String) {
        getPrefs(context).edit().putString(KEY_PIN, pin).putString(KEY_AUTH_TYPE, "pin").apply()
    }

    fun getPin(context: Context): String? {
        return getPrefs(context).getString(KEY_PIN, null)
    }

    fun savePattern(context: Context, pattern: String) {
        getPrefs(context).edit().putString(KEY_PATTERN, pattern).putString(KEY_AUTH_TYPE, "pattern").apply()
    }

    fun getPattern(context: Context): String? {
        return getPrefs(context).getString(KEY_PATTERN, null)
    }

    fun getAuthType(context: Context): String? {
        return getPrefs(context).getString(KEY_AUTH_TYPE, null)
    }
}

