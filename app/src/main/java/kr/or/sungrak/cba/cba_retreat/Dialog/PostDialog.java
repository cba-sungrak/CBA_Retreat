package kr.or.sungrak.cba.cba_retreat.Dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import kr.or.sungrak.cba.cba_retreat.R;
import kr.or.sungrak.cba.cba_retreat.CBAUtil;
import kr.or.sungrak.cba.cba_retreat.models.MyInfo;
import kr.or.sungrak.cba.cba_retreat.models.Post;

public class PostDialog extends MyProgessDialog {

    private static final String TAG = "NewPostDialog";
    private static final String REQUIRED = "Required";

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    private EditText mNameField;
    private EditText mBodyField;
    private Button mSubmitButton;
    Context mContext;


    public PostDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        mNameField = findViewById(R.id.field_name);
        mBodyField = findViewById(R.id.field_body);
        mSubmitButton = findViewById(R.id.fab_submit_post);
        MyInfo myInfo = CBAUtil.loadMyInfo(getContext());
        if (myInfo != null) {
            mNameField.setText(myInfo.getName());
        }
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });
    }

    private void submitPost() {
        final String name = mNameField.getText().toString();
        final String body = mBodyField.getText().toString();
        // Title is required
        if (TextUtils.isEmpty(name)) {
            mNameField.setError(REQUIRED);
            return;
        }
        // Body is required
        if (TextUtils.isEmpty(body)) {
            mBodyField.setError(REQUIRED);
            return;
        }
        // Disable button so there are no multi-posts
        setEditingEnabled(false);
        showProgressDialog();

        // [START single_value_read]
        mDatabase.child("2019messages").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        writeNewPost(name, body);
                        // Finish this Activity, back to the stream
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
        setEditingEnabled(true);
        hideProgressDialog();
        dismiss();
        // [END single_value_read]
    }

    private void setEditingEnabled(boolean enabled) {
        mNameField.setEnabled(enabled);
        mBodyField.setEnabled(enabled);
        if (enabled) {
            mSubmitButton.setVisibility(View.VISIBLE);
        } else {
            mSubmitButton.setVisibility(View.GONE);

        }
    }

    // [START write_fan_out]
    private void writeNewPost(String username, String body) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("2019messages").push().getKey();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Post post;
        if (auth.getCurrentUser() == null) {
            post = new Post("NOTlogin", username, body, getCurrentTimeStr(), "NOTlogin");
        } else {
            MyInfo myInfo = CBAUtil.loadMyInfo(getContext());
            if (myInfo != null) {
                post = new Post(auth.getUid(), username, body, getCurrentTimeStr(), myInfo.getGbsLevel());
            } else {
                post = new Post(auth.getUid(), username, body, getCurrentTimeStr(), "NA");
            }

        }
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/2019messages/" + key, postValues);
        //        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }

    // [END write_fan_out]
    private static String getCurrentTimeStr() {
        long currTime = System.currentTimeMillis();
        java.text.DateFormat finalformat = new java.text.SimpleDateFormat("MM-dd HH:mm");
        return finalformat.format(new Date(currTime));
    }
}