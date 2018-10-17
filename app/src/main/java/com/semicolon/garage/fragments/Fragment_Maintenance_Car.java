package com.semicolon.garage.fragments;

import android.content.Intent;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.semicolon.garage.R;
import com.semicolon.garage.activities.MaintenanceDetailsActivity;
import com.semicolon.garage.adapters.MaintenanceAdapter;
import com.semicolon.garage.models.MaintenanceModel;
import com.semicolon.garage.models.UserModel;
import com.semicolon.garage.preferences.Preferences;
import com.semicolon.garage.remote.Api;
import com.semicolon.garage.singletone.UserSingleTone;
import com.semicolon.garage.tags.Tags;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Maintenance_Car extends Fragment{
    private ProgressBar progBar;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;
    private List<MaintenanceModel> maintenanceModelList;
    private TextView tv_no;
    private Preferences preferences;
    private String country_id;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_maintenance_car,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Maintenance_Car getInstance()
    {
        return new Fragment_Maintenance_Car();
    }

    private void initView(View view) {
        preferences = Preferences.getInstance();
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        country_id = preferences.getCountry_Nationality(getActivity()).getId_country();
        maintenanceModelList = new ArrayList<>();
        progBar = view.findViewById(R.id.progBar);
        tv_no = view.findViewById(R.id.tv_no);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.press_color), PorterDuff.Mode.SRC_IN);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        adapter = new MaintenanceAdapter(getActivity(), maintenanceModelList,this);
        recView.setAdapter(adapter);

        if (userModel==null)
        {
            getData("all",country_id, Tags.type_maintenance_cars);
        }else
        {
            getData(userModel.getUser_id(),country_id, Tags.type_maintenance_cars);

        }
    }
    private void getData(String user_id, String country_id, String type) {
        Log.e("user_id",user_id);
        Log.e("country_id",country_id);
        Log.e("type",type);

        Api.getService()
                .getMaintenance(user_id,country_id,type)
                .enqueue(new Callback<List<MaintenanceModel>>() {
                    @Override
                    public void onResponse(Call<List<MaintenanceModel>> call, Response<List<MaintenanceModel>> response) {
                        if (response.isSuccessful())
                        {
                            Log.e("dsfsdfsd","dfsdfsd");
                            progBar.setVisibility(View.GONE);
                            if (response.body().size()>0)
                            {
                                Log.e("dsfsdfsd2","dfsdfsd");

                                tv_no.setVisibility(View.GONE);
                                maintenanceModelList.addAll(response.body());
                                adapter.notifyDataSetChanged();
                            }else
                            {
                                Log.e("dsfsdfsd3","dfsdfsd");

                                tv_no.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<MaintenanceModel>> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        progBar.setVisibility(View.GONE);
                        Log.e("dsfsdfsd5","dfsdfsd");

                    }
                });
    }

    public void setItem(MaintenanceModel maintenanceModel)
    {
        Intent intent = new Intent(getActivity(), MaintenanceDetailsActivity.class);
        intent.putExtra("data", maintenanceModel);
        startActivity(intent);
    }
}
