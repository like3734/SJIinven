package com.fter.sopt.fter.main.network;

import java.util.ArrayList;

/**
 * Created by f on 2017-07-01.
 */


public class FeedInfo {

    public ArrayList<Result> result;
    public String message;

    public class Result{
        public PostInfo postinfo;
        public ArrayList<CommentInfo> commentinfo;
    }
}