package kr.or.sungrak.cba.cba_camp.models;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AttendList {
    @SerializedName("data")
    @Expose
    private List<AttendInfo> AttendInfos = null;
    @SerializedName("registered")
    @Expose
    private Integer registered;
    @SerializedName("attended")
    @Expose
    private Integer attended;

    public List<AttendInfo> getAttendInfos() {
        return AttendInfos;
    }

    public void setAttendInfos(List<AttendInfo> AttendInfos) {
        this.AttendInfos = AttendInfos;
    }

    public Integer getRegistered() {
        return registered;
    }

    public void setRegistered(Integer registered) {
        this.registered = registered;
    }

    public Integer getAttended() {
        return attended;
    }

    public void setAttended(Integer attended) {
        this.attended = attended;
    }

    public class AttendInfo {
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("note")
        @Expose
        private String note;
        @SerializedName("attended")
        @Expose
        private Boolean attended;

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

        public void setNote(CharSequence s) {
            Log.e("CBA", s.toString());
            this.note = s.toString();
        }

        public void setChecked(boolean isChecked) {
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
