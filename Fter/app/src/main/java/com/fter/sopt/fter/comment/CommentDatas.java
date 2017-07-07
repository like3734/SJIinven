package com.fter.sopt.fter.comment;

import java.util.ArrayList;

/**
 * Created by f on 2017-06-27.
 */

public class CommentDatas {

    public ArrayList<CommentData> result;
    public String message;

    public class CommentData {
//        public void setUser_nick(String user_nick) {
//            this.user_nick = user_nick;
//        }
//
//        public void setContent(String content) {
//            this.content = content;
//        }
//
//        public void setWritten_time(String written_time) {
//            this.written_time = written_time;
//        }
//
//        public void setImage(String image) {
//            this.image = image;
//        }

        public String user_nick;
        public String content;
        public String written_time;
        public String image;
        public int level;
        public String statemessage;
    }
}