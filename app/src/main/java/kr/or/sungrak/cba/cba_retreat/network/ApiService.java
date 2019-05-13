package kr.or.sungrak.cba.cba_retreat.network;

import org.json.JSONObject;

import java.util.List;

import kr.or.sungrak.cba.cba_retreat.models.AttendList;
import kr.or.sungrak.cba.cba_retreat.models.GBSInfo;
import kr.or.sungrak.cba.cba_retreat.models.MyInfo;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @GET("getMyInfo/{uid}")
    Call<MyInfo> getMemberRepositories(@Path("uid") String uid);

    @GET("getGBSInfo/{uid}")
    Call<GBSInfo> getGBSRepositories(@Path("uid") String uid);

    @GET("leaders/{uid}/campus/list")
    Call<List<String>> getCampusList(@Path("uid") String uid);

    @POST("attendance/list")
    Call<AttendList> getAttendList(@Body JSONObject object);

    @POST("attendance/list/new")
    Call<AttendList> createAttend(@Body JSONObject object);

    @POST("attendance/list/report")
    Call<ResponseBody> postAttend(@Body JSONObject object);
}
