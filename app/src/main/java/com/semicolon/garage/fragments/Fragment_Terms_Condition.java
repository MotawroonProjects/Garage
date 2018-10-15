package com.semicolon.garage.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.semicolon.garage.R;
import com.semicolon.garage.models.Terms_Conditions;
import com.semicolon.garage.remote.Api;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Terms_Condition extends Fragment{

    private SmoothProgressBar smoothProgress;
    private TextView tv_content;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_terms_condition,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Terms_Condition getInstance()
    {
        return new Fragment_Terms_Condition();
    }

    private void initView(View view) {
        smoothProgress = view.findViewById(R.id.smoothProgress);
        tv_content = view.findViewById(R.id.tv_content);

        getTerms_Condition();
    }

    private void getTerms_Condition() {
        Api.getService()
                .getTerms_Condition()
                .enqueue(new Callback<Terms_Conditions>() {
                    @Override
                    public void onResponse(Call<Terms_Conditions> call, Response<Terms_Conditions> response) {

                        if (response.isSuccessful())
                        {
                            smoothProgress.setVisibility(View.GONE);

                            tv_content.setText(response.body().getContent());
                        }
                    }

                    @Override
                    public void onFailure(Call<Terms_Conditions> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        smoothProgress.setVisibility(View.GONE);
                    }
                });
    }
}
