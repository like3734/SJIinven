package com.fter.sopt.fter.detail;

import java.util.ArrayList;

/**
 * Created by f on 2017-06-25.
 */

public class DetailResult {
    String message;
    Result result;

    class Result{
        PostInpo postinpo;
        ArrayList<CommentInfo> commentinfo;
    }

    class PostInpo{
        public ArrayList<String> image;
        String nickname;
        int level;
        String userpart;
        String profile;
        String title;
        String contents;
        String written_time;
        String postpart;
        String statemessage;
        int category;
        int likecount;
        int commentcount;
        int likecheck;
        int markcheck;
    }

    class CommentInfo{
        String user_nick;
        String content;
        String image;
        String written_time;
        int level;
        String part;
        String statemessage;
    }

    class Image{
        String image;
    }
}