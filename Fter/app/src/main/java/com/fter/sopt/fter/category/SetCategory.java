package com.fter.sopt.fter.category;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.fter.sopt.fter.R;
import com.fter.sopt.fter.main.MainTimeline;

public class SetCategory extends AppCompatActivity {

    ImageView problem;
    ImageView question;
    ImageView mystory;
    ImageView together;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String USERNICK;
    Handler handler;
    Thread thread;
    private Pager_ad pagerAdapter;

    ViewPager viewPager;

    int p = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();

        USERNICK = pref.getString("USERNICK",null);

        problem = (ImageView)findViewById(R.id.problem);
        question = (ImageView)findViewById(R.id.question);
        mystory = (ImageView)findViewById(R.id.mystory);
        together = (ImageView)findViewById(R.id.together);

        pagerAdapter = new Pager_ad(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager_ad);
        viewPager.setAdapter(pagerAdapter);

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

        View.OnClickListener clicklistener = new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                switch (v.getId()){
                    case R.id.problem:
                        intent.putExtra("CATEGORY",1);
                        break;
                    case R.id.question:
                        intent.putExtra("CATEGORY",2);
                        break;
                    case R.id.mystory:
                        intent.putExtra("CATEGORY",3);
                        break;
                    case R.id.together:
                        intent.putExtra("CATEGORY",4);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("USERNICK",USERNICK);
                MainTimeline mainTimeline = (MainTimeline)MainTimeline.activity;
                mainTimeline.finish();
                startActivity(intent);
                finish();
            }
        };



        problem.setOnClickListener(clicklistener);
        question.setOnClickListener(clicklistener);
        mystory.setOnClickListener(clicklistener);
        together.setOnClickListener(clicklistener);
    }
}
