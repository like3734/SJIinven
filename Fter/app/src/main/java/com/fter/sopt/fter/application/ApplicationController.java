package com.fter.sopt.fter.application;

import android.app.Activity;
import android.app.Application;

import com.fter.sopt.fter.first.KaKaoSDKAdapter;
import com.fter.sopt.fter.network.NetworkService;
import com.kakao.auth.KakaoSDK;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by f on 2017-06-30.
 */

public class ApplicationController extends Application {

    private static volatile ApplicationController instance = null;    // 먼저 어플리케이션 인스턴스 객체를 하나 선언
    private static volatile Activity currentActivity = null;

    private static String baseUrl = "http://52.78.166.21:3000";  // 베이스 url 초기화
    //HOST:  [서울서버]
    //http://52.192.28.227:3000
///"http://52.79.177.153:3000";

    private NetworkService networkService;                        // 네트워크 서비스 객체 선언

    public static ApplicationController getInstance() {
        return instance;
    }    // 인스턴스 객체 반환  왜? static 안드에서 static 으로 선언된 변수는 매번 객체를 새로 생성하지 않아도 다른 액티비티에서
    //자유롭게 사용가능합니다.

    public NetworkService getNetworkService() {
        return networkService;
    }    // 네트워크서비스 객체 반환


    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationController.instance = this; //인스턴스 객체 초기화
        buildService();
        KakaoSDK.init(new KaKaoSDKAdapter());
    }

    public void buildService() {
        Retrofit.Builder builder = new Retrofit.Builder();
        Retrofit retrofit = builder
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        networkService = retrofit.create(NetworkService.class);
    }
    public static Activity getCurrentActivity() {
        return currentActivity;
    }
    public static void setCurrentActivity(Activity currentActivity) {
        ApplicationController.currentActivity = currentActivity;
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }
    public static ApplicationController getGlobalApplicationContext() {
        if(instance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return instance;
    }
}
