package kr.or.sungrak.cba.cba_camp.fragment.attend;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import kr.or.sungrak.cba.cba_camp.R;
import kr.or.sungrak.cba.cba_camp.adapter.StatisticCampusAdapter;
import kr.or.sungrak.cba.cba_camp.common.CBAUtil;
import kr.or.sungrak.cba.cba_camp.databinding.StatisticDateLayoutBinding;
import kr.or.sungrak.cba.cba_camp.models.CampusStatisticList;
import kr.or.sungrak.cba.cba_camp.network.ApiService;
import kr.or.sungrak.cba.cba_camp.network.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kr.or.sungrak.cba.cba_camp.common.Tag.NAVI_CURRENT;
import static kr.or.sungrak.cba.cba_camp.common.Tag.NAVI_NEXT;
import static kr.or.sungrak.cba.cba_camp.common.Tag.NAVI_PREV;

public class DateStatisticFragment extends Fragment {
    StatisticDateLayoutBinding binding;
    String mSelectedDate;
    RecyclerView mRecyclerView;
    StatisticCampusAdapter mStatisticCampusAdapter;
    @SuppressLint("ValidFragment")
    public DateStatisticFragment(String date) {
        mSelectedDate = date;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.statistic_date_layout, container, false);
        binding.setFragment(this);
        View rootView = binding.getRoot();
        if (TextUtils.isEmpty(mSelectedDate)) mSelectedDate = CBAUtil.getCurrentDate();
        binding.statisticDate.setText(mSelectedDate);

        mRecyclerView = binding.campusStatisticItem;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mStatisticCampusAdapter = new StatisticCampusAdapter();

        getStatisticCampus(mSelectedDate, NAVI_CURRENT);
        mRecyclerView.setAdapter(mStatisticCampusAdapter);

        mStatisticCampusAdapter.setCustomOnItemClickListener((v, date, campusName) -> {
            if (!campusName.equalsIgnoreCase("CBA"))
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new AttendFragment(campusName, date)).addToBackStack(null).commit();
        });

        return rootView;
    }

    private void getStatisticCampus(String date, String navi) {
        ApiService service = ServiceGenerator.createService(ApiService.class);

        Call<CampusStatisticList> request = service.getStatisticCampusList(date, navi);

        request.enqueue(new Callback<CampusStatisticList>() {
            @Override
            public void onResponse(Call<CampusStatisticList> call, Response<CampusStatisticList> response) {
                if (response.code() / 100 == 4) {
                    Log.e("CBA","fail");
                } else {
                    CampusStatisticList as = response.body();
                    mStatisticCampusAdapter.updateItems(as.getData());
                    mSelectedDate = as.getData().get(0).getDate();
                    binding.statisticDate.setText(mSelectedDate);
                }
            }

            @Override
            public void onFailure(Call<CampusStatisticList> call, Throwable t) {
                Log.e("OKHttp_ERR", t.getMessage());
            }
        });
    }

    public void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.statistic_prev_date:
                getStatisticCampus(mSelectedDate, NAVI_PREV);
                break;
            case R.id.statistic_next_date:
                getStatisticCampus(mSelectedDate, NAVI_NEXT);
                break;
            case R.id.statistic_date:
                new DatePickerDialog(getContext(), (view, year, monthOfYear, dayOfMonth) -> {
                    try {
                        String selectedTime = String.format("%d-%d-%d", year, monthOfYear + 1,
                                dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                        String date = sdf.format(sdf.parse(selectedTime));
                        mSelectedDate = date;
                        binding.statisticDate.setText(date);
                        getStatisticCampus(date, NAVI_CURRENT);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }, Integer.parseInt(mSelectedDate.split("-")[0]), Integer.parseInt(mSelectedDate.split("-")[1])-1, Integer.parseInt(mSelectedDate.split("-")[2])).show();
                break;
            case R.id.period_statistic:
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new PeriodStatisticFragment()).commit();
                break;
        }
    }
}
