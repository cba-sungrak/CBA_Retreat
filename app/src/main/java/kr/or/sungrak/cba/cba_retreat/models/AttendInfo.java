package kr.or.sungrak.cba.cba_retreat.models;

import com.google.gson.annotations.SerializedName;

public class AttendInfo {
    @SerializedName("name")
    String name;
    @SerializedName("mobile")
    String mobile;
    @SerializedName("active")
    boolean active;
    @SerializedName("status")
    int status;

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public boolean getActive() {
        return active;
    }

    public int getStatus() {
        return status;
    }
}
