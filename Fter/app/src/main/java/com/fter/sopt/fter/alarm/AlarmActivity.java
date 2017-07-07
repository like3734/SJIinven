package com.fter.sopt.fter.alarm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fter.sopt.fter.R;
import com.fter.sopt.fter.application.ApplicationController;
import com.fter.sopt.fter.category.SetCategory;
import com.fter.sopt.fter.detail.DetailActivity;
import com.fter.sopt.fter.first.IDResult;
import com.fter.sopt.fter.first.IDdata;
import com.fter.sopt.fter.main.MainTimeline;
import com.fter.sopt.fter.myPage.MyPageActivity;
import com.fter.sopt.fter.network.NetworkService;
import com.fter.sopt.fter.register.RegisterActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlarmActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{


    private RecyclerView recyclerView;
    private ArrayList<AlarmResults.Result> itemdata;
    private LinearLayoutManager mLayoutManager;
    private AlarmRecyclerAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    public TextView alarmText;
    private String user_nick;
    private IDdata iDdata;
    NetworkService service;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private ImageView home;
    private ImageView mypage;
    private ImageView writing;
    private ImageView category;
    private ImageView alarm;
    private String user_id;


    //Back 키 두번 클릭 여부 확인
    private final long FINSH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();

        user_nick = pref.getString("USERNICK","");
        user_id = pref.getString("USERID","");
        Log.i("user",user_nick);
        iDdata = new IDdata();
        iDdata.id = user_id;

        ////////////////////////서비스 객체 초기화////////////////////////
        service = ApplicationController.getInstance().getNetworkService();
        home =(ImageView)findViewById(R.id.go_home);
        mypage =(ImageView)findViewById(R.id.go_mypage);
        writing =(ImageView)findViewById(R.id.go_writing);
        category =(ImageView)findViewById(R.id.go_category);
        alarm =(ImageView)findViewById(R.id.go_alam);
        mypage=(ImageView)findViewById(R.id.go_mypage);

        ////////////////////////뷰 객체 초기화////////////////////////
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_alamrm);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.alarm_RefreshLayout);
        recyclerView.setHasFixedSize(true);
        refreshLayout.setOnRefreshListener(this);
        alarmText = (TextView)findViewById(R.id.alarm_thing);

        ////////////////////////레이아웃 매니저 설정////////////////////////
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        ////////////////////////각 배열에 모델 개체를 가지는 ArrayList 초기화////////////////////////
        itemdata = new ArrayList<AlarmResults.Result>();
        adapter = new AlarmRecyclerAdapter(itemdata, getApplicationContext(), clickEvent,user_nick);
        recyclerView.setAdapter(adapter);

        Call<AlarmResults> requestMainData = service.getAlarmDatas(user_nick);
        requestMainData.enqueue(new Callback<AlarmResults>() {
            @Override
            public void onResponse(Call<AlarmResults> call, Response<AlarmResults> response) {

                if (response.isSuccessful()) {
                    if (response.body().message.equals("ok")) {
                        itemdata = response.body().result;
                        recyclerView.setAdapter(adapter);
                        adapter.setAdapter(itemdata);
                        adapter.notifyDataSetChanged();
                        Log.i("count", String.valueOf(response.body().count));
                        String count = String.valueOf(response.body().count);
                        alarmText.setText("확인하지 않은 알람이 총 "+count+"개 있습니다.");
                    }

                }
            }

            @Override
            public void onFailure(Call<AlarmResults> call, Throwable t) {

            }
        });

        ////////////////////////리사이클러 뷰와 어뎁터 연동////////////////////////
        ////////////////////////파라미터로 위의 ArrayList와 클릭이벤트////////////////////////
        //////////////////탭바////////////////////
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<IDResult> requestMainData = service.getUserInfo(iDdata);
                requestMainData.enqueue(new Callback<IDResult>() {
                    @Override
                    public void onResponse(Call<IDResult> call, final Response<IDResult> response) {
                        Log.i("thi log","dd");
                        if (response.isSuccessful()){
                            Intent intent = new Intent(getApplicationContext(), MainTimeline.class);
                            intent.putExtra("USERNICK",response.body().result.nickname);
                            intent.putExtra("PART",response.body().result.part);
                            Log.i("part",response.body().result.part);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
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





    }
    public View.OnClickListener clickEvent = new View.OnClickListener() {
        public void onClick(View v) {
            int itemPosition = recyclerView.getChildPosition(v);
            int tempId = itemdata.get(itemPosition).id;
            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
            intent.putExtra("USERNICK", String.valueOf(tempId));
            startActivity(intent);
        }
    };
    @Override
    protected void onRestart() {
        super.onRestart();
        ListReload();
    }
    @Override
    public void onRefresh() {
        ListReload();
        refreshLayout.setRefreshing(false);
        Toast.makeText(getApplicationContext(), "페이지 리로드", Toast.LENGTH_SHORT).show();
    }

    /*
    리스트를 갱신하는 메소드입니다.
     */
    public void ListReload() {
        final Call<AlarmResults> requestMainData = service.getAlarmDatas(user_nick);
        requestMainData.enqueue(new Callback<AlarmResults>() {
            @Override
            public void onResponse(Call<AlarmResults> call, Response<AlarmResults> response) {

                if (response.isSuccessful()) {
                    if (response.body().message.equals("ok")) {
                        itemdata = response.body().result;
                        String count = String.valueOf(response.body().count);
                        alarmText.setText("확인하지 않은 알람이 총 "+count+"개 있습니다.");
                        recyclerView.setAdapter(adapter);
                        adapter.setAdapter(itemdata);
                        adapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onFailure(Call<AlarmResults> call, Throwable t) {

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
}
