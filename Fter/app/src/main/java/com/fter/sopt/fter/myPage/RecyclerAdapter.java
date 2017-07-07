package com.fter.sopt.fter.myPage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fter.sopt.fter.R;

import java.util.ArrayList;

/**
 * Created by 김은영 on 2017-06-26.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<Mypage_ViewHolder>{

    ArrayList<MyPage_UserPostResult.Result> item;
    View.OnClickListener onClickListener;

    public RecyclerAdapter(ArrayList<MyPage_UserPostResult.Result> item, View.OnClickListener onClickListener) {
        this.item = item;
        this.onClickListener = onClickListener;
    }

    public void setAdapter(ArrayList<MyPage_UserPostResult.Result> item){
        this.item = item;
    }

    @Override
    public Mypage_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_page_write, parent, false);
        Mypage_ViewHolder mypage_viewHolder = new Mypage_ViewHolder(view);
        view.setOnClickListener(onClickListener);

        return mypage_viewHolder;
    }

    //이거 다시 확인
    @Override
    public void onBindViewHolder(Mypage_ViewHolder holder, int position) {
        holder.recycler_title.setText(item.get(position).title.toString());
        holder.recycler_date.setText(item.get(position).written_time.toString());
    }


    @Override
    public int getItemCount() {
        return item.size();
    }
}
