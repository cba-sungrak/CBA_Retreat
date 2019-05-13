package kr.or.sungrak.cba.cba_retreat.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kr.or.sungrak.cba.cba_retreat.R;
import kr.or.sungrak.cba.cba_retreat.databinding.AttendCampusLayoutBinding;
import kr.or.sungrak.cba.cba_retreat.network.ApiService;
import kr.or.sungrak.cba.cba_retreat.network.ServiceGenerator;
import retrofit2.Call;

public class AttendCampusFragment extends Fragment {

    private static final String TAG = "AttendCampusFragment";
    AttendCampusLayoutBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.attend_campus_layout, container, false);
        View rootView = binding.getRoot();
//        List<String> campusList = getCampusListInfo();
        List<String> campusList = getCampusListInfoDebug();

        if (!campusList.isEmpty()) {
            for (String campus : campusList) {
                Button btn = new Button(getContext());
                btn.setText(campus);
                final CharSequence campusName = btn.getText();
                binding.checkAttendance.addView(btn);
                btn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, new AttendFragment(campusName)).commit();
                    }
                });
            }
        }
        return rootView;
    }

    private List<String> getCampusListInfo() {
        ApiService service = ServiceGenerator.createService(ApiService.class);
        String uid = FirebaseAuth.getInstance().getUid();
        // API 요청.
        Call<List<String>> request = service.getCampusList(uid);
        try {
            return request.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<String> getCampusListInfoDebug() {
        List<String> campus = new ArrayList<>();
        campus.add("인성경");
        campus.add("천안");
        return campus;
    }
}
