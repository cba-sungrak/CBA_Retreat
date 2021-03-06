package kr.or.sungrak.cba.cba_camp.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import androidx.annotation.NonNull;

import kr.or.sungrak.cba.cba_camp.R;

public class MyProgessDialog extends Dialog {
    public ProgressDialog mProgressDialog;
    Context mContext;

    public MyProgessDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage(mContext.getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
