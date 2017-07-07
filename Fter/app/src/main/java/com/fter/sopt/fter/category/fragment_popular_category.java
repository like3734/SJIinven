package com.fter.sopt.fter.category;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fter.sopt.fter.R;
import com.fter.sopt.fter.application.ApplicationController;
import com.fter.sopt.fter.main.RecyclerAdapter;
import com.fter.sopt.fter.main.network.FeedInfo;
import com.fter.sopt.fter.network.NetworkService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by f on 2017-07-05.
 */

public class fragment_popular_category extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private RecyclerView recyclerView;
    private ArrayList<FeedInfo.Result> mDatas;
    private LinearLayoutManager mLayoutManager;
    private RecyclerAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private String user_nick;
    private int part;
    NetworkService service;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public fragment_popular_category() {}

    @Override
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_main, container, false);


        ////////////////////////서비스 객체 초기화////////////////////////
        service = ApplicationController.getInstance().getNetworkService();

        ////////////////////////뷰 객체 초기화////////////////////////
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mDatas = new ArrayList<FeedInfo.Result>();

        ////////////////////////레이아웃 매니저 설정////////////////////////
        mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setHasFixedSize(true);

        ////////////////////////리사이클러 뷰와 어뎁터 연동////////////////////////
        adapter = new RecyclerAdapter(mDatas,getContext(),user_nick);
        recyclerView.setAdapter(adapter);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.RefreshLayout);
        refreshLayout.setOnRefreshListener(this);

        //////네트워킹//////////
        Call<FeedInfo> requestMainData = service.getCategorypopulerInfo(user_nick,part);
        requestMainData.enqueue(new Callback<FeedInfo>() {
            @Override
            public void onResponse(Call<FeedInfo> call, Response<FeedInfo> response) {
                if (response.isSuccessful()) {
                    if (response.body().message.equals("ok")) {
                        mDatas = response.body().result;
                        adapter.setAdapter(mDatas);
                    }
                }
            }

            @Override
            public void onFailure(Call<FeedInfo> call, Throwable t) {
                Log.i("fail", t.getMessage());
            }
        });

        return view;
    }


    @Override
    public void onRefresh() {
        ListReload();
        refreshLayout.setRefreshing(false);
        Toast.makeText(getContext(), "페이지 리로드", Toast.LENGTH_SHORT).show();
    }
    /*
  리스트를 갱신하는 메소드입니다.
   */
    public void ListReload() {
        Call<FeedInfo> requestMainData = service.getCategorypopulerInfo(user_nick,part);
        requestMainData.enqueue(new Callback<FeedInfo>() {
            @Override
            public void onResponse(Call<FeedInfo> call, Response<FeedInfo> response) {

                if (response.isSuccessful()) {
                    if(response.body().message.equals("ok")){
                        Log.i("myTag", String.valueOf(response.body().result.size()));
                        mDatas = response.body().result;
                        adapter.setAdapter(mDatas);
                    }


                }
            }

            @Override
            public void onFailure(Call<FeedInfo> call, Throwable t) {
                Log.i("fail...",t.toString());
            }
        });
    }
    public void getData(String user_nick,int part){
        this.user_nick = user_nick;
        this.part = part;
    }
}
