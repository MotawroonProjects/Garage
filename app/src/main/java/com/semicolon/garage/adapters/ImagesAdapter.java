package com.semicolon.garage.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.semicolon.garage.R;
import com.semicolon.garage.models.VehicleModel;
import com.semicolon.garage.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by elashry on 15/10/2018.
 */

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.Holder>{
    private Context context;
    List<VehicleModel.GalleryInside> galleryInsideList;

    public ImagesAdapter(Context context, List<VehicleModel.GalleryInside> galleryInsideList) {
        this.context = context;
        this.galleryInsideList = galleryInsideList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_row, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        VehicleModel.GalleryInside galleryInside = galleryInsideList.get(position);
        holder.BindData(galleryInside);

    }

    @Override
    public int getItemCount() {
        return galleryInsideList.size();
    }

    class Holder extends RecyclerView.ViewHolder  {
        private RoundedImageView image;
        public Holder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);

        }

        public void BindData(VehicleModel.GalleryInside galleryInside)
        {
            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+galleryInside.getPhoto_name())).into(image);
        }


    }

}
