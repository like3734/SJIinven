package com.fter.sopt.fter.search;

/**
 * Created by 김민경 on 2017-07-01.
 */

public class SearchData {
    public int id;
    public String title;
    public String user_nick;


    public SearchData(String title, String user_nick,int id) {
        this.id = id;
        this.title = title;
        this.user_nick = user_nick;
    }
}
