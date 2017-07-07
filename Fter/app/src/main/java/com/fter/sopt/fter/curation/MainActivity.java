package com.fter.sopt.fter.curation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;

import com.fter.sopt.fter.R;


public class MainActivity extends ActionBarActivity {

    ImageView cardimg1,cardimg2, cardimg3, cardimg4, cardimg5, cardimg6, cardimg7, cardimg8,cardimg9, cardimg10;

    int MAX_PAGE=5;                         //View Pager의 총 페이지 갯수를 나타내는 변수 선언
    Fragment cur_fragment=new Fragment();   //현재 Viewpager가 가리키는 Fragment를 받을 변수 선언

    ViewPager viewPager = null;
    Thread thread = null;
    Handler handler = null;
    int p=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curation);

        cardimg1 = (ImageView) findViewById(R.id.pic1);
        cardimg2 = (ImageView) findViewById(R.id.pic2);
        cardimg3 = (ImageView) findViewById(R.id.pic3);
        cardimg4 = (ImageView) findViewById(R.id.pic4);
        cardimg5 = (ImageView) findViewById(R.id.pic5);
        cardimg6 = (ImageView) findViewById(R.id.pic6);

        cardimg1.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, CardnewsDetailActivity.class);
                intent.putExtra("ID",1);
                startActivity(intent);
            }
        });

        cardimg2.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent2 = new Intent(MainActivity.this, CardnewsDetailActivity.class);
                intent2.putExtra("ID",2);
                startActivity(intent2);
            }
        });

        cardimg3.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent3 = new Intent(MainActivity.this, CardnewsDetailActivity.class);
                intent3.putExtra("ID",3);
                startActivity(intent3);
            }
        });

        cardimg4.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent4 = new Intent(MainActivity.this, CardnewsDetailActivity.class);
                intent4.putExtra("ID",4);
                startActivity(intent4);
            }
        });

        cardimg5.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent5 = new Intent(MainActivity.this, CardnewsDetailActivity.class);
                intent5.putExtra("ID",5);
                startActivity(intent5);
            }
        });


        cardimg6.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent6 = new Intent(MainActivity.this, CardnewsDetailActivity.class);
                intent6.putExtra("ID",6);
                startActivity(intent6);
            }
        });

//        cardimg7.setOnClickListener(new ImageView.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent7 = new Intent(MainActivity.this, CardnewsDetailActivity.class);
//                intent7.putExtra("7",7);
//                startActivity(intent7);
//            }
//        });
//
//        cardimg8.setOnClickListener(new ImageView.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent8 = new Intent(MainActivity.this, CardnewsDetailActivity.class);
//                intent8.putExtra("8",8);
//                startActivity(intent8);
//            }
//        });
//        cardimg9.setOnClickListener(new ImageView.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent9 = new Intent(MainActivity.this, CardnewsDetailActivity.class);
//                intent9.putExtra("9",9);
//                startActivity(intent9);
//            }
//        });
//        cardimg10.setOnClickListener(new ImageView.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent10 = new Intent(MainActivity.this, CardnewsDetailActivity.class);
//                intent10.putExtra("10",10);
//                startActivity(intent10);
//            }
//        });





        final ViewPager viewPager=(ViewPager)findViewById(R.id.viewpager);        //Viewpager 선언 및 초기화
        viewPager.setAdapter(new adapter(getSupportFragmentManager()));//선언한 viewpager에 adapter를 연결


        handler = new Handler(){

            public void handleMessage(android.os.Message msg) {
                if(p != 4) {
                    p++;
                } else {
                    p = 0;
                }
                viewPager.setCurrentItem(p);
            }
        };
        thread = new Thread(){
            //run은 jvm이 쓰레드를 채택하면, 해당 쓰레드의 run메서드를 수행한다.
            public void run() {
                super.run();
                while(true){
                    try {
                        Thread.sleep(2000);
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                }
            }
        };
        thread.start();

    }




    private class adapter extends FragmentPagerAdapter {                    //adapter클래스
        public adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position<0 || MAX_PAGE<=position)        //가리키는 페이지가 0 이하거나 MAX_PAGE보다 많을 시 null로 리턴
                return null;
            switch (position){              //포지션에 맞는 Fragment찾아서 cur_fragment변수에 대입
                case 0:
                    cur_fragment=new page_1();
                    break;

                case 1:
                    cur_fragment=new page_2();
                    break;

                case 2:
                    cur_fragment=new page_3();
                    break;

                case 3:
                    cur_fragment=new page_4();
                    break;

                case 4:
                    cur_fragment=new page_5();
                    break;
            }

            return cur_fragment;
        }

        @Override
        public int getCount() {
            return MAX_PAGE;
        }
    }
}
