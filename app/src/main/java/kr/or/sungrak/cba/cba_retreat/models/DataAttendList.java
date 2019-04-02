package kr.or.sungrak.cba.cba_retreat.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataAttendList {
    @SerializedName("data")
    List<AttendInfo> data;

    public List<AttendInfo> AttendInfos() {
        return data;
    }
}
