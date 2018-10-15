package com.semicolon.garage.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.semicolon.garage.R;
import com.semicolon.garage.languageHelper.Language;
import com.semicolon.garage.models.Country_Nationality;
import com.semicolon.garage.preferences.Preferences;
import com.semicolon.garage.remote.Api;
import com.semicolon.garage.share.Common;
import com.semicolon.garage.tags.Tags;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ChooseCountry_LanguageActivity extends AppCompatActivity {
    private String language = "ar";
    private Button btn_ar,btn_en,btn_continue;
    private String lang;
    private Preferences preferences;
    private Spinner spinner_country;
    private ArrayAdapter<String> adapter;
    private List<String> adapterList;
    private List<Country_Nationality> country_nationalityList;
    private Country_Nationality country_nationality;
    private ProgressDialog dialog;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        lang = Paper.book().read("language");
        if (lang!=null)
        {
            super.attachBaseContext(CalligraphyContextWrapper.wrap(Language.onAttach(newBase,lang)));

        }else
            {
                super.attachBaseContext(CalligraphyContextWrapper.wrap(Language.onAttach(newBase,"ar")));

            }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_country_language);
        initView();
    }

    private void initView() {
        adapterList = new ArrayList<>();
        preferences = Preferences.getInstance();
        Typeface typeface_ar = Typeface.createFromAsset(getAssets(),Tags.ar_font);
        Typeface typeface_en = Typeface.createFromAsset(getAssets(),Tags.en_font);
        btn_ar = findViewById(R.id.btn_ar);
        btn_en = findViewById(R.id.btn_en);
        btn_continue = findViewById(R.id.btn_continue);
        btn_ar.setTypeface(typeface_ar);
        btn_en.setTypeface(typeface_en);
        spinner_country =findViewById(R.id.spinner_country);






        btn_ar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language = "ar";
                Paper.book().write("language",language);
                btn_ar.setBackgroundResource(R.drawable.btn_ar_bg_sel);
                btn_ar.setTextColor(ContextCompat.getColor(ChooseCountry_LanguageActivity.this,R.color.white));
                btn_en.setBackgroundResource(R.drawable.btn_en_bg_unsel);
                btn_en.setTextColor(ContextCompat.getColor(ChooseCountry_LanguageActivity.this,R.color.black));

                refreshLayout(language);
            }
        });

        btn_en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language = "en";
                Paper.book().write("language",language);


                btn_ar.setBackgroundResource(R.drawable.btn_ar_bg_unsel);
                btn_ar.setTextColor(ContextCompat.getColor(ChooseCountry_LanguageActivity.this,R.color.black));
                btn_en.setBackgroundResource(R.drawable.btn_en_bg_sel);
                btn_en.setTextColor(ContextCompat.getColor(ChooseCountry_LanguageActivity.this,R.color.white));

                refreshLayout(language);

            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (country_nationality!=null)
                {
                    preferences.setCountry_NaionalityData(ChooseCountry_LanguageActivity.this,country_nationality);

                    Paper.book().write("language",language);
                    preferences.setIsLanguageSelected(ChooseCountry_LanguageActivity.this,true);


                    Intent intent = new Intent(ChooseCountry_LanguageActivity.this,Login_Register_Activity.class);
                    startActivity(intent);
                    finish();
                }else
                    {
                        Toast.makeText(ChooseCountry_LanguageActivity.this, R.string.ch_count, Toast.LENGTH_SHORT).show();
                    }



            }
        });

        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)parent.getChildAt(0)).setTextColor(ContextCompat.getColor(ChooseCountry_LanguageActivity.this,R.color.white));

                if (position==0)
                {
                    country_nationality=null;
                }else
                    {
                        country_nationality = country_nationalityList.get(position-1);


                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getCountries_Nationality();
    }

    private void getCountries_Nationality() {
        dialog = Common.createProgressDialog(this,getString(R.string.load_country));
        dialog.show();
        Api.getService().getCountries_Nationality()
                .enqueue(new Callback<List<Country_Nationality>>() {
                    @Override
                    public void onResponse(Call<List<Country_Nationality>> call, Response<List<Country_Nationality>> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();

                            if (response.body().size()>0)
                            {
                                country_nationalityList = response.body();
                                adapterList.clear();
                                adapterList.add(getString(R.string.ch));

                                for (Country_Nationality country_nationality:response.body())
                                {

                                    if (lang!=null)
                                    {
                                        if (lang.equals("ar"))
                                        {
                                            adapterList.add(country_nationality.getAr_name());
                                        }else
                                        {
                                            adapterList.add(country_nationality.getEn_name());

                                        }
                                    }else
                                        {
                                            adapterList.add(country_nationality.getAr_name());

                                        }

                                }
                                adapter = new ArrayAdapter<>(ChooseCountry_LanguageActivity.this,R.layout.spinner_row,adapterList);
                                spinner_country.setAdapter(adapter);

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Country_Nationality>> call, Throwable t) {
                        dialog.dismiss();
                        Log.e("Error",t.getMessage());
                        Toast.makeText(ChooseCountry_LanguageActivity.this, R.string.something, Toast.LENGTH_SHORT).show();

                    }
                });

    }


    private void refreshLayout(String language)
    {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
        Language.setLocality(this,language);
    }


}
