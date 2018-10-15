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
import com.semicolon.garage.fragments.Fragment_Rent_Car;
import com.semicolon.garage.fragments.Fragment_Rent_Motorcycle;
import com.semicolon.garage.fragments.Fragment_Rent_Truck;
import com.semicolon.garage.models.VehicleModel;
import com.semicolon.garage.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RentAdapter  extends RecyclerView.Adapter<RentAdapter.Holder>{
    private Context context;
    private List<VehicleModel> vehicleModelList;
    private Fragment fragment;
    public RentAdapter(Context context, List<VehicleModel> vehicleModelList, Fragment fragment) {
        this.context = context;
        this.vehicleModelList = vehicleModelList;
        this.fragment = fragment;
    }

    @Override
    public RentAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rent_row, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final RentAdapter.Holder holder, int position) {
        VehicleModel vehicleModel = vehicleModelList.get(position);
        holder.BindData(vehicleModel);
        holder.btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                VehicleModel vehicleModel = vehicleModelList.get(holder.getAdapterPosition());

                if (fragment instanceof Fragment_Rent_Car)
                {

                    Fragment_Rent_Car fragment_rent_car = (Fragment_Rent_Car) fragment;
                    fragment_rent_car.setItem(vehicleModel);

                }else if (fragment instanceof Fragment_Rent_Truck)
                {
                    Fragment_Rent_Truck fragment_rent_truck = (Fragment_Rent_Truck) fragment;
                    fragment_rent_truck.setItem(vehicleModel);
                }else if (fragment instanceof Fragment_Rent_Motorcycle)
                {
                    Fragment_Rent_Motorcycle fragment_rent_motorcycle = (Fragment_Rent_Motorcycle) fragment;
                    fragment_rent_motorcycle.setItem(vehicleModel);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return vehicleModelList.size();
    }

    class Holder extends RecyclerView.ViewHolder  {
        private RoundedImageView image;
        private TextView tv_model,tv_name,tv_year_size,tv_price;
        private Button btn_choose;
        public Holder(View itemView) {
            super(itemView);

            btn_choose = itemView.findViewById(R.id.btn_choose);
            image = itemView.findViewById(R.id.image);
            tv_model = itemView.findViewById(R.id.tv_model);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_year_size = itemView.findViewById(R.id.tv_year_size);
            tv_price = itemView.findViewById(R.id.tv_price);



        }

        public void BindData(VehicleModel vehicleModel)
        {
            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+vehicleModel.getMain_photo())).into(image);
            tv_model.setText(vehicleModel.getCar_trademarks());
            tv_name.setText(vehicleModel.getTitle());
            tv_price.setText(vehicleModel.getCost());
            if (vehicleModel.getCategory_id_fk().equals("1"))
            {
                tv_year_size.setText(vehicleModel.getCar_model());
            }else
                {
                    tv_year_size.setText(vehicleModel.getSize()+" "+context.getString(R.string.size));

                }

        }



    }
}
