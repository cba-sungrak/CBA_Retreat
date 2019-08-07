package kr.or.sungrak.cba.cba_camp.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import kr.or.sungrak.cba.cba_camp.R;
import kr.or.sungrak.cba.cba_camp.common.CBAUtil;
import kr.or.sungrak.cba.cba_camp.models.MyInfo;
import kr.or.sungrak.cba.cba_camp.network.ApiService;
import kr.or.sungrak.cba.cba_camp.network.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class LoginDialog extends MyProgessDialog implements View.OnClickListener {
    private static final String TAG = "LoginDialog";
    private EditText mEmailField;
    private EditText mPasswordField;
    Context mContext;
    boolean mLoginSuccess;
    boolean mNeedToChangeFrament;

    public LoginDialog(Context context, boolean needToChangeFrament) {
        super(context);
        mContext = context;
        mNeedToChangeFrament = needToChangeFrament;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        // Views
        mEmailField = findViewById(R.id.fieldEmail);
        mPasswordField = findViewById(R.id.fieldPassword);
        // Buttons
        findViewById(R.id.emailSignInButton).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        //
        findViewById(R.id.signUp).setOnClickListener(this);
        mLoginSuccess = false;
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }
        showProgressDialog();
        // [START sign_in_with_email]
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) mContext, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            getMyInfo(FirebaseAuth.getInstance().getUid());
                            mLoginSuccess = true;
                            Log.i(TAG, "FirebaseLogin success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(mContext, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            mLoginSuccess = false;
                            dismiss();
                            hideProgressDialog();
                        }

                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;
        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }
        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }
        return valid;
    }

    public void onClick(View v) {
        int i = v.getId();
        switch (v.getId()) {
            case R.id.emailSignInButton:
                signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
                break;
            case R.id.cancel:
                dismiss();
                break;
            case R.id.signUp:
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://cba.sungrak.or.kr:9000")));
                break;
        }
    }

    public boolean changeFrament() {
        return mLoginSuccess && mNeedToChangeFrament;
    }

    public void getMyInfo(String uid) {
        ApiService service = ServiceGenerator.createService(ApiService.class);

        // API 요청.
        Call<MyInfo> request = service.getMyInfo(uid);
        request.enqueue(new Callback<MyInfo>() {
            @Override
            public void onResponse(Call<MyInfo> call, Response<MyInfo> response) {
                Log.i(TAG, "get My info success");
                if (response.code() / 100 == 4) {
                    //error 서버가 켜져 있으나 찾을 수가 없음
                    CBAUtil.signOut(mContext);
                } else {
                    CBAUtil.saveMyInfo(mContext, response);
                }
                dismiss();
                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<MyInfo> call, Throwable t) {
                Log.i(TAG, "faild " + t.getMessage());
                CBAUtil.signOut(mContext);
                dismiss();
                hideProgressDialog();
            }
        });
    }

}
