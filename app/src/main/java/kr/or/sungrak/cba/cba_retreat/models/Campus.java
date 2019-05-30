package kr.or.sungrak.cba.cba_retreat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Campus {
    @SerializedName("names")
    @Expose
    private List<String> names = null;

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

}
