package com.fter.sopt.fter.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by f on 2017-07-01.
 */

public class PagerAdapter_feed extends FragmentStatePagerAdapter {

    private int tabcount;
    String user_nick;
    String part;

    public PagerAdapter_feed(FragmentManager fm, int tabcount,String user_nick, String part) {
        super(fm);
        this.tabcount = tabcount;
        this.user_nick = user_nick;
        this.part = part;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                fragment_latest fragmentLatest = new fragment_latest();
                fragmentLatest.getData(user_nick,part);
                fragmentLatest.setRetainInstance(true);
                return fragmentLatest;
            case 1:
                fragment_popular fragmentPopular = new fragment_popular();
                fragmentPopular.getData(user_nick,part);
                fragmentPopular.setRetainInstance(true);
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