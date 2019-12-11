package kr.or.sungrak.cba.cba_camp.fragment.attend;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import kr.or.sungrak.cba.cba_camp.R;
import kr.or.sungrak.cba.cba_camp.databinding.StatisticPeriodLayoutBinding;
import kr.or.sungrak.cba.cba_camp.models.PeriodStatistic;
import kr.or.sungrak.cba.cba_camp.network.ApiService;
import kr.or.sungrak.cba.cba_camp.network.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeriodStatisticFragment extends Fragment {
    StatisticPeriodLayoutBinding binding;
    String mStartDate;
    String mEndDate;
    String mSelectedCampus;
    int mStartY, mStartM, mStartD;
    int mEndY, mEndM, mEndD;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.statistic_period_layout, container, false);
        binding.setFragment(this);
        View rootView = binding.getRoot();
        binding.spinner.setOnItemSelectedListener(onItemSelectedListener);
        setDate();
        getPeriodSatistic(mStartDate, mEndDate, "천안");
        return rootView;
    }

    private void setChart(final List<PeriodStatistic.item> items) {
        LineChart chart = binding.chart;
        List<Entry> entries = new ArrayList<>();
        final ArrayList<String> labels = new ArrayList<>();
        float i = 0;
        for (PeriodStatistic.item item : items) {
            double percent = (double) item.getAttended() / (double) item.getRegistered() * 100.0;
            entries.add(new Entry(i++, (float) percent));
            labels.add(item.getDate().substring(5));
        }
        LineDataSet dataSet = new LineDataSet(entries, mSelectedCampus);
        dataSet.setLineWidth(2);
        dataSet.setCircleRadius(2);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        LineData lineData = new LineData(dataSet);


        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        YAxis yRAxis = chart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);


        chart.setPinchZoom(false);
        chart.setHighlightPerTapEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setDescription(null);

        chart.setData(lineData);
        chart.invalidate();
    }

    private void setDate() {
        binding.startBtn.setOnClickListener(onClickListener);
        binding.endBtn.setOnClickListener(onClickListener);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        mEndY = calendar.get(Calendar.YEAR);
        mEndM = calendar.get(Calendar.MONTH);
        mEndD = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, -1);
        mStartY = calendar.get(Calendar.YEAR);
        mStartM = calendar.get(Calendar.MONTH);
        mStartD = calendar.get(Calendar.DAY_OF_MONTH);
        try {
            if (TextUtils.isEmpty(mStartDate)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                mStartDate = sdf.format(sdf.parse(String.format("%d-%d-%d", mStartY, mStartM + 1,
                        mStartD)));
                binding.startBtn.setText(mStartDate);
            } else {
                binding.startBtn.setText(mStartDate);
            }
            if (TextUtils.isEmpty(mEndDate)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                mEndDate = sdf.format(sdf.parse(String.format("%d-%d-%d", mEndY, mEndM + 1,
                        mEndD)));
                binding.endBtn.setText(mEndDate);
            } else {
                binding.endBtn.setText(mEndDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mSelectedCampus = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.startBtn:
                    // DatePickerDialog로 가져온 날짜를 planItem에 String 형태로 저장한다.
                    // 나중에 DB에서 가져올때 /로 파싱해서 가져올예정
                    new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            try {
                                String selectedTime = String.format("%d-%d-%d", year, monthOfYear + 1,
                                        dayOfMonth);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                                String date = sdf.format(sdf.parse(selectedTime));
                                mStartDate = date;
                                binding.startBtn.setText(mStartDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, mStartY, mStartM, mStartD).show();
                    break;

                case R.id.endBtn:
                    new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        // DatePickerDialog로 가져온 날짜를 planItem에 String 형태로 저장한다.
                        // 나중에 DB에서 가져올때 /로 파싱해서 가져올예정
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            try {
                                String endTime = String.format("%d-%d-%d", year, monthOfYear + 1,
                                        dayOfMonth);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                                String date = sdf.format(sdf.parse(endTime));
//                                if (endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis() < 0) {
//                                    Toast.makeText(getContext(), "시작 이후의 날짜를 선택해주세요.", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
                                mEndDate = date;
                                binding.endBtn.setText(mEndDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, mEndY, mEndM, mEndD).show();
                    break;
                default:
                    break;
            }
        }
    };

    public void onButtonClick(View v) {
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(new Date());
            mEndY = calendar.get(Calendar.YEAR);
            mEndM = calendar.get(Calendar.MONTH);
            mEndD = calendar.get(Calendar.DAY_OF_MONTH);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
            mEndDate = sdf.format(sdf.parse(String.format("%d-%d-%d", mEndY, mEndM + 1, mEndD)));

            switch (v.getId()) {
                case R.id.month_btn:
                    calendar.add(Calendar.MONTH, -1);
                    break;
                case R.id.month3_btn:
                    calendar.add(Calendar.MONTH, -3);
                    break;
                case R.id.month6_btn:
                    calendar.add(Calendar.MONTH, -6);
                    break;
                case R.id.year_btn:
                    calendar.add(Calendar.YEAR, -1);
                    break;
            }

            mStartY = calendar.get(Calendar.YEAR);
            mStartM = calendar.get(Calendar.MONTH);
            mStartD = calendar.get(Calendar.DAY_OF_MONTH);

            mStartDate = sdf.format(sdf.parse(String.format("%d-%d-%d", mStartY, mStartM + 1, mStartD)));

            binding.startBtn.setText(mStartDate);
            binding.endBtn.setText(mEndDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void onConfirmClick(View v) {
        getPeriodSatistic(mStartDate, mEndDate, mSelectedCampus);
    }

    public GregorianCalendar getCalendar(String startTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date endDate = null;
        try {
            endDate = formatter.parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(endDate);
        return calendar;
    }

    private void getPeriodSatistic(String from, String to, String campus) {
        ApiService service = ServiceGenerator.createService(ApiService.class);

        Call<PeriodStatistic> request = service.getPeriodSatistic(from, to, campus);

        request.enqueue(new Callback<PeriodStatistic>() {
            @Override
            public void onResponse(Call<PeriodStatistic> call, Response<PeriodStatistic> response) {
                if (response.code() / 100 == 4) {
                    Log.e("CBA", "fail");
                } else {
                    PeriodStatistic as = response.body();
                    setChart(as.getData());
                }
            }

            @Override
            public void onFailure(Call<PeriodStatistic> call, Throwable t) {
                Log.e("OKHttp_ERR", t.getMessage());
            }
        });
    }

}
