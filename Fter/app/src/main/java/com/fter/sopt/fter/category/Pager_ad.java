package com.fter.sopt.fter.category;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by f on 2017-07-01.
 */

public class Pager_ad extends FragmentStatePagerAdapter {

    FragmentManager fm;
    public Pager_ad(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {
        com.fter.sopt.fter.category.Fragment_AD fragment = new com.fter.sopt.fter.category.Fragment_AD();
        fragment.i = position;

        return fragment;
    }

    @Override
    public int getCount() {
        return 5;
    }

}