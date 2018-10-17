package com.semicolon.garage.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.semicolon.garage.R;
import com.semicolon.garage.fragments.Fragment_Maintenance_Car;
import com.semicolon.garage.fragments.Fragment_Maintenance_Motorcycle;
import com.semicolon.garage.fragments.Fragment_Maintenance_Truck;
import com.semicolon.garage.models.MaintenanceModel;
import com.semicolon.garage.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MaintenanceAdapter extends RecyclerView.Adapter<MaintenanceAdapter.Holder>{
    private Context context;
    private List<MaintenanceModel> maintenanceModelList;
    private Fragment fragment;
    public MaintenanceAdapter(Context context, List<MaintenanceModel> maintenanceModelList, Fragment fragment) {
        this.context = context;
        this.maintenanceModelList = maintenanceModelList;
        this.fragment = fragment;
    }

    @Override
    public MaintenanceAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.maintenance_row, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final MaintenanceAdapter.Holder holder, int position) {
        MaintenanceModel maintenanceModel = maintenanceModelList.get(position);
        holder.BindData(maintenanceModel);
        holder.btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MaintenanceModel maintenanceModel1 = maintenanceModelList.get(holder.getAdapterPosition());

                if (fragment instanceof Fragment_Maintenance_Car)
                {
                    Fragment_Maintenance_Car fragment_maintenance_car  = (Fragment_Maintenance_Car) fragment;
                    fragment_maintenance_car.setItem(maintenanceModel1);
                }else if (fragment instanceof Fragment_Maintenance_Truck)
                {
                    Fragment_Maintenance_Truck fragment_maintenance_truck  = (Fragment_Maintenance_Truck) fragment;
                    fragment_maintenance_truck.setItem(maintenanceModel1);
                }else if (fragment instanceof Fragment_Maintenance_Motorcycle)
                {
                    Fragment_Maintenance_Motorcycle fragment_maintenance_motorcycle  = (Fragment_Maintenance_Motorcycle) fragment;
                    fragment_maintenance_motorcycle.setItem(maintenanceModel1);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return maintenanceModelList.size();
    }

    class Holder extends RecyclerView.ViewHolder  {
        private RoundedImageView image;
        private TextView tv_name;
        private Button btn_choose;
        public Holder(View itemView) {
            super(itemView);

            btn_choose = itemView.findViewById(R.id.btn_choose);
            image = itemView.findViewById(R.id.image);
            tv_name = itemView.findViewById(R.id.tv_name);



        }

        public void BindData(MaintenanceModel maintenanceModel)
        {
            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+ maintenanceModel.getMain_photo())).into(image);
            tv_name.setText(maintenanceModel.getTitle());


        }



    }
}
