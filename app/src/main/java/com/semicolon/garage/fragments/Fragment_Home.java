package com.semicolon.garage.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.semicolon.garage.R;
import com.semicolon.garage.activities.HomeActivity;

public class Fragment_Home extends Fragment{

    private HomeActivity homeActivity;
    private FrameLayout fl_car,fl_truck,fl_tank;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Home getInstance()
    {
        return new Fragment_Home();
    }

    private void initView(View view) {
        homeActivity = (HomeActivity) getActivity();
        fl_car = view.findViewById(R.id.fl_car);
        fl_truck = view.findViewById(R.id.fl_truck);
        fl_tank = view.findViewById(R.id.fl_tank);

        fl_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.UpdateTitle(getString(R.string.cars));
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home_container,Fragment_Car_Container.getInstance()).commit();

            }
        });

        fl_truck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.UpdateTitle(getString(R.string.trucks));
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home_container,Fragment_Truck_Container.getInstance()).commit();

            }
        });

        fl_tank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.UpdateTitle(getString(R.string.tank));
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home_container, Fragment_Motorcycle_Container.getInstance()).commit();

            }
        });

    }
}
