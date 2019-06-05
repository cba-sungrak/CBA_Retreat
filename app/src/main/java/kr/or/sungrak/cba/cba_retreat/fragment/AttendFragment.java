package kr.or.sungrak.cba.cba_retreat.fragment;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
    List<String> mDates;
    int mDateIndex = -1;


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

        getAttendDate(mSelectedDate, mRequestCampusName);

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
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());

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
                    binding.attendTotal.setText(getString(as));
                    mAttendMemberAdapter.updateItems(as.getAttendInfos());
                    binding.attendDate.setText(mSelectedDate);
                }
            }

            @Override
            public void onFailure(Call<AttendList> call, Throwable t) {

            }
        });
    }

    @NonNull
    private String getString(AttendList as) {
        List<AttendList.AttendInfo> list = as.getAttendInfos();
        int total = list.size();
        int attendCount = 0;
        for(AttendList.AttendInfo a : list){
            if(a.getStatus().equalsIgnoreCase("ATTENDED")){
                attendCount++;
            }
        }
        int percent = (int)((double)attendCount/(double)total*100.0);
        return mRequestCampusName + " 출석 " + attendCount + " / 전체 " + total + " / " + percent + "%";
    }

    private void createAttendList(String date, String campus) {
        ApiService service = ServiceGenerator.createService(ApiService.class);
        JSONObject obj = new JSONObject();
        try {
            obj.put("date", date);
            obj.put("campus", campus);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
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
                    getAttendDate(mSelectedDate, mRequestCampusName);
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

    private void postAttendList() {
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

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
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
            case R.id.attend_prev_date:
                if (mDates == null) {
                    mDates = loadGBSInfo().getDates();
                }
                int current = mDates.indexOf(mSelectedDate);
                if (current == 0) {
//                    if(mDates.size()==1){
//                        Toast.makeText(getActivity(), "더이상 출석부가 없습니다.", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    getAttendDate(mSelectedDate, mRequestCampusName);
//                    mDates = null;
//                    Toast.makeText(getActivity(), "출석부 날짜를 다시 가져 옵니다. 잠시 후 다시 눌러주세요", Toast.LENGTH_SHORT).show();

                    Toast.makeText(getActivity(), "더이상 출석부가 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (current == -1) {
                    for (String s : mDates) {
                        if (s.compareTo(mSelectedDate) < 0) {
                            current = mDates.indexOf(s);
                        }
                    }
                    if (current == -1) {
                        Toast.makeText(getActivity(), "더이상 출석부가 없습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    current++;
                }

                mSelectedDate = mDates.get(current - 1);
                getAttendInfo(mSelectedDate, mRequestCampusName);
                break;
            case R.id.attend_next_date:
                if (mDates == null) {
                    Toast.makeText(getActivity(), "출석부가 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                int next = mDates.indexOf(mSelectedDate);
                if (next >= mDates.size() - 1) {
                    Toast.makeText(getActivity(), "출석부가 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (next == -1) {
                    String temp = "";
                    for (String s : mDates) {
                        if (s.compareTo(mSelectedDate) > 0) {
                            temp = s;
                            break;
                        }
                    }
                    if (TextUtils.isEmpty(temp)) {
                        Toast.makeText(getActivity(), "더이상 출석부가 없습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mSelectedDate = temp;
                    getAttendInfo(mSelectedDate, mRequestCampusName);
                } else {
                    mSelectedDate = mDates.get(next + 1);
                    getAttendInfo(mSelectedDate, mRequestCampusName);
                }


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
            case R.id.edit_attend:

                break;
            case R.id.create_attend:
                createAttendList(mSelectedDate, mRequestCampusName);
                break;
            case R.id.confirm_attend:
                postAttendList();
                break;
        }
    }

    private void getAttendDate(String date, String campus) {
        ApiService service = ServiceGenerator.createService(ApiService.class);
        JSONObject obj = new JSONObject();
        try {
            obj.put("date", date);
            obj.put("campus", campus);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        final Call<AttendDates> request = service.getAttendDate(body);

        request.enqueue(new Callback<AttendDates>() {
            @Override
            public void onResponse(Call<AttendDates> call, Response<AttendDates> response) {
                AttendDates attendDates = response.body();
                saveDateList(attendDates);
                mDates = loadGBSInfo().getDates();
            }

            @Override
            public void onFailure(Call<AttendDates> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void saveDateList(AttendDates attendDates) {
        Gson gson = new Gson();
        String myInfo = gson.toJson(attendDates);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("AttendDates", myInfo);
        editor.commit();
    }

    private AttendDates loadGBSInfo() {
        Gson gson = new Gson();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String json = pref.getString("AttendDates", "");
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        Log.i(TAG, "/// " + json);
        return gson.fromJson(json, AttendDates.class);
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
