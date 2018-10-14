package com.semicolon.garage.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semicolon.garage.R;

public class Fragment_SignUp extends Fragment{

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

    }
}
