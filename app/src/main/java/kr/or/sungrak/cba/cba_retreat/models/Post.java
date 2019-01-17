package kr.or.sungrak.cba.cba_retreat.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Post {

    public String uid;
    public String author;
    public String message;
    public String time;
    public String isStaff;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String uid, String author, String message, String time, String isStaff) {
        this.uid = uid;
        this.author = author;
        this.message = message;
        this.time = time;
        this.isStaff = isStaff;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("message", message);
        result.put("time", time);
        result.put("isStaff", isStaff);
        return result;
    }
    // [END post_to_map]

}
