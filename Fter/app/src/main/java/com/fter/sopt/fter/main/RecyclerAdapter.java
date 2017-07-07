package com.fter.sopt.fter.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fter.sopt.fter.R;
import com.fter.sopt.fter.application.ApplicationController;
import com.fter.sopt.fter.comment.CommentActivity;
import com.fter.sopt.fter.detail.DetailActivity;
import com.fter.sopt.fter.enlargement.EnlargementActivity;
import com.fter.sopt.fter.main.network.BookmarkInfo;
import com.fter.sopt.fter.main.network.FeedInfo;
import com.fter.sopt.fter.main.network.LikeInfo;
import com.fter.sopt.fter.network.NetworkService;
import com.fter.sopt.fter.register.deleteInfo;
import com.fter.sopt.fter.register.deleteResult;
import com.fter.sopt.fter.register.deletecheckResult;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by f on 2017-07-01.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private ArrayList<FeedInfo.Result> mainListDatas;
    private View.OnClickListener onClickListener;
    private final Context context;
    NetworkService service;
    String user_nick;
    final int REQ_CODE_SELECT_IMAGE = 100;
    String imgUrl = "";
    Uri data;

    SharedPreferences pref;
    SharedPreferences.Editor editor;



    public RecyclerAdapter(ArrayList<FeedInfo.Result> mainListDatas, Context context,String user_nick) {
        this.mainListDatas = mainListDatas;
        this.context = context;
        this.user_nick = user_nick;
    }


    public void setAdapter(ArrayList<FeedInfo.Result> mainListDatas) {
        this.mainListDatas = mainListDatas;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        view.setOnClickListener(onClickListener);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final int temp = position;
        final MyViewHolder temp_v = holder;
        holder.VH_main_writer.setText(mainListDatas.get(position).postinfo.nickname);
        Log.i("RECY",mainListDatas.get(position).postinfo.nickname);
        if(mainListDatas.get(position).postinfo.profile!=""){
            Glide.with(getApplicationContext())
                    .load(mainListDatas.get(position).postinfo.profile)
                    .into(holder.VH_main_img);
        }
        holder.VH_main_time.setText(mainListDatas.get(position).postinfo.written_time);

        switch (mainListDatas.get(position).postinfo.part){
            case "bm":
                holder.VH_main_part.setText("경영/마케팅");
                break;
            case "design":
                holder.VH_main_part.setText("디자인");
                break;
            case "develop":
                holder.VH_main_part.setText("개발");
                break;
            default:
                break;
        }
        holder.VH_main_title.setText(mainListDatas.get(position).postinfo.title);
        if(mainListDatas.get(position).postinfo.contents.length() > 80){
            holder.VH_main_content.setText(mainListDatas.get(position).postinfo.contents.substring(0,80) + "..더보기");
        }else {
            holder.VH_main_content.setText(mainListDatas.get(position).postinfo.contents);
        }
        holder.VH_main_level.setText("LV"+Integer.toString(mainListDatas.get(position).postinfo.level));
        holder.VH_main_likecount.setText(Integer.toString(mainListDatas.get(position).postinfo.likecount));
        holder.VH_main_comment_count.setText(Integer.toString(mainListDatas.get(position).postinfo.commentcount));
        if(mainListDatas.get(position).postinfo.profile!=""){
            Glide.with(getApplicationContext())
                    .load(mainListDatas.get(position).postinfo.profile)
                    .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                    .into(holder.VH_main_img);
        }
        if(mainListDatas.get(position).commentinfo.size()==0){
            holder.VH_main_commentshat1.setVisibility(View.GONE);
            holder.VH_main_commentshat2.setVisibility(View.GONE);
        } else if(mainListDatas.get(position).commentinfo.size()==1){
            holder.VH_main_commentshat1.setVisibility(View.VISIBLE);
            holder.VH_main_commentshat2.setVisibility(View.GONE);
            holder.VH_main_comment1_name.setText(mainListDatas.get(position).commentinfo.get(0).user_nick);
            holder.VH_main_comment1_content.setText(mainListDatas.get(position).commentinfo.get(0).content);
            if(mainListDatas.get(position).commentinfo.get(0).image!=""){
                Glide.with(getApplicationContext())
                        .load(mainListDatas.get(position).commentinfo.get(0).image)
                        .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                        .into(holder.VH_main_comment1_img);
            }
        } else {
            holder.VH_main_comment1_name.setText(mainListDatas.get(position).commentinfo.get(0).user_nick);
            holder.VH_main_comment1_content.setText(mainListDatas.get(position).commentinfo.get(0).content);
            if(mainListDatas.get(position).commentinfo.get(0).image!=""){
                Glide.with(getApplicationContext())
                        .load(mainListDatas.get(position).commentinfo.get(0).image)
                        .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                        .into(holder.VH_main_comment1_img);
            }
            holder.VH_main_comment2_name.setText(mainListDatas.get(position).commentinfo.get(1).user_nick);
            holder.VH_main_comment2_content.setText(mainListDatas.get(position).commentinfo.get(1).content);
            if(mainListDatas.get(position).commentinfo.get(1).image!=""){
                Glide.with(getApplicationContext())
                        .load(mainListDatas.get(position).commentinfo.get(1).image)
                        .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                        .into(holder.VH_main_comment2_img);
            }
        }

        if (mainListDatas.get(position).postinfo.likecheck == 0) {
            holder.VH_main_likeicon.setBackgroundResource(R.drawable.heartgray);
        } else {
            holder.VH_main_likeicon.setBackgroundResource(R.drawable.heart_2);
        }


        if (mainListDatas.get(position).postinfo.markcheck == 0) {
            holder.VH_main_bookmark.setBackgroundResource(R.drawable.save_1);
        } else {
            holder.VH_main_bookmark.setBackgroundResource(R.drawable.save);
            /////서버통신
        }


        holder.VH_main_likeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service = ApplicationController.getInstance().getNetworkService();

                Call<LikeInfo> requestLike = service.getLikeInfo(user_nick,mainListDatas.get(temp).postinfo.id);
                requestLike.enqueue(new Callback<LikeInfo>() {
                    @Override
                    public void onResponse(Call<LikeInfo> call, Response<LikeInfo> response) {
                        if (response.isSuccessful()) {
                            if (response.body().message.equals("like")) {
                                temp_v.VH_main_likeicon.setBackgroundResource(R.drawable.heart_2);
                            } else if(response.body().message.equals("unlike")){
                                temp_v.VH_main_likeicon.setBackgroundResource(R.drawable.heartgray);
                            }
                            temp_v.VH_main_likecount.setText(Integer.toString(response.body().result.get(0).likecount));
                        }
                    }

                    @Override
                    public void onFailure(Call<LikeInfo> call, Throwable t) {
                        Log.i("err", t.getMessage());
                    }
                });
            }
        });
        holder.VH_main_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service = ApplicationController.getInstance().getNetworkService();
                Call<BookmarkInfo> requestLike = service.getBookmarkInfo(user_nick,mainListDatas.get(temp).postinfo.id);
                requestLike.enqueue(new Callback<BookmarkInfo>() {
                    @Override
                    public void onResponse(Call<BookmarkInfo> call, Response<BookmarkInfo> response) {
                        if (response.isSuccessful()) {
                            if (response.body().message.equals("mark")) {
                                temp_v.VH_main_bookmark.setBackgroundResource(R.drawable.save);
                            } else if(response.body().message.equals("unmark")){
                                temp_v.VH_main_bookmark.setBackgroundResource(R.drawable.save_1);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BookmarkInfo> call, Throwable t) {
                        Log.i("err", t.getMessage());
                    }
                });
            }
        });




        holder.VH_Writer_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfileInfo(mainListDatas.get(temp).postinfo.profile,
                        mainListDatas.get(temp).postinfo.level,
                        mainListDatas.get(temp).postinfo.nickname, mainListDatas.get(temp).postinfo.statemessage);
                //사용자 정보랑 사진 넘겨줘야함
            }
        });

        //seeDetail 기능 // 삭제하기 신고하기 공유하기
        holder.VH_main_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final deleteInfo deleteinfo = new deleteInfo();
                deleteinfo.post_id = mainListDatas.get(temp).postinfo.id;
                deleteinfo.user_nick = user_nick;

                final boolean[] check = {false};

                service = ApplicationController.getInstance().getNetworkService();
                Call<deletecheckResult> requestLike = service.deletecheck(deleteinfo);
                requestLike.enqueue(new Callback<deletecheckResult>() {
                    @Override
                    public void onResponse(Call<deletecheckResult> call, Response<deletecheckResult> response) {
                        if (response.isSuccessful()) {
                            if (response.body().message.equals("deny")) {
                                check[0] = false;
                                Log.i("deny", String.valueOf(check[0]));
                            } else if(response.body().message.equals("ok")){
                                check[0] = true;
                                Log.i("ok", String.valueOf(check[0]));
                            }
                            showDetailOptionDialog(check[0], deleteinfo, temp);
                        }
                    }

                    @Override
                    public void onFailure(Call<deletecheckResult> call, Throwable t) {
                        Log.i("err", t.getMessage());
                    }
                });
            }
        });


        //게시글보기로 넘어가기
        holder.VH_main_space.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("POSTID",mainListDatas.get(temp).postinfo.id);
                intent.putExtra("USERNICK",user_nick);
                context.startActivity(intent);
            }
        });

        //댓글 모두 보기로 넘어가기
        holder.VH_main_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("POSTID",mainListDatas.get(temp).postinfo.id);
                context.startActivity(intent);
            }
        });

        //댓글 작성자 프로필 보여주기
        holder.VH_Comment1_userinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfileInfo(mainListDatas.get(temp).commentinfo.get(0).image,
                        mainListDatas.get(temp).commentinfo.get(0).level,
                        mainListDatas.get(temp).commentinfo.get(0).user_nick, mainListDatas.get(temp).commentinfo.get(0).statemessage );
            }
        });

        holder.VH_Comment2_userinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfileInfo(mainListDatas.get(temp).commentinfo.get(1).image,
                        mainListDatas.get(temp).commentinfo.get(1).level,
                        mainListDatas.get(temp).commentinfo.get(1).user_nick, mainListDatas.get(temp).commentinfo.get(1).statemessage);
            }
        });


        //댓글 내용 누르면 모두 보기로 넘어가기
        holder.VH_main_comment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("POSTID",mainListDatas.get(temp).postinfo.id);
                context.startActivity(intent);
            }
        });


        holder.VH_main_comment2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("POSTID",mainListDatas.get(temp).postinfo.id);
                context.startActivity(intent);
            }
        });
        if(mainListDatas.get(temp).postinfo.image.size()==0){
            holder.VH_main_images_layout.setVisibility(View.GONE);
        } else {
            for(int i = 0;i<mainListDatas.get(temp).postinfo.image.size();i++){
                holder.VH_main_images[i].setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load(mainListDatas.get(temp).postinfo.image.get(i))
                        .into(holder.VH_main_images[i]);
            }
        }

//        Log.i("recycler", String.valueOf(mainListDatas.get(temp).postinfo.image.size()));
        for(int i=0;i<mainListDatas.get(temp).postinfo.image.size();i++){
            final int finalI = i;
            holder.VH_main_images[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), EnlargementActivity.class);
                    intent.putExtra("IMAGES",mainListDatas.get(temp).postinfo.image);
                    intent.putExtra("SEQ", finalI);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mainListDatas != null ? mainListDatas.size() : 0;
    }

    public void likeButton() {

    }


    private void showDetailOptionDialog(boolean deletecheck, final deleteInfo deleteinfo, final int position) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customdialog_seedetail);
        // set the custom dialog components - text and button

        final RelativeLayout delete_post = (RelativeLayout) dialog.findViewById(R.id.delete);
        RelativeLayout share_post = (RelativeLayout) dialog.findViewById(R.id.share);
        RelativeLayout report_post = (RelativeLayout) dialog.findViewById(R.id.report);

        if(!deletecheck){
            delete_post.setVisibility(View.GONE);
        }

        // if button is clicked, close the custom dialog
        delete_post.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

                final Dialog f_dialog = new Dialog(context);
                f_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                f_dialog.setContentView(R.layout.delete_mypost);

//                ImageView delete_post_last = (ImageView) f_dialog.findViewById(R.id.delete_post_last);
//                ImageView delete_post_message = (ImageView) f_dialog.findViewById(R.id.delete_post_message);
                Button delete_post_no = (Button) f_dialog.findViewById(R.id.delete_post_no);
                Button delete_post_yes = (Button) f_dialog.findViewById(R.id.delete_post_yes);

                f_dialog.show();

                delete_post_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        f_dialog.dismiss();
                    }
                });

                delete_post_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        f_dialog.dismiss();

                        service = ApplicationController.getInstance().getNetworkService();
                        Call<deleteResult> requestdeletepost = service.deletePost(deleteinfo);
                        requestdeletepost.enqueue(new Callback<deleteResult>() {
                            @Override
                            public void onResponse(Call<deleteResult> call, Response<deleteResult> response) {
                                if (response.isSuccessful()) {
                                    if (response.body().message.equals("success")) {
                                        Toast.makeText(context,"글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                        mainListDatas.remove(position);
                                        setAdapter(mainListDatas);
                                        notifyDataSetChanged();
                                    }
                                }

                            }

                            @Override
                            public void onFailure(Call<deleteResult> call, Throwable t) {

                            }
                            ////////////////////////글 삭제된거 서버한테 알려줘야////////////////////////////////
                        });
                    }
                });
            }
        });
        report_post.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

                final Dialog rdialog = new Dialog(context);
                rdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                rdialog.setContentView(R.layout.report_post);

                Button report_post_no = (Button) rdialog.findViewById(R.id.report_post_no);
                Button report_post_yes = (Button) rdialog.findViewById(R.id.report_post_yes);

                rdialog.show();

                report_post_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rdialog.dismiss();
                    }
                });

                report_post_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context ,"글이 신고되었습니다.", Toast.LENGTH_SHORT).show();
                        rdialog.dismiss();
                    }
                });


            }
        });
        dialog.show();
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