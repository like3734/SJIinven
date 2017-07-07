package com.fter.sopt.fter.myPage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by 김은영 on 2017-06-26.
 */

public class UserPagerAdapter extends FragmentStatePagerAdapter {
    private int tabcount;
    private String user_nick;

    public UserPagerAdapter(FragmentManager fm, int tabcount, String user_nick) {
        super(fm);
        this.tabcount = tabcount;
        this.user_nick = user_nick;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                Fragment_written fragment_written = new Fragment_written();
                Bundle w_bundle = new Bundle();
                w_bundle.putString("USERNICK", user_nick);
                fragment_written.setArguments(w_bundle);
                return fragment_written;
            case 1:
                Fragment_bookmark fragment_bookmark = new Fragment_bookmark();
                Bundle b_bundle = new Bundle();
                b_bundle.putString("USERNICK", user_nick);
                fragment_bookmark.setArguments(b_bundle);
                return fragment_bookmark;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return tabcount;
    }
}