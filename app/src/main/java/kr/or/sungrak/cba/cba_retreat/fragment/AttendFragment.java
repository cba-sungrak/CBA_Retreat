package kr.or.sungrak.cba.cba_retreat.fragment;


import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import kr.or.sungrak.cba.cba_retreat.R;
import kr.or.sungrak.cba.cba_retreat.adapter.AttendMemeberAdapter;
import kr.or.sungrak.cba.cba_retreat.databinding.AttendLayoutBinding;
import kr.or.sungrak.cba.cba_retreat.models.AttendList;
import kr.or.sungrak.cba.cba_retreat.network.ApiService;
import kr.or.sungrak.cba.cba_retreat.network.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class AttendFragment extends Fragment {

    private static final String TAG = "GBSFragment";
    AttendLayoutBinding binding;
    AttendMemeberAdapter mAttendMemeberAdapter;
    RecyclerView mRecyclerView;
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
        mRecyclerView = binding.attendMemberList;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAttendMemeberAdapter = new AttendMemeberAdapter();

        getAttendInfo();

        mRecyclerView.setAdapter(mAttendMemeberAdapter);

        mAttendMemeberAdapter.updateItems(TestDummy().getAttendInfos());
        return rootView;
    }


    private void getAttendInfo() {
        ApiService service = ServiceGenerator.createService(ApiService.class);
        String uid = FirebaseAuth.getInstance().getUid();
        // API 요청.
        String date = "2019-04-07";
        String campus = "천안";
        JSONObject obj = new JSONObject();
        try {
            obj.put("date", date);
            obj.put("campus", campus);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call<AttendList> request = service.getAttendList(obj);

        request.enqueue(new Callback<AttendList>() {
            @Override
            public void onResponse(Call<AttendList> call, Response<AttendList> response) {
                if (response.code() / 100 == 4) {
                    //실패처리
                } else {
                    AttendList as = response.body();
                    mAttendMemeberAdapter.updateItems(as.getAttendInfos());
                }
            }

            @Override
            public void onFailure(Call<AttendList> call, Throwable t) {

            }
        });
    }

    private AttendList TestDummy() {
        Gson gson = new Gson();
        String json = "\n" +
                "{\n" +
                "\"data\": [\n" +
                "            {\n" +
                "            \"id\": 1,\n" +
                "            \"date\": \"2019-05-05\",\n" +
                "            \"name\": \"학생1\",\n" +
                "            \"mobile\": \"01012341234\",\n" +
                "            \"status\": \"ATTENDED\",\n" +
                "            \"note\": null\n" +
                "            },\n" +
                "            {\n" +
                "            \"id\": 2,\n" +
                "            \"date\": \"2019-05-05\",\n" +
                "            \"name\": \"학생2\",\n" +
                "            \"mobile\": \"01012341234\",\n" +
                "            \"status\": \"ABSENT\",\n" +
                "            \"note\": \"테스트 노트\"\n" +
                "            }\n" +
                "]\n" +
                "}";
        AttendList attendList = gson.fromJson(json, AttendList.class);
        Log.e(TAG, "attendList"+attendList);
        return attendList;
    }

}
