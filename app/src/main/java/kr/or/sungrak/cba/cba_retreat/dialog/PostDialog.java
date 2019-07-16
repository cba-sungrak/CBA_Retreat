package kr.or.sungrak.cba.cba_retreat.dialog;

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

import kr.or.sungrak.cba.cba_retreat.FCM.SendFCM;
import kr.or.sungrak.cba.cba_retreat.R;
import kr.or.sungrak.cba.cba_retreat.common.CBAUtil;
import kr.or.sungrak.cba.cba_retreat.common.Tag;
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
    private String mTitle;
    private String mTopic;


    public PostDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        mNameField = findViewById(R.id.field_name);
        mBodyField = findViewById(R.id.field_body);
        mSubmitButton = findViewById(R.id.fab_submit_post);
        mIsNotiChk = findViewById(R.id.is_noti_chk);
        TextView tv = findViewById(R.id.postTitle);
        tv.setText("공지사항");
        mBodyField.setHint("공지사항을 입력해주세요 아래 check 박스를 선택하시면 사용자에게 알림이 울립니다.");
        mBodyField.requestFocus();
        mIsNotiChk.setChecked(true);

        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        switch (CBAUtil.getRetreat(mContext)) {
            case Tag.RETREAT_CBA:
                mDatabase = FirebaseDatabase.getInstance().getReference(Tag.RETREAT_CBA);
                mNameField.setText("CBA 본부");
                mTitle = "예수로 사는 교회";
                break;
            case Tag.RETREAT_SUNGRAK:
                mDatabase = FirebaseDatabase.getInstance().getReference(Tag.RETREAT_SUNGRAK);
                mNameField.setText("몽산포 수련회 진행");
                mTitle = "내 영혼아 교회를 수호하자";
                break;
        }

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
//        // Disable button so there are no multi-posts
//        setEditingEnabled(false);
//        showProgressDialog();
        writeNewPost(name, body, isNoti);
//        setEditingEnabled(true);
//        hideProgressDialog();
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
            post = new Post(auth.getUid(), username, body, getCurrentTimeStr(), "알림공지");
            SendFCM.sendOKhttp(mTitle, body, CBAUtil.getRetreat(mContext));
        } else {
            post = new Post(auth.getUid(), username, body, getCurrentTimeStr(), "공지");
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