package com.fter.sopt.fter.alarm;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fter.sopt.fter.R;

/**
 * Created by f on 2017-07-07.
 */

public class AlarmViewHolder extends  RecyclerView.ViewHolder{
    public TextView title_alarm;
    public TextView written_time_alarm;
    public LinearLayout backColor_alarm;


    public AlarmViewHolder(View itemView) {
        super(itemView);
        backColor_alarm = (LinearLayout)itemView.findViewById(R.id.alarmCheck);
        title_alarm = (TextView)itemView.findViewById(R.id.alarm_title);
        written_time_alarm = (TextView)itemView.findViewById(R.id.alarm_time);
    }
}
