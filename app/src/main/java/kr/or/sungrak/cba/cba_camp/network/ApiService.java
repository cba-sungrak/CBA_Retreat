package kr.or.sungrak.cba.cba_camp.network;

import kr.or.sungrak.cba.cba_camp.fragment.camp.CampMemList;
import kr.or.sungrak.cba.cba_camp.models.AttendList;
import kr.or.sungrak.cba.cba_camp.models.Campus;
import kr.or.sungrak.cba.cba_camp.models.CampusStatisticList;
import kr.or.sungrak.cba.cba_camp.models.GBSInfo;
import kr.or.sungrak.cba.cba_camp.models.GBSStepStatisticDatas;
import kr.or.sungrak.cba.cba_camp.models.GBSTotalStatisticDatas;
import kr.or.sungrak.cba.cba_camp.models.MyInfo;
import kr.or.sungrak.cba.cba_camp.models.PeriodStatistic;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    //0. 본인 정보 조희
    @GET("members/info")
    Call<MyInfo> getMyInfo(@Query("uid") String uid);

    //1.관리자 여부 체크
//    @GET("getMyInfo/{uid}")
//    Call<MyInfo> getMyInfo(@Path("uid") String uid);

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

    //7. 출석부 명단 수정
    @POST("/attendance/members/edit")
    Call<ResponseBody> postEditAttendMember(@Body RequestBody body);

    //8. 출석부 삭제
    @HTTP(method = "DELETE", path = "/attendance/list", hasBody = true)
    Call<ResponseBody> deleteAttend(@Body RequestBody object);

    //9. 개인별 출석 현황 조희
    @GET("attendance/{id}/history")
    Call<AttendList> getindividualAttend(@Path("id") String id);

    //--------gbs 관리-----------------------------------------------------------------------
    //1. 출석부 조회
    @GET("gbs/attendance/list")
    Call<AttendList> getGBSAttendList(@Query("date") String date, @Query("leaderMemberId") String leaderMemId, @Query("nav") String nav);

    //2. 출석부 생성
    @POST("gbs/attendance/list/new")
    Call<AttendList> createGBSAttend(@Body RequestBody body);

    //3. 출석부 저장
    @POST("gbs/attendance/list/report")
    Call<ResponseBody> postGBSAttend(@Body RequestBody body);

    //4. 출석부 삭제
    @HTTP(method = "DELETE", path = "gbs/attendance/list", hasBody = true)
    Call<ResponseBody> deleteGBSAttend(@Body RequestBody object);

    //5. 출석부 명단 수정
//    @POST("/attendance/members/edit")
//    Call<ResponseBody> postEditAttendMember(@Body RequestBody body);





    //-------- 캠퍼스 통 계 --------------------------------------------------------------------------------------------------------------//

    //1. 기간 통계
    //GET /statistics?from=2019-05-05&to=2019-05-12&campus=천안
    @GET("/statistics")
    Call<PeriodStatistic> getPeriodStatistics(@Query("from") String from, @Query("to") String to, @Query("campus") String campus);

    //2. 전체 통계(캠퍼스별 + 합계)
    //GET /statistics/total?date=2019-05-05&nav=PREV / NEXT / CURRENT(또는 빈값. 이외의 값은 에러)
    @GET("/statistics/total")
    Call<CampusStatisticList> getStatisticCampusList(@Query("date") String date, @Query("nav") String nav, @Query("department") String department);

    //-------- GBS 통 계 --------------------------------------------------------------------------------------------------------------//
    //1. 전체 통계
    //GET /statistics/total?date=2019-05-05&nav=PREV / NEXT / CURRENT(또는 빈값. 이외의 값은 에러)
    @GET("/statistics/gbs/total")
    Call<GBSTotalStatisticDatas> getGBSTotalStatistic(@Query("date") String date, @Query("nav") String nav, @Query("department") String department);

    //2. 단계 통계
    //GET /statistics/gbs?date=2020-05-08&nav=CURRENT&gbsId=12
    @GET("/statistics/gbs")
    Call<GBSStepStatisticDatas> getGBSStepStatistic(@Query("date") String date, @Query("nav") String nav, @Query("gbsId") Integer gbsId);


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
