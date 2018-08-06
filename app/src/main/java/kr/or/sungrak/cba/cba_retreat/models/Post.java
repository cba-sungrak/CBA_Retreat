package kr.or.sungrak.cba.cba_retreat.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Post {

    public String uid;
    public String author;
    public String title;
    public String message;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String uid, String author, String title, String message) {
        this.uid = uid;
        this.author = author;
        this.title = title;
        this.message = message;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("message", message);
        return result;
    }
    // [END post_to_map]

}
