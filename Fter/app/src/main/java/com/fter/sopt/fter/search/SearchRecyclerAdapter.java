package com.fter.sopt.fter.search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fter.sopt.fter.R;
import com.fter.sopt.fter.detail.DetailActivity;

import java.util.ArrayList;

/**
 * Created by yhjinny on 2017-07-01.
 */

public class SearchRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<SearchData> itemDatas;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    Context context;
    SearchActivity searchActivity;
    String user_nick;
    View.OnClickListener onClickListener;


    public SearchRecyclerAdapter(ArrayList<SearchData> itemDatas, RecyclerView recyclerView,
                                 LinearLayoutManager linearLayoutManager, Context context,
                                 SearchActivity searchActivity,String user_nick) {
        this.itemDatas = itemDatas;
        this.recyclerView = recyclerView;
        this.linearLayoutManager = linearLayoutManager;
        this.context = context;
        this.searchActivity = searchActivity;
        this.user_nick = user_nick;
    }

    public void setAdapter( ArrayList<SearchData> itemDatas) {
        this.itemDatas = itemDatas;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.search_detail, parent, false);
        return new SearchViewHolder(v);

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final SearchData currentitem = itemDatas.get(position);
        SearchViewHolder searchViewHolder = (SearchViewHolder) holder;
        searchViewHolder.search_result_writer.setText(currentitem.user_nick);
        searchViewHolder.search_result_title.setText(currentitem.title);
        searchViewHolder.search_result_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("POSTID",currentitem.id);
                intent.putExtra("USERNICK",user_nick);
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return itemDatas.size();
    }
    


}
