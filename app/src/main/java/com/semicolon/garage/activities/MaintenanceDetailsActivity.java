package com.semicolon.garage.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.semicolon.garage.R;
import com.semicolon.garage.languageHelper.Language;
import com.semicolon.garage.models.MaintenanceModel;
import com.semicolon.garage.tags.Tags;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MaintenanceDetailsActivity extends AppCompatActivity {
    private String lang;
    private ImageView image_back;
    private RoundedImageView image;
    private KenBurnsView image_bg;
    private TextView tv_name,tv_phone,tv_address,tv_country;
    private Button btn_location;
    private CardView cardView_PhoneCall;
    private MaintenanceModel maintenanceModel;
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
        setContentView(R.layout.activity_maintenance_details);
        initView();
        getDataFromIntent();
    }



    private void initView() {
        image_back = findViewById(R.id.image_back);
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
        //////////////////////////////////////////////////////////
        image = findViewById(R.id.image);
        image_bg = findViewById(R.id.image_bg);
        tv_name = findViewById(R.id.tv_name);
        tv_phone = findViewById(R.id.tv_phone);
        tv_address = findViewById(R.id.tv_address);
        tv_country = findViewById(R.id.tv_country);
        cardView_PhoneCall = findViewById(R.id.cardView_PhoneCall);
        btn_location = findViewById(R.id.btn_location);

        cardView_PhoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+maintenanceModel.getPhone()));
                startActivity(intent);
            }
        });

        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MaintenanceDetailsActivity.this,MapsActivity.class);
                intent.putExtra("lat",Double.parseDouble(maintenanceModel.getAddress_google_lat()));
                intent.putExtra("lng",Double.parseDouble(maintenanceModel.getAddress_google_long()));
                startActivity(intent);
            }
        });


    }
    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {
            maintenanceModel = (MaintenanceModel) intent.getSerializableExtra("data");
            UpdateUi(maintenanceModel);
        }
    }

    private void UpdateUi(MaintenanceModel maintenanceModel) {
        Picasso.with(this).load(Uri.parse(Tags.IMAGE_URL+maintenanceModel.getMain_photo())).into(image_bg);
        Picasso.with(this).load(Uri.parse(Tags.IMAGE_URL+maintenanceModel.getMain_photo())).into(image);
        tv_name.setText(maintenanceModel.getTitle());
        tv_address.setText(maintenanceModel.getDetails());
        tv_phone.setText(maintenanceModel.getPhone());
            if (lang.equals("ar"))
            {
                tv_country.setText(maintenanceModel.getAr_name());
            }else
                {
                    tv_country.setText(maintenanceModel.getEn_name());

                }


    }
}
