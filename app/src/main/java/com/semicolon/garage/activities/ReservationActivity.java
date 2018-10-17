package com.semicolon.garage.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.semicolon.garage.R;
import com.semicolon.garage.languageHelper.Language;
import com.semicolon.garage.models.RentModel;
import com.semicolon.garage.tags.Tags;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ReservationActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener{

    private String lang;
    private ImageView image_back;
    private TextView tv_location,tv_from_date,tv_to_date;
    private LinearLayout ll_from_date,ll_to_date;
    private DatePickerDialog datePickerDialog;
    private String flag = "-1";
    private RentModel rentModel;
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
        setContentView(R.layout.activity_reservation);
        initView();
        getDataFromIntent();


    }


    private void initView() {
        image_back = findViewById(R.id.image_back);
        if (lang.equals("ar"))
        {
            Language.setLocality(this,"ar");
            image_back.setRotation(180f);
        }

        tv_location = findViewById(R.id.tv_location);
        tv_from_date = findViewById(R.id.tv_from_date);
        tv_to_date = findViewById(R.id.tv_to_date);
        ll_from_date = findViewById(R.id.ll_from_date);
        ll_to_date = findViewById(R.id.ll_to_date);

        Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        datePickerDialog.setCancelColor(ContextCompat.getColor(this,R.color.press_color));
        datePickerDialog.setOkColor(ContextCompat.getColor(this,R.color.press_color));
        ll_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag="1";
                datePickerDialog.show(getFragmentManager(),"Select Date");
            }
        });

        ll_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag="2";
                datePickerDialog.show(getFragmentManager(),"Select Date");
            }
        });

    }

    private void getDataFromIntent() {

        Intent intent = getIntent();
        if (intent!=null)
        {
            rentModel = (RentModel) intent.getSerializableExtra("data");

        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        String m_date="";
        if (lang.equals("ar"))
        {
            m_date = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;

        }else if (lang.equals("en"))
        {
            m_date = dayOfMonth+"-"+(monthOfYear+1)+"-"+year;

        }
        String mdate = dayOfMonth+"-"+(monthOfYear+1)+"-"+year;

        Calendar selected_date = Calendar.getInstance(Locale.ENGLISH);
        selected_date.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        selected_date.set(Calendar.MONTH,monthOfYear);
        selected_date.set(Calendar.YEAR,year);

        Calendar now = Calendar.getInstance(Locale.ENGLISH);
        Date s_date = new Date(selected_date.getTimeInMillis());
        Date n_date = new Date(now.getTimeInMillis());

        if (s_date.before(n_date))
        {
            Toast.makeText(this, "Selected date is too old", Toast.LENGTH_SHORT).show();
        }else
            {
                if (flag.equals("1"))
                {
                    tv_from_date.setText(m_date);
                }else if (flag.equals("2"))
                {
                    tv_to_date.setText(m_date);

                }

            }







    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

    }
}
