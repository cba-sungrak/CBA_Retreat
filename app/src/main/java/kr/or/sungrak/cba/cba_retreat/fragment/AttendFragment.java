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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import kr.or.sungrak.cba.cba_retreat.R;
import kr.or.sungrak.cba.cba_retreat.adapter.AttendMemeberAdapter;
import kr.or.sungrak.cba.cba_retreat.databinding.AttendLayoutBinding;
import kr.or.sungrak.cba.cba_retreat.models.AttendDates;
import kr.or.sungrak.cba.cba_retreat.models.AttendList;
import kr.or.sungrak.cba.cba_retreat.network.ApiService;
import kr.or.sungrak.cba.cba_retreat.network.ServiceGenerator;
import okhttp3.MediaType;
import okhttp3.RequestBody;
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
    String mSelectedDate;

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
        mSelectedDate = getCurrentDate();
        binding.attendDate.setText(mSelectedDate);

        mRecyclerView = binding.attendMemberList;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAttendMemberAdapter = new AttendMemeberAdapter();

        getAttendInfo(mSelectedDate, mRequestCampusName);

        mRecyclerView.setAdapter(mAttendMemberAdapter);
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
        RequestBody body =  RequestBody.create(MediaType.parse("application/json"),obj.toString());

        Call<AttendList> request = service.getAttendList(body);

        request.enqueue(new Callback<AttendList>() {
            @Override
            public void onResponse(Call<AttendList> call, Response<AttendList> response) {
                if (response.code() / 100 == 4) {
                    binding.createAttend.setVisibility(getView().VISIBLE);
                    binding.attendMemberList.setVisibility(getView().GONE);
                    binding.confirmAttend.setVisibility(getView().GONE);
                } else {
                    binding.createAttend.setVisibility(getView().GONE);
                    binding.attendMemberList.setVisibility(getView().VISIBLE);
                    binding.confirmAttend.setVisibility(getView().VISIBLE);

                    AttendList as = response.body();
                    mAttendMemberAdapter.updateItems(as.getAttendInfos());
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
        RequestBody body =  RequestBody.create(MediaType.parse("application/json"),obj.toString());
        Call<AttendList> request = service.createAttend(body);

        request.enqueue(new Callback<AttendList>() {
            @Override
            public void onResponse(Call<AttendList> call, Response<AttendList> response) {
                if (response.code() / 100 == 4) {
                    Toast.makeText(getContext(),
                            "failed make attend ", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    binding.createAttend.setVisibility(getView().GONE);
                    binding.attendMemberList.setVisibility(getView().VISIBLE);
                    binding.confirmAttend.setVisibility(getView().VISIBLE);
                    AttendList as = response.body();
                    mAttendMemberAdapter.updateItems(as.getAttendInfos());
                    Toast.makeText(getContext(),
                            "load make attend win", Toast.LENGTH_SHORT)
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
        JSONArray jsonArray = new JSONArray();
        JSONObject obj = new JSONObject();
        try {
            for (AttendList.AttendInfo attendInfo : attendInfoList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", attendInfo.getId());
                jsonObject.put("status", attendInfo.getStatus());
                jsonObject.put("note", attendInfo.getNote());
                jsonArray.put(jsonObject);
            }


            obj.put("checkList", jsonArray);
            obj.put("leaderUid", "9999");

            Log.d(TAG, obj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body =  RequestBody.create(MediaType.parse("application/json"),obj.toString());
        Call<ResponseBody> request = service.postAttend(body);

        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() / 100 == 4) {
                    Toast.makeText(getContext(),
                            "post attend failed ", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(getContext(),
                            "post attend succes", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.attend_prev_btn:
                getAttendDate(mSelectedDate, mRequestCampusName);
                break;
            case R.id.attend_date:
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            String selectedTime = String.format("%d-%d-%d", year, monthOfYear + 1,
                                    dayOfMonth);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                            String date = sdf.format(sdf.parse(selectedTime));
                            mSelectedDate = date;
                            binding.attendDate.setText(date);
                            getAttendInfo(date, mRequestCampusName);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay).show();
                break;
            case R.id.create_attend:
                createAttendList(mSelectedDate, mRequestCampusName);
            case R.id.confirm_attend:
                postAttendList();
                break;
        }
    }

    private void getAttendDate(String date, String campus){
        ApiService service = ServiceGenerator.createService(ApiService.class);
        JSONObject obj = new JSONObject();
        try {
            obj.put("date", date);
            obj.put("campus", campus);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body =  RequestBody.create(MediaType.parse("application/json"),obj.toString());
        final Call<AttendDates> request = service.getAttendDate(body);

        request.enqueue(new Callback<AttendDates>() {
            @Override
            public void onResponse(Call<AttendDates> call, Response<AttendDates> response) {
                AttendDates attendDates = response.body();
                List<String> list = attendDates.getDates();

                Log.d(TAG, list.toString());
            }

            @Override
            public void onFailure(Call<AttendDates> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private String getCurrentDate() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        fmt.setCalendar(calendar);
        return fmt.format(calendar.getTime());
//        return String.format("%d-%d-%d", mYear, mMonth + 1, mDay);
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
