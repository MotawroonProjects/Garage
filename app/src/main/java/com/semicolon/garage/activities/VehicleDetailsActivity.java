package com.semicolon.garage.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.semicolon.garage.R;
import com.semicolon.garage.adapters.ImagesAdapter;
import com.semicolon.garage.adapters.MyPagerAdapterImages;
import com.semicolon.garage.fragments.Fragment_Images;
import com.semicolon.garage.languageHelper.Language;
import com.semicolon.garage.models.RentModel;
import com.semicolon.garage.models.ResponsModel;
import com.semicolon.garage.models.UserModel;
import com.semicolon.garage.remote.Api;
import com.semicolon.garage.share.Common;
import com.semicolon.garage.singletone.UserSingleTone;
import com.semicolon.garage.tags.Tags;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleDetailsActivity extends AppCompatActivity {
    private ImageView image_back,image_right,image_left;
    private ViewPager pager;
    private MyPagerAdapterImages myPagerAdapter;
    private TextView tv_price,tv_details;
    private Button btn_rate,btn_reserve;
    private RatingBar rateBar;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;
    private List<RentModel.GalleryInside> galleryInsideList;
    private RentModel rentModel;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private String lang;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_details);
        initView();
        getDataFromIntent();
    }



    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("language");
        galleryInsideList = new ArrayList<>();
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        image_back = findViewById(R.id.image_back);
        image_right = findViewById(R.id.image_right);
        image_left = findViewById(R.id.image_left);
        pager = findViewById(R.id.pager);
        tv_price = findViewById(R.id.tv_price);
        tv_details = findViewById(R.id.tv_details);
        btn_rate = findViewById(R.id.btn_rate);
        btn_reserve = findViewById(R.id.btn_reserve);
        rateBar = findViewById(R.id.rateBar);
        recView = findViewById(R.id.recView);
        manager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recView.setLayoutManager(manager);
        adapter = new ImagesAdapter(this,galleryInsideList);
        recView.setAdapter(adapter);
        pager.beginFakeDrag();
        LayerDrawable drawable = (LayerDrawable) rateBar.getProgressDrawable();
        drawable.getDrawable(0).setColorFilter(ContextCompat.getColor(this,R.color.start_end_color), PorterDuff.Mode.SRC_ATOP);
        drawable.getDrawable(1).setColorFilter(ContextCompat.getColor(this,R.color.start_end_color), PorterDuff.Mode.SRC_ATOP);
        drawable.getDrawable(2).setColorFilter(ContextCompat.getColor(this,R.color.rate_color), PorterDuff.Mode.SRC_ATOP);

        if (lang!=null)
        {
            if (lang.equals("ar"))
            {
                Language.setLocality(this,"ar");
                image_back.setRotation(180f);
            }
        }

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userModel==null)
                {
                    dialog = Common.CreateUserNotSignInAlertDialog(VehicleDetailsActivity.this);
                    dialog.show();
                }else
                    {
                        Intent intent = new Intent(VehicleDetailsActivity.this,ReservationActivity.class);
                        intent.putExtra("data", rentModel);
                        intent.putExtra("address","");
                        intent.putExtra("start_date","");
                        intent.putExtra("end_date","");
                        intent.putExtra("type",Tags.add_reservation);

                        startActivity(intent);
                    }
            }
        });

        btn_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog rateDialog = Common.createProgressDialog(VehicleDetailsActivity.this,getString(R.string.rating2));
                rateDialog.show();
                Api.getService()
                        .sendEvaluation(rentModel.getId_car_maintenance(),rateBar.getRating())
                        .enqueue(new Callback<ResponsModel>() {
                            @Override
                            public void onResponse(Call<ResponsModel> call, Response<ResponsModel> response) {
                                if (response.isSuccessful())
                                {
                                    rateDialog.dismiss();
                                    if (response.body().getSuccess_evaluation()==1)
                                    {
                                        Toast.makeText(VehicleDetailsActivity.this, R.string.eval_succ,Toast.LENGTH_LONG).show();
                                    }else if (response.body().getSuccess_evaluation()==0)
                                    {
                                        Toast.makeText(VehicleDetailsActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            @Override
                            public void onFailure(Call<ResponsModel> call, Throwable t) {
                                Log.e("Error",t.getMessage());
                                rateDialog.dismiss();

                            }
                        });

            }
        });


    }


    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {
            rentModel = (RentModel) intent.getSerializableExtra("data");
            UpdateUi(rentModel);
        }
    }

    private void UpdateUi(final RentModel rentModel)
    {
        tv_price.setText(rentModel.getCost());
        if (rentModel.getCategory_id_fk().equals(Tags.type_rent_cars))
        {
            tv_details.setText(rentModel.getCar_trademarks()+"\n"+ rentModel.getTitle()+"\n"+ rentModel.getCar_model());

        }

        else
            {
                tv_details.setText(rentModel.getCar_trademarks()+"\n"+ rentModel.getTitle()+"\n"+ rentModel.getSize()+" "+getString(R.string.size));

            }




            Log.e("size", rentModel.getGallary_color().size()+"");
            if (rentModel.getGallary_color().size()>0)
            {
                if (rentModel.getGallary_color().size()==1)
                {
                    myPagerAdapter = new MyPagerAdapterImages(getSupportFragmentManager());

                    for (RentModel.GalleryColor galleryColor: rentModel.getGallary_color())
                    {
                        myPagerAdapter.AddFragment(Fragment_Images.getInstance(galleryColor.getPhoto_name()));
                    }

                    pager.setAdapter(myPagerAdapter);
                    image_left.setVisibility(View.INVISIBLE);
                    image_right.setVisibility(View.INVISIBLE);

                }else if (rentModel.getGallary_color().size()>1)
                {
                    myPagerAdapter = new MyPagerAdapterImages(getSupportFragmentManager());

                    for (RentModel.GalleryColor galleryColor: rentModel.getGallary_color())
                    {
                        myPagerAdapter.AddFragment(Fragment_Images.getInstance(galleryColor.getPhoto_name()));
                    }
                    pager.setAdapter(myPagerAdapter);
                    image_right.setVisibility(View.VISIBLE);

                } else
                {
                    image_left.setVisibility(View.INVISIBLE);
                    image_right.setVisibility(View.INVISIBLE);


                }
            }



                image_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pager.setCurrentItem(pager.getCurrentItem()+1);
                        image_left.setVisibility(View.VISIBLE);
                        if (pager.getCurrentItem()== rentModel.getGallary_color().size()-1)
                        {
                            image_right.setVisibility(View.INVISIBLE);
                        }
                    }
                });

        image_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(pager.getCurrentItem()-1);
                image_right.setVisibility(View.VISIBLE);
                if (pager.getCurrentItem()==0)
                {
                    image_left.setVisibility(View.INVISIBLE);
                }
            }
        });



        galleryInsideList.addAll(rentModel.getGallary_inside());
        adapter.notifyDataSetChanged();

    }




}
