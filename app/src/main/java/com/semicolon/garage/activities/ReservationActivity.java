package com.semicolon.garage.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lamudi.phonefield.PhoneInputLayout;
import com.semicolon.garage.R;
import com.semicolon.garage.languageHelper.Language;
import com.semicolon.garage.models.RentModel;
import com.semicolon.garage.models.ResponsModel;
import com.semicolon.garage.models.UserModel;
import com.semicolon.garage.remote.Api;
import com.semicolon.garage.share.Common;
import com.semicolon.garage.singletone.UserSingleTone;
import com.semicolon.garage.tags.Tags;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener{

    private String lang;
    private ImageView image_back,image_back_sheet,image;
    private TextView tv_from_date,tv_to_date;
    private EditText edt_location;
    private LinearLayout ll_from_date,ll_to_date;
    private DatePickerDialog datePickerDialog;
    private Button bookBtn,btn_send;
    private String flag = "-1";
    private RentModel rentModel;
    private Date fromDate=null,toDate=null;
    private String start_date="",end_date="";
    private View root;
    private BottomSheetBehavior behavior;
    private EditText edt_name,edt_phone,edt_amount;
    private PhoneInputLayout edt_check_phone;
    private LinearLayout ll_upload_image;
    private final String read_per = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final int read_req = 1023;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private Uri uri=null;
    private final int img_req = 2022;
    private int reserve_days;
    private String address="";
    private String type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        initView();
        getDataFromIntent();


    }


    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("language");
        root = findViewById(R.id.root);
        behavior = BottomSheetBehavior.from(root);
        image_back_sheet = findViewById(R.id.image_back_sheet);

        image_back = findViewById(R.id.image_back);
        if (lang!=null)
        {
            if (lang.equals("ar"))
            {
                Language.setLocality(this,"ar");
                image_back.setRotation(180f);
                image_back_sheet.setRotation(180f);

            }
        }

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        image_back_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState==BottomSheetBehavior.STATE_DRAGGING){
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        ////////////////////////////////////////////////////
        image = findViewById(R.id.image);
        edt_name = findViewById(R.id.edt_name);
        edt_phone = findViewById(R.id.edt_phone);
        edt_amount = findViewById(R.id.edt_amount);
        edt_check_phone = findViewById(R.id.edt_check_phone);
        ll_upload_image = findViewById(R.id.ll_upload_image);
        btn_send = findViewById(R.id.btn_send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckTransferData();
            }
        });
        ////////////////////////////////////////////////////
        bookBtn = findViewById(R.id.bookBtn);
        edt_location = findViewById(R.id.edt_location);
        tv_from_date = findViewById(R.id.tv_from_date);
        tv_to_date = findViewById(R.id.tv_to_date);
        ll_from_date = findViewById(R.id.ll_from_date);
        ll_to_date = findViewById(R.id.ll_to_date);

        final Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        datePickerDialog.setCancelColor(ContextCompat.getColor(this,R.color.press_color));
        datePickerDialog.setOkColor(ContextCompat.getColor(this,R.color.press_color));
        ll_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = "1";
                Calendar min = Calendar.getInstance();
                min.setTime(new Date());
                datePickerDialog.setMinDate(min);
                datePickerDialog.show(getFragmentManager(),"Select Date");
                end_date="";
                tv_to_date.setText("");
            }
        });

        ll_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromDate==null)
                {
                    Toast.makeText(ReservationActivity.this, R.string.sel_sd, Toast.LENGTH_SHORT).show();
                }else
                    {
                        flag="2";
                        Calendar min = Calendar.getInstance();
                        min.setTime(fromDate);
                        datePickerDialog.setMinDate(min);
                        datePickerDialog.show(getFragmentManager(),"Select Date");
                    }


            }
        });

        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                CheckData();
            }
        });

        ll_upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });



    }

    private void getDataFromIntent() {

        Intent intent = getIntent();
        if (intent!=null)
        {
            rentModel = (RentModel) intent.getSerializableExtra("data");
            String address = intent.getStringExtra("address");
            String sd = intent.getStringExtra("start_date");
            String ed = intent.getStringExtra("end_date");
            type = intent.getStringExtra("type");
            updateUi(address,sd,ed);
        }
    }

    private void updateUi(String address, String sd, String ed) {
        edt_location.setText(address);
        tv_from_date.setText(sd);
        tv_to_date.setText(ed);
        if (type.equals(Tags.add_reservation))
        {
            bookBtn.setText(getString(R.string.book));
        }else if (type.equals(Tags.update_reservation))
        {
            bookBtn.setText(getString(R.string.update));

        }
    }

    private void SelectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent,"Select Image"),img_req);
    }

    private void CheckData() {
        String m_address = edt_location.getText().toString();
        if (!TextUtils.isEmpty(m_address)&&!TextUtils.isEmpty(start_date)&&!TextUtils.isEmpty(end_date))
        {
            tv_to_date.setError(null);
            tv_from_date.setError(null);
            edt_location.setError(null);
            canReserve(m_address,start_date,end_date);
        }else
            {
                if (TextUtils.isEmpty(m_address))
                {
                    edt_location.setError(getString(R.string.enter_resr_add));
                }else
                    {
                        edt_location.setError(null);

                    }
                if (TextUtils.isEmpty(start_date))
                {
                    tv_from_date.setError(getString(R.string.sel_sd));
                }else
                {
                    tv_from_date.setError(null);

                }

                if (TextUtils.isEmpty(end_date))
                {
                    tv_to_date.setError(getString(R.string.sel_ed));
                }else
                {
                    tv_to_date.setError(null);

                }
            }
    }

    private void CheckTransferData() {
        String m_name = edt_name.getText().toString();
        String m_phone = edt_phone.getText().toString();
        String m_amount = edt_amount.getText().toString();

        if (!TextUtils.isEmpty(m_phone))
        {
            if (!m_phone.startsWith("+"))
            {
                m_phone ="+"+m_phone;
            }

            edt_check_phone.setPhoneNumber(m_phone);
        }

        if (!TextUtils.isEmpty(m_name)&&!TextUtils.isEmpty(m_phone)&&edt_check_phone.isValid()&&!TextUtils.isEmpty(m_amount)&&uri!=null)
        {
            edt_phone.setError(null);
            edt_name.setError(null);
            edt_amount.setError(null);
            Book(m_name,m_phone,m_amount);
        }else
            {
                if (!TextUtils.isEmpty(m_name))
                {
                    edt_name.setError(null);

                }else
                {
                    edt_name.setError(getString(R.string.name_req));
                }

                if (TextUtils.isEmpty(m_phone))
                {

                    edt_phone.setError(getString(R.string.phone_req));
                }else if (!edt_check_phone.isValid())
                {
                    edt_phone.setError(getString(R.string.inv_phone));
                }else
                    {
                        edt_phone.setError(null);

                    }

                if (!TextUtils.isEmpty(m_amount))
                {
                    edt_amount.setError(null);

                }else
                {
                    edt_amount.setError(getString(R.string.amo_req));
                }

                if (uri==null)
                {
                    Toast.makeText(this, R.string.sel_trans_img, Toast.LENGTH_SHORT).show();
                }
            }
    }

    private void Book(String m_name, String m_phone, String m_amount) {

        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.bokng));
        dialog.show();
        int cost = Integer.parseInt(rentModel.getCost())*reserve_days;

        RequestBody id_car_part = Common.getRequestBodyText(rentModel.getId_car_maintenance());
        RequestBody user_id_part = Common.getRequestBodyText(userModel.getUser_id());
        RequestBody cost_part = Common.getRequestBodyText(String.valueOf(cost));
        RequestBody day_num_part = Common.getRequestBodyText(String.valueOf(reserve_days));

        RequestBody start_date_part = Common.getRequestBodyText(start_date);
        RequestBody end_date_part = Common.getRequestBodyText(end_date);
        RequestBody address_part = Common.getRequestBodyText(address);
        RequestBody lat_part = Common.getRequestBodyText("0.0");
        RequestBody lng_part = Common.getRequestBodyText("0.0");
        RequestBody name_part = Common.getRequestBodyText(m_name);
        RequestBody phone_part = Common.getRequestBodyText(m_phone);

        RequestBody amount_part = Common.getRequestBodyText(m_amount);

        MultipartBody.Part image_part = Common.getMultiPart(this,uri,"transformation_image");

        Api.getService()
                .Reserve(id_car_part,user_id_part,cost_part,day_num_part,start_date_part,end_date_part,address_part,lat_part,lng_part,name_part,phone_part,amount_part,image_part)
                .enqueue(new Callback<ResponsModel>() {
                    @Override
                    public void onResponse(Call<ResponsModel> call, Response<ResponsModel> response) {
                        if (response.isSuccessful())
                        {
                          dialog.dismiss();
                          if (response.body().getSuccess_reservation()==1)
                          {
                              behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                              finish();
                              Toast.makeText(ReservationActivity.this, R.string.bok_suc, Toast.LENGTH_SHORT).show();
                          }else
                              {
                                  Toast.makeText(ReservationActivity.this, R.string.something, Toast.LENGTH_SHORT).show();

                              }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        dialog.dismiss();
                        Toast.makeText(ReservationActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void canReserve(String m_address, final String start_date, final String end_date) {

        final ProgressDialog dialog1 = Common.createProgressDialog(this,getString(R.string.wait));
        dialog1.show();
        Log.e("madd",m_address);
        Log.e("sd",start_date);
        Log.e("ed",end_date);

        long d =  toDate.getTime()-fromDate.getTime();
        reserve_days = (int) ((d / (24 * 60 * 60 * 1000))+1);
        address = m_address;
        Log.e("diff",reserve_days+"_");

        Api.getService()
                .canReserve(start_date,end_date,rentModel.getId_car_maintenance())
                .enqueue(new Callback<ResponsModel>() {
                    @Override
                    public void onResponse(Call<ResponsModel> call, Response<ResponsModel> response) {
                        if (response.isSuccessful())
                        {
                            dialog1.dismiss();
                            Log.e("can_reserv",response.body().getCan_reservation()+"_");
                            if (response.body().getCan_reservation()==0)
                            {
                                Toast.makeText(ReservationActivity.this, R.string.date_notav, Toast.LENGTH_SHORT).show();

                            }else if (response.body().getCan_reservation()==1)
                            {
                                if (type.equals(Tags.add_reservation))
                                {
                                    CheckPermission();

                                }else if (type.equals(Tags.update_reservation))
                                {
                                    UpdateReservation(start_date,end_date,rentModel.getId_car_maintenance(),reserve_days,rentModel.getId_reservation());
                                }

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        dialog1.dismiss();
                        Toast.makeText(ReservationActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                    }
                });



    }

    private void UpdateReservation(String start_date, String end_date, String id_car_maintenance, int reserve_days, String id_reservation) {

        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.upd_rese));
        dialog.show();
        Api.getService()
                .UpdateReservation(id_reservation,start_date,end_date,id_car_maintenance,reserve_days)
                .enqueue(new Callback<ResponsModel>() {
                    @Override
                    public void onResponse(Call<ResponsModel> call, Response<ResponsModel> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            if (response.body().getSuccess_update_reservation()==1)
                            {
                                Toast.makeText(ReservationActivity.this,R.string.upd_succ, Toast.LENGTH_SHORT).show();
                                finish();
                            }else
                                {
                                    Toast.makeText(ReservationActivity.this,R.string.something, Toast.LENGTH_SHORT).show();

                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        dialog.dismiss();
                        Toast.makeText(ReservationActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void CheckPermission() {
        if (ContextCompat.checkSelfPermission(this,read_per)!= PackageManager.PERMISSION_GRANTED)
        {
            String [] per = {read_per};
            ActivityCompat.requestPermissions(this,per,read_req);
        }else
            {
                userSingleTone = UserSingleTone.getInstance();
                userModel = userSingleTone.getUserModel();
                edt_name.setText(userModel.getUser_full_name());
                edt_phone.setText(userModel.getUser_phone());
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==read_req)
        {
            if (grantResults.length>0)
            {
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    userSingleTone = UserSingleTone.getInstance();
                    userModel = userSingleTone.getUserModel();
                    edt_name.setText(userModel.getUser_full_name());
                    edt_phone.setText(userModel.getUser_phone());

                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                }else
                    {
                        Toast.makeText(this, R.string.per_den, Toast.LENGTH_SHORT).show();
                    }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == img_req && resultCode==RESULT_OK && data!=null)
        {
            uri = data.getData();
            Bitmap bitmap = BitmapFactory.decodeFile(Common.getImagePath(ReservationActivity.this,uri));
            image.setImageBitmap(bitmap);
        }
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        if (flag.equals("1"))
        {
            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            calendar.set(Calendar.MONTH,monthOfYear);
            calendar.set(Calendar.YEAR,year);
            fromDate = new Date(calendar.getTimeInMillis());

            String m_date="";
            if (lang.equals("ar"))
            {
                m_date = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;

            }else if (lang.equals("en"))
            {
                m_date = dayOfMonth+"-"+(monthOfYear+1)+"-"+year;

            }

            tv_from_date.setText(m_date);
            start_date = dayOfMonth+"-"+(monthOfYear+1)+"-"+year;


        }else if (flag.equals("2"))
        {
            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            calendar.set(Calendar.MONTH,monthOfYear);
            calendar.set(Calendar.YEAR,year);
            toDate = new Date(calendar.getTimeInMillis());

            String m_date="";
            if (lang.equals("ar"))
            {
                m_date = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;

            }else if (lang.equals("en"))
            {
                m_date = dayOfMonth+"-"+(monthOfYear+1)+"-"+year;

            }

            tv_to_date.setText(m_date);

            end_date = dayOfMonth+"-"+(monthOfYear+1)+"-"+year;



        }


        /*String m_date="";
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
            Toast.makeText(this, R.string.date_old, Toast.LENGTH_SHORT).show();
        }else
            {
                if (flag.equals("1"))
                {
                    tv_from_date.setText(m_date);

                    date1 = selected_date.getTime();

                }else if (flag.equals("2"))
                {
                    tv_to_date.setText(m_date);
                    date2 = selected_date.getTime();

                    long d =  date2.getTime()-date1.getTime();
                    int d2 = (int) (d / (24 * 60 * 60 * 1000));

                    Log.e("diff",d2+"_");

                }

            }*/







    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

    }
}
