package com.semicolon.garage.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.semicolon.garage.R;
import com.semicolon.garage.activities.HomeActivity;

public class Fragment_SignIn extends Fragment{
    private LinearLayout ll_skip;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signin,container,false);
        initView(view);
        return view;
    }

    public static Fragment_SignIn getInstance()
    {
        return new Fragment_SignIn();
    }

    private void initView(View view) {

        ll_skip = view.findViewById(R.id.ll_skip);
        ll_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);

            }
        });
    }
}
