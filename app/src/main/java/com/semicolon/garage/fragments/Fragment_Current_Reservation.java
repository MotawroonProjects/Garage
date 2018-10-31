package com.semicolon.garage.fragments;

import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.semicolon.garage.R;
import com.semicolon.garage.activities.ReservationActivity;
import com.semicolon.garage.adapters.CurrentReservationAdapter;
import com.semicolon.garage.models.CanReserveState;
import com.semicolon.garage.models.RentModel;
import com.semicolon.garage.models.ResponsModel;
import com.semicolon.garage.models.UserModel;
import com.semicolon.garage.remote.Api;
import com.semicolon.garage.share.Common;
import com.semicolon.garage.singletone.UserSingleTone;
import com.semicolon.garage.tags.Tags;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Current_Reservation extends Fragment{

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

        View view = inflater.inflate(R.layout.fragment_current_reservation,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Current_Reservation getInstance()
    {
        return new Fragment_Current_Reservation();
    }

    private void initView(View view) {
        rentModelList = new ArrayList<>();
        ll_no_reserve = view.findViewById(R.id.ll_no_reserve);
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.press_color), PorterDuff.Mode.SRC_IN);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        adapter = new CurrentReservationAdapter(getActivity(),rentModelList,this);
        recView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        Api.getService()
                .getMyCurrentReservation(userModel.getUser_id())
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

    public void setItemForUpdate(final RentModel rentModel)
    {
        final ProgressDialog dialog = Common.createProgressDialog(getActivity(),getString(R.string.wait));
        dialog.show();
        Api.getService()
                .getMyReservation_CanEdit(rentModel.getId_reservation())
                .enqueue(new Callback<CanReserveState>() {
                    @Override
                    public void onResponse(Call<CanReserveState> call, Response<CanReserveState> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            if (response.body().getCan_edit().equals(Tags.can_edit))
                            {
                                Intent intent = new Intent(getActivity(),ReservationActivity.class);
                                intent.putExtra("data", rentModel);
                                intent.putExtra("address",rentModel.getReservation_address());
                                intent.putExtra("start_date",rentModel.getReservation_start_date());
                                intent.putExtra("end_date",rentModel.getReservation_end_date());
                                intent.putExtra("type", Tags.update_reservation);
                                getActivity().startActivity(intent);
                            }else if (response.body().getCan_edit().equals(Tags.cannot_edit))
                            {
                                Toast.makeText(getActivity(), R.string.cnt_edit, Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CanReserveState> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        dialog.dismiss();
                        Toast.makeText(getActivity(),R.string.something, Toast.LENGTH_SHORT).show();

                    }
                });




    }

    public void setItemForDelete(final RentModel rentModel, final int pos)
    {

        final ProgressDialog dialog = Common.createProgressDialog(getActivity(),getString(R.string.wait));
        dialog.show();
        Api.getService()
                .getMyReservation_CanEdit(rentModel.getId_reservation())
                .enqueue(new Callback<CanReserveState>() {
                    @Override
                    public void onResponse(Call<CanReserveState> call, Response<CanReserveState> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            if (response.body().getCan_cancel().equals(Tags.can_cancel))
                            {
                                CancelReservation(rentModel,pos);


                            }else if (response.body().getCan_cancel().equals(Tags.cannot_cancel))
                            {
                                Toast.makeText(getActivity(), R.string.cnt_cancel, Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CanReserveState> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        dialog.dismiss();
                        Toast.makeText(getActivity(),R.string.something, Toast.LENGTH_SHORT).show();

                    }
                });



    }

    private void CancelReservation(final RentModel rentModel, final int pos) {
        final ProgressDialog dialog = Common.createProgressDialog(getActivity(),getString(R.string.deltng_resrv));
        dialog.show();
        Api.getService().deleteReservation(rentModel.getId_reservation(),userModel.getUser_id())
                .enqueue(new Callback<ResponsModel>() {
                    @Override
                    public void onResponse(Call<ResponsModel> call, Response<ResponsModel> response) {

                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            if (response.body().getSuccess_delete_reservation()==1)
                            {
                                rentModelList.remove(pos);
                                adapter.notifyDataSetChanged();
                                if (rentModelList.size()>0)
                                {
                                    ll_no_reserve.setVisibility(View.GONE);
                                }else
                                    {
                                        ll_no_reserve.setVisibility(View.VISIBLE);
                                    }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponsModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        dialog.dismiss();

                        Toast.makeText(getActivity(),R.string.something, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
