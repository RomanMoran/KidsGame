package android1601.itstep.org.kidsgame.program.data


import android.content.Context
import android.preference.PreferenceManager
import android1601.itstep.org.kidsgame.program.KidsApplication
import java.util.*

object PreferencesData {
    private val TAG = PreferencesData::class.java.name

    private val KEY_SELECTED_LANGUAGE = "selectedLanguage"

    var language: String?
        get() = getString(KEY_SELECTED_LANGUAGE, Locale.getDefault().language)
        set(language) = save(KEY_SELECTED_LANGUAGE, language ?: "")

    private val applicationContext: Context?
        get() = KidsApplication.instance

    ////////////////////
    fun getInt(key: String, defValue: Int): Int {
        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        return prefs.getInt(key, defValue)
    }

    fun getLong(key: String, defValue: Long): Long {
        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        return prefs.getLong(key, defValue)
    }

    fun getString(key: String, defValue: String): String? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        return prefs.getString(key, defValue)
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        return prefs.getBoolean(key, defValue)
    }

    fun save(key: String, value: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun save(key: String, value: Long) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val editor = prefs.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun save(key: String, value: Int) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val editor = prefs.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun save(key: String, value: Boolean) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val editor = prefs.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

}