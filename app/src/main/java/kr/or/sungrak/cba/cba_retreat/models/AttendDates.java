package kr.or.sungrak.cba.cba_retreat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AttendDates {
    @SerializedName("dates")
    @Expose
    private List<String> dates = null;

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }
}
