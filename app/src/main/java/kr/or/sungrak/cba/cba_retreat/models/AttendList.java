package kr.or.sungrak.cba.cba_retreat.models;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AttendList {
    @SerializedName("data")
    List<AttendInfo> attendInfos;

    public List<AttendInfo> getAttendInfos() {
        return attendInfos;
    }

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
        @SerializedName("date")
        String date;

        public int getId() {
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

        public String getDate() {
            return date;
        }

        public void test(CharSequence s) {
            Log.e("CBA", s.toString());
            this.note = s.toString();
        }

        public void test2(boolean isChecked) {
            if (isChecked) {
                this.status = "ATTENDED";
            } else {
                this.status = "ABSENT";
            }
            Log.e("CBA", this.status);
            return;
        }

    }

}
