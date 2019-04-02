package kr.or.sungrak.cba.cba_retreat.fragment;


import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import kr.or.sungrak.cba.cba_retreat.R;
import kr.or.sungrak.cba.cba_retreat.adapter.AttendMemeberAdapter;
import kr.or.sungrak.cba.cba_retreat.databinding.AttendLayoutBinding;
import kr.or.sungrak.cba.cba_retreat.models.AttendInfo;
import kr.or.sungrak.cba.cba_retreat.models.DataAttendList;
import kr.or.sungrak.cba.cba_retreat.network.ApiService;
import kr.or.sungrak.cba.cba_retreat.network.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class AttendFragment extends Fragment {

    private static final String TAG = "GBSFragment";
    AttendLayoutBinding binding;
    AttendMemeberAdapter gbsMemeberAdapter;
    RecyclerView recyclerView;
    String mRequestCampusName;

    @SuppressLint("ValidFragment")
    public AttendFragment(CharSequence campusName) {
        mRequestCampusName = campusName.toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.attend_layout, container, false);
        View rootView = binding.getRoot();
        recyclerView = binding.attendMemberList;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        gbsMemeberAdapter = new AttendMemeberAdapter();

        getAttendInfo();

        recyclerView.setAdapter(gbsMemeberAdapter);
        return rootView;
    }


    private void getAttendInfo() {
        ApiService service = ServiceGenerator.createService(ApiService.class);
        String uid = FirebaseAuth.getInstance().getUid();
        // API 요청.
        String date = "2019-04-07";
        Call<DataAttendList> request = service.getAttendList(uid,mRequestCampusName,date);
        request.enqueue(new Callback<DataAttendList>() {
            @Override
            public void onResponse(Call<DataAttendList> call, Response<DataAttendList> response) {
                if (response.code() / 100 == 4) {
                    DataAttendList as = response.body();
                    List<AttendInfo> a = as.AttendInfos();
                } else {
                }
            }

            @Override
            public void onFailure(Call<DataAttendList> call, Throwable t) {

            }
        });
    }

}
