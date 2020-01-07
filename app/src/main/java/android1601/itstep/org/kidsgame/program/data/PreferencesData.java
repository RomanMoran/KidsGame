package android1601.itstep.org.kidsgame.program.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;


import java.util.Locale;

import android1601.itstep.org.kidsgame.program.KidsApplication;
import android1601.itstep.org.kidsgame.program.KidsKotlinApplication;

public class PreferencesData {
    private static final String TAG = PreferencesData.class.getName();

    private static final String KEY_SELECTED_LANGUAGE = "selectedLanguage";

    public static String getLanguage() {
        return getString(KEY_SELECTED_LANGUAGE, Locale.getDefault().getLanguage());
    }

    public static void setLanguage(String language) {
        save(KEY_SELECTED_LANGUAGE, language);
    }

    ////////////////////
    public static int getInt(String key, int defValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return prefs.getInt(key, defValue);
    }

    public static long getLong(String key, long defValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return prefs.getLong(key, defValue);
    }

    public static String getString(String key, String defValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return prefs.getString(key, defValue);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return prefs.getBoolean(key, defValue);
    }

    public static void save(String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void save(String key, long value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Editor editor = prefs.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void save(String key, int value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void save(String key, boolean value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private static Context getApplicationContext() {
        return KidsKotlinApplication.Companion.getInstance();
    }

}