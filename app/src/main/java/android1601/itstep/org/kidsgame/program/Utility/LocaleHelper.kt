package android1601.itstep.org.kidsgame.program.Utility

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.text.TextUtils

import android1601.itstep.org.kidsgame.program.data.PreferencesData

import java.util.Locale

/**
 * Created by Kulykov Roman on 15.09.16.
 */

object LocaleHelper {

    val language: String?
        get() = persistedData

    private val persistedData: String?
        get() = PreferencesData.language

    fun onCreate(context: Context) {
        val lang = persistedData
        setLocale(context, lang)
    }

    fun setLocale(context: Context, language: String?) {
        persist(language)
        updateResources(context, language)
    }

    fun setLocaleFromPrefs(context: Context) {
        val language = persistedData
        persist(language)
        updateResources(context, language)
    }

    private fun persist(language: String?) {
        PreferencesData.language = language
    }

    private fun updateResources(context: Context, language: String?) {
        val locale = if (!TextUtils.isEmpty(language)) Locale(language!!) else Locale.getDefault()
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        if (Build.VERSION.SDK_INT >= 17)
            configuration.setLocale(locale)
        else
            configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}

