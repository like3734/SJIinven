package com.fter.sopt.fter.first;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fter.sopt.fter.R;
import com.fter.sopt.fter.application.ApplicationController;
import com.fter.sopt.fter.main.MainTimeline;
import com.fter.sopt.fter.network.NetworkService;
import com.fter.sopt.fter.profile.SetProfile;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    private int loginType = 0;
    private ImageView facebook_login;
    private ImageView kakao_login;
    private SessionCallback callback;
    private com.kakao.usermgmt.LoginButton loginButton;

    NetworkService service;

    // NOTE: mk, sharedpreferencep에 저장해놔야할것: 닉네임,아이디
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    IDdata iDdata;

    String message, id;

    //해시코드 얻을 때 이 함수 쓰면 로그통해서 쉽게 해시코드얻을 수 있음
    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.d("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        service = ApplicationController.getInstance().getNetworkService();

        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();

        iDdata = new IDdata();
        iDdata.id = pref.getString("USERID","");

        callback = new SessionCallback();                  // 이 두개의 함수 중요함
        Session.getCurrentSession().addCallback(callback);

        loginButton = (com.kakao.usermgmt.LoginButton)findViewById(R.id.loginButton);
        kakao_login = (ImageView) findViewById(R.id.kakao_login);
        kakao_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginButton.performClick();
            }
        });

        callbackManager = CallbackManager.Factory.create();

        facebook_login = (ImageView) findViewById(R.id.facebook_login);
        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_facebook);
        facebook_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();

            }
        });


        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("result", object.toString());
                        try {
                            iDdata.id = (String) object.get("id");
                            editor.putString("USERID",iDdata.id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.v("result", (String) iDdata.id);
                        Call<IDResult> requestMainData = service.getUserInfo(iDdata);
                        requestMainData.enqueue(new Callback<IDResult>() {
                            @Override
                            public void onResponse(Call<IDResult> call, final Response<IDResult> response) {
                                if (response.isSuccessful()){
                                    if(response.body().message.equals("new")){
                                        editor.commit();
                                        Intent intent = new Intent(getApplicationContext(), SetProfile.class);
                                        intent.putExtra("ID",iDdata.id);
                                        startActivity(intent);
                                        finish();
                                    } else if(response.body().message.equals("old")){

                                        editor.putString("USERNICK", response.body().result.nickname);
                                        editor.commit();
                                        Intent intent = new Intent(getApplicationContext(), MainTimeline.class);
                                        intent.putExtra("PART",response.body().result.part);
                                        intent.putExtra("USERNICK",response.body().result.nickname);
                                        startActivity(intent);
                                        finish();
                                    }
                                    editor.commit(); //완료한다.
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

                });
                Log.i("LoginErr","this is log see that");

                Toast.makeText(getApplicationContext(),"갸라라라라라라",Toast.LENGTH_LONG);
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.i("LoginErr","this is log see that");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("LoginErr",error.toString());
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }
    public void onLoginButtonClick(View v){
        switch (v.getId()){
            case R.id.login_facebook:
                loginType = 1;
                break;
        }
    }

//    private ImageView facebook_login;
//    private com.facebook.CallbackManager callbackManager;
//    private String ID;
//
//    private int loginType = 0;
////    private ImageView kakao_login;
////    private SessionCallback callback;
////    private com.kakao.usermgmt.LoginButton loginButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_login);
//        FacebookSdk.sdkInitialize(getApplicationContext());
//
//        callbackManager = CallbackManager.Factory.create();
//        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_facebook);
//        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
//                    @Override
//                    public void onCompleted(JSONObject object, GraphResponse response) {
//                        Log.v("result",object.toString());
//                        try {
//                            Toast.makeText(getApplicationContext(),"dndndndndndnndndndndndndndnd",Toast.LENGTH_LONG);
//                            Log.v("result", (String)object.get("id"));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        Intent intent = new Intent(getApplicationContext(), SetProfile.class);
//                        startActivity(intent);
//

//                    }
//                });
//                Toast.makeText(getApplicationContext(),"된거다!!!!!!!!!!!!!!!!!!!!!!!!!!!",Toast.LENGTH_SHORT).show();
//                ID = loginResult.getAccessToken().getUserId();
//                SharedPreferences USERID = getSharedPreferences("USERID", MODE_PRIVATE);
//                final SharedPreferences.Editor prefsEditor = USERID.edit();
//                prefsEditor.putString("First", ID);
//                prefsEditor.commit();
//
//
//                Log.e("onSuccess", "onSuccess");
//                Intent intent = new Intent(getApplicationContext(), SetProfile.class);
//                startActivity(intent);
//
//                Toast.makeText(getApplicationContext(),"갸라라라라라랄dndndndndndnndndndndndndndnd",Toast.LENGTH_LONG);
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "id,name,email,gender,birthday");
//                graphRequest.setParameters(parameters);
//                graphRequest.executeAsync();
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Log.e("LoginErr",error.toString());
//                Toast.makeText(getApplicationContext(),"어머나",Toast.LENGTH_LONG);
//            }
//        });
//
//        facebook_login = (ImageView) findViewById(facebook_login);
//        facebook_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loginButton.performClick();
//
//            }
//        });
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(loginType == 1)
//            callbackManager.onActivityResult(requestCode, resultCode, data);//여긴 페북
//
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//    public void onLoginButtonClick(View v){
//        switch (v.getId()){
//            case R.id.login_facebook:
//                loginType = 1;
//                break;
//        }
//    }
//
//
////        callbackManager = com.facebook.CallbackManager.Factory.create();
////        LoginManager.getInstance().registerCallback(callbackManager,
////                new FacebookCallback<LoginResult>() {
////                    @Override
////                    public void onSuccess(LoginResult loginResult) {
//////                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
//////                    @Override
//////                    public void onCompleted(JSONObject object, GraphResponse response) {
//////                        try {
//////                            Log.v("result", (String) object.get("id"));
//////                            Toast.makeText(getApplicationContext(),"떳다!!",Toast.LENGTH_SHORT).show();
//////                        } catch (JSONException e) {
//////                            e.printStackTrace();
//////                        }
//////                    }
//////                });
////
////                        Toast.makeText(getApplicationContext(),"된거다!!!!!!!!!!!!!!!!!!!!!!!!!!!",Toast.LENGTH_SHORT).show();
////                        ID = loginResult.getAccessToken().getUserId();
////                        SharedPreferences USERID = getSharedPreferences("USERID", MODE_PRIVATE);
////                        final SharedPreferences.Editor prefsEditor = USERID.edit();
////                        prefsEditor.putString("First", ID);
////                        prefsEditor.commit();
////
////
////                        Log.e("onSuccess", "onSuccess");
////                        Intent intent = new Intent(getApplicationContext(), SetProfile.class);
////                        startActivity(intent);
////                        finish();
////                    }
////
////                    @Override
////                    public void onCancel() {
////                        Log.e("onCancel", "onCancel");
////                        Toast.makeText(getApplicationContext(),"캔ㅅㄹ",Toast.LENGTH_SHORT).show();
////                    }
////
////                    @Override
////                    public void onError(FacebookException exception) {
////
////                        Log.e("onError", "onError " + exception.getLocalizedMessage());
////                        Toast.makeText(getApplicationContext(),"실패!!",Toast.LENGTH_SHORT).show();
////                    }
////                });
////
////
////    }
//////    @Override
//////    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
//////        super.onActivityResult(requestCode, resultCode, data);
//////
//////        callbackManager.onActivityResult(requestCode, resultCode, data);
//////    }
    @Override
    protected void onDestroy() {
         super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            redirectSignupActivity();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Logger.e(exception);
            }
            setContentView(R.layout.activity_login);
        }
    }

    protected void redirectSignupActivity() {
        final Intent intent = new Intent(this, KakaoSignupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
}

