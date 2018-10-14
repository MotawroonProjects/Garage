package com.semicolon.garage.activities;

import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.semicolon.garage.R;

public class MachineDetailsActivity extends AppCompatActivity {
    private ImageView image_back,image_right,image_left;
    private ViewPager pager;
    private TextView tv_price,tv_details;
    private Button btn_rate,btn_reserve;
    private RatingBar rateBar;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_details);
        initView();
    }

    private void initView() {
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

        LayerDrawable drawable = (LayerDrawable) rateBar.getProgressDrawable();
        drawable.getDrawable(0).setColorFilter(ContextCompat.getColor(this,R.color.start_end_color), PorterDuff.Mode.SRC_ATOP);
        drawable.getDrawable(1).setColorFilter(ContextCompat.getColor(this,R.color.start_end_color), PorterDuff.Mode.SRC_ATOP);
        drawable.getDrawable(2).setColorFilter(ContextCompat.getColor(this,R.color.rate_color), PorterDuff.Mode.SRC_ATOP);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void UpdateUi()
    {
        /*tv_price.setText("");
        tv_details.setText("");*/

    }
}
