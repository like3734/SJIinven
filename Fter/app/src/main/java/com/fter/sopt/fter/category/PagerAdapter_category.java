package com.fter.sopt.fter.category;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by f on 2017-07-05.
 */

public class PagerAdapter_category extends FragmentStatePagerAdapter {

    private int tabcount;
    String user_nick;
    int part;

    public PagerAdapter_category(FragmentManager fm, int tabcount, String user_nick, int part) {
        super(fm);
        this.tabcount = tabcount;
        this.user_nick = user_nick;
        this.part = part;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                fragment_latest_category fragmentLatest = new fragment_latest_category();
                fragmentLatest.getData(user_nick,part);
//                fragmentLatest.setRetainInstance(true);
                return fragmentLatest;
            case 1:
                fragment_popular_category fragmentPopular = new fragment_popular_category();
                fragmentPopular.getData(user_nick,part);
//                fragmentPopular.setRetainInstance(true);
                return fragmentPopular;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}