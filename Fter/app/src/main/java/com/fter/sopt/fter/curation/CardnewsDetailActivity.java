package com.fter.sopt.fter.curation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fter.sopt.fter.R;

//////////////////혜민_큐레이션 상세보기 페이지
public class CardnewsDetailActivity extends ActionBarActivity {

    int id;
    TextView title_cardnews;
    ImageView detailimg1,detailimg2, detailimg3, detailimg4, detailimg5, detailimg6, detailimg7, detailimg8 ,detailimg9,
            detailimg10,detailimg11,detailimg12,detailimg13,detailimg14,detailimg15,detailimg16,detailimg17,detailimg18,
            detailimg19,detailimg20,detailimg21,detailimg22,detailimg23,detailimg24,detailimg25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardnews_detail);

        title_cardnews = (TextView)findViewById(R.id.txtLinkRoom);

        detailimg1 = (ImageView) findViewById(R.id.detailpic1);
        detailimg2 = (ImageView) findViewById(R.id.detailpic2);
        detailimg3 = (ImageView) findViewById(R.id.detailpic3);
        detailimg4 = (ImageView) findViewById(R.id.detailpic4);
        detailimg5 = (ImageView) findViewById(R.id.detailpic5);
        detailimg6 = (ImageView) findViewById(R.id.detailpic6);
        detailimg7 = (ImageView) findViewById(R.id.detailpic7);
        detailimg8 = (ImageView) findViewById(R.id.detailpic8);
        detailimg9 = (ImageView) findViewById(R.id.detailpic9);
        detailimg10 = (ImageView) findViewById(R.id.detailpic10);
        detailimg11 = (ImageView) findViewById(R.id.detailpic11);
        detailimg12 = (ImageView) findViewById(R.id.detailpic12);
        detailimg13 = (ImageView) findViewById(R.id.detailpic13);
        detailimg14 = (ImageView) findViewById(R.id.detailpic14);
        detailimg15 = (ImageView) findViewById(R.id.detailpic15);
        detailimg16 = (ImageView) findViewById(R.id.detailpic16);
        detailimg17 = (ImageView) findViewById(R.id.detailpic17);
        detailimg18 = (ImageView) findViewById(R.id.detailpic18);
        detailimg19 = (ImageView) findViewById(R.id.detailpic19);
        detailimg20 = (ImageView) findViewById(R.id.detailpic20);
        detailimg21 = (ImageView) findViewById(R.id.detailpic21);
        detailimg22 = (ImageView) findViewById(R.id.detailpic22);
        detailimg23 = (ImageView) findViewById(R.id.detailpic23);
        detailimg24 = (ImageView) findViewById(R.id.detailpic24);
        detailimg25 = (ImageView) findViewById(R.id.detailpic25);


        Intent intent = getIntent();
        id = intent.getIntExtra("ID",0);


        Log.i("Mytag", "id = " + id);

        if((id==1)){
            // detailimg1.setBackground(this.getResources().getDrawable(R.drawable.profile_4));
            Log.i("Mytag", "" + "if1");
            //detailimg1.setBackground(new ColorDrawable(0xff6dc6d2));


            title_cardnews.setText("서울 무료 공간 대여 TOP5");
            detailimg1.setBackground(this.getResources().getDrawable(R.drawable.card1_1));
            detailimg2.setBackground(this.getResources().getDrawable(R.drawable.card1_2));
            detailimg3.setBackground(this.getResources().getDrawable(R.drawable.card1_3));
            detailimg4.setBackground(this.getResources().getDrawable(R.drawable.card1_4));
            detailimg5.setBackground(this.getResources().getDrawable(R.drawable.card1_5));
            detailimg6.setBackground(this.getResources().getDrawable(R.drawable.card1_6));
            detailimg7.setBackground(this.getResources().getDrawable(R.drawable.card1_7));
            detailimg8.setBackground(this.getResources().getDrawable(R.drawable.card1_8));
            detailimg9.setBackground(this.getResources().getDrawable(R.drawable.card1_9));
            detailimg10.setVisibility(View.GONE);
            detailimg11.setVisibility(View.GONE);
            detailimg12.setVisibility(View.GONE);
            detailimg13.setVisibility(View.GONE);
            detailimg14.setVisibility(View.GONE);
            detailimg15.setVisibility(View.GONE);
            detailimg16.setVisibility(View.GONE);
            detailimg17.setVisibility(View.GONE);
            detailimg18.setVisibility(View.GONE);
            detailimg19.setVisibility(View.GONE);
            detailimg20.setVisibility(View.GONE);
            detailimg21.setVisibility(View.GONE);
            detailimg22.setVisibility(View.GONE);
            detailimg23.setVisibility(View.GONE);
            detailimg24.setVisibility(View.GONE);
            detailimg25.setVisibility(View.GONE);

            //이런식으로 이미지 25장에 해당하는 거 디자인한테 받아와서 뿌려주기 테스트 해보려고 저렇게 넣엇삼
            //이전 페이지에서 int로 숫자를 구분해서 넘겨줌
            //detailimg1.setBackground();

        }else if(id==2){
            title_cardnews.setText("앱 기획자가 알면 좋은 개발 용어 모음");

            detailimg1.setBackground(this.getResources().getDrawable(R.drawable.card2_1));
            detailimg2.setBackground(this.getResources().getDrawable(R.drawable.card2_2));
            detailimg3.setBackground(this.getResources().getDrawable(R.drawable.card2_3));
            detailimg4.setBackground(this.getResources().getDrawable(R.drawable.card2_4));
            detailimg5.setBackground(this.getResources().getDrawable(R.drawable.card2_5));
            detailimg6.setBackground(this.getResources().getDrawable(R.drawable.card2_6));
            detailimg7.setBackground(this.getResources().getDrawable(R.drawable.card2_7));
            detailimg8.setBackground(this.getResources().getDrawable(R.drawable.card2_8));
            detailimg9.setBackground(this.getResources().getDrawable(R.drawable.card2_9));
            detailimg10.setBackground(this.getResources().getDrawable(R.drawable.card2_10));
            detailimg11.setBackground(this.getResources().getDrawable(R.drawable.card2_11));
            detailimg12.setVisibility(View.GONE);
            detailimg13.setVisibility(View.GONE);
            detailimg14.setVisibility(View.GONE);
            detailimg15.setVisibility(View.GONE);
            detailimg16.setVisibility(View.GONE);
            detailimg17.setVisibility(View.GONE);
            detailimg18.setVisibility(View.GONE);
            detailimg19.setVisibility(View.GONE);
            detailimg20.setVisibility(View.GONE);
            detailimg21.setVisibility(View.GONE);
            detailimg22.setVisibility(View.GONE);
            detailimg23.setVisibility(View.GONE);
            detailimg24.setVisibility(View.GONE);
            detailimg25.setVisibility(View.GONE);


            //detailimg10.setBackground(this.getResources().getDrawable(R.drawable.profile_4));
            //detailimg10.setBackground(new ColorDrawable(0xff6dc6d2));
            Log.i("Mytag", "" + "if2");
        }else if(id==3){
            title_cardnews.setText("스타트업 사람들이 보면 좋은 영화 TOP7");

            detailimg1.setBackground(this.getResources().getDrawable(R.drawable.card3_1));
            detailimg2.setBackground(this.getResources().getDrawable(R.drawable.card3_2));
            detailimg3.setBackground(this.getResources().getDrawable(R.drawable.card3_3));
            detailimg4.setBackground(this.getResources().getDrawable(R.drawable.card3_4));
            detailimg5.setBackground(this.getResources().getDrawable(R.drawable.card3_5));
            detailimg6.setBackground(this.getResources().getDrawable(R.drawable.card3_6));
            detailimg7.setBackground(this.getResources().getDrawable(R.drawable.card3_7));
            detailimg8.setBackground(this.getResources().getDrawable(R.drawable.card3_8));
            detailimg9.setBackground(this.getResources().getDrawable(R.drawable.card3_9));
            detailimg10.setBackground(this.getResources().getDrawable(R.drawable.card3_10));
            detailimg11.setBackground(this.getResources().getDrawable(R.drawable.card3_11));
            detailimg12.setBackground(this.getResources().getDrawable(R.drawable.card3_12));
            detailimg13.setBackground(this.getResources().getDrawable(R.drawable.card3_13));
            detailimg14.setBackground(this.getResources().getDrawable(R.drawable.card3_14));
            detailimg15.setBackground(this.getResources().getDrawable(R.drawable.card3_15));
            detailimg16.setVisibility(View.GONE);
            detailimg17.setVisibility(View.GONE);
            detailimg18.setVisibility(View.GONE);
            detailimg19.setVisibility(View.GONE);
            detailimg20.setVisibility(View.GONE);
            detailimg21.setVisibility(View.GONE);
            detailimg22.setVisibility(View.GONE);
            detailimg23.setVisibility(View.GONE);
            detailimg24.setVisibility(View.GONE);
            detailimg25.setVisibility(View.GONE);


            //마찬가지로 위의 주석처럼 이미지 넣어주세요
        }else if(id==4){

            title_cardnews.setText("디자이너가 빡치는 순간 TOP5");

            detailimg1.setBackground(this.getResources().getDrawable(R.drawable.card4_1));
            detailimg2.setBackground(this.getResources().getDrawable(R.drawable.card4_2));
            detailimg3.setBackground(this.getResources().getDrawable(R.drawable.card4_3));
            detailimg4.setBackground(this.getResources().getDrawable(R.drawable.card4_4));
            detailimg5.setBackground(this.getResources().getDrawable(R.drawable.card4_5));
            detailimg6.setBackground(this.getResources().getDrawable(R.drawable.card4_6));
            detailimg7.setBackground(this.getResources().getDrawable(R.drawable.card4_7));
            detailimg8.setBackground(this.getResources().getDrawable(R.drawable.card4_8));
            detailimg9.setBackground(this.getResources().getDrawable(R.drawable.card4_9));
            detailimg10.setBackground(this.getResources().getDrawable(R.drawable.card4_10));
            detailimg11.setBackground(this.getResources().getDrawable(R.drawable.card4_11));
            detailimg12.setVisibility(View.GONE);
            detailimg13.setVisibility(View.GONE);
            detailimg14.setVisibility(View.GONE);
            detailimg15.setVisibility(View.GONE);
            detailimg16.setVisibility(View.GONE);
            detailimg17.setVisibility(View.GONE);
            detailimg18.setVisibility(View.GONE);
            detailimg19.setVisibility(View.GONE);
            detailimg20.setVisibility(View.GONE);
            detailimg21.setVisibility(View.GONE);
            detailimg22.setVisibility(View.GONE);
            detailimg23.setVisibility(View.GONE);
            detailimg24.setVisibility(View.GONE);
            detailimg25.setVisibility(View.GONE);


            //마찬가지로 위의 주석처럼 이미지 넣어주세요
        }else if(id==5){


            title_cardnews.setText("햄버거는 리필 안되나요?");
            detailimg1.setBackground(this.getResources().getDrawable(R.drawable.card5_1));
            detailimg2.setBackground(this.getResources().getDrawable(R.drawable.card5_2));
            detailimg3.setBackground(this.getResources().getDrawable(R.drawable.card5_3));
            detailimg4.setBackground(this.getResources().getDrawable(R.drawable.card5_4));
            detailimg5.setBackground(this.getResources().getDrawable(R.drawable.card5_5));
            detailimg6.setBackground(this.getResources().getDrawable(R.drawable.card5_6));
            detailimg7.setBackground(this.getResources().getDrawable(R.drawable.card5_7));
            detailimg8.setBackground(this.getResources().getDrawable(R.drawable.card5_8));
            detailimg9.setBackground(this.getResources().getDrawable(R.drawable.card5_9));
            detailimg10.setBackground(this.getResources().getDrawable(R.drawable.card5_10));
            detailimg11.setBackground(this.getResources().getDrawable(R.drawable.card5_11));
            detailimg12.setBackground(this.getResources().getDrawable(R.drawable.card5_12));
            detailimg13.setBackground(this.getResources().getDrawable(R.drawable.card5_13));
            detailimg14.setBackground(this.getResources().getDrawable(R.drawable.card5_14));
            detailimg15.setBackground(this.getResources().getDrawable(R.drawable.card5_15));
            detailimg16.setBackground(this.getResources().getDrawable(R.drawable.card5_16));
            detailimg17.setBackground(this.getResources().getDrawable(R.drawable.card5_17));
            detailimg18.setBackground(this.getResources().getDrawable(R.drawable.card5_18));
            detailimg19.setBackground(this.getResources().getDrawable(R.drawable.card5_19));
            detailimg20.setBackground(this.getResources().getDrawable(R.drawable.card5_20));
            detailimg21.setBackground(this.getResources().getDrawable(R.drawable.card5_21));
            detailimg22.setBackground(this.getResources().getDrawable(R.drawable.card5_22));
            detailimg23.setBackground(this.getResources().getDrawable(R.drawable.card5_23));
            detailimg24.setVisibility(View.GONE);
            detailimg25.setVisibility(View.GONE);
            //마찬가지로 위의 주석처럼 이미지 넣어주세요
        }else if (id==6){
            title_cardnews.setText("2017년 떠오르는 10대기술");
            detailimg1.setBackground(this.getResources().getDrawable(R.drawable.card6_1));
            detailimg2.setBackground(this.getResources().getDrawable(R.drawable.card6_2));
            detailimg3.setBackground(this.getResources().getDrawable(R.drawable.card6_3));
            detailimg4.setBackground(this.getResources().getDrawable(R.drawable.card6_4));
            detailimg5.setBackground(this.getResources().getDrawable(R.drawable.card6_5));
            detailimg6.setBackground(this.getResources().getDrawable(R.drawable.card6_6));
            detailimg7.setBackground(this.getResources().getDrawable(R.drawable.card6_7));
            detailimg8.setBackground(this.getResources().getDrawable(R.drawable.card6_8));
            detailimg9.setBackground(this.getResources().getDrawable(R.drawable.card6_9));
            detailimg10.setBackground(this.getResources().getDrawable(R.drawable.card6_10));
            detailimg11.setVisibility(View.GONE);
            detailimg12.setVisibility(View.GONE);
            detailimg13.setVisibility(View.GONE);
            detailimg14.setVisibility(View.GONE);
            detailimg15.setVisibility(View.GONE);
            detailimg16.setVisibility(View.GONE);
            detailimg17.setVisibility(View.GONE);
            detailimg18.setVisibility(View.GONE);
            detailimg19.setVisibility(View.GONE);
            detailimg20.setVisibility(View.GONE);
            detailimg21.setVisibility(View.GONE);
            detailimg22.setVisibility(View.GONE);
            detailimg23.setVisibility(View.GONE);
            detailimg24.setVisibility(View.GONE);
            detailimg25.setVisibility(View.GONE);
        }
    }
}