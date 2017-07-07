package com.fter.sopt.fter.detail;

import java.util.ArrayList;

/**
 * Created by 김은영 on 2017-06-30.
 */

public class DetailListItem {

    DetailResult.Result result;

    public DetailListItem(){};

    public DetailListItem(DetailResult.Result result) {
        this.result = result;
    }

    class Result{
        public PostInpo postinpo;
        public ArrayList<com.fter.sopt.fter.comment.CommentInfo> commentinfo;
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
    }

    class Image{
        String image;
    }
}
