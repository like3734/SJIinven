package com.fter.sopt.fter.comment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fter.sopt.fter.R;

/**
 * Created by f on 2017-06-27.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder{

    public TextView Co_writer;
    public ImageView Co_img;
    public TextView Co_time;
    public TextView Co_content;

    public BaseViewHolder(View itemView) {
        super(itemView);

        Co_writer = (TextView)itemView.findViewById(R.id.Co_writer);
        Co_img = (ImageView)itemView.findViewById(R.id.Co_img);
        Co_time = (TextView)itemView.findViewById(R.id.Co_time);
        Co_content = (TextView)itemView.findViewById(R.id.Co_content);
    }
}
