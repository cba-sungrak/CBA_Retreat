package kr.or.sungrak.cba.cba_retreat.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import kr.or.sungrak.cba.cba_retreat.R;
import kr.or.sungrak.cba.cba_retreat.models.GBSInfo;
import kr.or.sungrak.cba.cba_retreat.models.MyInfo;
import kr.or.sungrak.cba.cba_retreat.network.ApiService;
import kr.or.sungrak.cba.cba_retreat.network.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GBSFragment extends Fragment {

    private static final String TAG = "GBSFragment";
    private TextView mlname;
    private TextView mlage;
    private TextView mlcampus;
    private TextView mMname;
    private TextView mMage;
    private TextView mMcampus;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.gbs_layout, container, false);
        mlname = rootView.findViewById(R.id.gbs_leader_name);
        mlage = rootView.findViewById(R.id.gbs_leader_age);
        mlcampus = rootView.findViewById(R.id.gbs_leader_campus);
        mMname = rootView.findViewById(R.id.gbs_member_name);
        mMage = rootView.findViewById(R.id.gbs_member_age);
        mMcampus = rootView.findViewById(R.id.gbs_member_campus);


        ApiService service = ServiceGenerator.createService(ApiService.class);

        String uid  = FirebaseAuth.getInstance().getUid();

        // API 요청.
        Call<GBSInfo> request = service.getGBSRepositories(uid);
        request.enqueue(new Callback<GBSInfo>() {
            @Override
            public void onResponse(Call<GBSInfo> call, Response<GBSInfo> response) {
                Log.i(TAG, "reponse" + response.body().toString());
                mlname.setText(response.body().getLeader().getName());
                mlage.setText(response.body().getLeader().getAge());
                mlcampus.setText(response.body().getLeader().getCampus());
            }

            @Override
            public void onFailure(Call<GBSInfo> call, Throwable t) {

            }


        });

        return rootView;
    }
}
