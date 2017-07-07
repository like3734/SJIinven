package com.fter.sopt.fter.myPage;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.fter.sopt.fter.R;
import com.fter.sopt.fter.first.SplashActivity;

public class MyPageSettingActivity extends AppCompatActivity {
    Switch pushAlarm;
    RelativeLayout logout;
    RelativeLayout gogoFriend;
    String url;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page_setting);

        logout = (RelativeLayout)findViewById(R.id.logout);

        url = "http:// ";
        pushAlarm = (Switch) findViewById(R.id.pushAlarm);
        gogoFriend = (RelativeLayout)findViewById(R.id.setting_gogofriend);

        pushAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(pushAlarm.isChecked()){
                    Toast.makeText(getApplicationContext(), "푸시 알림이 켜졌습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "푸시 알림이 꺼졌습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        gogoFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClipBoardLink(getApplicationContext(), url);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref = getSharedPreferences("pref", MODE_PRIVATE);
                editor = pref.edit();
                editor.clear();
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                startActivity(intent);
                finish();

            }
        });


    }
    public static void setClipBoardLink(Context context, String link){
        ClipboardManager clipboardManager = (ClipboardManager)context.getSystemService(context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("label", link);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(context, "클립보드에 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }
}
