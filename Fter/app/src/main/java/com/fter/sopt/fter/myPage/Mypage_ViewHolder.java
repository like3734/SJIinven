package com.fter.sopt.fter.myPage;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fter.sopt.fter.R;

/**
 * Created by 김은영 on 2017-06-26.
 */

public class Mypage_ViewHolder extends RecyclerView.ViewHolder{
    TextView recycler_title;
    TextView recycler_date;

    public Mypage_ViewHolder(View itemView) {
        super(itemView);
        recycler_title = (TextView)itemView.findViewById(R.id.my_page_title);
        recycler_date = (TextView)itemView.findViewById(R.id.my_page_date);
    }
}
