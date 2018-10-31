package com.semicolon.garage.services;

import android.app.Application;
import android.content.Context;

import com.semicolon.garage.languageHelper.Language;

import io.paperdb.Paper;

public class MyApp extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language.onAttach(base,"ar"));

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(this);

        String lang = Paper.book().read("language");
        if (lang==null)
        {
            Paper.book().write("language","ar");

        }else
            {
                Paper.book().write("language",lang);

            }
    }
}
