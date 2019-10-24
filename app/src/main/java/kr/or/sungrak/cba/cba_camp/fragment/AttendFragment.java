package kr.or.sungrak.cba.cba_camp.fragment;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import kr.or.sungrak.cba.cba_camp.R;
import kr.or.sungrak.cba.cba_camp.adapter.AttendMemeberAdapter;
import kr.or.sungrak.cba.cba_camp.common.CBAUtil;
import kr.or.sungrak.cba.cba_camp.databinding.AttendLayoutBinding;
import kr.or.sungrak.cba.cba_camp.models.AttendList;
import kr.or.sungrak.cba.cba_camp.network.ApiService;
import kr.or.sungrak.cba.cba_camp.network.ServiceGenerator;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class AttendFragment extends Fragment {
    private static final String TAG = "AttendFragment";
    private static final String NAVI_PREV = "PREV";
    private static final String NAVI_NEXT = "NEXT";
    private static final String NAVI_CURRENT = "CURRENT";
    AttendLayoutBinding binding;
    AttendMemeberAdapter mAttendMemberAdapter;
    RecyclerView mRecyclerView;
    String mRequestCampusName;
    String mSelectedDate;
    AttendList mAttendMemberList;

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
        mSelectedDate = CBAUtil.getCurrentDate();
        binding.attendDate.setText(mSelectedDate);

        mRecyclerView = binding.attendMemberList;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAttendMemberAdapter = new AttendMemeberAdapter();

        getAttendInfo(mSelectedDate, mRequestCampusName, NAVI_CURRENT);

        mRecyclerView.setAdapter(mAttendMemberAdapter);

        mAttendMemberAdapter.setCustomOnItemClickListener(v -> {
            CheckBox cb = (CheckBox) v;
            if (cb.isChecked()) {
                mAttendMemberList.setAttended(mAttendMemberList.getAttended() + 1);
            } else {
                mAttendMemberList.setAttended(mAttendMemberList.getAttended() - 1);
            }
            binding.attendTotal.setText(getString(mAttendMemberList));
        });

        return rootView;
    }


    private void getAttendInfo(String date, String campus, String navi) {
        ApiService service = ServiceGenerator.createService(ApiService.class);

        Call<AttendList> request = service.getAttendList(date, campus, navi);

        request.enqueue(new Callback<AttendList>() {
            @Override
            public void onResponse(Call<AttendList> call, Response<AttendList> response) {
                if (response.code() / 100 == 4) {
                    if (navi.equalsIgnoreCase(NAVI_CURRENT)) {
                        binding.createAttend.setVisibility(getView().VISIBLE);
                        binding.attendMemberList.setVisibility(getView().GONE);
                        binding.confirmAttend.setVisibility(getView().GONE);
                        binding.deleteAttend.setVisibility(getView().GONE);
                    } else {
                        Toast.makeText(getContext(),
                                "출석 내역이 존재 하지 않습니다.", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    binding.createAttend.setVisibility(getView().GONE);
                    binding.attendMemberList.setVisibility(getView().VISIBLE);
                    binding.confirmAttend.setVisibility(getView().VISIBLE);
                    binding.deleteAttend.setVisibility(getView().VISIBLE);

                    mAttendMemberList = response.body();
                    binding.attendTotal.setText(getString(mAttendMemberList));
                    mAttendMemberAdapter.updateItems(mAttendMemberList.getAttendInfos());
                    mSelectedDate = mAttendMemberList.getAttendInfos().get(0).getDate();
                    binding.attendDate.setText(mSelectedDate);
                }
            }

            @Override
            public void onFailure(Call<AttendList> call, Throwable t) {
                Log.e("OKHttp_ERR", t.getMessage());

            }
        });
    }

    @NonNull
    private String getString(AttendList as) {
        int percent = (int) ((double) as.getAttended() / (double) as.getRegistered() * 100.0);
        return "[" + mRequestCampusName + "] 출석 " + as.getAttended() + " / 전체 " + as.getRegistered() + " / " + percent + "%";
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
                    Toast.makeText(getContext(),
                            "출석부가 생성 되었습니다.", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<AttendList> call, Throwable t) {
                Log.e("OKHttp_ERR", t.getMessage());
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
            obj.put("leaderUid", FirebaseAuth.getInstance().getUid());

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
                Log.e("OKHttp_ERR", t.getMessage());
            }
        });
    }

    private void deleteAtteand() {
        ApiService service = ServiceGenerator.createService(ApiService.class);
        JSONObject jsonObject = new JSONObject();
        //  {date: "2019-05-05", campus: "천안", "leaderUid": "9999"}
        try {
            jsonObject.put("date", mSelectedDate);
            jsonObject.put("campus", mRequestCampusName);
            jsonObject.put("leaderUid", FirebaseAuth.getInstance().getUid());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.d(TAG, jsonObject.toString());

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<ResponseBody> request = service.deleteAttend(body);

        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() / 100 == 4) {
                    Toast.makeText(getContext(),
                            "Delete attend failed ", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(getContext(),
                            "Delete attend success", Toast.LENGTH_SHORT)
                            .show();
                }
                getAttendInfo(mSelectedDate, mRequestCampusName, NAVI_CURRENT);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("OKHttp_ERR", t.getMessage());
            }
        });
    }

    public void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.attend_prev_date:
                getAttendInfo(mSelectedDate, mRequestCampusName, NAVI_PREV);
                break;
            case R.id.attend_next_date:
                getAttendInfo(mSelectedDate, mRequestCampusName, NAVI_NEXT);
                break;
            case R.id.attend_date:
                new DatePickerDialog(getContext(), (view, year, monthOfYear, dayOfMonth) -> {
                    try {
                        String selectedTime = String.format("%d-%d-%d", year, monthOfYear + 1,
                                dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                        String date = sdf.format(sdf.parse(selectedTime));
                        mSelectedDate = date;
                        binding.attendDate.setText(date);
                        getAttendInfo(date, mRequestCampusName, NAVI_CURRENT);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }, Integer.parseInt(mSelectedDate.split("-")[0]), Integer.parseInt(mSelectedDate.split("-")[1]) - 1, Integer.parseInt(mSelectedDate.split("-")[2])).show();
                break;
            case R.id.edit_attend:
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new AttendEditFragment(mRequestCampusName, mAttendMemberList)).commit();
                break;
            case R.id.create_attend:
                createAttendList(mSelectedDate, mRequestCampusName);
                break;
            case R.id.confirm_attend:
                postAttendList();
                break;
            case R.id.delete_attend:
                new AlertDialog.Builder(getActivity())
                        .setTitle("출석부 삭제")
                        .setMessage(mSelectedDate + "날짜의 출석부를 삭제 하시겠습니까?")
                        .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                            deleteAtteand();
                        })
                        .setNegativeButton(android.R.string.no, (dialog, whichButton) -> {
                            // 취소시 처리 로직
                            Toast.makeText(getActivity(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
                        })
                        .show();
                break;
        }
    }

}
