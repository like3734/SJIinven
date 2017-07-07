package com.fter.sopt.fter.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

/**
 * Created by f on 2017-07-01.
 */

public class PagerAdapter_curation extends FragmentStatePagerAdapter {

    FragmentManager fm;
    View.OnClickListener onClickListener;
    public PagerAdapter_curation(FragmentManager fm, View.OnClickListener onClickListener) {
        super(fm);
        this.fm = fm;
        this.onClickListener = onClickListener;
    }

    @Override
    public Fragment getItem(int position) {
        com.fter.sopt.fter.main.fragment_curation fragment = new com.fter.sopt.fter.main.fragment_curation();
        fragment.onClickListener = onClickListener;
        fragment.i = position;

        return fragment;
    }

    @Override
    public int getCount() {
        return 5;
    }

}