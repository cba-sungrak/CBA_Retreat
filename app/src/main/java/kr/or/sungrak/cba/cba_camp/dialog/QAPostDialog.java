package kr.or.sungrak.cba.cba_camp.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import kr.or.sungrak.cba.cba_camp.FCM.SendFCM;
import kr.or.sungrak.cba.cba_camp.R;
import kr.or.sungrak.cba.cba_camp.common.CBAUtil;
import kr.or.sungrak.cba.cba_camp.common.Tag;
import kr.or.sungrak.cba.cba_camp.models.Post;

public class QAPostDialog extends MyProgessDialog {

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
    private String mAuth;
    private String mUid;
    private String mTopic;


    public QAPostDialog(@NonNull Context context, String uid, String auth) {
        super(context);
        mContext = context;
        mAuth = auth;
        mUid = uid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        TextView tv = findViewById(R.id.postTitle);
        mNameField = findViewById(R.id.field_name);
        mBodyField = findViewById(R.id.field_body);
        mSubmitButton = findViewById(R.id.fab_submit_post);
        mIsNotiChk = findViewById(R.id.is_noti_chk);
        mIsNotiChk.setVisibility(View.GONE);
        mBodyField.setHint("메세지를 입력해주세요 ");
        mBodyField.requestFocus();
        switch (CBAUtil.getRetreat(mContext)) {
            case Tag.RETREAT_CBA:
                mDatabase = FirebaseDatabase.getInstance().getReference(Tag.RETREAT_CBA);
                tv.setText("Q&A");
                mTopic = Tag.CBA_ADMIN;
                if (CBAUtil.isAdmin(getContext())) {
                    mNameField.setText(mAuth + " 답장");
                }
                break;
            case Tag.RETREAT_SUNGRAK:
                mDatabase = FirebaseDatabase.getInstance().getReference(Tag.RETREAT_SUNGRAK);
                tv.setText("불편접수");
                mTopic = Tag.SR_ADMIN;
                if (CBAUtil.isAdmin(getContext())) {
                    mNameField.setText(mAuth + " 답장");
                } else {
                    mNameField.setText(mAuth);
                    if (!mAuth.isEmpty()) {
                        mNameField.setFocusable(false);
                    }
                }
                break;
        }
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        mSubmitButton.setOnClickListener(v -> submitPost());
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

        writeNewPost(name, body);
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
        String key = mDatabase.child(Tag.MESSAGE).push().getKey();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Post post;

        if (CBAUtil.isAdmin(getContext())) {
            post = new Post(mUid, username, body, getCurrentTimeStr(), "공지");
        } else {
            post = new Post(mUid, username, body, getCurrentTimeStr(), "");
        }

        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, postValues);
        mDatabase.child(Tag.MESSAGE).updateChildren(childUpdates);

        if (!CBAUtil.isAdmin(getContext())) {
            SendFCM.sendOKhttp("건의사항 " + username, body, mTopic);
        }

    }

    // [END write_fan_out]
    private static String getCurrentTimeStr() {
        long currTime = System.currentTimeMillis();
        java.text.DateFormat finalformat = new java.text.SimpleDateFormat("MM-dd HH:mm");
        return finalformat.format(new Date(currTime));
    }


}