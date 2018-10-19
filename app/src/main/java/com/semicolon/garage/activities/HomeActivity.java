package com.semicolon.garage.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
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
import com.semicolon.garage.models.UnReadeModel;
import com.semicolon.garage.models.UserModel;
import com.semicolon.garage.preferences.Preferences;
import com.semicolon.garage.remote.Api;
import com.semicolon.garage.services.UpdateLocation;
import com.semicolon.garage.share.Common;
import com.semicolon.garage.singletone.UserSingleTone;
import com.semicolon.garage.tags.Tags;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

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
                    .setDefaultFontPath(Tags.ar_font)
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
                ReadNotification();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home_container, Fragment_Notification.getInstance()).commit();

            }
        });

        if (session.equals(Tags.session_login))
        {
            CheckPermission();
            userModel = preferences.getUserData(this);
            if (!EventBus.getDefault().isRegistered(this))
            {
                EventBus.getDefault().register(this);
            }

            UpdateTokenId();
        }else
            {
                fl_not.setVisibility(View.INVISIBLE);
            }

    }



    private void UpdateTokenId() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful())
                        {
                            String token = task.getResult().getToken();

                            Api.getService()
                                    .updateTokenId(userModel.getUser_id(),token)
                                    .enqueue(new Callback<ResponsModel>() {
                                        @Override
                                        public void onResponse(Call<ResponsModel> call, Response<ResponsModel> response) {
                                            if (response.isSuccessful())
                                            {
                                                if (response.body().getSuccess_token_id()==1)
                                                {
                                                    Log.e("Token","Token updated successfully");
                                                }else if (response.body().getSuccess_token_id()==0)
                                                {
                                                    Log.e("Token","Token updated Failed");

                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponsModel> call, Throwable t) {
                                            Log.e("Error",t.getMessage());
                                        }
                                    });
                        }
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();
        String session =preferences.getSession(this);
        if (session.equals(Tags.session_login))
        {
            userModel = preferences.getUserData(this);
            UpdateUI(userModel);
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

        Log.e("usernaem",userModel.getUser_full_name()+"__");
        userSingleTone.setUserModel(userModel);
        user_name.setText(userModel.getUser_full_name());
        Picasso.with(this).load(Uri.parse(Tags.IMAGE_URL+userModel.getUser_photo())).into(user_image);
        getUnreadNotificationCount();

    }

    private void ReadNotification() {
        Api.getService()
                .readNotification(userModel.getUser_id(),"1")
                .enqueue(new Callback<ResponsModel>() {
                    @Override
                    public void onResponse(Call<ResponsModel> call, Response<ResponsModel> response) {
                        if(response.isSuccessful())
                        {
                            if (response.body().getSuccess_read()==1)
                            {
                                updateNotificationUi(0);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                    }
                });
    }
    private void getUnreadNotificationCount()
    {

        Api.getService()
                .getUnReadNotification(userModel.getUser_id())
                .enqueue(new Callback<UnReadeModel>() {
                    @Override
                    public void onResponse(Call<UnReadeModel> call, Response<UnReadeModel> response) {
                        if (response.isSuccessful())
                        {
                            updateNotificationUi(response.body().getAlert_count());
                        }
                    }

                    @Override
                    public void onFailure(Call<UnReadeModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                    }
                });
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

                if (userModel!=null)
                {
                    UpdateTitle(getString(R.string.profile));

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home_container, Fragment_Profile.getInstance(userModel)).commit();

                }else
                    {

                        AlertDialog alertDialog = Common.CreateUserNotSignInAlertDialog(HomeActivity.this);
                        alertDialog.show();
                        navigationView.getMenu().getItem(1).setChecked(false);

                        navigationView.getMenu().getItem(0).setChecked(true);

                    }

                break;
            case R.id.reservation:
                if (userModel!=null)
                {
                    UpdateTitle(getString(R.string.reservations));

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home_container, Fragment_Reservation.getInstance()).commit();

                }else
                {
                    AlertDialog alertDialog = Common.CreateUserNotSignInAlertDialog(HomeActivity.this);
                    alertDialog.show();
                    navigationView.getMenu().getItem(2).setChecked(false);

                    navigationView.getMenu().getItem(0).setChecked(true);

                }


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
        final ProgressDialog outdialog = Common.createProgressDialog(this,getString(R.string.logging_out));
        outdialog.show();
        Api.getService()
                .logout(userModel.getUser_id())
                .enqueue(new Callback<ResponsModel>() {
                    @Override
                    public void onResponse(Call<ResponsModel> call, Response<ResponsModel> response) {
                        if (response.isSuccessful())
                        {
                            outdialog.dismiss();
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
                        outdialog.dismiss();
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
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment :fragmentList)
        {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
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
