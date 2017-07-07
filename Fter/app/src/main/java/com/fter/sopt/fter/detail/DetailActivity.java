package com.fter.sopt.fter.detail;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fter.sopt.fter.R;
import com.fter.sopt.fter.application.ApplicationController;
import com.fter.sopt.fter.comment.CommentActivity;
import com.fter.sopt.fter.comment.CommentInfo;
import com.fter.sopt.fter.comment.CommentResult;
import com.fter.sopt.fter.enlargement.EnlargementActivity;
import com.fter.sopt.fter.main.MainTimeline;
import com.fter.sopt.fter.main.network.BookmarkInfo;
import com.fter.sopt.fter.main.network.LikeInfo;
import com.fter.sopt.fter.network.NetworkService;
import com.fter.sopt.fter.register.deleteInfo;
import com.fter.sopt.fter.register.deleteResult;
import com.fter.sopt.fter.register.deletecheckResult;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fter.sopt.fter.R.drawable.daily;
import static com.fter.sopt.fter.R.drawable.design_detail;
import static com.fter.sopt.fter.R.drawable.develpment;
import static com.fter.sopt.fter.R.drawable.fffff;
import static com.fter.sopt.fter.R.drawable.heart_2;
import static com.fter.sopt.fter.R.drawable.heartgray;
import static com.fter.sopt.fter.R.drawable.question;
import static com.fter.sopt.fter.R.drawable.save;
import static com.fter.sopt.fter.R.drawable.save_1;
import static com.fter.sopt.fter.R.drawable.thinking;
import static com.fter.sopt.fter.R.drawable.together;

public class DetailActivity extends AppCompatActivity {

    RelativeLayout seeAllComments;
    ListView listView;
    ImageButton sendBtn;
    CheckBox useful;
    boolean usefulCheckFlag;
    NetworkService service;
    DetailListItem Item;
    DetailListItem refreshItem;
    DetailInfo detailInfo;
    CommentInfo commentInfo;
    EditText editComment;
    ImageView FocusToeditComment;

    ImageView profile;
    TextView nickname;
    TextView level;
    TextView workpart;
    TextView title;
    TextView time;
    TextView content;
    TextView writtenTime;

    ImageView category;
    ImageView post_part;
    ImageView moreInfo;

    ImageView image0;
    ImageView image1;
    ImageView image2;
    ImageView image3;
    ImageView image4;


    ImageView commentIcon;
    ImageView comment1_profile;
    TextView comment1_nickname;
    TextView comment1_content;

    ImageView comment2_profile;
    TextView comment2_nickname;
    TextView comment2_content;

    LinearLayout commentLayout;
    LinearLayout comment1Layout;
    LinearLayout post_images;
    ArrayList<ImageView> imageArray;


    int postid;
    String user_Nick;

    LinearLayout detailBody;
    LinearLayout ignoreSection;
    InputMethodManager imm;

    TextView likeCount;
    TextView commentCount;

    ImageView likeIcon;
    ImageView bookmarkIcon;
    deleteInfo deleteinfo;

    LinearLayout detail_comment1_content;
    LinearLayout detail_comment2_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ///////////////////////////////통신 초기화///////////////////////////////////////////////
        service = ApplicationController.getInstance().getNetworkService();

        /////////////////////////////인텐트로 값 넘겨받기////////////////////////////////////
        Intent intent = getIntent();
        postid = intent.getIntExtra("POSTID",0);
        user_Nick = intent.getStringExtra("USERNICK");

        detailInfo = new DetailInfo();
        detailInfo.post_id = postid;
        detailInfo.user_nick = user_Nick;

        deleteinfo = new deleteInfo();
        deleteinfo.post_id = postid;
        deleteinfo.user_nick = user_Nick;

        editComment = (EditText)findViewById(R.id.detailMain_comment_edit);
        FocusToeditComment = (ImageView)findViewById(R.id.detail_EditText_Needto_Focus);

//        itemArray = new ArrayList<DetailListItem>();

        //인텐트로 넘어온 값을 받아야함

//
//        listView = (ListView)findViewById(R.id.detail_listView);
//        adapter = new DetailListAdapter(getApplicationContext(), itemArray, postid, user_Nick);
//        listView.setAdapter(adapter);

        /////////////////////////////뷰 항목들 초기화//////////////////////////////////////////
        profile = (ImageView)findViewById(R.id.detail_user_photo);
        nickname = (TextView)findViewById(R.id.detail_user_nickname);
        level = (TextView)findViewById(R.id.detail_user_level);
        workpart = (TextView)findViewById(R.id.detail_user_part);
        title = (TextView)findViewById(R.id.detail_title);
        time = (TextView)findViewById(R.id.detail_time);
        content = (TextView)findViewById(R.id.detail_content);
        writtenTime = (TextView)findViewById(R.id.detail_time);

        category = (ImageView)findViewById(R.id.detail_post_category);
        post_part = (ImageView)findViewById(R.id.detail_post_part);

        image0 = (ImageView)findViewById(R.id.detail_image0);
        image1 = (ImageView)findViewById(R.id.detail_image1);
        image2 = (ImageView)findViewById(R.id.detail_image2);
        image3 = (ImageView)findViewById(R.id.detail_image3);
        image4 = (ImageView)findViewById(R.id.detail_image4);

        seeAllComments = (RelativeLayout)findViewById(R.id.seeAllComments);
        post_images = (LinearLayout)findViewById(R.id.detail_postimages);


        commentIcon = (ImageView)findViewById(R.id.detail_comment_icon);
        likeIcon = (ImageView)findViewById(R.id.detail_likeicon);
        bookmarkIcon = (ImageView)findViewById(R.id.detail_bookmark);

        likeCount = (TextView)findViewById(R.id.detail_likecount);
        commentCount = (TextView)findViewById(R.id.detail_commentcount);

        detail_comment1_content = (LinearLayout)findViewById(R.id.de_comment1_click);
        detail_comment2_content = (LinearLayout)findViewById(R.id.de_comment2_click);

        imageArray = new ArrayList<ImageView>();
        imageArray.add(image0);
        imageArray.add(image1);
        imageArray.add(image2);
        imageArray.add(image3);
        imageArray.add(image4);


        comment1_profile = (ImageView)findViewById(R.id.detail_comment1_profile);
        comment1_nickname = (TextView)findViewById(R.id.detail_comment1_nickname);
        comment1_content = (TextView)findViewById(R.id.detail_comment1_content);

        comment2_profile = (ImageView)findViewById(R.id.detail_comment2_profile);
        comment2_nickname = (TextView)findViewById(R.id.detail_comment2_nickname);
        comment2_content = (TextView)findViewById(R.id.detail_comment2_content);

        commentLayout = (LinearLayout)findViewById(R.id.detail_Comment);
        comment1Layout = (LinearLayout)findViewById(R.id.detail_Comment1);

        moreInfo = (ImageView)findViewById(R.id.detail_postInfo);

        detailBody = (LinearLayout)findViewById(R.id.detail_body);
        ignoreSection = (LinearLayout)findViewById(R.id.detail_needToIgnore);
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        Item = new DetailListItem();


        /////////////////////////////////////본문 통신///////////////////////////////////////////
        Call<DetailResult> detailResultCall = service.getDetailPost(detailInfo);
        detailResultCall.enqueue(new Callback<DetailResult>() {
            @Override
            public void onResponse(Call<DetailResult> call, Response<DetailResult> response) {
                if(response.isSuccessful()){
                    if(response.body().message.equals("success")){
                        //통신이 성공했다면
                        Item.result = response.body().result;

                        if(Item.result.postinpo.image.size() == 0){
                            post_images.setVisibility(View.GONE);
                        }
                        else{
                            for(int i = 0; i < Item.result.postinpo.image.size() ; i++){
                                if(Item.result.postinpo.image.get(i) != null){
                                    Glide.with(getApplicationContext())
                                            .load(Item.result.postinpo.image.get(i))
                                            .into(imageArray.get(i));
                                }
                            }
                            //////////////////////기본 이미지 안보이게끔 설정/////////////////////////
                            for(int i = Item.result.postinpo.image.size(); i < 5; i++){
                                imageArray.get(i).setVisibility(View.GONE);
                            }
                        }


                        if (Item.result.postinpo.profile != null) {
                            Glide.with(getApplicationContext())
                                    .load(Item.result.postinpo.profile)
                                    .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                                    .into(profile);
                        }
                        nickname.setText(Item.result.postinpo.nickname);
                        level.setText("Lv."+Item.result.postinpo.level);

                        switch (Item.result.postinpo.userpart){
                            case "bm":
                                workpart.setText("경영/마케팅");
                                break;
                            case "develop":
                                workpart.setText("개발");
                                break;
                            case "design":
                                workpart.setText("디자인");
                                break;
                            default:
                                break;
                        }


                        title.setText(Item.result.postinpo.title);
                        time.setText(Item.result.postinpo.title);
                        content.setText(Item.result.postinpo.contents);
                        writtenTime.setText(Item.result.postinpo.written_time);

                        switch (Item.result.postinpo.postpart){
                            case "bm":
                                post_part.setImageResource(fffff);
                                break;
                            case "develop":
                                post_part.setImageResource(develpment);
                                break;
                            case "design":
                                post_part.setImageResource(design_detail);
                                break;
                            default:
                                break;
                        }

                        switch (Item.result.postinpo.category){
                            case 1:
                                category.setImageResource(thinking);
                                break;
                            case 2:
                                category.setImageResource(question);
                                break;
                            case 3:
                                category.setImageResource(daily);
                                break;
                            case 4:
                                category.setImageResource(together);
                                break;
                            default:
                                break;
                        }

                        if(Item.result.postinpo.likecheck == 1){
                            likeIcon.setImageResource(heart_2);
                        }
                        if(Item.result.postinpo.markcheck == 1){
                            bookmarkIcon.setImageResource(save);
                        }

                        likeCount.setText(Integer.toString(Item.result.postinpo.likecount));
                        commentCount.setText(Integer.toString(Item.result.postinpo.commentcount));

                        if(Item.result.commentinfo.size() == 0){
                            commentLayout.setVisibility(View.GONE);
                        }
                        else {
                            if (Item.result.commentinfo.size() == 1) {

                                commentLayout.setVisibility(View.VISIBLE);
                                comment1Layout.setVisibility(View.GONE);

                                if (Item.result.commentinfo.get(0).image != null) {
                                    Glide.with(getApplicationContext())
                                            .load(Item.result.commentinfo.get(0).image)
                                            .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                                            .into(comment1_profile);
                                }

                                comment1_nickname.setText(Item.result.commentinfo.get(0).user_nick);
                                comment1_content.setText(Item.result.commentinfo.get(0).content);

                                comment1_profile.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showProfileInfo(Item.result.commentinfo.get(0).image, Item.result.commentinfo.get(0).level, Item.result.commentinfo.get(0).user_nick , Item.result.commentinfo.get(0).statemessage);
                                    }
                                });

                                detail_comment1_content.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplication(), CommentActivity.class);
                                        intent.putExtra("POSTID", postid);
                                        intent.putExtra("USERNICK", user_Nick);
                                        startActivity(intent);
                                    }
                                });
                            }
                            else {

                                commentLayout.setVisibility(View.VISIBLE);
                                comment1Layout.setVisibility(View.VISIBLE);

                                if (Item.result.commentinfo.get(0).image != null) {
                                    Glide.with(getApplicationContext())
                                            .load(Item.result.commentinfo.get(0).image)
                                            .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                                            .into(comment1_profile);
                                }

                                if (Item.result.commentinfo.get(1).image != null) {
                                    Glide.with(getApplicationContext())
                                            .load(Item.result.commentinfo.get(1).image)
                                            .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                                            .into(comment2_profile);
                                }

                                comment1_nickname.setText(Item.result.commentinfo.get(0).user_nick);
                                comment1_content.setText(Item.result.commentinfo.get(0).content);
                                comment2_nickname.setText(Item.result.commentinfo.get(1).user_nick);
                                comment2_content.setText(Item.result.commentinfo.get(1).content);

                                comment1_profile.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //통신해서 statement 받아오기
                                        showProfileInfo(Item.result.commentinfo.get(0).image, Item.result.commentinfo.get(0).level, Item.result.commentinfo.get(0).user_nick , Item.result.commentinfo.get(0).statemessage);
                                    }
                                });

                                comment2_profile.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //통신해서 statement 받아오기
                                        showProfileInfo(Item.result.commentinfo.get(1).image, Item.result.commentinfo.get(1).level, Item.result.commentinfo.get(1).user_nick , Item.result.commentinfo.get(1).statemessage);
                                    }
                                });

                                detail_comment1_content.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplication(), CommentActivity.class);
                                        intent.putExtra("POSTID", postid);
                                        intent.putExtra("USERNICK", user_Nick);
                                        startActivity(intent);
                                    }
                                });

                                detail_comment2_content.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplication(), CommentActivity.class);
                                        intent.putExtra("POSTID", postid);
                                        intent.putExtra("USERNICK", user_Nick);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                        if(Item.result.postinpo.image.size()==0){
                            post_images.setVisibility(View.GONE);
                        }
                        //////////////////////////////////글 이미지 누르면 사진 크게 보기////////////////////////////////////
                        for(int i = 0; i < Item.result.postinpo.image.size(); i++){
                            if(Item.result.postinpo.image.get(i) != null){
                                final int finalI = i;
                                imageArray.get(i).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplicationContext(), EnlargementActivity.class);
                                        intent.putExtra("IMAGES",Item.result.postinpo.image);
                                        intent.putExtra("SEQ", finalI);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailResult> call, Throwable t) {
                Log.i("err", t.getMessage());
            }
        });

        ////////////////////////////////댓글 모두보기/////////////////////////////////////
        seeAllComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), CommentActivity.class);
                intent.putExtra("POSTID", postid);
                intent.putExtra("USERNICK", user_Nick);
                startActivity(intent);
            }
        });

        /////////////////////////////작성자 다이얼로그///////////////////////////////////////
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //통신해서 statement 받아오기
                //다이얼로그 띄우는 함수
                showProfileInfo(Item.result.postinpo.profile, Item.result.postinpo.level, Item.result.postinpo.nickname, Item.result.postinpo.statemessage);
            }
        });


        ///////////////////////////////////////좋아요 통신//////////////////////////////////////
        likeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<LikeInfo> likeInfoCall = service.getLikeInfo(user_Nick,postid);
                likeInfoCall.enqueue(new Callback<LikeInfo>() {
                    @Override
                    public void onResponse(Call<LikeInfo> call, Response<LikeInfo> response) {
                        if(response.isSuccessful()){
                            if(response.body().message.equals("like")){
                                likeIcon.setImageResource(heart_2);
                                likeCount.setText(Integer.toString(response.body().result.get(0).likecount));
                                Networking();


                            }
                            else if(response.body().message.equals("unlike")){
                                likeIcon.setImageResource(heartgray);
                                likeCount.setText(Integer.toString(response.body().result.get(0).likecount));
                                Networking();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LikeInfo> call, Throwable t) {

                    }
                });
            }
        });

        ///////////////////////////찜하기 통신//////////////////////////////////////////
        bookmarkIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<BookmarkInfo> bookmarkResultCall = service.getBookmarkInfo(user_Nick, postid);
                bookmarkResultCall.enqueue(new Callback<BookmarkInfo>() {
                    @Override
                    public void onResponse(Call<BookmarkInfo> call, Response<BookmarkInfo> response) {
                        if(response.isSuccessful()){
                            if(response.body().message.equals("mark")){
                                bookmarkIcon.setImageResource(save);
//                                Networking();
                            }
                            else if(response.body().message.equals("unmark")){
                                bookmarkIcon.setImageResource(save_1);
//                                Networking();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BookmarkInfo> call, Throwable t) {

                    }
                });
            }
        });

        //////////////////////////////댓글 아이콘 누르면 댓글 모두보기로 이동//////////////////////////
        commentIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), CommentActivity.class);
                intent.putExtra("POSTID", postid);
                intent.putExtra("USERNICK", user_Nick);
                startActivity(intent);
            }
        });

        //////////////////////////유용한 댓글////////////////////////////////////
        useful = (CheckBox)findViewById(R.id.use_check);
        useful.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    usefulCheckFlag = true;
                }
                else{
                    usefulCheckFlag = false;
                }
            }
        });

        ///////////////////////////////댓글 전송//////////////////////////////////////////
        sendBtn = (ImageButton) findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //댓글 서버로 올리는 통신부분
                if((editComment.getText().length() == 0)||(editComment.getText().length() == 1 && (editComment.getText().equals(" ")))){
                    Toast.makeText(getApplicationContext(), "댓글을 작성해주세요", Toast.LENGTH_SHORT).show();
                }
                else{
                    commentInfo = new CommentInfo();
                    commentInfo.user_nick = user_Nick;
                    commentInfo.post_id = postid;
                    commentInfo.content = editComment.getText().toString();
                    if(usefulCheckFlag == true){
                        commentInfo.useful = 1;
                    }
                    else{
                        commentInfo.useful = 0;
                    }

                    Call<CommentResult>commentResultCall = service.registerComment(commentInfo);
                    commentResultCall.enqueue(new Callback<CommentResult>() {
                        @Override
                        public void onResponse(Call<CommentResult> call, Response<CommentResult> response) {
                            if(response.isSuccessful()){
                                if(response.body().message.equals("ok")){
                                    Networking();
                                    Toast.makeText(getApplicationContext(), "댓글 등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                    editComment.setText("");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CommentResult> call, Throwable t) {
                            Log.i("err", t.getMessage());
                        }
                    });
                }
            }
        });

        moreInfo.setOnClickListener(new View.OnClickListener() {

            final boolean[] check = {false};

            @Override
            public void onClick(View v) {
                Call<deletecheckResult> requestLike = service.deletecheck(deleteinfo);
                requestLike.enqueue(new Callback<deletecheckResult>() {
                    @Override
                    public void onResponse(Call<deletecheckResult> call, Response<deletecheckResult> response) {
                        if (response.isSuccessful()) {
                            if (response.body().message.equals("deny")) {
                                check[0] = false;
                                Log.i("deny", String.valueOf(check[0]));
                            } else if(response.body().message.equals("ok")){
                                check[0] = true;
                                Log.i("ok", String.valueOf(check[0]));
                            }
                            showDetailOptionDialog(check[0], deleteinfo);
                        }
                    }

                    @Override
                    public void onFailure(Call<deletecheckResult> call, Throwable t) {
                        Log.i("err", t.getMessage());
                    }
                });
            }
        });


        /////////////////////////////본문 누르면 키보드 내리기/////////////////////////////////

        detailBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imm.isAcceptingText()){
                    hideKeyboard();
                }
            }
        });



        //////////////////////////////////키보드 올라와 있으면 터치 인식x////////////////////////////////
        if(imm.isAcceptingText()){
            ignoreSection.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }


        //////////////////////////댓글 창 회색부분 누르면 포커스//////////////////////////////////
        FocusToeditComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editComment.requestFocus();
                //showKeyboard();
            }
        });

    }

    public void Networking(){

        //게시글 전체를 리프레쉬
        Call<DetailResult> detailResultCall = service.getDetailPost(detailInfo);
        detailResultCall.enqueue(new Callback<DetailResult>() {
            @Override
            public void onResponse(Call<DetailResult> call, Response<DetailResult> response) {
                if(response.isSuccessful()){
                    if(response.body().message.equals("success")){
                        //통신이 성공했다면
                        refreshItem = new DetailListItem();
                        Item.result = response.body().result;

                        if(Item.result.postinpo.image.size() == 0){
                            post_images.setVisibility(View.GONE);
                        }
                        else{
                            for(int i = 0; i < Item.result.postinpo.image.size() ; i++){
                                if(Item.result.postinpo.image.get(i) != null){
                                    Glide.with(getApplicationContext())
                                            .load(Item.result.postinpo.image.get(i))
                                            .into(imageArray.get(i));
                                }
                            }
                        }


                        if (Item.result.postinpo.profile != null) {
                            Glide.with(getApplicationContext())
                                    .load(Item.result.postinpo.profile)
                                    .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                                    .into(profile);
                        }
                        nickname.setText(Item.result.postinpo.nickname);
                        level.setText("Lv."+Item.result.postinpo.level);

                        switch (Item.result.postinpo.userpart){
                            case "bm":
                                workpart.setText("경영/마케팅");
                                break;
                            case "develop":
                                workpart.setText("개발");
                                break;
                            case "design":
                                workpart.setText("디자인");
                                break;
                            default:
                                break;
                        }

                        title.setText(Item.result.postinpo.title);
                        time.setText(Item.result.postinpo.title);
                        content.setText(Item.result.postinpo.contents);
                        writtenTime.setText(Item.result.postinpo.written_time);

                        switch (Item.result.postinpo.postpart){
                            case "bm":
                                post_part.setImageResource(fffff);
                                break;
                            case "develop":
                                post_part.setImageResource(develpment);
                                break;
                            case "design":
                                post_part.setImageResource(design_detail);
                                break;
                            default:
                                break;
                        }

                        switch (Item.result.postinpo.category){
                            case 1:
                                category.setImageResource(thinking);
                                break;
                            case 2:
                                category.setImageResource(question);
                                break;
                            case 3:
                                category.setImageResource(daily);
                                break;
                            case 4:
                                category.setImageResource(together);
                                break;
                            default:
                                break;
                        }

                        if(Item.result.postinpo.likecheck == 1){
                            likeIcon.setImageResource(heart_2);
                        }
                        if(Item.result.postinpo.markcheck == 1){
                            bookmarkIcon.setImageResource(save);
                        }

                        likeCount.setText(Integer.toString(Item.result.postinpo.likecount));
                        commentCount.setText(Integer.toString(Item.result.postinpo.commentcount));

                        if(Item.result.commentinfo.size() == 0){
                            commentLayout.setVisibility(View.GONE);
                        }
                        else {
                            if (Item.result.commentinfo.size() == 1) {

                                commentLayout.setVisibility(View.VISIBLE);
                                comment1Layout.setVisibility(View.GONE);

                                if (Item.result.commentinfo.get(0).image != null) {
                                    Glide.with(getApplicationContext())
                                            .load(Item.result.commentinfo.get(0).image)
                                            .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                                            .into(comment1_profile);
                                }

                                comment1_nickname.setText(Item.result.commentinfo.get(0).user_nick);
                                comment1_content.setText(Item.result.commentinfo.get(0).content);

                            }
                            else {

                                commentLayout.setVisibility(View.VISIBLE);
                                comment1Layout.setVisibility(View.VISIBLE);
                                if (Item.result.commentinfo.get(0).image != null) {
                                    Glide.with(getApplicationContext())
                                            .load(Item.result.commentinfo.get(0).image)
                                            .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                                            .into(comment1_profile);
                                }

                                if (Item.result.commentinfo.get(1).image != null) {
                                    Glide.with(getApplicationContext())
                                            .load(Item.result.commentinfo.get(1).image)
                                            .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                                            .into(comment2_profile);
                                }

                                comment1_nickname.setText(Item.result.commentinfo.get(0).user_nick);
                                comment1_content.setText(Item.result.commentinfo.get(0).content);
                                comment2_nickname.setText(Item.result.commentinfo.get(1).user_nick);
                                comment2_content.setText(Item.result.commentinfo.get(1).content);

                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailResult> call, Throwable t) {
                Log.i("err", t.getMessage());
            }
        });
    }

    private void hideKeyboard(){
        //imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        imm.hideSoftInputFromWindow(editComment.getWindowToken(), 0);
    }

    private void showDetailOptionDialog(boolean deletecheck, final deleteInfo deleteinfo) {

        final Dialog dialog = new Dialog(DetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customdialog_seedetail);
        // set the custom dialog components - text and button

        final RelativeLayout delete_post = (RelativeLayout) dialog.findViewById(R.id.delete);
        RelativeLayout share_post = (RelativeLayout) dialog.findViewById(R.id.share);
        RelativeLayout report_post = (RelativeLayout) dialog.findViewById(R.id.report);

        if(!deletecheck){
            delete_post.setVisibility(View.GONE);
        }

        // if button is clicked, close the custom dialog
        delete_post.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

                final Dialog f_dialog = new Dialog(DetailActivity.this);
                f_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                f_dialog.setContentView(R.layout.delete_mypost);

//                ImageView delete_post_last = (ImageView) f_dialog.findViewById(R.id.delete_post_last);
//                ImageView delete_post_message = (ImageView) f_dialog.findViewById(R.id.delete_post_message);
                Button delete_post_no = (Button) f_dialog.findViewById(R.id.delete_post_no);
                Button delete_post_yes = (Button) f_dialog.findViewById(R.id.delete_post_yes);

                f_dialog.show();

                delete_post_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        f_dialog.dismiss();
                    }
                });

                delete_post_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        f_dialog.dismiss();

                        service = ApplicationController.getInstance().getNetworkService();
                        Call<deleteResult> requestdeletepost = service.deletePost(deleteinfo);
                        requestdeletepost.enqueue(new Callback<deleteResult>() {
                            @Override
                            public void onResponse(Call<deleteResult> call, Response<deleteResult> response) {
                                if (response.isSuccessful()) {
                                    if (response.body().message.equals("success")) {
                                        Toast.makeText(DetailActivity.this,"글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                        MainTimeline activity = (MainTimeline)MainTimeline.activity;
//                                        activity.recreate();
                                        finish();
                                    }
                                }

                            }

                            @Override
                            public void onFailure(Call<deleteResult> call, Throwable t) {

                            }
                            ////////////////////////글 삭제된거 서버한테 알려줘야////////////////////////////////
                        });
                    }
                });
            }
        });
        report_post.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

                final Dialog rdialog = new Dialog(DetailActivity.this);
                rdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                rdialog.setContentView(R.layout.report_post);

                Button report_post_no = (Button) rdialog.findViewById(R.id.report_post_no);
                Button report_post_yes = (Button) rdialog.findViewById(R.id.report_post_yes);

                rdialog.show();

                report_post_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("cancle","sd");
                        rdialog.dismiss();
                    }
                });

                report_post_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("ok","l;");
                        Toast.makeText(DetailActivity.this,"글이 신고되었습니다.", Toast.LENGTH_SHORT).show();
                        rdialog.dismiss();
                    }
                });


            }
        });
        dialog.show();
    }

    private void showProfileInfo(String image, int level, String nickname, String statement){

        final Dialog pdialog = new Dialog(DetailActivity.this);
        pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pdialog.setContentView(R.layout.show_profile_dialog);

        ImageView profile_img =(ImageView)pdialog.findViewById(R.id.show_profile_photo);
        TextView user_level =(TextView)pdialog.findViewById(R.id.show_profile_level);
        TextView user_nickname =(TextView)pdialog.findViewById(R.id.show_profile_nickname);
        TextView user_statement =(TextView)pdialog.findViewById(R.id.show_profile_statement);
        ImageView profile_cancelBtn=(ImageView)pdialog.findViewById(R.id.show_profile_cancelBtn);

        if(image!=""){
            Glide.with(getApplicationContext()).load(image)
                    .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                    .into(profile_img);
        }
        user_level.setText("LV. "+Integer.toString(level));
        user_nickname.setText(nickname);
        user_statement.setText(statement);
        pdialog.show();

        profile_cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdialog.dismiss();
            }
        });
    }

}
