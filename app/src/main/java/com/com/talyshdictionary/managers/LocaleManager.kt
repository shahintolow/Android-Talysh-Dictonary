package com.com.talyshdictionary.managers

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.res.Configuration
import com.com.talyshdictionary.models.TranslateType
import java.util.*


object LocaleManager {
    private const val languageName = "language"

    fun setLocale(context: Context?) : Context? {
        if(context == null) return context

        val code = getLanguage(context)
        val locale = Locale(code)
        Locale.setDefault(locale)
        val configuration = Configuration(context.resources.configuration)

        configuration.setLocale(locale)

        return context.createConfigurationContext(configuration)
    }

    fun saveLanguage(context: Context, code: TranslateType) {
        val preferences = context.getSharedPreferences(languageName, MODE_PRIVATE)
        val editor = preferences.edit()

        editor.putString(languageName, code.name)
        editor.apply()
    }

    fun getLanguage(context: Context) = context.getSharedPreferences(languageName, MODE_PRIVATE)
        .getString(languageName, TranslateType.EN.name) ?: TranslateType.EN.name
}