package com.fter.sopt.fter.comment;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fter.sopt.fter.R;
import com.fter.sopt.fter.network.NetworkService;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by f on 2017-06-25.
 */

public class CommentRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder>{

    private int post_id;
    private NetworkService service;

    ArrayList<CommentDatas.CommentData> itemdata;
    Context context;


    public CommentRecyclerAdapter(Context context,ArrayList<CommentDatas.CommentData> itemdata,int post_id) {
        this.context = context;
        this.itemdata = itemdata;
        this.post_id = post_id;
    }

    public void setAdapter(ArrayList<CommentDatas.CommentData> itemdata) {
        this.itemdata = itemdata;
        notifyDataSetChanged();
    }

    @Override
    public BaseViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        if(itemdata.size()==0){
            return null;
        }else {
            View v = LayoutInflater.from(context).inflate(R.layout.commentcontent, parent, false);
            return new BaseViewHolder(v);
        }

    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if(itemdata.size()==0){

        } else{

            final CommentDatas.CommentData oneItem = itemdata.get(position);

            holder.Co_writer.setText(itemdata.get(position).user_nick);
            holder.Co_content.setText(itemdata.get(position).content);
            holder.Co_time.setText(itemdata.get(position).written_time);
            if(itemdata.get(position).image!=""){
                Glide.with(getApplicationContext()).load(itemdata.get(position).image)
                        .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                        .into(holder.Co_img);
            }

            holder.Co_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProfileInfo(oneItem.image, oneItem.level, oneItem.user_nick , oneItem.statemessage);
                }
            });

        }
//            baseViewHolder.Co_img.setImageResource(currentitem.comment_img);
    }

    @Override
    public int getItemViewType(int position) {
        return itemdata != null ? itemdata.size() : 0;
    }


    @Override
    public int getItemCount () {
        return itemdata.size();
    }

    private void showProfileInfo(String image, int level, String nickname, String statement){

        final Dialog pdialog = new Dialog(context);
        pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pdialog.setContentView(R.layout.show_profile_dialog);

        ImageView profile_img =(ImageView)pdialog.findViewById(R.id.show_profile_photo);
        TextView user_level =(TextView)pdialog.findViewById(R.id.show_profile_level);
        TextView user_nickname =(TextView)pdialog.findViewById(R.id.show_profile_nickname);
        TextView user_statement =(TextView)pdialog.findViewById(R.id.show_profile_statement);
        ImageView profile_cancelBtn=(ImageView)pdialog.findViewById(R.id.show_profile_cancelBtn);

        if(image!=""){
            Glide.with(getApplicationContext()).load(image)
                    .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                    .into(profile_img);
        }
        user_level.setText("LV. "+Integer.toString(level));
        user_nickname.setText(nickname);
        user_statement.setText(statement);
        pdialog.show();

        profile_cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdialog.dismiss();
            }
        });
    }


}