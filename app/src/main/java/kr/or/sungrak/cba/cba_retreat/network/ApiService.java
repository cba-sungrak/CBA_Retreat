package kr.or.sungrak.cba.cba_retreat.network;

import kr.or.sungrak.cba.cba_retreat.fragment.CampMemList;
import kr.or.sungrak.cba.cba_retreat.models.AttendList;
import kr.or.sungrak.cba.cba_retreat.models.Campus;
import kr.or.sungrak.cba.cba_retreat.models.CampusStatisticList;
import kr.or.sungrak.cba.cba_retreat.models.GBSInfo;
import kr.or.sungrak.cba.cba_retreat.models.MyInfo;
import kr.or.sungrak.cba.cba_retreat.models.PeriodStatistic;
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

    //-------- 통 계 --------------------------------------------------------------------------------------------------------------//

    //1. 기간 통계
    //GET /statistics?from=2019-05-05&to=2019-05-12&campus=천안
    @GET("/statistics")
    Call<PeriodStatistic> getPeriodSatistic(@Query("from") String from, @Query("to") String to, @Query("campus") String campus);

    //2. 전체 통계(캠퍼스별 + 합계)
    //GET /statistics/total?date=2019-05-05&nav=PREV / NEXT / CURRENT(또는 빈값. 이외의 값은 에러)
    @GET("/statistics/total")
    Call<CampusStatisticList> getStatisticCampusList(@Query("date") String date, @Query("nav") String nav);

    //-------- 몽산포 수련회 --------------------------------------------------------------------------------------------------------------//
    //1. 수련회 등록
    //POST /mongsanpo/members
    @POST("/mongsanpo/members")
    Call<ResponseBody> regiCampMember(@Body RequestBody body);

    //2. 수련회 등록 명단
    //GET /mongsanpo/members
    @GET("/mongsanpo/members")
    Call<CampMemList> getRegiCampMember();
}
