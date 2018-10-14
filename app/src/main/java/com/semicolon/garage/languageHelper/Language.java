package com.semicolon.garage.languageHelper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

public class Language {
    private static final String SELECTED_LANGUAGE="SELECTED_LANGUAGE";

    public static Context onAttach(Context context,String language)
    {
        String lang = getLanguage(context,language);
        return setLocality(context,lang);
    }

    private static String getLanguage(Context context,String defaultLanguage)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String lang =preferences.getString(SELECTED_LANGUAGE,defaultLanguage);
        return lang;
    }

    public static Context setLocality(Context context,String language)
    {
        SaveLanguage(context,language);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
        {
            return UpdateResource(context,language);
        }
        return UpdateLegacy(context,language);
    }

    @SuppressWarnings("deprecation")
    private static Context UpdateLegacy(Context context, String language) {
        Locale locale = new Locale(language);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            configuration.setLayoutDirection(locale);
        }
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());

        return context;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context UpdateResource(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        return context.createConfigurationContext(configuration);
    }

    private static void SaveLanguage(Context context, String language) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SELECTED_LANGUAGE,language);
        editor.apply();
    }
}
