package com.example.testapplication.utils

import android.content.Context
import android.content.res.Configuration
import androidx.core.content.edit
import java.util.Locale

object LocaleHelper {

    private const val LANGUAGE_KEY = "app_language"
    const val KEY_LANGUAGE_VI = "vi"
    const val KEY_LANGUAGE_ENG = "en"

    fun setLocale(context: Context, language: String): Context {
        saveLanguagePreference(context, language)
        return updateResources(context, language)
    }

    fun loadLocale(context: Context): Context {
        val lang = getSavedLanguage(context) ?: Locale.getDefault().language
        return updateResources(context, lang)
    }

    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }

    private fun saveLanguagePreference(context: Context, language: String) {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        prefs.edit { putString(LANGUAGE_KEY, language) }
    }

    fun getSavedLanguage(context: Context): String? {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return prefs.getString(LANGUAGE_KEY, null)
    }
}
