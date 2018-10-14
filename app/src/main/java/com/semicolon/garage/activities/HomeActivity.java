package com.semicolon.garage.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.semicolon.garage.R;
import com.semicolon.garage.fragments.Fragment_Contactus;
import com.semicolon.garage.fragments.Fragment_Home;
import com.semicolon.garage.fragments.Fragment_Notification;
import com.semicolon.garage.fragments.Fragment_Profile;
import com.semicolon.garage.fragments.Fragment_Reservation;
import com.semicolon.garage.fragments.Fragment_Terms_Condition;
import com.semicolon.garage.languageHelper.Language;
import com.semicolon.garage.models.UserModel;
import com.semicolon.garage.preferences.Preferences;
import com.semicolon.garage.singletone.UserSingleTone;

import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView tv_title,tv_not;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private String lang;
    private ImageView user_image;
    private TextView user_name;
    private FrameLayout fl_not;
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
        setContentView(R.layout.activity_home);
        initView();

    }

    private void initView() {

        preferences = Preferences.getInstance();
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        String session =preferences.getSession(this);


        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawer =  findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view = navigationView.getHeaderView(0);

        user_image = view.findViewById(R.id.image);
        user_name = view.findViewById(R.id.tv_name);

        tv_title = findViewById(R.id.tv_title);
        tv_not = findViewById(R.id.tv_not);
        fl_not = findViewById(R.id.fl_not);



        UpdateTitle(getString(R.string.home));
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home_container, Fragment_Home.getInstance()).commit();
        fl_not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateTitle(getString(R.string.notification));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home_container, Fragment_Notification.getInstance()).commit();

            }
        });
        updateNoticationUi(20);

        /*if (session.equals(Tags.session_login))
        {
            userModel = preferences.getUserData(this);

            UpdateUI(userModel);
        }else
            {
                fl_not.setVisibility(View.INVISIBLE);
            }*/

    }

    private void UpdateUI(UserModel userModel) {
        userSingleTone.setUserModel(userModel);
        /*user_name.setText();
        Picasso.with(this).load(Uri.parse(Tags.IMAGE_URL+"")).into(user_image);*/
        getUnreadNotificationCount();

    }

    private void getUnreadNotificationCount() {

    }

    private void updateNoticationUi(int count)
    {
        if (count==0)
        {
            tv_not.setVisibility(View.INVISIBLE);
        }else if (count>0)
        {
            tv_not.setVisibility(View.VISIBLE);
            tv_not.setText(String.valueOf(count));

        }
    }


    public void UpdateTitle(String title)
    {
        tv_title.setText(title);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.home:
                UpdateTitle(getString(R.string.home));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home_container,Fragment_Home.getInstance()).commit();
                break;
            case R.id.profile:
                UpdateTitle(getString(R.string.profile));

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home_container, Fragment_Profile.getInstance()).commit();

                break;
            case R.id.reservation:
                UpdateTitle(getString(R.string.reservations));

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home_container, Fragment_Reservation.getInstance()).commit();

                break;
            case R.id.contact:
                UpdateTitle(getString(R.string.contact_us));

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home_container, Fragment_Contactus.getInstance()).commit();

                break;
            case R.id.term:
                UpdateTitle(getString(R.string.terms_and_conditions));

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home_container, Fragment_Terms_Condition.getInstance()).commit();

                break;
            case R.id.logout:
                if (userModel==null)
                {
                    finish();
                }else
                    {
                        logout();
                    }
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {

    }


    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_home_container);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (! (fragment instanceof Fragment_Home))
        {
            navigationView.getMenu().getItem(0).setChecked(true);
            UpdateTitle(getString(R.string.home));
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home_container, Fragment_Home.getInstance()).commit();

        }

        else {
            super.onBackPressed();
        }
    }
}
