package kr.or.sungrak.cba.cba_camp.fragment.camp;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import kr.or.sungrak.cba.cba_camp.MainActivity;
import kr.or.sungrak.cba.cba_camp.R;
import kr.or.sungrak.cba.cba_camp.adapter.GBSMemberAdapter;
import kr.or.sungrak.cba.cba_camp.databinding.GbsLayoutBinding;
import kr.or.sungrak.cba.cba_camp.models.GBSInfo;
import kr.or.sungrak.cba.cba_camp.network.ApiService;
import kr.or.sungrak.cba.cba_camp.network.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampGBSFragment extends Fragment {

    private static final String TAG = "GBSFragment";
    GbsLayoutBinding binding;
    GBSMemberAdapter gbsMemeberAdapter;
    RecyclerView recyclerView;

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.gbs_layout, container, false);
        View rootView = binding.getRoot();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            ((MainActivity) getActivity()).showLoginDialog(true);
        } else {
            recyclerView = binding.gbsMemberList;
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            gbsMemeberAdapter = new GBSMemberAdapter();
            if (loadCampGBSInfo() == null || loadCampGBSInfo().getLeader() == null) {
                getCampGBSInfo();
            } else {
                updateCampGBSInfo();
            }
            recyclerView.setAdapter(gbsMemeberAdapter);
        }

        return rootView;
    }

    private void updateCampGBSInfo() {
        GBSInfo gbsInfo = loadCampGBSInfo();
        if (gbsInfo.getLeader() != null) {
            binding.setGbs(gbsInfo);
            gbsMemeberAdapter.updateItems(gbsInfo.getMembers());
        }

    }

    private void getCampGBSInfo() {
        ApiService service = ServiceGenerator.createService(ApiService.class);
        String uid = FirebaseAuth.getInstance().getUid();
        // API 요청.
        Call<GBSInfo> request = service.getGBSRepositories(uid);
        request.enqueue(new Callback<GBSInfo>() {
            @Override
            public void onResponse(Call<GBSInfo> call, Response<GBSInfo> response) {
                if (response.code() / 100 == 4) {
                    //error 서버가 켜져 있으나 찾을 수가 없음
                } else {
                    if (response.body().getLeader() == null) {
                        //조배치 되지 않음
                    } else {
                        saveCampGBSInfo(response);
                        updateCampGBSInfo();
                    }
                }
            }

            @Override
            public void onFailure(Call<GBSInfo> call, Throwable t) {

            }
        });
    }

    private void saveCampGBSInfo(Response<GBSInfo> response) {
        Gson gson = new Gson();
        String myInfo = gson.toJson(response.body());
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("GBSInfo", myInfo);
        editor.commit();
    }

    private GBSInfo loadCampGBSInfo() {
        Gson gson = new Gson();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String json = pref.getString("GBSInfo", "");
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        Log.i(TAG, "/// " + json);
        return gson.fromJson(json, GBSInfo.class);
    }
}
