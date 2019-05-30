package kr.or.sungrak.cba.cba_retreat.network;

import kr.or.sungrak.cba.cba_retreat.models.AttendList;
import kr.or.sungrak.cba.cba_retreat.models.Campus;
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

public interface ApiService {
    @GET("getMyInfo/{uid}")
    Call<MyInfo> getMemberRepositories(@Path("uid") String uid);

    @GET("getGBSInfo/{uid}")
    Call<GBSInfo> getGBSRepositories(@Path("uid") String uid);

    @GET("leaders/{uid}/campus/list")
    Call<Campus> getCampusList(@Header("Content-Type") String content_type, @Path("uid") String uid);

    @POST("attendance/list")
    Call<AttendList> getAttendList(@Body RequestBody body);

    @POST("attendance/list/new")
    Call<AttendList> createAttend(@Body RequestBody body);

    @POST("attendance/list/report")
    Call<ResponseBody> postAttend(@Body RequestBody body);

    @POST("/attendance/dates")
    Call<ResponseBody> getAttendDate(@Body RequestBody body);
}
