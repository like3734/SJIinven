package com.fter.sopt.fter.main;

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

public class fragment_curation extends android.support.v4.app.Fragment{

    ImageView imageView;
    int i;
    View.OnClickListener onClickListener;
    public fragment_curation() {}

    @Override
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = layoutInflater.inflate(R.layout.fragment_curation, container, false);
        imageView = (ImageView)view.findViewById(R.id.frgimg);
        view.setOnClickListener(onClickListener);
        switch (i){
            case 0:
                imageView.setBackgroundResource(R.drawable.a);
                break;
            case 1:
                imageView.setBackgroundResource(R.drawable.b);
                break;
            case 2:
                imageView.setBackgroundResource(R.drawable.c);
                break;
            case 3:
                imageView.setBackgroundResource(R.drawable.d);
                break;
            case 4:
                imageView.setBackgroundResource(R.drawable.f);
                break;
        }

        return view;
    }
}
