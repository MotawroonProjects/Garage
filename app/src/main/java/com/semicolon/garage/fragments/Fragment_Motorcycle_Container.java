package com.semicolon.garage.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semicolon.garage.R;
import com.semicolon.garage.adapters.MyPagerAdapter;

public class Fragment_Motorcycle_Container extends Fragment{

    private TabLayout tab;
    private ViewPager pager;
    private MyPagerAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_motorcycle_container,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Motorcycle_Container getInstance()
    {
        return new Fragment_Motorcycle_Container();
    }

    private void initView(View view) {
        tab =view.findViewById(R.id.tab);
        pager = view.findViewById(R.id.pager);
        tab.setupWithViewPager(pager);
        adapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());

        adapter.AddFragment(Fragment_Rent_Motorcycle.getInstance());
        adapter.AddFragment(Fragment_Maintenance_Motorcycle.getInstance());

        adapter.AddTitle(getString(R.string.rent));
        adapter.AddTitle(getString(R.string.maintenance));
        pager.setAdapter(adapter);
    }
}
