package com.semicolon.garage.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semicolon.garage.R;

public class Fragment_Terms_Condition extends Fragment{

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

    }
}
