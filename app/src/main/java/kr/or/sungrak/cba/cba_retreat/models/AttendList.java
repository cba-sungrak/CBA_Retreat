package kr.or.sungrak.cba.cba_retreat.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AttendList {
    @SerializedName("data")
    List<AttendInfo> attendInfos;
    public List<AttendInfo> getAttendInfos() {return attendInfos;}

    public class AttendInfo {
        @SerializedName("id")
        int id;
        @SerializedName("name")
        String name;
        @SerializedName("mobile")
        String mobile;
        @SerializedName("status")
        String status;
        @SerializedName("note")
        String note;

        public int getId(){
            return id;
        }
        public String getName() {
            return name;
        }
        public String getMobile() {
            return mobile;
        }
        public String getStatus() {
            return status;
        }
        public String getNote() {
            return note;
        }

    }

}
