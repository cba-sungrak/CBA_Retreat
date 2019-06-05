package kr.or.sungrak.cba.cba_retreat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CampusStatisticList {
    @SerializedName("data")
    @Expose
    private List<CampusStatisticInfo> data = null;

    public List<CampusStatisticInfo> getData() {
        return data;
    }

    public void setData(List<CampusStatisticInfo> data) {
        this.data = data;
    }

    public class CampusStatisticInfo {
        @SerializedName("campus")
        @Expose
        private String campus;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("registered")
        @Expose
        private Integer registered;
        @SerializedName("attended")
        @Expose
        private Integer attended;

        public String getCampus() {
            return campus;
        }

        public void setCampus(String campus) {
            this.campus = campus;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
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

        public String getPercent(){
            int percent = (int)((double)attended/(double)registered*100.0);
            return Integer.toString(percent)+"%";
        }
    }


}
