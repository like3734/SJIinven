package com.fter.sopt.fter.comment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fter.sopt.fter.R;
import com.fter.sopt.fter.application.ApplicationController;
import com.fter.sopt.fter.network.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {

    public int post_id;

    private ViewPager viewPager;
    private CommentPagerAdapter commentPagerAdapter;
    private TabLayout tabLayout;
    private com.fter.sopt.fter.comment.CommentInfo commentInfo;
    private EditText comment_edit;
    private ImageView add_comment;
    private CheckBox use_check;
    private RelativeLayout write_comment;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String user_nick;

    private NetworkService service;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Intent intent = getIntent();
        post_id = intent.getIntExtra("POSTID",27);
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();

        user_nick = pref.getString("USERNICK",null);

        service = ApplicationController.getInstance().getNetworkService();

        ////////////////////////뷰 객체 초기화////////////////////////
        comment_edit = (EditText)findViewById(R.id.comment_edit);
        add_comment = (ImageView)findViewById(R.id.add_comment);
        use_check = (CheckBox)findViewById(R.id.comment_use_check);

        ////////////////////////리스트 목록 추가 버튼에 리스너 설정////////////////////////

        //////taplayout/////
        tabLayout = (TabLayout)findViewById(R.id.co_tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("최신순"));
        tabLayout.addTab(tabLayout.newTab().setText("유용한 댓글"));
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);

        commentPagerAdapter = new CommentPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),post_id);
        viewPager.setAdapter(commentPagerAdapter);
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
        commentInfo = new  com.fter.sopt.fter.comment.CommentInfo();

        add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("버튼눌러지나","아던노");
                if(comment_edit.getText().length() == 0){
                    Toast.makeText(getApplicationContext(), "댓글을 작성하시오.", Toast.LENGTH_SHORT).show();
                }
                else{
                    commentInfo.user_nick = user_nick;
                    commentInfo.post_id = post_id;
                    commentInfo.content = comment_edit.getText().toString();
                    if(use_check.isChecked() == true){
                        commentInfo.useful = 1;//이거 뭐라고 보내야지 useful인거임?
                    }
                    else{
                        commentInfo.useful = 0;
                    }

                    Call<CommentResult> commentResultCall = service.registerComment(commentInfo);
                    commentResultCall.enqueue(new Callback<CommentResult>() {
                        @Override
                        public void onResponse(Call<CommentResult> call, Response<CommentResult> response) {
                            if(response.isSuccessful()){
                                if(response.body().message.equals("ok")){
                                    Toast.makeText(getApplicationContext(), "댓글 등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                    comment_edit.setText("");

//                                    Call<CommentDatas> requestMainData = service.getComment(post_id);
//                                    requestMainData.enqueue(new Callback<CommentDatas>() {
//                                        @Override
//                                        public void onResponse(Call<CommentDatas> call, Response<CommentDatas> response) {
//
//                                            if (response.isSuccessful()) {
////                                                commentPagerAdapter.commentFragment.settingAdapter(response.body().result);
//
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onFailure(Call<CommentDatas> call, Throwable t) {
//
//                                        }
//                                    });
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CommentResult> call, Throwable t) {
                            Log.i("err", t.getMessage());
                            Toast.makeText(getApplicationContext(), "network error", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });
    }
}