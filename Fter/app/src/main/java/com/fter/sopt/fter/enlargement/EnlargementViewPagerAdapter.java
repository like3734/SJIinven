package com.fter.sopt.fter.enlargement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by 김은영 on 2017-07-04.
 */

public class EnlargementViewPagerAdapter extends FragmentStatePagerAdapter{

    private int tabCount;
    private ArrayList<String>images;

    public EnlargementViewPagerAdapter(FragmentManager fm, int tabCount, ArrayList<String> images) {
        super(fm);
        this.tabCount = tabCount;
        this.images = images;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
//                if(images.get(0)!=null){
                    Enlargement enlargement0 = new Enlargement();
                    Bundle bundle0 = new Bundle();
                    bundle0.putString("IMAGES", images.get(0));
                    enlargement0.setArguments(bundle0);
                    return enlargement0;
//                }

            case 1:
//                if(images.get(1)!=null) {
                    Enlargement enlargement1 = new Enlargement();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("IMAGES", images.get(1));
                    enlargement1.setArguments(bundle1);
                    return enlargement1;
//                }
            case 2:
//                if(images.get(2)!=null) {
                    Enlargement enlargement2 = new Enlargement();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("IMAGES", images.get(2));
                    enlargement2.setArguments(bundle2);
                    return enlargement2;
//                }
            case 3:
//                if(images.get(3)!=null) {
                    Enlargement enlargement3 = new Enlargement();
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("IMAGES", images.get(3));
                    enlargement3.setArguments(bundle3);
                    return enlargement3;
//                }
            case 4:
//                if(images.get(4)!=null) {
                    Enlargement enlargement4 = new Enlargement();
                    Bundle bundle4 = new Bundle();
                    bundle4.putString("IMAGES", images.get(4));
                    enlargement4.setArguments(bundle4);
                    return enlargement4;
//                }
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
