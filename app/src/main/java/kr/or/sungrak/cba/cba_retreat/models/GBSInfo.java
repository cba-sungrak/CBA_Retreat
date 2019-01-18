package kr.or.sungrak.cba.cba_retreat.models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

import java.util.List;

// [START blog_user_class]
@IgnoreExtraProperties
public class GBSInfo {
      // GBSLevel STAFF = 99;
    @SerializedName("gbsLevel")
    public String gbsLevel;
    @SerializedName("leader")
    public MyInfo leader;
    @SerializedName("members")
    public List<MyInfo> members;

    public String getGbsLevel() {
        return gbsLevel;
    }

    public MyInfo getLeader() {
        return leader;
    }

    public List<MyInfo> getMembers() {
        return members;
    }

}
// [END blog_user_class]