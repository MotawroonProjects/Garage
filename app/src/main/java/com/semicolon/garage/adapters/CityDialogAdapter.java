package com.semicolon.garage.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.semicolon.garage.R;
import com.semicolon.garage.fragments.Fragment_Profile;
import com.semicolon.garage.models.CityModel;

import java.util.List;

public class CityDialogAdapter extends RecyclerView.Adapter<CityDialogAdapter.Holder>{
    private Context context;
    private List<CityModel> cityModelList;
    private String lang;
    private Fragment fragment;
    public CityDialogAdapter(Context context, List<CityModel> cityModelList, String lang, Fragment fragment) {
        this.context = context;
        this.cityModelList = cityModelList;
        this.lang = lang;
        this.fragment = fragment;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.city_row, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        CityModel cityModel= cityModelList.get(position);

        holder.BindData(cityModel);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CityModel cityModel= cityModelList.get(holder.getAdapterPosition());
                Fragment_Profile fragment_profile = (Fragment_Profile) fragment;
                fragment_profile.setCityItem(cityModel);

            }
        });

        holder.rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CityModel cityModel= cityModelList.get(holder.getAdapterPosition());
                Fragment_Profile fragment_profile = (Fragment_Profile) fragment;
                fragment_profile.setCityItem(cityModel);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cityModelList.size();
    }

    class Holder extends RecyclerView.ViewHolder  {
        private TextView tv_name;
        private RadioButton rb;
        public Holder(View itemView) {
            super(itemView);

            tv_name =itemView.findViewById(R.id.tv_name);
            rb =itemView.findViewById(R.id.rb);


        }


        private void BindData(CityModel cityModel)
        {

            if (lang.equals("ar"))
            {
                tv_name.setText(cityModel.getAr_city_title());

            }else if (lang.equals("en"))
            {
                tv_name.setText(cityModel.getEn_city_title());

            }
        }

    }

}
