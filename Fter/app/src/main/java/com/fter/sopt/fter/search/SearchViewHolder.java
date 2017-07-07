package com.fter.sopt.fter.search;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fter.sopt.fter.R;

/**
 * Created by yhjinny on 2017-07-01.
 */

public class SearchViewHolder extends RecyclerView.ViewHolder {

    public TextView search_result_writer;
    public TextView search_result_title;


    public SearchViewHolder(View itemView) {
        super(itemView);

        search_result_writer = (TextView)itemView.findViewById(R.id.search_detail_writer);
        search_result_title =(TextView)itemView.findViewById(R.id.search_detail_title);

    }


}
