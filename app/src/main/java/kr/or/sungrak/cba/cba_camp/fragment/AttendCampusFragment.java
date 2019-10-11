package kr.or.sungrak.cba.cba_camp.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;

import kr.or.sungrak.cba.cba_camp.R;
import kr.or.sungrak.cba.cba_camp.databinding.AttendCampusLayoutBinding;
import kr.or.sungrak.cba.cba_camp.models.Campus;
import kr.or.sungrak.cba.cba_camp.network.ApiService;
import kr.or.sungrak.cba.cba_camp.network.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendCampusFragment extends Fragment {

    private static final String TAG = "AttendCampusFragment";
    AttendCampusLayoutBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.attend_campus_layout, container, false);
        View rootView = mBinding.getRoot();

        getCampusListInfo();

        return rootView;
    }

    private void getCampusListInfo() {
        ApiService service = ServiceGenerator.createService(ApiService.class);
        String uid = FirebaseAuth.getInstance().getUid();
        // API 요청.
//        Call<List<String>> request = service.getCampusList(uid);
        Call<Campus> request = service.getCampusList("application/json",uid);
        request.enqueue(new Callback<Campus>() {
            @Override
            public void onResponse(Call<Campus> call, Response<Campus> response) {
                Campus campusList = response.body();
                Log.d(TAG,campusList.toString());
                for (String campus : campusList.getNames()) {
                    Button btn = new Button(getContext());
                    btn.setText(campus);
                    final CharSequence campusName = btn.getText();
                    mBinding.checkAttendance.addView(btn);
                    btn.setOnClickListener(view -> {
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, new AttendFragment(campusName)).commit();
                    });
                }
            }

            @Override
            public void onFailure(Call<Campus> call, Throwable t) {
                TextView text = new TextView(getContext());
                text.setText("출석체크 할수 있는 캠퍼스가 없습니다. ");
                mBinding.checkAttendance.addView(text);
                Log.e("OKHttp_ERR", t.getMessage());
            }
        });
    }
}
