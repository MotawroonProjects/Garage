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

public class Fragment_Reservation_Container extends Fragment{

    private TabLayout tab;
    private ViewPager pager;
    private MyPagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reservation_container,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Reservation_Container getInstance()
    {
        return new Fragment_Reservation_Container();
    }

    private void initView(View view) {

        tab = view.findViewById(R.id.tab);
        pager = view.findViewById(R.id.pager);
        tab.setupWithViewPager(pager);


        adapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.AddTitle(getString(R.string.current));
        adapter.AddTitle(getString(R.string.previous));
        adapter.AddFragment(Fragment_Current_Reservation.getInstance());
        adapter.AddFragment(Fragment_Previous_Reservation.getInstance());
        pager.setAdapter(adapter);

        for(int i=0; i < tab.getTabCount(); i++) {
            View mtab = ((ViewGroup) tab.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) mtab.getLayoutParams();
            p.setMargins(20, 0, 20, 0);
            mtab.requestLayout();
        }
    }
}
