package kr.or.sungrak.cba.cba_retreat.models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.SerializedName;

// [START blog_user_class]
@IgnoreExtraProperties
public class MyInfo {
    // GBSLevel STAFF = 99;
    @SerializedName("name")
    public String name;
    @SerializedName("campus")
    public String campus;
    @SerializedName("gbsLevel")
    public String gbsLevel;
    @SerializedName("position")
    public String position;
    @SerializedName("age")
    public String age;
    @SerializedName("mobile")
    public String mobile;

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
