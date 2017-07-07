package com.fter.sopt.fter.myPage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fter.sopt.fter.application.ApplicationController;
import com.fter.sopt.fter.detail.DetailActivity;
import com.fter.sopt.fter.network.NetworkService;
import com.fter.sopt.fter.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 김은영 on 2017-06-26.
 */

public class Fragment_written extends Fragment{
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;

    private Context context;
    private LinearLayoutManager layoutManager;
    private MyPage_UserPostInfo userWritePost;
    private String user_nick; //path로 넘길 변수명
    NetworkService service;

    public Fragment_written() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_page_written, container, false);

        service = ApplicationController.getInstance().getNetworkService();

        user_nick = getArguments().getString("USERNICK");
        context = getContext();
        ////////////////////ArrayList 초기화/////////////////////
        userWritePost = new MyPage_UserPostInfo();
        userWritePost.result = new ArrayList<MyPage_UserPostResult.Result>();

        ///////////////리사이클러뷰와 어댑터 연동//////////////
        recyclerView = (RecyclerView)(view.findViewById(R.id.mypage_recyclerView));
        adapter = new RecyclerAdapter(userWritePost.result, clickEvent);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        Call<MyPage_UserPostResult> myPage_userPostResultCall = service.getUserWritePost(user_nick);
        myPage_userPostResultCall.enqueue(new Callback<MyPage_UserPostResult>() {
            @Override
            public void onResponse(Call<MyPage_UserPostResult> call, Response<MyPage_UserPostResult> response) {
                if(response.isSuccessful()){
                    if(response.body().message.equals("ok")){
                        userWritePost.result = response.body().result;//서버가 주는 변수명;
                        adapter.setAdapter(userWritePost.result);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyPage_UserPostResult> call, Throwable t) {
                Log.i("err", t.getMessage());
            }
        });


        return view;
    }

    public View.OnClickListener clickEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int itemPosition = recyclerView.getChildPosition(v);
            int tempId = userWritePost.result.get(itemPosition).id;
            //int tempNick = userWritePost.result.get(itemPosition).;
            String tempNick = user_nick;
            //이거 다시 확인하기
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            //이거 다시 확인
            intent.putExtra("POSTID", tempId);
            intent.putExtra("USERNICK", tempNick);
            startActivity(intent);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        Networking();
    }

    @Override
    public void onStart() {
        super.onStart();
        Networking();
    }

    public void Networking(){
        Call<MyPage_UserPostResult> myPage_userPostResultCall = service.getUserWritePost(user_nick);
        myPage_userPostResultCall.enqueue(new Callback<MyPage_UserPostResult>() {
            @Override
            public void onResponse(Call<MyPage_UserPostResult> call, Response<MyPage_UserPostResult> response) {
                if(response.isSuccessful()){
                    if(response.body().message.equals("ok")){
                        userWritePost.result = response.body().result;
                        adapter.setAdapter(userWritePost.result);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyPage_UserPostResult> call, Throwable t) {

            }
        });
    }

}