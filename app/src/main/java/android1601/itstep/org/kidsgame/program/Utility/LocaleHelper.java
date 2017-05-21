package android1601.itstep.org.kidsgame.program.Utility;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;

import android1601.itstep.org.kidsgame.program.data.PreferencesData;

import java.util.Locale;

/**
 * Created by Kulykov Roman on 15.09.16.
 */

public class LocaleHelper {

    public static void onCreate(Context context) {
        String lang = getPersistedData();
        setLocale(context, lang);
    }

    public static String getLanguage() {
        return getPersistedData();
    }

    public static void setLocale(Context context, String language) {
        persist(language);
        updateResources(context, language);
    }

    public static void setLocaleFromPrefs(Context context) {
        String language = getPersistedData();
        persist(language);
        updateResources(context, language);
    }

    private static String getPersistedData() {
        return PreferencesData.getLanguage();
    }

    private static void persist(String language) {
        PreferencesData.setLanguage(language);
    }

    @SuppressWarnings("deprecation")
    private static void updateResources(Context context, String language) {
        Locale locale = !TextUtils.isEmpty(language) ? new Locale(language) : Locale.getDefault();
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= 17)
            configuration.setLocale(locale);
        else
            configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}

