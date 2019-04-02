package kr.or.sungrak.cba.cba_retreat.network;

import java.util.List;

import kr.or.sungrak.cba.cba_retreat.models.DataAttendList;
import kr.or.sungrak.cba.cba_retreat.models.GBSInfo;
import kr.or.sungrak.cba.cba_retreat.models.MyInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("getMyInfo/{uid}")
    Call<MyInfo> getMemberRepositories(@Path("uid") String uid);

    @GET("getGBSInfo/{uid}")
    Call<GBSInfo> getGBSRepositories(@Path("uid") String uid);

    @GET("leaders/{uid}/campus/list")
    Call<List<String>> getCampusList(@Path("uid") String uid);

    @GET("leaders/{uid}/campus/{name}/attend?")
    ///leaders/{uid}/campus/{name}/attend?date="yyyy-mm-dd"
    Call<DataAttendList> getAttendList(@Path("uid") String uid, @Path("name") String name, @Query("date") String date);
}
