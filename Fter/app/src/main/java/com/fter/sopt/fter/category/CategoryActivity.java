package com.fter.sopt.fter.category;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fter.sopt.fter.R;
import com.fter.sopt.fter.alarm.AlarmActivity;
import com.fter.sopt.fter.application.ApplicationController;
import com.fter.sopt.fter.curation.MainActivity;
import com.fter.sopt.fter.first.IDInfo;
import com.fter.sopt.fter.first.IDResult;
import com.fter.sopt.fter.first.IDdata;
import com.fter.sopt.fter.main.MainTimeline;
import com.fter.sopt.fter.main.PagerAdapter_curation;
import com.fter.sopt.fter.main.SetPart;
import com.fter.sopt.fter.myPage.MyPageActivity;
import com.fter.sopt.fter.network.NetworkService;
import com.fter.sopt.fter.register.RegisterActivity;
import com.fter.sopt.fter.search.SearchActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ViewPager m_viewPager;
    private PagerAdapter pagerAdapter;
    private PagerAdapter_category m_pagerAdapter;
    private TabLayout tabLayout;
    private TabLayout m_tabLayout;
    private String id_part;
    private int category_int;
    private String user_nick;
    NetworkService service;
    private ImageView mapbutton;
    private ImageView searchbutton;
    private ArrayList<IDInfo> idInfo;
    private com.fter.sopt.fter.first.IDdata iDdata;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    TextView myPart;

    private ImageView home;
    private ImageView mypage;
    private ImageView writing;
    private ImageView category;
    private ImageView alarm;
    public static CategoryActivity activity;

    Handler handler;
    Thread thread;
    int p;

    //Back 키 두번 클릭 여부 확인
    private final long FINSH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_timeline);

        activity = CategoryActivity.this;

        final Intent intent = getIntent();
        category_int = intent.getIntExtra("CATEGORY",0);
        user_nick = intent.getStringExtra("USERNICK");

        //////////////////////뷰 객체 초기화////////////////////////
        mapbutton = (ImageView)findViewById(R.id.mapbutton);
        searchbutton =(ImageView)findViewById(R.id.searchbutton);
        home =(ImageView)findViewById(R.id.go_home);
        mypage =(ImageView)findViewById(R.id.go_mypage);
        writing =(ImageView)findViewById(R.id.go_writing);
        category =(ImageView)findViewById(R.id.go_category);
        alarm =(ImageView)findViewById(R.id.go_alam);
        mypage=(ImageView)findViewById(R.id.go_mypage);
        myPart = (TextView)findViewById(R.id.mypart);

        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();
        iDdata = new IDdata();
        iDdata.id = pref.getString("USERID",null);

        //////////객체화/////////
        id_part = new String();
        idInfo = new ArrayList<IDInfo>();

        switch(category_int){
            case 1:
                myPart.setText("고민이에요");
                break;
            case 2:
                myPart.setText("궁금해요");
                break;
            case 3:
                myPart.setText("일상이야기");
                break;
            case 4:
                myPart.setText("함께해요");
                break;
            default:
                break;
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


        //////네트워킹//////
        service = ApplicationController.getInstance().getNetworkService();

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getApplication(), MainTimeline.class);
                Call<IDResult> requestMainData = service.getUserInfo(iDdata);
                requestMainData.enqueue(new Callback<IDResult>() {
                    @Override
                    public void onResponse(Call<IDResult> call, final Response<IDResult> response) {
                        Log.i("thi log","dd");
                        if (response.isSuccessful()){
                            if(response.body().message.equals("new")){

                            } else if(response.body().message.equals("old")){
                                Log.i("message",response.body().message.toString());
                                intent.putExtra("USERNICK",response.body().result.nickname);
                                intent.putExtra("PART",response.body().result.part);
                                finish();
                                startActivity(intent);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<IDResult> call, Throwable t) {
                    }
                });

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



        //////큐레이션카드설정/////
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);

        pagerAdapter = new PagerAdapter_curation(getSupportFragmentManager(),onClickListener);
        Log.i("main",user_nick);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

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
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        m_viewPager =(ViewPager)findViewById(R.id.m_pager);

        m_pagerAdapter =new PagerAdapter_category(getSupportFragmentManager(),m_tabLayout.getTabCount(),user_nick,category_int);
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