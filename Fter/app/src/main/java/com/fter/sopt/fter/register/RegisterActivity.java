package com.fter.sopt.fter.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fter.sopt.fter.R;
import com.fter.sopt.fter.application.ApplicationController;
import com.fter.sopt.fter.main.MainTimeline;
import com.fter.sopt.fter.network.NetworkService;
import com.yongbeam.y_photopicker.util.photopicker.PhotoPickerActivity;
import com.yongbeam.y_photopicker.util.photopicker.utils.YPhotoPickerIntent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private ImageView post;
    private ImageView register_writer;
    private TextView register_writer_level;
    private TextView register_writer_name;
    private TextView register_writer_part;
    private Spinner register_mypart;
    private Spinner register_mycategory;
    private EditText register_title;
    private EditText write_content;
    private TextView text_length;
    final int REQUST_CODE = 100;
    private Uri[] data;
    private ImageView[] img;
    private RelativeLayout[] imglayout;
    private ImageView[] addimg;
    ArrayList<String> imgurls;
    YPhotoPickerIntent intent;

    final int REQUEST_CODE = 57;
    String user_nick;
    NetworkService service;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        intent = new YPhotoPickerIntent(RegisterActivity.this);

        img = new ImageView[5];
        data = new Uri[5];
        imglayout = new RelativeLayout[5];
        addimg = new ImageView[5];
        imgurls = new ArrayList<String>();

        post = (ImageView)findViewById(R.id.register_post);
        register_writer = (ImageView)findViewById(R.id.register_writer);
        register_writer_level = (TextView)findViewById(R.id.register_writer_level);
        register_writer_name = (TextView)findViewById(R.id.register_writer_name);
        register_writer_part = (TextView)findViewById(R.id.register_writer_part);
        register_mypart = (Spinner)findViewById(R.id.mypart_spinner);
        register_mycategory = (Spinner)findViewById(R.id.mycategory_spinner);
        register_title = (EditText)findViewById(R.id.register_write_title);
        write_content = (EditText)findViewById(R.id.write_content);
        text_length = (TextView) findViewById(R.id.text_length);
        img[0] = (ImageView)findViewById(R.id.img1);
        img[1] = (ImageView)findViewById(R.id.img2);
        img[2] = (ImageView)findViewById(R.id.img3);
        img[3] = (ImageView)findViewById(R.id.img4);
        img[4] = (ImageView)findViewById(R.id.img5);
        imglayout[0] = (RelativeLayout) findViewById(R.id.imglayout1);
        imglayout[1] = (RelativeLayout)findViewById(R.id.imglayout2);
        imglayout[2] = (RelativeLayout)findViewById(R.id.imglayout3);
        imglayout[3] = (RelativeLayout)findViewById(R.id.imglayout4);
        imglayout[4] = (RelativeLayout)findViewById(R.id.imglayout5);
        addimg[0] = (ImageView)findViewById(R.id.addimg1);
        addimg[1] = (ImageView)findViewById(R.id.addimg2);
        addimg[2] = (ImageView)findViewById(R.id.addimg3);
        addimg[3] = (ImageView)findViewById(R.id.addimg4);
        addimg[4] = (ImageView)findViewById(R.id.addimg5);


        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();

        user_nick = pref.getString("USERNICK", null);  // NOTE: "USERID"에 값이 없을 경우 null반환
        Log.i("user_nick",user_nick);

        final Spinner dropdown = (Spinner) findViewById(R.id.mypart_spinner);
        final String[] items = new String[]{"", "경영/마케팅", "개발", "디자인"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);

        final Spinner dropdown1 = (Spinner) findViewById(R.id.mycategory_spinner);
        final String[] items1 = new String[]{"", "고민있어요", "궁금해요", "일상이야기", "함께해요"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items1);
        dropdown1.setAdapter(adapter1);

        service = ApplicationController.getInstance().getNetworkService();
        Call<RegisterWriterDatas> requestwriterData = service.getWriterData(user_nick);
        requestwriterData.enqueue(new Callback<RegisterWriterDatas>() {
            @Override
            public void onResponse(Call<RegisterWriterDatas> call, final Response<RegisterWriterDatas> response) {
                if (response.isSuccessful()) {
                    if (response.body().message.equals("ok")) {
                        if (response.body().result.get(0).profile != null) {
                            Glide.with(getApplicationContext()).load(response.body().result.get(0).profile)
                                    .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                                    .into(register_writer);
                        }
//                        register_writer_level.setText(Integer.toString(response.body().result.get(0).level));
//                        register_writer_name.setText(response.body().result.get(0).nickname);
//                        register_writer_part.setText(response.body().result.get(0).part);
                        register_writer_level.setText("LV." + Integer.toString(response.body().result.get(0).level));
                        register_writer_name.setText(response.body().result.get(0).nickname);

                        switch (response.body().result.get(0).part){
                            case "bm":
                                register_writer_part.setText("경영/마케팅");
                                break;
                            case "develop":
                                register_writer_part.setText("개발");
                                break;
                            case "design":
                                register_writer_part.setText("디자인");
                                break;
                            default:
                                break;
                        }
                        Log.i("ok",response.body().result.get(0).nickname);

                    }
                }
                Log.i("su",response.body().result.get(0).nickname);

            }

            @Override
            public void onFailure(Call<RegisterWriterDatas> call, Throwable t) {
                Log.i("ER","b");
            }
        });

        ////////////////////////프로그래스 다이얼로그 입니다////////////////////////
        mProgressDialog = new ProgressDialog(RegisterActivity.this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("등록 중...");
        mProgressDialog.setIndeterminate(true);


        ////////////////////////갤러리를 호출합니다////////////////////////
        for(int i=0;i<5;i++){
//            imglayout[i].setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    YPhotoPickerIntent intent;
//                    intent = new YPhotoPickerIntent(RegisterActivity.this);
//                    intent.setMaxSelectCount(5); //선택 가능한 체크박스 수
//                    intent.setShowCamera(true); //사진찍는 부분 추가할건지
//                    intent.setShowGif(false); //동영상 gif 포함시킬건지
//                    intent.setSelectCheckBox(true); //true 하면 사진클릭할때 무조건 체크됨. false 하면 사진클릭하면 사진확대, 체크박스에 가깝게 눌러야 체크됨.
//                    intent.setMaxGrideItemCount(5); //열 개수
//                    startActivityForResult(intent, REQUEST_CODE);    //INTENT_PHOTO는 정수
//                }
//            });
            imglayout[i].setOnClickListener(onClickListener);
        }


        ////////////////////////text 필드의 텍스트 길이를 출력!!////////////////////////
        write_content.addTextChangedListener(new TextWatcher() {
            String strCur;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                strCur = s.toString();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                text_length.setText(String.valueOf(s.length()) + "/500");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ////////////////////////Edit Text 길이제한!!////////////////////////
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(500);
        write_content.setFilters(FilterArray);


        ////////////////////////완료버튼!!////////////////////////
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mypart = register_mypart.getSelectedItem().toString();
                String mycategory = register_mycategory.getSelectedItem().toString();
                int category_int = 0;

                if(mypart=="경영/마케팅"){
                    mypart = "bm";
                }else if(mypart == "개발")
                {
                    mypart = "develop";
                }else{
                    mypart ="design";
                }


                switch (mycategory){
                    case("고민있어요"):
                        category_int=1;
                        break;
                    case("궁금해요"):
                        category_int=2;
                        break;
                    case("일상이야기"):
                        category_int=3;
                        break;
                    case("함께해요"):
                        category_int=4;
                        break;
                }



                if (register_title.length() == 0 || write_content.length() == 0) {
                    Toast.makeText(getApplicationContext(), "제목 및 내용을 확인해주세요.", Toast.LENGTH_SHORT).show();
                } else if(register_title.length()>25){
                    Toast.makeText(getApplicationContext(), "제목을 25자 이내로 설정해주세요.", Toast.LENGTH_SHORT).show();
                } else if(mypart.length()==0){
                    Toast.makeText(getApplicationContext(), "파트를 선택해주세요", Toast.LENGTH_SHORT).show();
                }else if(mycategory.length()==0){
                    Toast.makeText(getApplicationContext(), "카테고리를 선택해주세요", Toast.LENGTH_SHORT).show();
                } else{

                    mProgressDialog.show();

                   /*
                   RequestBody 객체에 edittext값들을 저장합니다.
                    */

                    RequestBody title = RequestBody.create(MediaType.parse("multipart/form-data"), register_title.getText().toString());
                    RequestBody contents = RequestBody.create(MediaType.parse("multipart/form-data"), write_content.getText().toString());
                    RequestBody user_nick = RequestBody.create(MediaType.parse("multipart/form-data"), register_writer_name.getText().toString());
                    RequestBody part = RequestBody.create(MediaType.parse("multipart/form-data"), mypart);
                    ////////////int형으로 multipart넘기기

                    MultipartBody.Part[] image = new MultipartBody.Part[5];

                    for(int i=0;i<imgurls.size();i++)

                    {
                        if (imgurls.get(i) == "") {
                            image = null;
                        } else {
                            data[i]=Uri.fromFile(new File(imgurls.get(i)));

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

                            options.inSampleSize = 2;
                            //                       options.inSampleSize = 4; //얼마나 줄일지 설정하는 옵션 4--> 1/4로 줄이겠다

                            InputStream in = null; // here, you need to get your context.
                            try {
                                in = getContentResolver().openInputStream(data[i]);
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

                            File photo = new File(imgurls.get(i)); // 가져온 파일의 이름을 알아내려고 사용합니다

                            // MultipartBody.Part 실제 파일의 이름을 보내기 위해 사용!!
                            image[i] = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);


                        }
                    }


                    /*
                    통신부는 따로 정리해드리겠습니다.
                    이번에는 post 메소드 입니다. body(이미지),writer,title,content 를 넘깁니다.
                     파일과 텍스트를 함께 넘길 때는 multipart를 사용합니다.
                     */
                    Call<PostResult> requestImgNotice = service.registerPost(category_int, part,title,contents,user_nick,image);
                    final String finalMypart = mypart;
                    requestImgNotice.enqueue(new Callback<PostResult>() {
                        @Override
                        public void onResponse(Call<PostResult> call, Response<PostResult> response) {
                            if (response.isSuccessful()) {
                                if (response.body().message.equals("ok")) {
                                    mProgressDialog.dismiss();
                                    Intent intent = new Intent(getApplicationContext(), MainTimeline.class);
                                    intent.putExtra("PART", finalMypart);
                                    intent.putExtra("USERNICK",register_writer_name.getText().toString());
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                                mProgressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<PostResult> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                            Log.i("myTag", t.toString());
                            mProgressDialog.dismiss();
                        }
                    });
                }
            }
        });


    }
    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view){

            intent.setMaxSelectCount(5); //선택 가능한 체크박스 수
            intent.setShowCamera(true); //사진찍는 부분 추가할건지
            intent.setShowGif(false); //동영상 gif 포함시킬건지
            intent.setSelectCheckBox(true); //true 하면 사진클릭할때 무조건 체크됨. false 하면 사진클릭하면 사진확대, 체크박스에 가깝게 눌러야 체크됨.
            intent.setMaxGrideItemCount(5); //열 개수
            startActivityForResult(intent, REQUEST_CODE);    //INTENT_PHOTO는 정수
        }
    };

    // 선택된 이미지 가져오기
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ArrayList<String> photos = null;
        if(resultCode==RESULT_OK && requestCode==REQUEST_CODE){
            if(data!=null){
                imgurls.clear();
                for(int i=0;i<5;i++){
                    imglayout[i].setVisibility(View.GONE);
                }
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
                for(int i=photos.size();i<5;i++){
                    imglayout[i].setVisibility(View.GONE);
                }
                if(photos.size()!=5) {
                    imglayout[photos.size()].setVisibility(View.VISIBLE);
                    img[photos.size()].setVisibility(View.VISIBLE);
                    addimg[photos.size()].setVisibility(View.VISIBLE);
                    img[photos.size()].setImageResource(R.drawable.camera);
                }
                for(int i=0;i<photos.size();i++){
                    addimg[i].setVisibility(View.INVISIBLE);
                    img[i].setVisibility(View.VISIBLE);
                    imglayout[i].setVisibility(View.VISIBLE);
                    Uri uri = Uri.fromFile(new File(photos.get(i)));

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

                    options.inSampleSize = 64;
                    //                       options.inSampleSize = 4; //얼마나 줄일지 설정하는 옵션 4--> 1/4로 줄이겠다

                    InputStream in = null; // here, you need to get your context.
                    try {
                        in = getContentResolver().openInputStream(uri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                            /*inputstream 형태로 받은 이미지로 부터 비트맵을 만들어 바이트 단위로 압축
                            그이우 스트림 배열에 담아서 전송합니다.
                             */

                    Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                    img[i].setImageBitmap(bitmap);
                    this.imgurls.add(i,photos.get(i));
                }
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        recycleView(findViewById(R.id.img1));
        recycleView(findViewById(R.id.img2));
        recycleView(findViewById(R.id.img3));
        recycleView(findViewById(R.id.img4));
        recycleView(findViewById(R.id.img5));
        recycleView(findViewById(R.id.imglayout1));
        recycleView(findViewById(R.id.imglayout2));
        recycleView(findViewById(R.id.imglayout3));
        recycleView(findViewById(R.id.imglayout4));
        recycleView(findViewById(R.id.imglayout5));
    }

    private void recycleView(View view) {
        if (view != null) {
            Drawable bg = view.getBackground();
            if (bg != null) {
                bg.setCallback(null);
                ((BitmapDrawable) bg).getBitmap().recycle();
                view.setBackgroundDrawable(null);
            }
        }
    }
}