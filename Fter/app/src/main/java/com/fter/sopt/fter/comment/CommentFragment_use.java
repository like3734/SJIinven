package com.fter.sopt.fter.comment;

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
import com.fter.sopt.fter.network.NetworkService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by f on 2017-07-04.
 */

public class CommentFragment_use extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView recyclerView;
    public ArrayList<CommentDatas.CommentData> itemDatas;
    private LinearLayoutManager mLayoutManager;
    private CommentRecyclerAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    public int post_id;
    private String useful;
    NetworkService service;
    private  CommentInfo commentInfo;


    public CommentFragment_use() {}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.comment_fragment, container, false);

        itemDatas = new ArrayList<CommentDatas.CommentData>();

        ////////////////////////서비스 객체 초기화////////////////////////
        service = ApplicationController.getInstance().getNetworkService();


        ////////////////////////뷰 객체 초기화////////////////////////
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        Log.i("pager",Integer.toString(post_id));

        ////////////////////////레이아웃 매니저 설정////////////////////////
        mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setHasFixedSize(true);

        ////////////////////////리사이클러 뷰와 어뎁터 연동////////////////////////
        adapter = new CommentRecyclerAdapter(getContext(),itemDatas,post_id);
        recyclerView.setAdapter(adapter);


        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.RefreshLayout);
        refreshLayout.setOnRefreshListener(this);

        //////네트워킹//////////
        Call<CommentDatas> requestMainData = service.getUsefulComment(post_id);
        requestMainData.enqueue(new Callback<CommentDatas>() {
            @Override
            public void onResponse(Call<CommentDatas> call, Response<CommentDatas> response) {

                if (response.isSuccessful()) {
                    if (response.body().message.equals("ok")) {
                        recyclerView.setAdapter(adapter);
                        adapter.setAdapter(response.body().result);
                    }
                }
            }

            @Override
            public void onFailure(Call<CommentDatas> call, Throwable t) {
                Log.i("fail", t.getMessage());
            }
        });


        return view;
    }

    public void Networking(){

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
        Call<CommentDatas> requestMainData = service.getUsefulComment(post_id);
        requestMainData.enqueue(new Callback<CommentDatas>() {
            @Override
            public void onResponse(Call<CommentDatas> call, Response<CommentDatas> response) {

                if (response.isSuccessful()) {
                    adapter.setAdapter(response.body().result);
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<CommentDatas> call, Throwable t) {

            }
        });
    }
    public void settingAdapter(ArrayList<CommentDatas.CommentData> itemData){
        this.adapter.setAdapter(itemDatas);
        this.adapter.notifyDataSetChanged();
    }
}