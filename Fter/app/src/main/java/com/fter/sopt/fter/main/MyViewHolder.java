package com.fter.sopt.fter.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fter.sopt.fter.R;

/**
 * Created by f on 2017-07-01.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {

    ////메인바꿔야하는것들/////
    public TextView VH_main_writer;     ///작성자
    public ImageView VH_main_img;       //프로필
    public TextView VH_main_time;       //작성시간
    public TextView VH_main_part;       //작성자파트
    public TextView VH_main_level;      //작성자레벨
    public TextView VH_main_title;      //글제목
    public TextView VH_main_content;    //글내용
    public TextView VH_main_likecount;       //좋아요수
    public TextView VH_main_comment_count;  //댓글수
    public ImageView VH_main_comment1_img;  //댓글1프로필
    public ImageView VH_main_comment2_img;  //댓글2프로필
    public TextView VH_main_comment1_name;  //댓글1작성자
    public TextView VH_main_comment2_name;  //댓글2작성자
    public TextView VH_main_comment1_content;   //댓글1내용
    public TextView VH_main_comment2_content;   //댓글2내용


    ///바뀜+기능달린것들
    public ImageView VH_main_likeicon;    //좋아요
    public ImageView VH_main_bookmark;          //북마크
    public ImageView[] VH_main_images;
    public RelativeLayout VH_main_images_layout;

    ///메인피드 기능달린것들
    //작성자프로필보기
    public RelativeLayout VH_Writer_bar;
    //삭제하기 공유하기 신고하기 다이얼로그 뜸
    public ImageView VH_main_option;
    //게시글보기
    public RelativeLayout VH_main_space;
    //사진크게보기 추가해야됨

    //댓글보기
    public RelativeLayout VH_main_comment;
    public RelativeLayout VH_main_comment1;
    public RelativeLayout VH_main_comment2;

    //댓글작성자 프로필보기
    public RelativeLayout VH_Comment1_userinfo;
    public RelativeLayout VH_Comment2_userinfo;

    ///댓글예외처리
    public RelativeLayout VH_main_commentshat1;
    public RelativeLayout VH_main_commentshat2;


    public MyViewHolder(View itemView) {
        super(itemView);
        //////메인피드///////

        VH_main_images = new ImageView[5];
        ////메인에서 바꿔야하는것들////
        VH_main_writer = (TextView)itemView.findViewById(R.id.mainList_writer); //작성자
        VH_main_img = (ImageView)itemView.findViewById(R.id.mainList_writer_img);   //프로필
        VH_main_time = (TextView)itemView.findViewById(R.id.mainList_time); //작성시간
        VH_main_part = (TextView)itemView.findViewById(R.id.mainList_part); //직상지파트
        VH_main_level = (TextView)itemView.findViewById(R.id.mainList_level);   //레벨
        VH_main_title = (TextView)itemView.findViewById(R.id.mainList_title);   //글제목
        VH_main_content = (TextView)itemView.findViewById(R.id.mainList_content);   //글내용
        VH_main_likecount = (TextView)itemView.findViewById(R.id.mainList_like);    //좋아요수
        VH_main_comment_count = (TextView)itemView.findViewById(R.id.mainList_comment_count); //댓글수
        VH_main_comment1_img = (ImageView) itemView.findViewById(R.id.mainList_comment1_img);   //댓글1프로필
        VH_main_comment2_img = (ImageView) itemView.findViewById(R.id.mainList_comment2_img);   //댓글2프로필
        VH_main_comment1_name = (TextView)itemView.findViewById(R.id.mainList_comment1_name);     //댓1작
        VH_main_comment2_name = (TextView)itemView.findViewById(R.id.mainList_comment2_name);       //댓2작
        VH_main_comment1_content = (TextView)itemView.findViewById(R.id.mainList_comment1_content); //댓1내용
        VH_main_comment2_content = (TextView)itemView.findViewById(R.id.mainList_comment2_content); //댓2내용

        //댓글예외처리
        VH_main_commentshat1 = (RelativeLayout)itemView.findViewById(R.id.latest_comment1);
        VH_main_commentshat2 = (RelativeLayout)itemView.findViewById(R.id.latest_comment2);

        //바뀜+기능달린거
        VH_main_likeicon = (ImageView)itemView.findViewById(R.id.mainList_likeicon);//좋아요
        VH_main_bookmark = (ImageView)itemView.findViewById(R.id.mainList_bookmarkicon); //북마크

        VH_main_images[0] = (ImageView)itemView.findViewById(R.id.mainList_img1);
        VH_main_images[1] = (ImageView)itemView.findViewById(R.id.mainList_img2);
        VH_main_images[2] = (ImageView)itemView.findViewById(R.id.mainList_img3);
        VH_main_images[3] = (ImageView)itemView.findViewById(R.id.mainList_img4);
        VH_main_images[4] = (ImageView)itemView.findViewById(R.id.mainList_img5);
        VH_main_images_layout = (RelativeLayout)itemView.findViewById(R.id.mainList_images);


        //메인피드 기능만 달린것들
        //작성자 프로필 보기
        VH_Writer_bar = (RelativeLayout)itemView.findViewById(R.id.writer_bar);
        //삭제하는 다이얼로그 쓰는거
        VH_main_option = (ImageView)itemView.findViewById(R.id.seeDetail);
        //게시글보기
        VH_main_space =(RelativeLayout)itemView.findViewById(R.id.mainList_space);
        //////////////////////////사진크게보기 추가해야됨

        //댓글보기
        VH_main_comment =(RelativeLayout)itemView.findViewById(R.id.comment_content);
        VH_main_comment1 =(RelativeLayout)itemView.findViewById(R.id.comment1_content);
        VH_main_comment2 =(RelativeLayout)itemView.findViewById(R.id.comment2_content);

        //댓글 작성자 프로필 보기
        VH_Comment1_userinfo =(RelativeLayout)itemView.findViewById(R.id.comment1_userinfo);
        VH_Comment2_userinfo =(RelativeLayout)itemView.findViewById(R.id.comment2_userinfo);


    }

}
