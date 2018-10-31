package com.semicolon.garage.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.semicolon.garage.R;
import com.semicolon.garage.adapters.PreviousReservationAdapter;
import com.semicolon.garage.models.RentModel;
import com.semicolon.garage.models.UserModel;
import com.semicolon.garage.remote.Api;
import com.semicolon.garage.singletone.UserSingleTone;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Previous_Reservation extends Fragment{

    private ProgressBar progBar;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;
    private LinearLayout ll_no_reserve;
    private List<RentModel> rentModelList;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_previous_reservation,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Previous_Reservation getInstance()
    {
        return new Fragment_Previous_Reservation();
    }

    private void initView(View view) {
        rentModelList = new ArrayList<>();
        ll_no_reserve = view.findViewById(R.id.ll_no_reserve);
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.press_color), PorterDuff.Mode.SRC_IN);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        adapter = new PreviousReservationAdapter(getActivity(),rentModelList);
        recView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        Log.e("user_id",userModel.getUser_id());
        Api.getService()
                .getMyPreviousReservation(userModel.getUser_id())
                .enqueue(new Callback<List<RentModel>>() {
                    @Override
                    public void onResponse(Call<List<RentModel>> call, Response<List<RentModel>> response) {
                        if (response.isSuccessful())
                        {
                            rentModelList.clear();
                            progBar.setVisibility(View.GONE);
                            rentModelList.addAll(response.body());
                            if (rentModelList.size()>0)
                            {
                                ll_no_reserve.setVisibility(View.GONE);
                                adapter.notifyDataSetChanged();
                            }else
                            {
                                ll_no_reserve.setVisibility(View.VISIBLE);

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<RentModel>> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        progBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(),R.string.something, Toast.LENGTH_LONG).show();
                    }
                });

    }
}
