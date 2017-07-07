package com.fter.sopt.fter.myPage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fter.sopt.fter.R;
import com.fter.sopt.fter.application.ApplicationController;
import com.fter.sopt.fter.network.NetworkService;
import com.fter.sopt.fter.profile.ProfileModify;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageActivity extends AppCompatActivity {

    private ViewPager viewPager;

    private UserPagerAdapter userPagerAdapter;
    private TabLayout tabLayout;

    private ImageView mypage_user_profile;
    private ImageView modify_profileBtn;
    private TextView mypage_user_level;
    private TextView mypage_user_nickname;
    private TextView mypage_user_part;
    private TextView mypage_user_statement;
    private String user_Nickname;
    private ImageView setting;

    String I_statemessage;
    String I_profile = null;
    String I_part;

    private NetworkService service;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();

        user_Nickname = pref.getString("USERNICK",null);

        //////////////////////////////인텐트로 넘겨온 값 받기/////////////////////////////
//        Intent intent = new Intent();
//        intent = getIntent();
//        intent.getExtras().getString("USERNICK");

        ///////////////////////서비스 객체 초기화//////////////////////
        service = ApplicationController.getInstance().getNetworkService();

        ////////////////////View 객체 초기화//////////////////////

        modify_profileBtn = (ImageView)findViewById(R.id.mypage_profileBtn);
        mypage_user_profile = (ImageView)findViewById(R.id.mypage_user_profile);
        mypage_user_level = (TextView)findViewById(R.id.mypage_user_level);
        mypage_user_part = (TextView)findViewById(R.id.mypage_user_workpart);
        mypage_user_nickname = (TextView)findViewById(R.id.mypage_user_nickname);
        mypage_user_statement = (TextView)findViewById(R.id.mypage_user_statement);
        setting = (ImageView)findViewById(R.id.gosetting);

        ////////////////////////user정보 setting/////////////////////////////

        Call<MyPage_UserResult> myPage_userResultCall = service.getUserInfo(user_Nickname);
        myPage_userResultCall.enqueue(new Callback<MyPage_UserResult>() {
            @Override
            public void onResponse(Call<MyPage_UserResult> call, Response<MyPage_UserResult> response) {
                if(response.isSuccessful()){
                    if(response.body().message.equals("ok")){

                        if(response.body().result.profile != null){
                            Glide.with(getApplicationContext())
                                    .load(response.body().result.profile)
                                    .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                                    .into(mypage_user_profile);
                            I_profile = response.body().result.profile;
                        }
                        mypage_user_level.setText("Lv." + response.body().result.level);
                        mypage_user_nickname.setText(response.body().result.nickname);
                        mypage_user_statement.setText(response.body().result.statemessage);

                        switch(response.body().result.part) {
                            case "bm":
                                mypage_user_part.setText("경영/마케팅");
                                break;
                            case "design":
                                mypage_user_part.setText("디자인");
                                break;
                            case "develop":
                                mypage_user_part.setText("개발");
                                break;
                            default:
                                break;
                        }

                        I_part = response.body().result.part;
                        I_statemessage = response.body().result.statemessage;
                        I_part = response.body().result.part;
                    }
                }
            }

            @Override
            public void onFailure(Call<MyPage_UserResult> call, Throwable t) {
                Log.i("err", t.getMessage());
            }
        });


        viewPager = (ViewPager)findViewById(R.id.mypage_viewPager);
        tabLayout = (TabLayout)findViewById(R.id.mypage_tablayout);

        tabLayout.addTab(tabLayout.newTab().setText("내가 쓴 글"));
        tabLayout.addTab(tabLayout.newTab().setText("내가 찜한 글"));

        userPagerAdapter = new UserPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),user_Nickname);
        viewPager.setAdapter(userPagerAdapter);
        viewPager.setCurrentItem(0);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

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

        modify_profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileModify.class);
                intent.putExtra("USERNICK", user_Nickname);
                intent.putExtra("STATEMESSAGE", I_statemessage);
                intent.putExtra("PROFILE", I_profile);
                intent.putExtra("PART", I_part);
                startActivity(intent);
            }
        });
        setting.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(),MyPageSettingActivity.class));
            }
        });
    }

}
