package com.semicolon.garage.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.semicolon.garage.R;
import com.semicolon.garage.activities.HomeActivity;
import com.semicolon.garage.models.CityModel;
import com.semicolon.garage.models.Country_Nationality;
import com.semicolon.garage.models.UserModel;
import com.semicolon.garage.preferences.Preferences;
import com.semicolon.garage.remote.Api;
import com.semicolon.garage.share.Common;
import com.semicolon.garage.singletone.UserSingleTone;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_SignUp extends Fragment{
    private CircleImageView image;
    private EditText edt_name,edt_phone_number,edt_phone_code,edt_email,edt_password;
    //private PhoneInputLayout edt_phone_check;
    private RoundedImageView uploaded_image;
    private LinearLayout ll_upload_image;
    private Button btn_done;
    private Spinner spinner_nationality,spinner_city;
    private ArrayAdapter<String> adapter,city_adapter;
    private List<String> adapterList,cityAdpterList;
    private List<Country_Nationality> country_nationalityList;
    private List<CityModel> cityList;
    private Country_Nationality country_nationality;
    private CityModel cityModel;
    private ProgressDialog dialog,dialog2;
    private String lang;
    private final String ReadPermission = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final int read_req = 120;
    private Uri imageUri=null,uploadedImageUri=null;
    private final int img1_req=10,img2_req=11;
    private UserSingleTone userSingleTone;
    private Preferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signup,container,false);
        CheckPermission();
        initView(view);
        return view;
    }

    public static Fragment_SignUp getInstance()
    {
        return new Fragment_SignUp();
    }

    private void initView(View view)
    {
        adapterList = new ArrayList<>();
        cityAdpterList = new ArrayList<>();

        Paper.init(getActivity());
        lang = Paper.book().read("language");

        image =view.findViewById(R.id.image);
        edt_name = view.findViewById(R.id.edt_name);
        edt_phone_number =view.findViewById(R.id.edt_phone_number);
        edt_phone_code =view.findViewById(R.id.edt_phone_code);
        edt_email =view.findViewById(R.id.edt_email);
        edt_password =view.findViewById(R.id.edt_password);
        //edt_phone_check =view.findViewById(R.id.edt_phone_check);
        uploaded_image =view.findViewById(R.id.uploaded_image);
        ll_upload_image =view.findViewById(R.id.ll_upload_image);
        btn_done =view.findViewById(R.id.btn_done);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();

            }
        });


        //////////////////////////////////////////////////////////////////////////////////////
        spinner_nationality =view.findViewById(R.id.spinner_nationality);
        spinner_nationality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, View view, int position, long id) {
                new Handler()
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ((TextView)parent.getChildAt(0)).setTextColor(ContextCompat.getColor(getActivity(),R.color.white));

                                }catch (NullPointerException e){}
                            }
                        },500);

                if (position==0)
                {
                    country_nationality=null;
                }else
                {
                    country_nationality = country_nationalityList.get(position-1);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /////////////////////////////////////////////////////////////////////
        spinner_city =view.findViewById(R.id.spinner_city);
        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, View view, int position, long id) {
                new Handler()
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ((TextView)parent.getChildAt(0)).setTextColor(ContextCompat.getColor(getActivity(),R.color.white));

                                }catch (NullPointerException e){}
                            }
                        },500);

                if (position==0)
                {
                    cityModel=null;
                }else
                {
                    cityModel = cityList.get(position-1);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /////////////////////////////////////////////////////////////////////

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage(img1_req);
            }
        });

        ll_upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage(img2_req);
            }
        });

        try {
            new Handler()
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getCountries_Nationality();

                        }
                    },1200);
        }catch (IllegalStateException e)
        {

        }

        catch (Exception e)
        {

        }


    }

    private void CheckData() {
        String m_name = edt_name.getText().toString();
        String m_phone = edt_phone_code.getText().toString()+edt_phone_number.getText().toString();
        String m_email = edt_email.getText().toString();
        String m_password = edt_password.getText().toString();

       /* if (!TextUtils.isEmpty(m_phone))
        {
            if (!m_phone.startsWith("+"))
            {
                m_phone ="+"+m_phone;
            }
            edt_phone_check.setPhoneNumber(m_phone);

        }*/


        if (imageUri!=null&&
                !TextUtils.isEmpty(m_name) &&
                !TextUtils.isEmpty(m_phone)&&
                !TextUtils.isEmpty(m_email)&&
                Patterns.EMAIL_ADDRESS.matcher(m_email).matches()&&
                !TextUtils.isEmpty(m_password)&&
                country_nationality!=null&&
                cityModel!=null&&
                uploadedImageUri!=null)
        {
            edt_phone_number.setError(null);
            edt_email.setError(null);
            edt_name.setError(null);
            edt_password.setError(null);
            Common.CloseKeyBoard(getActivity(),edt_name);

            SignUp(m_name,m_email,m_phone,m_password,imageUri,uploadedImageUri,country_nationality,cityModel);
        }else
            {
                if (TextUtils.isEmpty(m_name))
                {
                    edt_name.setError(getString(R.string.name_req));
                }else
                    {
                        edt_name.setError(null);
                    }

                if (TextUtils.isEmpty(m_phone))
                {
                    edt_phone_number.setError(getString(R.string.phone_req));
                }
                else
                {
                    edt_phone_number.setError(null);
                }


                if (TextUtils.isEmpty(m_email))
                {
                    edt_email.setError(getString(R.string.email_req));
                }else if (!Patterns.EMAIL_ADDRESS.matcher(m_email).matches())
                {
                    edt_email.setError(getString(R.string.inv_email));

                }
                else
                {
                    edt_email.setError(null);
                }

                if (TextUtils.isEmpty(m_password))
                {
                    edt_password.setError(getString(R.string.pass_req));
                }else
                {
                    edt_name.setError(null);
                }

                if (cityModel==null)
                {
                    Toast.makeText(getActivity(), R.string.ch_city, Toast.LENGTH_SHORT).show();

                }
                if (country_nationality==null)
                {
                    Toast.makeText(getActivity(), R.string.ch_nationality, Toast.LENGTH_SHORT).show();

                }
                if (imageUri==null)
                {
                    Toast.makeText(getActivity(), R.string.sel_per_img, Toast.LENGTH_SHORT).show();
                }

                if (uploadedImageUri==null)
                {
                    Toast.makeText(getActivity(), R.string.ch_doc_img, Toast.LENGTH_SHORT).show();
                }

            }
    }

    private void SignUp(String m_name, String m_email, String m_phone, String m_password, Uri imageUri, Uri uploadedImageUri, Country_Nationality country_nationality, CityModel cityModel) {

        final ProgressDialog reg_dialog  = Common.createProgressDialog(getActivity(),getString(R.string.signing_up));
        reg_dialog.show();
       /* Log.e("name",m_name);
        Log.e("email",m_email);
        Log.e("phone",m_phone);
        Log.e("password",m_password);
        Log.e("image_uri",imageUri+"");
        Log.e("uploaded_image_uri",uploadedImageUri+"");
        Log.e("nationality",country_nationality.getId_country());
        Log.e("city",cityModel.getId_city());*/

        String lat = "";
        String lng = "";
        String token_id = "";

        RequestBody name_part = Common.getRequestBodyText(m_name);
        RequestBody email_part = Common.getRequestBodyText(m_email);
        RequestBody phone_part = Common.getRequestBodyText(m_phone);
        RequestBody password_part = Common.getRequestBodyText(m_password);
        RequestBody nationality_part = Common.getRequestBodyText(country_nationality.getId_country());
        RequestBody city_part = Common.getRequestBodyText(cityModel.getId_city());
        RequestBody lat_part = Common.getRequestBodyText(lat);
        RequestBody lng_part = Common.getRequestBodyText(lng);
        RequestBody token_part = Common.getRequestBodyText(token_id);

        MultipartBody.Part img_profile_part = Common.getMultiPart(getActivity(),imageUri,"user_photo");
        MultipartBody.Part img_uploaded_part = Common.getMultiPart(getActivity(),imageUri,"document_type_definition");

        Api.getService()
                .SignUp(password_part,phone_part,nationality_part,email_part,name_part,token_part,lat_part,lng_part,city_part,img_profile_part,img_uploaded_part)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful())
                        {
                            reg_dialog.dismiss();
                            if (response.body().getSuccess_signup()==1)
                            {
                                UserModel userModel = response.body();
                                userSingleTone = UserSingleTone.getInstance();
                                preferences = Preferences.getInstance();
                                userSingleTone.setUserModel(userModel);
                                preferences.create_update_userData(getActivity(),userModel);

                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                startActivity(intent);
                                getActivity().finish();

                            }else if (response.body().getSuccess_signup()==2)
                            {
                                Toast.makeText(getActivity(), R.string.em_ph_exist, Toast.LENGTH_LONG).show();
                            }else if (response.body().getSuccess_signup()==0)
                            {
                                Toast.makeText(getActivity(),R.string.something, Toast.LENGTH_LONG).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        reg_dialog.dismiss();
                        Toast.makeText(getActivity(),R.string.something, Toast.LENGTH_SHORT).show();
                        Log.e("Erorr",t.getMessage());
                    }
                });




    }

    private void CheckPermission()
    {
        if (ContextCompat.checkSelfPermission(getActivity(),ReadPermission)!= PackageManager.PERMISSION_GRANTED)
        {
            String [] perm = {ReadPermission};
            ActivityCompat.requestPermissions(getActivity(),perm,read_req);
        }
    }
    private void SelectImage(int img_req)
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        getActivity().startActivityForResult(intent.createChooser(intent,"Select Image"),img_req);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==img1_req&&resultCode== Activity.RESULT_OK && data!=null)
        {
            imageUri = data.getData();
            Bitmap bitmap = BitmapFactory.decodeFile(Common.getImagePath(getActivity(),imageUri));
            image.setImageBitmap(bitmap);
        }
        else if (requestCode==img2_req&&resultCode== Activity.RESULT_OK && data!=null)
        {
            uploadedImageUri = data.getData();
            Bitmap bitmap = BitmapFactory.decodeFile(Common.getImagePath(getActivity(),uploadedImageUri));
            uploaded_image.setImageBitmap(bitmap);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==read_req)
        {
            if (grantResults.length>0)
            {
                if (grantResults[0]!=PackageManager.PERMISSION_GRANTED)
                {
                    return;
                }
            }
        }
    }
    private void getCountries_Nationality()
    {
        try {
            dialog = Common.createProgressDialog(getActivity(),getString(R.string.load_nat));
            dialog.show();

            Api.getService().getCountries_Nationality()
                    .enqueue(new Callback<List<Country_Nationality>>() {
                        @Override
                        public void onResponse(Call<List<Country_Nationality>> call, Response<List<Country_Nationality>> response) {
                            if (response.isSuccessful())
                            {
                                dialog.dismiss();

                                if (response.body().size()>0)
                                {
                                    try {
                                        country_nationalityList = response.body();
                                        adapterList.clear();
                                        adapterList.add(getString(R.string.ch));

                                        for (Country_Nationality country_nationality:response.body())
                                        {

                                            if (lang!=null)
                                            {
                                                if (lang.equals("ar"))
                                                {
                                                    adapterList.add(country_nationality.getAr_nationality());
                                                }else
                                                {
                                                    adapterList.add(country_nationality.getEn_nationality());

                                                }
                                            }else
                                            {
                                                adapterList.add(country_nationality.getAr_nationality());

                                            }

                                        }
                                        adapter = new ArrayAdapter<>(getActivity(),R.layout.spinner_row,adapterList);
                                        spinner_nationality.setAdapter(adapter);

                                    }catch (IllegalStateException e){}
                                    catch (Exception e){}

                                }
                                getCities();

                            }
                        }

                        @Override
                        public void onFailure(Call<List<Country_Nationality>> call, Throwable t) {
                            dialog.dismiss();
                            Log.e("Error",t.getMessage());
                            Toast.makeText(getActivity(), R.string.something, Toast.LENGTH_SHORT).show();

                        }
                    });

        }
        catch (IllegalStateException e) {}
        catch (Exception e){}



    }
    private void getCities()
    {

        try {
            dialog2 = Common.createProgressDialog(getActivity(),getString(R.string.load_city));
            dialog2.show();
            Api.getService()
                    .getCity()
                    .enqueue(new Callback<List<CityModel>>() {
                        @Override
                        public void onResponse(Call<List<CityModel>> call, Response<List<CityModel>> response) {
                            if (response.isSuccessful())
                            {
                                dialog2.dismiss();

                                if (response.body().size()>0)
                                {
                                    try {
                                        cityList = response.body();
                                        cityAdpterList.clear();
                                        cityAdpterList.add(getString(R.string.ch));

                                        for (CityModel cityModel:cityList)
                                        {

                                            if (lang!=null)
                                            {
                                                if (lang.equals("ar"))
                                                {
                                                    cityAdpterList.add(cityModel.getAr_city_title());
                                                }else
                                                {
                                                    cityAdpterList.add(cityModel.getEn_city_title());

                                                }
                                            }else
                                            {
                                                adapterList.add(cityModel.getAr_city_title());

                                            }

                                        }
                                        city_adapter = new ArrayAdapter<>(getActivity(),R.layout.spinner_row,cityAdpterList);
                                        spinner_city.setAdapter(city_adapter);

                                    }catch (IllegalStateException e){}
                                    catch (Exception e){}

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<CityModel>> call, Throwable t) {
                            dialog2.dismiss();
                            Log.e("Error",t.getMessage());
                            Toast.makeText(getActivity(), R.string.something, Toast.LENGTH_SHORT).show();

                        }
                    });
        }
        catch (IllegalStateException e) {}
        catch (Exception e){}

    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        try {
            if (dialog!=null)
            {
                if (dialog.isShowing())
                {
                    dialog.dismiss();
                }
            }

            if (dialog2!=null)
            {
                if (dialog2.isShowing())
                {
                    dialog2.dismiss();
                }
            }

        }catch (NullPointerException e){}

    }
}
