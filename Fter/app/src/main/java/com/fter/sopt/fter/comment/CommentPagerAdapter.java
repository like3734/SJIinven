package com.fter.sopt.fter.comment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by f on 2017-06-27.
 */

public class CommentPagerAdapter extends FragmentStatePagerAdapter {

    private int tabcount;
    private int post_id;

    public CommentPagerAdapter(FragmentManager fm, int tabcount,int post_id) {
        super(fm);
        this.tabcount = tabcount;
        this.post_id = post_id;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                CommentFragment commentFragment = new CommentFragment();
                commentFragment.post_id = post_id;
                return commentFragment;
            case 1:
                CommentFragment_use commentFragment_use = new CommentFragment_use();
                commentFragment_use.post_id = post_id;
                return commentFragment_use;
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}