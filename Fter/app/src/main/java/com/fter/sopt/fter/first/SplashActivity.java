package com.fter.sopt.fter.first;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.fter.sopt.fter.R;
import com.fter.sopt.fter.application.ApplicationController;
import com.fter.sopt.fter.main.MainTimeline;
import com.fter.sopt.fter.network.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.fter.sopt.fter.main.MainTimeline;

public class SplashActivity extends AppCompatActivity {

    NetworkService service;
    IDdata iDdata;
    String id;
    String name;
    String message;
    String USERID;
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        service = ApplicationController.getInstance().getNetworkService();

        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();
        iDdata = new IDdata();

        USERID = pref.getString("USERID","");  // NOTE: "USERID"에 값이 없을 경우 null반환
        iDdata.id = USERID;



        Call<IDResult> requestMainData = service.getUserInfo(iDdata);
        requestMainData.enqueue(new Callback<IDResult>() {
            @Override
            public void onResponse(Call<IDResult> call, final Response<IDResult> response) {
                Log.i("thi log","dd");
                if (response.isSuccessful()){
                    if(response.body().message.equals("new")){
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new  Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }, 3000);

                    } else if(response.body().message.equals("old")){
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                editor.putString("USERNICK",response.body().result.nickname);
                                editor.commit();
                                Intent intent = new Intent(getApplicationContext(), MainTimeline.class);
                                intent.putExtra("USERNICK",response.body().result.nickname);
                                intent.putExtra("PART",response.body().result.part);
                                startActivity(intent);

                                finish();
                            }
                        }, 3000);
                    } else{
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new  Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }, 3000);
                    }
                }

            }


            @Override
            public void onFailure(Call<IDResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
                Log.i("myTag",t.toString());
                Intent intent = new  Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
