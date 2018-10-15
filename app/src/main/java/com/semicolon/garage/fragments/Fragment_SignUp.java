package com.semicolon.garage.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lamudi.phonefield.PhoneInputLayout;
import com.semicolon.garage.R;
import com.semicolon.garage.activities.ChooseCountry_LanguageActivity;
import com.semicolon.garage.models.Country_Nationality;
import com.semicolon.garage.remote.Api;
import com.semicolon.garage.share.Common;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_SignUp extends Fragment{
    private CircleImageView image;
    private EditText edt_phone_number,edt_phone_code,edt_email,edt_password;
    private PhoneInputLayout edt_phone_check;
    private Spinner spinner_nationality;
    private ArrayAdapter<String> adapter;
    private List<String> adapterList;
    private List<Country_Nationality> country_nationalityList;
    private Country_Nationality country_nationality;
    private ProgressDialog dialog;
    private String lang;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signup,container,false);
        initView(view);
        return view;
    }

    public static Fragment_SignUp getInstance()
    {
        return new Fragment_SignUp();
    }
    private void initView(View view) {
        adapterList = new ArrayList<>();
        Paper.init(getActivity());
        lang = Paper.book().read("language");
        spinner_nationality =view.findViewById(R.id.spinner_nationality);

        spinner_nationality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)parent.getChildAt(0)).setTextColor(ContextCompat.getColor(getActivity(),R.color.white));

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
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getCountries_Nationality();

                    }
                },1500);
    }


    private void getCountries_Nationality() {
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

                            }
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

}
