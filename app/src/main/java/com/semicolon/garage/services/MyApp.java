package com.semicolon.garage.services;

import android.app.Application;
import android.content.Context;

import com.semicolon.garage.R;
import com.semicolon.garage.languageHelper.Language;
import com.semicolon.garage.tags.Tags;

import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MyApp extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(Language.onAttach(base,"ar")));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(this);
        String lang = Paper.book().read("language");
        if (lang!=null)
        {
            if (lang.equals("ar"))
            {
                CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath(Tags.ar_font)
                        .setFontAttrId(R.attr.fontPath)
                        .build());

            }else if (lang.equals("en"))
            {
                CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath(Tags.en_font)
                        .setFontAttrId(R.attr.fontPath)
                        .build());
            }
        }else
            {
                Paper.book().write("language","ar");
                CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath(Tags.en_font)
                        .setFontAttrId(R.attr.fontPath)
                        .build());
            }

    }
}
