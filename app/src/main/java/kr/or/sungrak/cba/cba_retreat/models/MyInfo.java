package kr.or.sungrak.cba.cba_retreat.models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.SerializedName;

// [START blog_user_class]
@IgnoreExtraProperties
public class MyInfo {
    // GBSLevel STAFF = 99;
    @SerializedName("name")
    String name;
    @SerializedName("campus")
    String campus;
    @SerializedName("gbsLevel")
    String gbsLevel;
    @SerializedName("position")
    String position;
    @SerializedName("age")
    String age;
    @SerializedName("mobile")
    String mobile;

    public String getName() {
        return name;
    }

    public String getCampus() {
        return campus;
    }

    public String getGbsLevel() {
        return gbsLevel;
    }

    public String getPosition() {
        return position;
    }

    public String getAge() {
        return age;
    }

    public String getMobile() {
        return mobile;
    }
}
// [END blog_user_class]
