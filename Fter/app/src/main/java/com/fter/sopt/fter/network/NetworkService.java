package com.fter.sopt.fter.network;

import com.fter.sopt.fter.alarm.AlarmCheck;
import com.fter.sopt.fter.alarm.AlarmResults;
import com.fter.sopt.fter.comment.CommentDatas;
import com.fter.sopt.fter.comment.CommentResult;
import com.fter.sopt.fter.detail.DetailInfo;
import com.fter.sopt.fter.detail.DetailResult;
import com.fter.sopt.fter.first.IDResult;
import com.fter.sopt.fter.first.IDdata;
import com.fter.sopt.fter.main.network.BookmarkInfo;
import com.fter.sopt.fter.main.network.FeedInfo;
import com.fter.sopt.fter.main.network.LikeInfo;
import com.fter.sopt.fter.main.network.PostlikeInfo;
import com.fter.sopt.fter.myPage.DuplicateMypageResult;
import com.fter.sopt.fter.myPage.DuplicateNickInfo;
import com.fter.sopt.fter.myPage.MyPage_UserPostResult;
import com.fter.sopt.fter.myPage.MyPage_UserResult;
import com.fter.sopt.fter.profile.DuplicateInfo;
import com.fter.sopt.fter.profile.Duplicate_Result;
import com.fter.sopt.fter.profile.MyPageModify_Result;
import com.fter.sopt.fter.profile.NickcheckInfo;
import com.fter.sopt.fter.profile.NicknameData;
import com.fter.sopt.fter.profile.ProfileModifyInfo;
import com.fter.sopt.fter.profile.ProfileResult;
import com.fter.sopt.fter.register.PostResult;
import com.fter.sopt.fter.register.RegisterWriterDatas;
import com.fter.sopt.fter.register.deleteInfo;
import com.fter.sopt.fter.register.deleteResult;
import com.fter.sopt.fter.register.deletecheckResult;
import com.fter.sopt.fter.search.SearchInfo;
import com.fter.sopt.fter.search.SearchResult;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by f on 2017-06-30.
 */

public interface NetworkService {
    @POST("/login")
    Call<IDResult> getUserInfo(@Body IDdata id);

    @POST("/login/nickcheck")
    Call<NickcheckInfo> nickCheck(@Body NicknameData nickname);

    @Multipart
    @POST("/login/profile")
    Call<ProfileResult> registerProfile(@Part ("id") RequestBody id,
                                        @Part("nickname") RequestBody nickname,
                                        @Part("part") RequestBody part,
                                        @Part("statemessage") RequestBody statemessage,
                                        @Part MultipartBody.Part image);

    @GET("/main/partlatest/{user_nick}/{part}")
    Call<FeedInfo> getPartlatestInfo(@Path("user_nick") String user_nick,
                                     @Path("part") String part);

    @GET("/main/postlike/{user_nick}/{post_id}")
    Call<LikeInfo> getLikeInfo(@Path("user_nick") String user_nick,
                               @Path("post_id") int post_id);

    @GET("/main/partpopular/{user_nick}/{part}")
    Call<FeedInfo> getPartpopularInfo(@Path("user_nick") String user_nick,
                                      @Path("part") String part);

    @GET("/main/categorylatest/{user_nick}/{part}")
    Call<FeedInfo> getCategorlatestInfo(@Path("user_nick") String user_nick,
                                        @Path("part") int category);

    @GET("/main/categorypopular/{user_nick}/{part}")
    Call<FeedInfo> getCategorypopulerInfo(@Path("user_nick") String user_nick,
                                          @Path("part") int category);


    @GET("/main/postlike/{user_nick}/{post_id}")
    Call<PostlikeInfo> getPostlikeInfo(@Path("user_nick") String user_nick,
                                       @Path("post_id") int post_id);

    @GET("/main/bookmark/{user_nick}/{post_id}")
    Call<BookmarkInfo> getBookmarkInfo(@Path("user_nick") String user_nick,
                                       @Path("post_id") int post_id);

    //마이페이지 유저정보 불러오기
    @GET("/mypage/{user_nick}")
    Call<MyPage_UserResult> getUserInfo(@Path("user_nick") String nickname);

    //마이페이지 내가 쓴 글
    @GET("/mypage/write/{user_nick}")
    Call<MyPage_UserPostResult> getUserWritePost(@Path("user_nick")String user_nick);

    //마이페이지 내가 찜한 글
    @GET("/mypage/like/{user_nick}")
    Call<MyPage_UserPostResult> getUserLikePost(@Path("user_nick") String user_nick);

    //마이페이지 수정하기
    @POST("/mypage/edit")
    Call<MyPageModify_Result> registerProfileModify(@Body ProfileModifyInfo profileModifyInfo);

    //닉네임 중복 체크
    @POST("/login/nickcheck")
    Call<Duplicate_Result> getDuplicateCheck(@Body DuplicateInfo duplicateInfo);

    //게시글 자세히 보기
    @POST("/post/read")
    Call<DetailResult> getDetailPost(@Body DetailInfo detailInfo);

    //댓글 작성
    @POST("/comment/add")
    Call<CommentResult> registerComment(@Body  com.fter.sopt.fter.comment.CommentInfo commentInfo);

    @POST("/main/find")
    Call<SearchResult> getSearchPost(@Body SearchInfo searchInfo);

    @GET("/comment/{post_id}")
    Call<CommentDatas> getComment(@Path("post_id") int post_id);

    @GET("/comment/useful/{post_id}")
    Call<CommentDatas> getUsefulComment(@Path("post_id") int post_id);

    @GET("/post/write/{user_nick}")
    Call<RegisterWriterDatas> getWriterData(@Path("user_nick") String user_nick);

    //마이페이지 닉네임 체크
    @POST("mypage/nickcheck")
    Call<DuplicateMypageResult> getDuplicateMyPageCheck(@Body DuplicateNickInfo duplicateNickInfo);

    //마이페이지 수정하기
    @Multipart
    @POST("/mypage/edit")
    Call<MyPageModify_Result> registerProfileModify(@Part("user_nick") RequestBody user_nick,
                                                    @Part("nickname") RequestBody nickname,
                                                    @Part("part") RequestBody part,
                                                    @Part ("statemessage") RequestBody statemessage,
                                                    @Part MultipartBody.Part image);

    @Multipart
    @POST("/post/add")
    Call<PostResult> registerPost(@Part ("category") int category,
                                  @Part("part") RequestBody part,
                                  @Part("title") RequestBody title,
                                  @Part("contents") RequestBody contents,
                                  @Part("user_nick") RequestBody user_nick,
                                  @Part MultipartBody.Part[] image);

    @POST("/post/deletecheck")
    Call<deletecheckResult> deletecheck(@Body deleteInfo deleteinfo);

    @POST("/post/delete")
    Call<deleteResult> deletePost(@Body deleteInfo deleteInfo);

    @GET("/alarm/{user_nick}")
    Call<AlarmResults> getAlarmDatas(@Path("user_nick") String user_nick);

    @GET("/alarm/readinfo/{alarm_id}")
    Call<AlarmCheck> postAlarmCheck(@Path("alarm_id")int alarm_id );

}
