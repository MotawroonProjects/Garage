package com.semicolon.garage.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.semicolon.garage.R;
import com.semicolon.garage.fragments.Fragment_Contactus;
import com.semicolon.garage.fragments.Fragment_Home;
import com.semicolon.garage.fragments.Fragment_Notification;
import com.semicolon.garage.fragments.Fragment_Profile;
import com.semicolon.garage.fragments.Fragment_Reservation;
import com.semicolon.garage.fragments.Fragment_Terms_Condition;
import com.semicolon.garage.languageHelper.Language;
import com.semicolon.garage.models.LocationModel;
import com.semicolon.garage.models.ResponsModel;
import com.semicolon.garage.models.UserModel;
import com.semicolon.garage.preferences.Preferences;
import com.semicolon.garage.remote.Api;
import com.semicolon.garage.services.UpdateLocation;
import com.semicolon.garage.singletone.UserSingleTone;
import com.semicolon.garage.tags.Tags;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
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
    private final String finLoc= Manifest.permission.ACCESS_FINE_LOCATION;
    private final int gps_req = 1202,per_req = 1023;
    private AlertDialog dialog;
    private Intent intentService;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        lang = Paper.book().read("language");

        if (lang!=null)
        {
            super.attachBaseContext(CalligraphyContextWrapper.wrap(Language.onAttach(newBase,lang)));
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
            super.attachBaseContext(CalligraphyContextWrapper.wrap(Language.onAttach(newBase,"ar")));
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                    .setDefaultFontPath(Tags.en_font)
                    .setFontAttrId(R.attr.fontPath)
                    .build());
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
        updateNotificationUi(20);

        if (session.equals(Tags.session_login))
        {
            CheckPermission();
            userModel = preferences.getUserData(this);
            if (!EventBus.getDefault().isRegistered(this))
            {
                EventBus.getDefault().register(this);
            }

            UpdateUI(userModel);
        }else
            {
                fl_not.setVisibility(View.INVISIBLE);
            }

    }

    private boolean isGpsOpen()
    {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (manager!=null)
        {
            return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        }else
            {
                return false;
            }
    }


    private void UpdateUI(UserModel userModel) {

        userSingleTone.setUserModel(userModel);
        /*user_name.setText();
        Picasso.with(this).load(Uri.parse(Tags.IMAGE_URL+"")).into(user_image);*/
        getUnreadNotificationCount();

    }

    private void getUnreadNotificationCount() {

    }

    private void updateNotificationUi(int count)
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ListenToLocationUpdate(LocationModel locationModel)
    {
        Log.e("lat",locationModel.getLat()+"::");
        Log.e("lng",locationModel.getLng()+"::");

        UpdateLocation(locationModel);

    }


    private void UpdateLocation(LocationModel locationModel)
    {
        Api.getService().updateLocation
                (userModel.getUser_id(),locationModel.getLat(),locationModel.getLng())
                .enqueue(new Callback<ResponsModel>() {
                    @Override
                    public void onResponse(Call<ResponsModel> call, Response<ResponsModel> response) {
                        if (response.isSuccessful())
                        {
                            if (response.body().getSuccess_location()==1)
                            {
                                Log.e("Location update","Location updated successfully");
                            }else
                                {
                                    Log.e("Location update","Location updated failed ");

                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());

                    }
                });
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
        Api.getService()
                .logout(userModel.getUser_id())
                .enqueue(new Callback<ResponsModel>() {
                    @Override
                    public void onResponse(Call<ResponsModel> call, Response<ResponsModel> response) {
                        if (response.isSuccessful())
                        {
                            if (response.body().getSuccess_logout()==1)
                            {
                                userModel = null;
                                userSingleTone.clear();
                                preferences.create_update_session(HomeActivity.this, Tags.session_logout);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        Toast.makeText(HomeActivity.this, R.string.something, Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void  openGps()
    {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent,gps_req);

    }

    private void CheckPermission()
    {
        if (ContextCompat.checkSelfPermission(this,finLoc)!= PackageManager.PERMISSION_GRANTED)
        {
            String [] per = {finLoc};
            ActivityCompat.requestPermissions(this,per,per_req);
        }else
        {
            if (isGpsOpen())
            {
                startLocationUpdate();
            }else
            {
                CreateAlertDialog();
            }
        }
    }

    private void CreateAlertDialog()
    {
        dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .create();

        View view = LayoutInflater.from(this).inflate(R.layout.gps_dialog,null);
        Button openBtn = view.findViewById(R.id.openBtn);
        Button cancelBtn = view.findViewById(R.id.cancelBtn);

        openBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openGps();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(view);
        dialog.show();
    }


    private void startLocationUpdate()
    {
        if (intentService==null)
        {
            intentService = new Intent(this, UpdateLocation.class);
        }
        startService(intentService);
    }
    private void stopLocationUpdate()
    {
        if (intentService!=null)
        {
            stopService(intentService);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==per_req)
        {
            if (grantResults.length>0)
            {
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    if (isGpsOpen())
                    {
                        startLocationUpdate();
                    }else
                    {
                        CreateAlertDialog();
                    }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==gps_req)
        {
            if (resultCode==RESULT_OK)
            {
                if (isGpsOpen())
                {
                    startLocationUpdate();
                }else
                {
                    openGps();
                }
            }else if (resultCode==RESULT_CANCELED)
            {
                if (isGpsOpen())
                {
                    startLocationUpdate();
                }else
                {
                    openGps();
                }
            }
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (intentService!=null)
        {
            stopLocationUpdate();
        }

        if (EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().unregister(this);
        }
    }
}
