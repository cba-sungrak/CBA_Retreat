package kr.or.sungrak.cba.cba_retreat.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import kr.or.sungrak.cba.cba_retreat.FCM.SendFCM;
import kr.or.sungrak.cba.cba_retreat.R;
import kr.or.sungrak.cba.cba_retreat.common.CBAUtil;
import kr.or.sungrak.cba.cba_retreat.common.Tag;
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
    private CheckBox mIsNotiChk;
    Context mContext;


    public PostDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        switch (CBAUtil.getRetreat(mContext)) {
            case Tag.RETREAT_CBA:
                mDatabase = FirebaseDatabase.getInstance().getReference(Tag.RETREAT_CBA);
                break;
            case Tag.RETREAT_SUNGRAK:
                mDatabase = FirebaseDatabase.getInstance().getReference(Tag.RETREAT_SUNGRAK);
                break;
        }

        mNameField = findViewById(R.id.field_name);
        mBodyField = findViewById(R.id.field_body);
        mSubmitButton = findViewById(R.id.fab_submit_post);
        mIsNotiChk = findViewById(R.id.is_noti_chk);
        MyInfo myInfo = CBAUtil.loadMyInfo(getContext());
//        if (myInfo != null) {
//            mNameField.setText(myInfo.getName());
//            if (myInfo.getGbsLevel().equals("봉사자")) {
//                mIsNotiChk.setVisibility(View.VISIBLE);
//            }
//        }
        mSubmitButton.setOnClickListener(v -> submitPost());
    }

    private void submitPost() {
        final String name = mNameField.getText().toString();
        final String body = mBodyField.getText().toString();
        final boolean isNoti = mIsNotiChk.isChecked();
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
        mDatabase.child(Tag.NOTI).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        writeNewPost(name, body, isNoti);
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
    private void writeNewPost(String username, String body, boolean isNoti) {
        String key = mDatabase.child(Tag.NOTI).push().getKey();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Post post;
        if (isNoti) {
            post = new Post(auth.getUid(), username, body, getCurrentTimeStr(), "공지");
            SendFCM.sendOKhttp(body, mContext);
        } else {
            post = new Post(auth.getUid(), username, body, getCurrentTimeStr(), "NA");
        }
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, postValues);
        mDatabase.child(Tag.NOTI).updateChildren(childUpdates);

    }

    // [END write_fan_out]
    private static String getCurrentTimeStr() {
        long currTime = System.currentTimeMillis();
        java.text.DateFormat finalformat = new java.text.SimpleDateFormat("MM-dd HH:mm");
        return finalformat.format(new Date(currTime));
    }
}