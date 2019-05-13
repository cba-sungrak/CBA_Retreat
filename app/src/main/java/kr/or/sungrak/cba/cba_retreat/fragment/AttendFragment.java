package kr.or.sungrak.cba.cba_retreat.fragment;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import kr.or.sungrak.cba.cba_retreat.R;
import kr.or.sungrak.cba.cba_retreat.adapter.AttendMemeberAdapter;
import kr.or.sungrak.cba.cba_retreat.databinding.AttendLayoutBinding;
import kr.or.sungrak.cba.cba_retreat.models.AttendList;
import kr.or.sungrak.cba.cba_retreat.network.ApiService;
import kr.or.sungrak.cba.cba_retreat.network.ServiceGenerator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class AttendFragment extends Fragment {

    private static final String TAG = "GBSFragment";
    AttendLayoutBinding binding;
    AttendMemeberAdapter mAttendMemberAdapter;
    RecyclerView mRecyclerView;
    String mRequestCampusName;
    int mYear, mMonth, mDay;

    @SuppressLint("ValidFragment")
    public AttendFragment(CharSequence campusName) {
        mRequestCampusName = campusName.toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.attend_layout, container, false);
        binding.setFragment(this);
        View rootView = binding.getRoot();

//        binding.createAttend.setVisibility(getView().GONE);
        binding.attendDate.setText(getCurrentDate());
//        binding.attendDate.setOnClickListener(onClickListener);

        mRecyclerView = binding.attendMemberList;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAttendMemberAdapter = new AttendMemeberAdapter();

        getAttendInfo(getCurrentDate(), mRequestCampusName);

        mRecyclerView.setAdapter(mAttendMemberAdapter);

        mAttendMemberAdapter.updateItems(TestDummy().getAttendInfos());
        return rootView;
    }


    private void getAttendInfo(String date, String campus) {
        ApiService service = ServiceGenerator.createService(ApiService.class);

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
                    binding.createAttend.setVisibility(getView().VISIBLE);
                    binding.attendMemberList.setVisibility(getView().GONE);
                    Toast.makeText(getContext(),
                            "load attend fail", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    binding.createAttend.setVisibility(getView().GONE);
                    binding.attendMemberList.setVisibility(getView().VISIBLE);
                    AttendList as = response.body();
                    mAttendMemberAdapter.updateItems(as.getAttendInfos());
                    Toast.makeText(getContext(),
                            "load attedn win", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<AttendList> call, Throwable t) {

            }
        });
    }
    private void createAttendList(String date, String campus){
        ApiService service = ServiceGenerator.createService(ApiService.class);
        JSONObject obj = new JSONObject();
        try {
            obj.put("date", date);
            obj.put("campus", campus);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call<AttendList> request = service.createAttend(obj);

        request.enqueue(new Callback<AttendList>() {
            @Override
            public void onResponse(Call<AttendList> call, Response<AttendList> response) {
                if (response.code() / 100 == 4) {
                } else {
                    binding.createAttend.setVisibility(getView().GONE);
                    binding.attendMemberList.setVisibility(getView().VISIBLE);
                    AttendList as = response.body();
                    mAttendMemberAdapter.updateItems(as.getAttendInfos());
                    Toast.makeText(getContext(),
                            "load attedn win", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<AttendList> call, Throwable t) {

            }
        });
    }

    private void postAttendList(){
        ApiService service = ServiceGenerator.createService(ApiService.class);

        List<AttendList.AttendInfo> attendInfoList = mAttendMemberAdapter.getAttendInfoList();
        for (AttendList.AttendInfo attendInfo : attendInfoList) {
            Log.e(TAG, "id " + attendInfo.getId());
            Log.e(TAG, "status " + attendInfo.getStatus());
            Log.e(TAG, "note " + attendInfo.getNote());
        }
        JSONObject obj = new JSONObject();
        Call<ResponseBody> request = service.postAttend(obj);

    }

    public void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.attend_date:
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String startTime = String.format("%d-%d-%d", year, monthOfYear + 1,
                                dayOfMonth);
                        binding.attendDate.setText(startTime);

                    }
                }, mYear, mMonth, mDay).show();
                break;
            case R.id.create_attend:
                createAttendList(getCurrentDate(), mRequestCampusName);
            case R.id.confirm_attend:
                postAttendList();
                break;
        }
    }

    private String getCurrentDate() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        return String.format("%d-%d-%d", mYear, mMonth + 1, mDay);
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
        Log.e(TAG, "attendList" + attendList);
        return attendList;
    }

}
