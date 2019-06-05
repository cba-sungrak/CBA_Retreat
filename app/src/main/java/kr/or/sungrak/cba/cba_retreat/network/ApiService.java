package kr.or.sungrak.cba.cba_retreat.network;

import kr.or.sungrak.cba.cba_retreat.models.AttendList;
import kr.or.sungrak.cba.cba_retreat.models.Campus;
import kr.or.sungrak.cba.cba_retreat.models.CampusStatisticList;
import kr.or.sungrak.cba.cba_retreat.models.GBSInfo;
import kr.or.sungrak.cba.cba_retreat.models.MyInfo;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    //1.관리자 여부 체크
    @GET("getMyInfo/{uid}")
    Call<MyInfo> getMemberRepositories(@Path("uid") String uid);

    @GET("getGBSInfo/{uid}")
    Call<GBSInfo> getGBSRepositories(@Path("uid") String uid);

    //2. 담당 캠퍼스 목록 조희
    @GET("leaders/{uid}/campus/list")
    Call<Campus> getCampusList(@Header("Content-Type") String content_type, @Path("uid") String uid);

    //3. 출석부 조회
    @GET("attendance/list")
    Call<AttendList> getAttendList(@Query("date") String date, @Query("campus") String campus, @Query("nav") String nav);

    //4. 출석부 생성
    @POST("attendance/list/new")
    Call<AttendList> createAttend(@Body RequestBody body);

    //5. 출석부 저장
    @POST("attendance/list/report")
    Call<ResponseBody> postAttend(@Body RequestBody body);

    @GET("/statistics/total")
    Call<CampusStatisticList> getStatisticCampusList(@Query("date") String date);

}
