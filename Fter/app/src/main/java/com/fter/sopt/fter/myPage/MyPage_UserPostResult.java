package com.fter.sopt.fter.myPage;

import java.util.ArrayList;

/**
 * Created by 김은영 on 2017-06-28.
 */

public class MyPage_UserPostResult {

    ArrayList<Result> result;
    String message;

    public class Result{
        String title;
        String written_time;
        int id;
    }
}
