package com.semicolon.garage.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.semicolon.garage.R;
import com.semicolon.garage.tags.Tags;
import com.squareup.picasso.Picasso;

public class Fragment_Images extends Fragment{
    private static final String IMG_URL="URL";
    private ImageView image;
    private String img_url="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_images,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Images getInstance(String url)

    {
        Bundle bundle = new Bundle();
        bundle.putString(IMG_URL,url);
        Fragment_Images fragment = new Fragment_Images();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void initView(View view) {

        image = view.findViewById(R.id.image);

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            img_url = bundle.getString(IMG_URL);
            Picasso.with(getActivity()).load(Uri.parse(Tags.IMAGE_URL+img_url)).into(image);
        }

    }
}
