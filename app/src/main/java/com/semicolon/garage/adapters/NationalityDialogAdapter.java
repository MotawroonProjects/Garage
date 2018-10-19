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
import com.semicolon.garage.models.Country_Nationality;

import java.util.List;

public class NationalityDialogAdapter extends RecyclerView.Adapter<NationalityDialogAdapter.Holder>{
    private Context context;
    private List<Country_Nationality> country_nationalityList;
    private String lang;
    private Fragment fragment;

    public NationalityDialogAdapter(Context context, List<Country_Nationality> country_nationalityList, String lang,Fragment fragment)
    {
        this.context = context;
        this.country_nationalityList = country_nationalityList;
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
        Country_Nationality country_nationality= country_nationalityList.get(position);

        holder.BindData(country_nationality);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Country_Nationality country_nationality= country_nationalityList.get(holder.getAdapterPosition());
                Fragment_Profile fragment_profile = (Fragment_Profile) fragment;
                fragment_profile.setNationalityItem(country_nationality);

            }
        });

        holder.rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Country_Nationality country_nationality= country_nationalityList.get(holder.getAdapterPosition());
                Fragment_Profile fragment_profile = (Fragment_Profile) fragment;
                fragment_profile.setNationalityItem(country_nationality);
            }
        });

    }

    @Override
    public int getItemCount() {
        return country_nationalityList.size();
    }

    class Holder extends RecyclerView.ViewHolder  {
        private TextView tv_name;
        private RadioButton rb;
        public Holder(View itemView) {
            super(itemView);

            tv_name =itemView.findViewById(R.id.tv_name);
            rb =itemView.findViewById(R.id.rb);


        }


        private void BindData(Country_Nationality country_nationality)
        {

            if (lang.equals("ar"))
            {
                tv_name.setText(country_nationality.getAr_nationality());

            }else if (lang.equals("en"))
            {
                tv_name.setText(country_nationality.getEn_nationality());

            }
        }

    }

}
