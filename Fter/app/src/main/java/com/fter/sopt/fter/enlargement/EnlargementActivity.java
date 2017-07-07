package com.fter.sopt.fter.enlargement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.fter.sopt.fter.R;

import java.util.ArrayList;

public class EnlargementActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private EnlargementViewPagerAdapter viewPagerAdapter;
    private int seq;
    private ArrayList<String> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlargement);

        images = new ArrayList<String>();

        Intent intent = getIntent();
        images = intent.getStringArrayListExtra("IMAGES");
        seq = intent.getIntExtra("SEQ",0);

        viewPager = (ViewPager)findViewById(R.id.enlargement_viewPager);

        viewPagerAdapter = new EnlargementViewPagerAdapter(getSupportFragmentManager(), images.size(), images);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(seq);
    }
}
