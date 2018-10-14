package com.semicolon.garage.activities;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.semicolon.garage.R;
import com.semicolon.garage.languageHelper.Language;
import com.semicolon.garage.preferences.Preferences;
import com.semicolon.garage.tags.Tags;

import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ChooseCountry_LanguageActivity extends AppCompatActivity {
    private String language = "ar";
    private Button btn_ar,btn_en,btn_continue;
    private String lang;
    private Preferences preferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        lang = Paper.book().read("language");
        if (lang!=null)
        {
            super.attachBaseContext(CalligraphyContextWrapper.wrap(Language.onAttach(newBase,lang)));

        }else
            {
                super.attachBaseContext(CalligraphyContextWrapper.wrap(Language.onAttach(newBase,"en")));

            }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_country_language);
        initView();
    }

    private void initView() {
        preferences = Preferences.getInstance();
        Typeface typeface_ar = Typeface.createFromAsset(getAssets(),Tags.ar_font);
        Typeface typeface_en = Typeface.createFromAsset(getAssets(),Tags.en_font);
        btn_ar = findViewById(R.id.btn_ar);
        btn_en = findViewById(R.id.btn_en);
        btn_continue = findViewById(R.id.btn_continue);
        btn_ar.setTypeface(typeface_ar);
        btn_en.setTypeface(typeface_en);
        Log.e("laaaaaaaaaaaaang",lang+"______");




        btn_ar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language = "ar";
                btn_ar.setBackgroundResource(R.drawable.btn_ar_bg_sel);
                btn_ar.setTextColor(ContextCompat.getColor(ChooseCountry_LanguageActivity.this,R.color.white));
                btn_en.setBackgroundResource(R.drawable.btn_en_bg_unsel);
                btn_en.setTextColor(ContextCompat.getColor(ChooseCountry_LanguageActivity.this,R.color.black));

            }
        });

        btn_en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language = "en";
                btn_ar.setBackgroundResource(R.drawable.btn_ar_bg_unsel);
                btn_ar.setTextColor(ContextCompat.getColor(ChooseCountry_LanguageActivity.this,R.color.black));
                btn_en.setBackgroundResource(R.drawable.btn_en_bg_sel);
                btn_en.setTextColor(ContextCompat.getColor(ChooseCountry_LanguageActivity.this,R.color.white));

            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().write("language",language);
                preferences.setIsLanguageSelected(ChooseCountry_LanguageActivity.this,true);

            }
        });
    }



}
