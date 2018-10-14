package com.semicolon.garage.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.semicolon.garage.R;
import com.semicolon.garage.fragments.Fragment_SignIn;
import com.semicolon.garage.fragments.Fragment_SignUp;
import com.semicolon.garage.languageHelper.Language;
import com.semicolon.garage.preferences.Preferences;

import net.cachapa.expandablelayout.ExpandableLayout;

import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Login_Register_Activity extends AppCompatActivity {
    private LinearLayout ll_bg;
    private Button btn_sign_in,btn_sign_up;
    private Preferences preferences;
    private String lang;
    private ExpandableLayout expand_layout;
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
        setContentView(R.layout.activity_login_register);
        initView();
    }

    private void initView() {
        preferences = Preferences.getInstance();
        ll_bg = findViewById(R.id.ll_bg);
        btn_sign_in = findViewById(R.id.btn_sign_in);
        btn_sign_up = findViewById(R.id.btn_sign_up);
        expand_layout = findViewById(R.id.expand_layout);


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, Fragment_SignIn.getInstance()).commit();
        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_sign_in.setBackgroundResource(R.drawable.btn_ar_bg_sel);
                btn_sign_in.setTextColor(ContextCompat.getColor(Login_Register_Activity.this,R.color.white));
                btn_sign_up.setBackgroundResource(R.drawable.btn_en_bg_unsel);
                btn_sign_up.setTextColor(ContextCompat.getColor(Login_Register_Activity.this,R.color.black));
                ll_bg.setBackgroundResource(R.drawable.login_bg);
                expand_layout.collapse(true);

                new Handler()
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, Fragment_SignIn.getInstance()).commit();
                                expand_layout.expand(true);

                            }
                        },2000);

            }
        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_sign_in.setBackgroundResource(R.drawable.btn_ar_bg_unsel);
                btn_sign_in.setTextColor(ContextCompat.getColor(Login_Register_Activity.this,R.color.black));
                btn_sign_up.setBackgroundResource(R.drawable.btn_en_bg_sel);
                btn_sign_up.setTextColor(ContextCompat.getColor(Login_Register_Activity.this,R.color.white));
                ll_bg.setBackgroundResource(R.drawable.register_bg);
                expand_layout.collapse(true);

                new Handler()
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, Fragment_SignUp.getInstance()).commit();
                                expand_layout.expand(true);

                            }
                        },2000);

            }
        });


    }
}
