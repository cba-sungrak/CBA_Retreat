package kr.or.sungrak.cba.cba_camp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PeriodStatistic {
    @SerializedName("data")
    @Expose
    private List<item> data = null;

    public List<item> getData() {
        return data;
    }

    public void setData(List<item> data) {
        this.data = data;
    }

    public class item {
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
    }
}
