package com.fter.sopt.fter.alarm;

import java.util.ArrayList;

/**
 * Created by f on 2017-07-07.
 */

public class AlarmResults {
    int count;
    ArrayList<Result> result;
    String message;
    class Result{
        int id;
        int readinfo;
        String written_time;
        int postid;
        String title;
    }
}
