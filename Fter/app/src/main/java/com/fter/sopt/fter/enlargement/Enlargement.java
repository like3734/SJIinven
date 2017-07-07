package com.fter.sopt.fter.enlargement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fter.sopt.fter.R;

/**
 * Created by 김은영 on 2017-07-04.
 */

public class Enlargement extends Fragment{
    ImageView image;
    String imageUrl;

    public Enlargement() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.enlargement_fragment, container, false);

        image = (ImageView)view.findViewById(R.id.enlargement_image);

        imageUrl = getArguments().getString("IMAGES");
        Glide.with(getContext())
                .load(imageUrl)
                .into(image);

        return view;
    }
}
