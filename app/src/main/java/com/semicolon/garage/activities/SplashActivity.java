package com.semicolon.garage.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.semicolon.garage.R;
import com.semicolon.garage.preferences.Preferences;
import com.semicolon.garage.tags.Tags;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {
    private String lang;
    private ImageView image;
    private Preferences preferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        preferences = Preferences.getInstance();
        Paper.init(this);
        lang = Paper.book().read("language");
        image = findViewById(R.id.image);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.fade);
        image.clearAnimation();
        image.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ////

                if (!preferences.isLanguageSelected(SplashActivity.this))
                {
                    Intent intent = new Intent(SplashActivity.this,ChooseCountry_LanguageActivity.class);
                    startActivity(intent);
                    finish();
                }else
                    {
                        String session = preferences.getSession(SplashActivity.this);

                        if (session.equals(Tags.session_login))
                        {
                            Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
                            startActivity(intent);
                            finish();
                            Log.e("lllllllng",lang+"_+_+");
                        }else
                            {
                                Intent intent = new Intent(SplashActivity.this,Login_Register_Activity.class);
                                startActivity(intent);
                                finish();
                                Log.e("lllllllng",Paper.book().read("language")+"_+_+");
                            }

                    }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
