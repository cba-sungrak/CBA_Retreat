package kr.or.sungrak.cba.cba_camp.models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.SerializedName;

import java.util.List;

// [START blog_user_class]
@IgnoreExtraProperties
public class GBSInfo {
    // GBSLevel STAFF = 99;
    @SerializedName("gbsLevel")
    String gbsLevel;
    @SerializedName("leader")
    MemberInfo leader;
    @SerializedName("members")
    List<MemberInfo> members;

    public String getGbsLevel() {
        return gbsLevel;
    }

    public MemberInfo getLeader() {
        return leader;
    }

    public List<MemberInfo> getMembers() {
        return members;
    }

}
// [END blog_user_class]
