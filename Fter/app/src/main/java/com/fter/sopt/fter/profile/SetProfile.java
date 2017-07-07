package com.fter.sopt.fter.profile;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.fter.sopt.fter.R;
import com.fter.sopt.fter.application.ApplicationController;
import com.fter.sopt.fter.main.MainTimeline;
import com.fter.sopt.fter.network.NetworkService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.makeText;

//import com.fter.sopt.fter.main.MainTimeline;

public class SetProfile extends AppCompatActivity {

    private ImageView setpicture;
    private EditText status_message;
    private Spinner spinner;
    private EditText nickname_edit;
    private Spinner myworkpart;
    private Toolbar profile_bar;
    private ImageView complete;
    private ImageView nickcheck_img;
    String nickckeck;
    private NicknameData nicknameData;
    private String user_id;
    // private ProgressDialog mProgressDialog;

    final int REQ_CODE_SELECT_IMAGE = 100;
    String imgUrl = "";
    Uri data;
    NetworkService service;
    ImageView setprofile;
    ImageView image;

    private ProgressDialog mProgressDialog;

    // NOTE: mk, sharedpreferencep에 저장해놔야할것: 닉네임,아이디
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    //private NetworkService service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);

        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();

        user_id = pref.getString("USERID",null);
        Log.i("Set",user_id);

        ////////////////////////서비스 객체 초기화////////////////////////


        ////////////////////////뷰 객체 초기화////////////////////////
        setprofile = (ImageView)findViewById(R.id.setprofile);
        setpicture = (ImageView) findViewById(R.id.pick_user_img);
        status_message = (EditText) findViewById(R.id.status_message);
        nickname_edit = (EditText) findViewById(R.id.nickname);
        myworkpart = (Spinner) findViewById(R.id.myworkpart);
        complete = (ImageView) findViewById(R.id.ok);
        nickcheck_img = (ImageView) findViewById(R.id.nickcheck);
        image = (ImageView)findViewById(R.id.user_img);

        service = ApplicationController.getInstance().getNetworkService();


        nicknameData = new NicknameData();
        nickckeck = new String();
        nickckeck = "false";

//        Intent intent = getIntent();
//        user_id = intent.getStringExtra("ID");

        final Spinner dropdown = (Spinner) findViewById(R.id.myworkpart);
        final String[] items = new String[]{"", "경영/마케팅", "개발", "디자인"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);

//        dropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                workPartStr = items[position];
//            }
//        });

        mProgressDialog = new ProgressDialog(SetProfile.this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("등록 중...");
        mProgressDialog.setIndeterminate(true);




        ////////////////////////갤러리 호출////////////////////////
        setpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
                selectProfileimg();
            }
        });

        nickcheck_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nicknameData.nickname = nickname_edit.getText().toString();
                if (nicknameData.nickname.equals("")) {
                    makeText(getApplicationContext(), "닉네임을 입력해주십시오", Toast.LENGTH_SHORT).show();
                } else if (nicknameData.nickname.length() > 7) {
                    makeText(getApplicationContext(), "닉네임을 7글자 이하로 써주십시오", Toast.LENGTH_SHORT).show();
                } else {
                    Call<NickcheckInfo> nickcheckInfoCall = service.nickCheck(nicknameData);
                    nickcheckInfoCall.enqueue(new Callback<NickcheckInfo>() {
                        @Override
                        public void onResponse(Call<NickcheckInfo> call, Response<NickcheckInfo> response) {
                            if (response.isSuccessful()) {
                                if (response.body().message.equals("false")) {
                                    makeText(getApplicationContext(), "닉네임이 이미 사용중입니다.", Toast.LENGTH_SHORT).show();
                                    nickckeck = "false";
                                } else if (response.body().message.equals("true")) {
                                    makeText(getApplicationContext(), "사용가능한 닉네임입니다.", Toast.LENGTH_SHORT).show();
                                    nickckeck = "true";
                                }
                            } else {
                                makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<NickcheckInfo> call, Throwable t) {
                            makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                            Log.i("myTag", t.toString());
                        }
                    });
                }
            }
        });

        complete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // profileData.part = myworkpart.getSelectedItem().toString();
                String text = myworkpart.getSelectedItem().toString();
                Log.i("Mytag" , "text" + text);

                if(text=="경영/마케팅"){
                    text = "bm";
                }else if(text == "개발")
                {
                    text = "develop";
                }else{
                    text ="design";
                }
                // Log.i("Mytag" , "part길이" + profileData.part.length());
                // Log.i("Mytag" , "part" + profileData.part);
                if (text.length() == 0) {

                    makeText(getApplicationContext(), "업무를 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if (nickckeck.equals("false") || !(nicknameData.nickname.equals(nickname_edit.getText().toString()))){
                    makeText(getApplicationContext(), nickckeck+"닉네임 중복확인을 해주세요."+nicknameData.nickname, Toast.LENGTH_SHORT).show();
                    makeText(getApplicationContext(), "닉네임 중복확인을 해주세요.", Toast.LENGTH_SHORT).show();
                }else {



                    // Log.i("Mytag", "RequestBody 전" + text);


                    RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"), user_id);
                    final RequestBody nickname = RequestBody.create(MediaType.parse("multipart/form-data"), nickname_edit.getText().toString());
                    RequestBody part = RequestBody.create(MediaType.parse("multipart/form-data"), text);
                    RequestBody statemessage = RequestBody.create(MediaType.parse("multipart/form-data"), status_message.getText().toString());

//                    Log.i("this",user_id);Log.i("this",nickname_edit.getText().toString());Log.i("this", text);Log.i("this", status_message.getText().toString());
                    Log.i("Mytag", "RequestBody" + part);
                    MultipartBody.Part image;
                    // MultipartBody.Part body = null;




                    if (imgUrl == "") {
                        imgUrl =ContentResolver.SCHEME_ANDROID_RESOURCE+"://"
                                + getApplicationContext().getResources().getResourcePackageName(R.drawable.propile_edit_89x89)
                                +'/'+ getApplicationContext().getResources().getResourceTypeName(R.drawable.propile_edit_89x89)
                                + '/' + getApplicationContext().getResources().getResourceEntryName(R.drawable.propile_edit_89x89);
                        data = Uri.parse(imgUrl);

//                        imgUrl = "android.resource://com.fter.sopt.fter/drawable/propile_edit_89x89";
//                        Log.i("IMG",imgUrl);

                    }

                        /**
                         * 비트맵 관련한 자료는 아래의 링크에서 참고
                         * http://mainia.tistory.com/468
                         */

                    /*
                    이미지를 리사이징하는 부분입니다.
                    리사이징하는 이유!! 안드로이드는 메모리에 민감하다고 세미나에서 말씀드렸습니다~
                    구글에서는 최소 16MByte로 정하고 있으나, 제조사 별로 또 디바이스별로 메모리의 크기는 다릅니다.
                    또한, 이미지를 서버에 업로드할 때 이미지의 크기가 크다면 시간이 오래 걸리고 데이터 소모가 큽니다!!
                     */
                        BitmapFactory.Options options = new BitmapFactory.Options();
//                       options.inSampleSize = 4; //얼마나 줄일지 설정하는 옵션 4--> 1/4로 줄이겠다

                        InputStream in = null; // here, you need to get your context.
                        try {
                            in = getContentResolver().openInputStream(data);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        /*inputstream 형태로 받은 이미지로 부터 비트맵을 만들어 바이트 단위로 압축
                        그이우 스트림 배열에 담아서 전송합니다.
                         */

                        Bitmap bitmap = BitmapFactory.decodeStream(in, null, options); // InputStream 으로부터 Bitmap 을 만들어 준다.
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                        // 압축 옵션( JPEG, PNG ) , 품질 설정 ( 0 - 100까지의 int형 ), 압축된 바이트 배열을 담을 스트림
                        RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray());

                        File photo = new File(imgUrl); // 가져온 파일의 이름을 알아내려고 사용합니다

                    Log.i("QWE", imgUrl);

                        // MultipartBody.Part 실제 파일의 이름을 보내기 위해 사용!!
                        image = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);



                    /*
                    통신부는 따로 정리해드리겠습니다.
                    이번에는 post 메소드 입니다. body(이미지),writer,title,content 를 넘깁니다.
                     파일과 텍스트를 함께 넘길 때는 multipart를 사용합니다.
                     */
                    Call<ProfileResult> RegisterProfile = service.registerProfile(id, nickname, part, statemessage, image);
                    final String finalText = text;
                    RegisterProfile.enqueue(new Callback<ProfileResult>() {
                        @Override
                        public void onResponse(Call<ProfileResult> call, Response<ProfileResult> response) {
                            Log.i("qwer",response.body().message);
                            if (response.body().message.equals("ok")) {
                                Toast.makeText(getApplicationContext(), "프로필 설정 완료", Toast.LENGTH_SHORT).show();
                                mProgressDialog.dismiss();
                                editor.putString("USERNICK", nickname_edit.getText().toString());
                                Log.i("USERNICK",nickname_edit.getText().toString());
                                editor.commit();
                                Intent intent1 = new Intent(getApplicationContext(), MainTimeline.class);
                                intent1.putExtra("USERNICK",nickname_edit.getText().toString());
                                intent1.putExtra("PART", finalText);
                                startActivity(intent1);
                                finish();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "엘스문", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ProfileResult> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "프로필 설정 완전실패", Toast.LENGTH_SHORT).show();
                            Log.i("myTag", t.toString());
                            mProgressDialog.dismiss();
                        }
                    });
                }
            }
        });
    }



    // 선택된 이미지 가져오기
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    String name_Str = getImageNameToUri(data.getData());
                    this.data = data.getData();

                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    ImageView image = (ImageView) findViewById(R.id.user_img);

                    //배치해놓은 ImageView에 set
                    image.setImageBitmap(image_bitmap);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                imgUrl = "";
            }
        }
    }

    // 선택된 이미지 파일명 가져오기
    public String getImageNameToUri(Uri data) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);

        imgUrl = imgPath;
        return imgName;
    }
    private void selectProfileimg(){

        final Dialog pdialog = new Dialog(SetProfile.this);
        pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pdialog.setContentView(R.layout.set_profile_img);

        RelativeLayout selectdefault =(RelativeLayout)pdialog.findViewById(R.id.set_img_type1);
        RelativeLayout selectmyphoto =(RelativeLayout)pdialog.findViewById(R.id.set_img_type2);

        pdialog.show();

        selectdefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdialog.dismiss();
                image.setImageResource(R.drawable.propile_edit_89x89);
            }
        });

        selectmyphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);

            }
        });
    }

}
