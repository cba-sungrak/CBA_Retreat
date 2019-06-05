package kr.or.sungrak.cba.cba_retreat.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.or.sungrak.cba.cba_retreat.CBAUtil;
import kr.or.sungrak.cba.cba_retreat.R;
import kr.or.sungrak.cba.cba_retreat.adapter.StatisticCampusAdapter;
import kr.or.sungrak.cba.cba_retreat.databinding.DateStatisticLayoutBinding;
import kr.or.sungrak.cba.cba_retreat.models.CampusStatisticList;
import kr.or.sungrak.cba.cba_retreat.network.ApiService;
import kr.or.sungrak.cba.cba_retreat.network.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DateStatisticFragment extends Fragment {
    DateStatisticLayoutBinding binding;
    String mSelectedDate;
    RecyclerView mRecyclerView;
    StatisticCampusAdapter mStatisticCampusAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.date_statistic_layout, container, false);
        binding.setFragment(this);
        View rootView = binding.getRoot();
        mSelectedDate = CBAUtil.getCurrentDate();
        binding.attendDate.setText(mSelectedDate);

        mRecyclerView = binding.campusStatisticItem;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mStatisticCampusAdapter = new StatisticCampusAdapter();

        getStatisticCampus(mSelectedDate);
        mRecyclerView.setAdapter(mStatisticCampusAdapter);


        return rootView;
    }

    private void getStatisticCampus(String date) {
        ApiService service = ServiceGenerator.createService(ApiService.class);

        Call<CampusStatisticList> request = service.getStatisticCampusList(date,"Basic YWRtaW46ZGh3bHJybGVoISEh");

        request.enqueue(new Callback<CampusStatisticList>() {
            @Override
            public void onResponse(Call<CampusStatisticList> call, Response<CampusStatisticList> response) {
                if (response.code() / 100 == 4) {
                    Log.e("CBA","fail");
                } else {
                    CampusStatisticList as = response.body();
                    mStatisticCampusAdapter.updateItems(as.getData());
                    binding.attendDate.setText(mSelectedDate);
                }
            }

            @Override
            public void onFailure(Call<CampusStatisticList> call, Throwable t) {

            }
        });
    }

    public void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.attend_prev_date:
        }
    }
}
