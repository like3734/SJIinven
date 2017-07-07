package com.fter.sopt.fter.first;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.fter.sopt.fter.application.ApplicationController;
import com.fter.sopt.fter.main.MainTimeline;
import com.fter.sopt.fter.network.NetworkService;
import com.fter.sopt.fter.profile.SetProfile;
import com.kakao.auth.ErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.helper.log.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by f on 2017-06-30.
 */

public class
KakaoSignupActivity extends Activity {


    public String ID;
    NetworkService service;
    IDdata iDdata;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    /**
     * Main으로 넘길지 가입 페이지를 그릴지 판단하기 위해 me를 호출한다.
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestMe();
    }

    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected void requestMe() { //유저의 정보를 받아오는 함수
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);
                Log.v("fail", "fail");

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                } else {
                    redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() {} // 카카오톡 회원이 아닐 시 showSignup(); 호출해야함

            @Override
            public void onSuccess(UserProfile userProfile) {
                service = ApplicationController.getInstance().getNetworkService();

                iDdata = new IDdata();
                ID = Long.toString(userProfile.getId());
                iDdata.id = ID;
                Call<IDResult> requestMainData = service.getUserInfo(iDdata);
                requestMainData.enqueue(new Callback<IDResult>() {
                    @Override
                    public void onResponse(Call<IDResult> call, final Response<IDResult> response) {
                        if (response.isSuccessful()){

                            pref = getSharedPreferences("pref", MODE_PRIVATE);
                            editor = pref.edit();
                            editor.putString("USERID", iDdata.id); //First라는 key값으로 infoFirst 데이터를 저장한다.
                            Log.i("ID값이란다",iDdata.id);

                            if(response.body().message.equals("new")){
                                redirectMainActivity();

                            } else if(response.body().message.equals("old")){
                                editor.putString("USERNICK", response.body().result.nickname);
                                Log.i("err",response.body().result.nickname);
                                Intent intent = new Intent(getApplicationContext(), MainTimeline.class);
                                intent.putExtra("PART",response.body().result.part);
                                intent.putExtra("USERNICK",response.body().result.nickname);
                                startActivity(intent);
                                finish();
                            }
                            editor.commit();

                        }
                    }

                    @Override
                    public void onFailure(Call<IDResult> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
                        redirectLoginActivity();
                    }
                });

                //성공 시 userProfile 형태로 반환
                Logger.d("UserProfile : " + userProfile);
                Log.v("user", userProfile.toString());
                 // 로그인 V성공시 MainActivity로
            }
        });
    }

    private void redirectMainActivity() {
        Intent intent = new Intent(getApplicationContext(), SetProfile.class);
        intent.putExtra("ID",iDdata.id);
        startActivity(intent);
        finish();
    }

    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

}


