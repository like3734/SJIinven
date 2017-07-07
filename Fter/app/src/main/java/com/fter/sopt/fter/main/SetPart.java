package com.fter.sopt.fter.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.fter.sopt.fter.R;
import com.fter.sopt.fter.category.CategoryActivity;

public class SetPart extends AppCompatActivity {

    ImageView develop;
    ImageView design;
    ImageView marketing;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String USERNICK;
    private static MainTimeline activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_part);

        develop = (ImageView)findViewById(R.id.develop);
        design = (ImageView)findViewById(R.id.design);
        marketing = (ImageView)findViewById(R.id.marketing);

        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();

        USERNICK = pref.getString("USERNICK",null);

        View.OnClickListener clicklistener = new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(getApplicationContext(), MainTimeline.class);
                switch (v.getId()){
                    case R.id.develop:
                        intent.putExtra("PART","develop");
                        break;
                    case R.id.design:
                        intent.putExtra("PART","design");
                        break;
                    case R.id.marketing:
                        intent.putExtra("PART","bm");
                        break;
                }
                intent.putExtra("USERNICK",USERNICK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                CategoryActivity categoryActivity = (CategoryActivity)CategoryActivity.activity;
                if(categoryActivity!=null){
                    categoryActivity.finish();
                }
                startActivity(intent);
                finish();
            }
        };

        develop.setOnClickListener(clicklistener);
        design.setOnClickListener(clicklistener);
        marketing.setOnClickListener(clicklistener);
    }

}
