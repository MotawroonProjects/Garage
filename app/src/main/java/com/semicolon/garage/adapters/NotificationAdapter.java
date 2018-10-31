package com.semicolon.garage.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.semicolon.garage.R;
import com.semicolon.garage.models.NotificationModel;
import com.semicolon.garage.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.Holder>{
    private Context context;
    private List<NotificationModel> notificationModelList;

    public NotificationAdapter(Context context, List<NotificationModel> notificationModelList) {
        this.context = context;
        this.notificationModelList = notificationModelList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_row, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        NotificationModel notificationModel = notificationModelList.get(position);
        holder.BindData(notificationModel);

    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }

    class Holder extends RecyclerView.ViewHolder  {

        private RoundedImageView image;
        private TextView tv_date,tv_name,tv_reply;
        public Holder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_reply = itemView.findViewById(R.id.tv_reply);



        }

        private void BindData(NotificationModel notificationModel)
        {
            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+notificationModel.getMain_photo())).into(image);
            tv_date.setText(notificationModel.getApproved_date());
            tv_name.setText(notificationModel.getTitle());
            if (notificationModel.getApproved().equals("1"))
            {
                tv_reply.setText(R.string.res_acc);

            }else if (notificationModel.getApproved().equals("2"))
            {
                tv_reply.setText(R.string.res_can);

            }
        }



    }

}
