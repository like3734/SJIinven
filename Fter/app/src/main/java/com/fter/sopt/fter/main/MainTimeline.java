package com.fter.sopt.fter.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fter.sopt.fter.R;
import com.fter.sopt.fter.alarm.AlarmActivity;
import com.fter.sopt.fter.application.ApplicationController;
import com.fter.sopt.fter.category.SetCategory;
import com.fter.sopt.fter.curation.MainActivity;
import com.fter.sopt.fter.first.IDInfo;
import com.fter.sopt.fter.myPage.MyPageActivity;
import com.fter.sopt.fter.network.NetworkService;
import com.fter.sopt.fter.register.RegisterActivity;
import com.fter.sopt.fter.search.SearchActivity;

import java.util.ArrayList;

public class MainTimeline extends AppCompatActivity {

    public static Activity activity;
    private ViewPager viewPager;
    private ViewPager m_viewPager;
    private PagerAdapter_curation pagerAdapter;
    private PagerAdapter_feed m_pagerAdapter;
    private TabLayout m_tabLayout;
    private String id_part;
    private String part;
    private String user_nick;
    NetworkService service;

    private TextView currentpart;
    private ImageView mapbutton;
    private ImageView searchbutton;
    private ArrayList<IDInfo> idInfo;
    private com.fter.sopt.fter.first.IDdata iDdata;

    private ImageView home;
    private ImageView mypage;
    private ImageView writing;
    private ImageView category;
    private ImageView alarm;
    private RelativeLayout timeline_title;

    private Handler handler;
    private Thread thread;
    int p = 0;

    //Back 키 두번 클릭 여부 확인
    private final long FINSH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_timeline);

        activity = MainTimeline.this;

        Intent intent = getIntent();
        part = intent.getStringExtra("PART");
        user_nick = intent.getStringExtra("USERNICK");

        //////////////////////뷰 객체 초기화////////////////////////
        currentpart =(TextView)findViewById(R.id.mypart);
        mapbutton = (ImageView)findViewById(R.id.mapbutton);
        searchbutton =(ImageView)findViewById(R.id.searchbutton);
        home =(ImageView)findViewById(R.id.go_home);
        mypage =(ImageView)findViewById(R.id.go_mypage);
        writing =(ImageView)findViewById(R.id.go_writing);
        category =(ImageView)findViewById(R.id.go_category);
        alarm =(ImageView)findViewById(R.id.go_alam);
        mypage=(ImageView)findViewById(R.id.go_mypage);
//        timeline_title = (RelativeLayout)findViewById(R.id.timeline_title);

        //////////객체화/////////
        id_part = new String();
        idInfo = new ArrayList<IDInfo>();


        //////////파트 이름 설정
        if(part.equals("design")){
            currentpart.setText("디자인");
        }
        else if(part.equals("develop")){
            currentpart.setText("개발");
        }
        else if(part.equals("bm")) {
            currentpart.setText("경영/마케팅");
        }

        ////////////////////////리스트 목록 추가 버튼에 리스너 설정////////////////////////
        mapbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SetPart.class);
                intent.putExtra("USERNICK",user_nick);
                startActivity(intent);
            }
        });

        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent1);
            }
        });

        //////////////////탭바////////////////////
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainTimeline.class);
                intent.putExtra("PART",part);
                intent.putExtra("USERNICK",user_nick);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        writing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SetCategory.class);
                startActivity(intent);
            }
        });
        mypage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
                startActivity(intent);
            }
        });

        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });



        //////네트워킹//////
        service = ApplicationController.getInstance().getNetworkService();



//        //////큐레이션카드설정/////
//        tabLayout = (TabLayout) findViewById(R.id.tablayout);
//        tabLayout.addTab(tabLayout.newTab());
//        tabLayout.addTab(tabLayout.newTab());
//        tabLayout.addTab(tabLayout.newTab());
//        tabLayout.addTab(tabLayout.newTab());
//        tabLayout.addTab(tabLayout.newTab());
////        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//
//        viewPager = (ViewPager) findViewById(R.id.pager);
//
//        pagerAdapter = new PagerAdapter_curation(getSupportFragmentManager(), tabLayout.getTabCount());
//        Log.i("main",user_nick);
//        Log.i("main",part);
//        viewPager.setAdapter(pagerAdapter);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//
//
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });


        pagerAdapter = new PagerAdapter_curation(getSupportFragmentManager(),onClickListener);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);
//        timeline_title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//            }
//        });

        handler = new Handler(){

            public void handleMessage(android.os.Message msg) {
                p = viewPager.getCurrentItem();
                if(p != 4){
                    p++;
                }
                else{
                    p = 0;
                }
                viewPager.setCurrentItem(p);
            }
        };

        thread = new Thread(){
            public void run() {
                super.run();
                while(true){
                    try {
                        Thread.sleep(5000);
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        thread.start();



        ////////m프래그먼트//////
        m_tabLayout =(TabLayout)findViewById(R.id.m_tablayout);
        m_tabLayout.addTab(m_tabLayout.newTab().setText("최신순"));
        m_tabLayout.addTab(m_tabLayout.newTab().setText("인기순"));
        m_tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        m_viewPager =(ViewPager)findViewById(R.id.m_pager);

        m_pagerAdapter =new PagerAdapter_feed(getSupportFragmentManager(),m_tabLayout.getTabCount(),user_nick,part);
        m_viewPager.setAdapter(m_pagerAdapter);
        m_viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(m_tabLayout));

        m_tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected (TabLayout.Tab tab){
                m_viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected (TabLayout.Tab tab){
            }

            @Override
            public void onTabReselected (TabLayout.Tab tab){
            }
        });

    }

    ////////////////////////취소 버튼을 오버라이드////////////////////////
    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        /**
         * Back키 두번 연속 클릭 시 앱 종료
         */
        if (0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "뒤로 가기 키을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }

    }
    public View.OnClickListener onClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    };
}