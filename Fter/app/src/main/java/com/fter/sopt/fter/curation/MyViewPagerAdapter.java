package com.fter.sopt.fter.curation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by f on 2017-07-07.
 */

public class MyViewPagerAdapter extends FragmentStatePagerAdapter {
	/*
	 * 이 클래스의 부모생성자 호출시 인수로 반드시 FragmentManager객체를 넘겨야한다.
	 * 이 객체는 Activity에서만 만들수 있고, 여기서사용중인 Fragment가 v4이기 때문에
	 * Activity중에서도 ActionBarActivity에서 얻어와야한다.
	 */

    Fragment[] fragments = new Fragment[5];


    public MyViewPagerAdapter(FragmentManager fm) {
        super(fm);

//        fragments[0] = new fragment1();
//        fragments[1] = new fragment2();
//        fragments[2] = new fragment3();

        fragments[0] = new page_1();
        fragments[1] = new page_2();
        fragments[2] = new page_3();
        fragments[3] = new page_4();
        fragments[4] = new page_5();
    }

    //아래의 메서드들의 호출 주체는 ViewPager이다.
    //ListView와 원리가 같다.

    /*
     * 여러 프레그먼트 중 어떤 프레그먼트를 보여줄지 결정
     */
    public Fragment getItem(int arg0) {
        return fragments[arg0];
    }

    /*
     * 보여질 프레그먼트가 몇개인지 결정
     */
    public int getCount() {
        return fragments.length;
    }

}