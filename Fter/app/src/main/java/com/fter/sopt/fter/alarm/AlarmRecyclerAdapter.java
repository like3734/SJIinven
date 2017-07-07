package com.fter.sopt.fter.alarm;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fter.sopt.fter.R;
import com.fter.sopt.fter.application.ApplicationController;
import com.fter.sopt.fter.detail.DetailActivity;
import com.fter.sopt.fter.network.NetworkService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by f on 2017-07-07.
 */

public class AlarmRecyclerAdapter extends RecyclerView.Adapter<AlarmViewHolder>{

    private ArrayList<AlarmResults.Result> itemdata;
    Context context;
    private View.OnClickListener onClickListener;
    private int post_id;
    private String user_nick;
    NetworkService service;

    public AlarmRecyclerAdapter(ArrayList<AlarmResults.Result> itemdata, Context context, View.OnClickListener onClickListener,String user_nick){
        this.itemdata = itemdata;
        this.context = context;
        this.onClickListener = onClickListener;
        this.user_nick = user_nick;
    }

    public void setAdapter(ArrayList<AlarmResults.Result> itemdata){
        this.itemdata = itemdata;
        notifyDataSetChanged();
    }
    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_detail, parent, false);
        AlarmViewHolder viewHolder = new AlarmViewHolder(view);

        view.setOnClickListener(onClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AlarmViewHolder holder, final int position) {
        holder.title_alarm.setText(itemdata.get(position).title);
        holder.written_time_alarm.setText(itemdata.get(position).written_time);
        if(itemdata.get(position).readinfo==0){
            holder.backColor_alarm.setBackgroundColor(context.getResources().getColor(R.color.alarmcheck));
        } else {
            holder.backColor_alarm.setBackgroundColor(context.getResources().getColor(R.color.alarmdone));
        }
        holder.backColor_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                service = ApplicationController.getInstance().getNetworkService();
                Call<AlarmCheck> postAlarmCheck = service.postAlarmCheck(itemdata.get(position).id);
                postAlarmCheck.enqueue(new Callback<AlarmCheck>() {
                    @Override
                    public void onResponse(Call<AlarmCheck> call, Response<AlarmCheck> response) {

                        if (response.isSuccessful()) {
                            if (response.body().message.equals("ok")) {
                                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                                intent.putExtra("USERNICK", user_nick);
                                intent.putExtra("POSTID",itemdata.get(position).postid);
                                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AlarmCheck> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemdata != null ? itemdata.size() : 0;
    }
}
