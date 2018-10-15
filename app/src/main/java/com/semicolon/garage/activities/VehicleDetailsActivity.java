package com.semicolon.garage.activities;

import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.semicolon.garage.R;
import com.semicolon.garage.adapters.ImagesAdapter;
import com.semicolon.garage.adapters.MyPagerAdapterImages;
import com.semicolon.garage.fragments.Fragment_Images;
import com.semicolon.garage.languageHelper.Language;
import com.semicolon.garage.models.UserModel;
import com.semicolon.garage.models.VehicleModel;
import com.semicolon.garage.singletone.UserSingleTone;
import com.semicolon.garage.tags.Tags;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

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
    private List<VehicleModel.GalleryInside> galleryInsideList;
    private VehicleModel vehicleModel;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private String lang;
    private AlertDialog dialog;

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
        setContentView(R.layout.activity_machine_details);
        initView();
        getDataFromIntent();
    }



    private void initView() {
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
                image_back.setRotation(180);

            }
        }else
            {
                image_back.setRotation(180);

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
                    CreateAlertDialog();
                }else
                    {

                    }
            }
        });

        btn_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("rate",rateBar.getRating()+"_");

            }
        });


    }

    private void CreateAlertDialog()
    {
        dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(this).inflate(R.layout.custom_dialog,null);
        Button doneBtn = view.findViewById(R.id.doneBtn);

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations=R.style.dialog;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(view);
        dialog.show();
    }
    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {
            vehicleModel = (VehicleModel) intent.getSerializableExtra("data");
            UpdateUi(vehicleModel);
        }
    }

    private void UpdateUi(final VehicleModel vehicleModel)
    {
        tv_price.setText(vehicleModel.getCost());
        if (vehicleModel.getCategory_id_fk().equals(Tags.type_cars))
        {
            tv_details.setText(vehicleModel.getCar_trademarks()+"\n"+vehicleModel.getTitle()+"\n"+vehicleModel.getCar_model());

        }

        else
            {
                tv_details.setText(vehicleModel.getCar_trademarks()+"\n"+vehicleModel.getTitle()+"\n"+vehicleModel.getSize()+" "+getString(R.string.size));

            }




            Log.e("size",vehicleModel.getGallary_color().size()+"");
            if (vehicleModel.getGallary_color().size()>0)
            {
                if (vehicleModel.getGallary_color().size()==1)
                {
                    myPagerAdapter = new MyPagerAdapterImages(getSupportFragmentManager());

                    for (VehicleModel.GalleryColor galleryColor:vehicleModel.getGallary_color())
                    {
                        myPagerAdapter.AddFragment(Fragment_Images.getInstance(galleryColor.getPhoto_name()));
                    }

                    pager.setAdapter(myPagerAdapter);
                    image_left.setVisibility(View.INVISIBLE);
                    image_right.setVisibility(View.INVISIBLE);

                }else if (vehicleModel.getGallary_color().size()>1)
                {
                    myPagerAdapter = new MyPagerAdapterImages(getSupportFragmentManager());

                    for (VehicleModel.GalleryColor galleryColor:vehicleModel.getGallary_color())
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
                        if (pager.getCurrentItem()==vehicleModel.getGallary_color().size()-1)
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



        galleryInsideList.addAll(vehicleModel.getGallary_inside());
        adapter.notifyDataSetChanged();

    }




}
