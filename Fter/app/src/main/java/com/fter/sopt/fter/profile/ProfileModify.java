package com.fter.sopt.fter.profile;

import android.app.Activity;
import android.app.Dialog;
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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fter.sopt.fter.R;
import com.fter.sopt.fter.application.ApplicationController;
import com.fter.sopt.fter.myPage.DuplicateMypageResult;
import com.fter.sopt.fter.myPage.DuplicateNickInfo;
import com.fter.sopt.fter.myPage.MyPageActivity;
import com.fter.sopt.fter.network.NetworkService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileModify extends AppCompatActivity {
    ImageView modifyBtn;
    EditText editNickname;
    EditText editStatement;
    Spinner workPart;
    ImageView duplicateBtn;
    ImageView okBtn;
    DuplicateNickInfo duplicateNickInfo;
    ImageView userProfile;

    String workpartStr;
    String nickname;
    String checkedNickname = "";
    String original_nickname = ""; //path로 넘어온, 바꾸기 전의 nickname

    final int REQ_CODE_SELECT_IMAGE = 100;
    String imgUrl ="";
    Uri data;

    String D_statemessage;
    String D_part;
    String D_profile;

    boolean duplicateFlag;

    NetworkService service;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_modify);

        duplicateFlag = true;

        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();

        Intent intent = getIntent();
        original_nickname = intent.getStringExtra("USERNICK");
        D_part = intent.getStringExtra("PART");
        D_statemessage = intent.getStringExtra("STATEMESSAGE");
        D_profile = intent.getStringExtra("PROFILE");


        final String[] workPartItem = {"경영/마케팅", "디자인", "개발"};

        modifyBtn = (ImageView)findViewById(R.id.mypage_modify_profileBtn);
        editNickname = (EditText)findViewById(R.id.mypage_modify_nickname);
        editStatement = (EditText)findViewById(R.id.mypage_modify_statement);
        workPart = (Spinner)findViewById(R.id.mypage_modify_workpart_spinner);

        duplicateBtn = (ImageView)findViewById(R.id.mypage_modify_duplicateCheck);
        okBtn = (ImageView)findViewById(R.id.mypage_okButton);

        service = ApplicationController.getInstance().getNetworkService();

        ///////////////////////////display할 사용자의 정보/////////////////////////////////

        service = ApplicationController.getInstance().getNetworkService();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, workPartItem);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        workPart.setAdapter(adapter);

        //////////////////////////원래 사용자의 정보 display/////////////////////////////////
        userProfile = (ImageView) findViewById(R.id.mypage_modify_profile);

        if(D_profile != null){
            Glide.with(getApplicationContext())
                    .load(D_profile)
                    .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                    .into(userProfile);
        }

        editNickname.setText(original_nickname);
        editStatement.setText(D_statemessage);

        switch (D_part){
            case "bm":
                workPart.setSelection(0);
                break;
            case "design":
                workPart.setSelection(1);
                break;
            case "develop":
                workPart.setSelection(2);
                break;
            default:
                break;
        }


        workPart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        workpartStr = "bm";
                        break;
                    case 1:
                        workpartStr = "design";
                        break;
                    case 2:
                        workpartStr = "develop";
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        workPart.setAdapter(adapter);

        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //프로필 사진 수정
                //여기서는 사진만 받아오고 통신할 때 보내기
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
                selectProfileimg();
        }
        });



        duplicateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //중복 확인 버튼 눌렀을 때 통신
                duplicateNickInfo = new DuplicateNickInfo();
                duplicateNickInfo.oldnick = original_nickname;
                duplicateNickInfo.newnick = editNickname.getText().toString();
                Call<DuplicateMypageResult> duplicateMypageResultCall = service.getDuplicateMyPageCheck(duplicateNickInfo);
                duplicateMypageResultCall.enqueue(new Callback<DuplicateMypageResult>() {
                    @Override
                    public void onResponse(Call<DuplicateMypageResult> call, Response<DuplicateMypageResult> response) {
                        if(response.isSuccessful()){
                            if(response.body().message.equals("true")){
                                checkedNickname = editNickname.getText().toString();
                                duplicateFlag = true;
                                Toast.makeText(getApplicationContext(), "사용할 수 있는 닉네임입니다.", Toast.LENGTH_SHORT).show();
                            }
                            else if(response.body().message.equals("false")){
                                duplicateFlag = false;
                                Toast.makeText(getApplicationContext(), "사용할 수 없는 닉네임입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DuplicateMypageResult> call, Throwable t) {
                        Log.i("err", t.getMessage());
                    }
                });
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            ProfileModifyInfo profileModifyInfo = new ProfileModifyInfo();
            @Override
            public void onClick(View v) {
                nickname = editNickname.getText().toString();
                String statement = editStatement.getText().toString();
                if(nickname.length() == 0){
                    Toast.makeText(getApplicationContext(), "닉네임을 입력해주십시오", Toast.LENGTH_SHORT).show();
                }
                else if(nickname.length() > 7){
                    Toast.makeText(getApplicationContext(),"닉네임을 7자 이내로 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(statement.length() > 30){
                    Toast.makeText(getApplicationContext(), "30자 이내로 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(duplicateFlag == false){
                    Toast.makeText(getApplicationContext(), "닉네임 중복확인을 해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(workpartStr.length() == 0){
                    Toast.makeText(getApplicationContext(), "파트를 선택해 주세요", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(nickname.equals(checkedNickname)||nickname.equals(original_nickname)) {
                        //마이페이지에서 원래 사용자의 닉네임을 받아와야함

                        RequestBody user_nick = RequestBody.create(MediaType.parse("multipart/form-data"), original_nickname);
                        RequestBody nicknameM = RequestBody.create(MediaType.parse("multipart/form-data"), editNickname.getText().toString());
                        RequestBody part = RequestBody.create(MediaType.parse("multipart/form-data"), workpartStr);
                        RequestBody statemessage = RequestBody.create(MediaType.parse("multipart/form-data"), statement);

                        MultipartBody.Part body = null;
                        if(imgUrl == ""){
                            imgUrl = ContentResolver.SCHEME_ANDROID_RESOURCE+"://"
                                    + getApplicationContext().getResources().getResourcePackageName(R.drawable.propile_edit_89x89)
                                    +'/'+ getApplicationContext().getResources().getResourceTypeName(R.drawable.propile_edit_89x89)
                                    + '/' + getApplicationContext().getResources().getResourceEntryName(R.drawable.propile_edit_89x89);
                            data = Uri.parse(imgUrl);
                        }
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        //options.inSampleSize = 8; //얼마나 줄일지 설정하는 옵션 4--> 1/4로 줄이겠다
                        InputStream in = null;
                        try {
                            in = getContentResolver().openInputStream(data);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        // here, you need to get your context.
                        Log.i("URL",imgUrl);
                        /*inputstream 형태로 받은 이미지로 부터 비트맵을 만들어 바이트 단위로 압축
                        그이우 스트림 배열에 담아서 전송합니다.
                         */

                        Bitmap bitmap = BitmapFactory.decodeStream(in, null, options); // InputStream 으로부터 Bitmap 을 만들어 준다.
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                        // 압축 옵션( JPEG, PNG ) , 품질 설정 ( 0 - 100까지의 int형 ), 압축된 바이트 배열을 담을 스트림
                        RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray());
                        File photo = new File(imgUrl); // 가져온 파일의 이름을 알아내려고 사용합니다
                        // MultipartBody.Part 실제 파일의 이름을 보내기 위해 사용!!
                        body = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);

                        Call<MyPageModify_Result> myPageModify_resultCall = service.registerProfileModify(user_nick, nicknameM, part, statemessage, body);
                        myPageModify_resultCall.enqueue(new Callback<MyPageModify_Result>() {
                            @Override
                            public void onResponse(Call<MyPageModify_Result> call, Response<MyPageModify_Result> response) {
                                if (response.isSuccessful()) {
                                    if (response.body().message.equals("update")) {
                                        editor.putString("USERNICK",nickname);
                                        editor.commit();
                                        Toast.makeText(getApplicationContext(), "회원정보 수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<MyPageModify_Result> call, Throwable t) {
                                Log.i("err", t.getMessage());
                            }
                        });
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "닉네임 중복확인을 해주세요", Toast.LENGTH_SHORT).show();
                    }
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

                    //imgNameTextView.setText(name_Str);
                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    ImageView image = (ImageView) findViewById(R.id.mypage_modify_profile);

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
    private void getData(String imgUrl){
        this.imgUrl = imgUrl;
    }

    private void selectProfileimg(){

        final Dialog pdialog = new Dialog(ProfileModify.this);
        pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pdialog.setContentView(R.layout.set_profile_img);

        RelativeLayout selectdefault =(RelativeLayout)pdialog.findViewById(R.id.set_img_type1);
        RelativeLayout selectmyphoto =(RelativeLayout)pdialog.findViewById(R.id.set_img_type2);

        pdialog.show();

        selectdefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdialog.dismiss();
                userProfile.setImageResource(R.drawable.propile_edit_89x89);
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
