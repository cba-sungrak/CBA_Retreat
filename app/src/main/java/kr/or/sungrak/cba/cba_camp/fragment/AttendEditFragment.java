package kr.or.sungrak.cba.cba_camp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

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

import java.util.List;

import kr.or.sungrak.cba.cba_camp.R;
import kr.or.sungrak.cba.cba_camp.adapter.AttendEditMemebeAdapter;
import kr.or.sungrak.cba.cba_camp.databinding.AttendEditLayoutBinding;
import kr.or.sungrak.cba.cba_camp.models.AttendList;
import kr.or.sungrak.cba.cba_camp.network.ApiService;
import kr.or.sungrak.cba.cba_camp.network.ServiceGenerator;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendEditFragment extends Fragment {
    private static final String TAG = "AttendFragment";

    private static final String DELETE = "DELETE";
    private static final String HIDE = "HIDE";
    private static final String SHOW = "SHOW";

    AttendEditLayoutBinding binding;
    RecyclerView mRecyclerView;
    AttendList mAttendEditMemberList;
    AttendEditMemebeAdapter mAttendEditMemberAdapter;
    String mRequestCampus;

    public AttendEditFragment(String campus, AttendList list) {
        mAttendEditMemberList = list;
        mRequestCampus = campus;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.attend_edit_layout, container, false);
        binding.setFragment(this);
        View rootView = binding.getRoot();

        mRecyclerView = binding.attendEditMemberList;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        mAttendEditMemberAdapter = new AttendEditMemebeAdapter();

        mRecyclerView.setAdapter(mAttendEditMemberAdapter);

        mAttendEditMemberAdapter.updateItems(mAttendEditMemberList.getAttendInfos());

        mAttendEditMemberAdapter.setCustomOnItemClickListener(v -> {
            CheckBox cb = (CheckBox) v;
            switch (v.getId()) {
                case R.id.delete_check:
                    if (cb.isChecked())
                        Toast.makeText(getActivity(), "deletecheck", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.hidden_check:
                    if (cb.isChecked())
                        Toast.makeText(getActivity(), "hiddencheck", Toast.LENGTH_SHORT).show();
                    break;

            }
        });
        return rootView;
    }

    private void postEditAttendMemberList() {
        ApiService service = ServiceGenerator.createService(ApiService.class);
        List<AttendList.AttendInfo> attendInfoList = mAttendEditMemberAdapter.getAttendInfoList();

        JSONArray jsonArray = new JSONArray();
        JSONObject obj = new JSONObject();
        try {
            for (AttendList.AttendInfo attendInfo : attendInfoList) {
                JSONObject jsonObject = new JSONObject();
                String action = SHOW;
                if (attendInfo.getDeleted()) {
                    action = DELETE;
                } else if (attendInfo.getHidden()) {
                    action = HIDE;
                }

                jsonObject.put("id", attendInfo.getId());
                jsonObject.put("action", action);
                jsonArray.put(jsonObject);
            }

            obj.put("editActions", jsonArray);
            obj.put("leaderUid", FirebaseAuth.getInstance().getUid());

            Log.d(TAG, obj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Call<ResponseBody> request = service.postEditAttendMember(body);

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

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new AttendFragment(mRequestCampus)).commit();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("OKHttp_ERR", t.getMessage());
            }
        });
    }

    public void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_edit_attend:
                postEditAttendMemberList();
                break;
        }
    }

}
