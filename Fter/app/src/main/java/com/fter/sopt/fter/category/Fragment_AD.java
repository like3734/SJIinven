package com.fter.sopt.fter.category;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fter.sopt.fter.R;

/**
 * Created by f on 2017-07-01.
 */

public class Fragment_AD extends android.support.v4.app.Fragment{

    ImageView imageView;
    int i;
    public Fragment_AD() {}

    @Override
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = layoutInflater.inflate(R.layout.fragment_curation, container, false);
        imageView = (ImageView)view.findViewById(R.id.frgimg);
        switch (i){
            case 0:
                imageView.setBackgroundResource(R.drawable.ad_image1);
                break;
            case 1:
                imageView.setBackgroundResource(R.drawable.ad_image2);
                break;
            case 2:
                imageView.setBackgroundResource(R.drawable.ad_image3);
                break;
            case 3:
                imageView.setBackgroundResource(R.drawable.ad_image4);
                break;
            case 4:
                imageView.setBackgroundResource(R.drawable.ad_image5);
                break;
        }

        return view;
    }
}
