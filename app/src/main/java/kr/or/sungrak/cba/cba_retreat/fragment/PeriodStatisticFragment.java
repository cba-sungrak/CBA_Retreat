package kr.or.sungrak.cba.cba_retreat.fragment;

import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import kr.or.sungrak.cba.cba_retreat.R;
import kr.or.sungrak.cba.cba_retreat.databinding.PeriodStatisticLayoutBinding;

public class PeriodStatisticFragment extends Fragment {
    PeriodStatisticLayoutBinding binding;
    String mStartDate;
    String mEndDate;
    int mStartY, mStartM, mStartD;
    int mEndY, mEndM, mEndD;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.period_statistic_layout, container, false);
        View rootView = binding.getRoot();
        binding.spinner.setOnItemSelectedListener(onItemSelectedListener);
        setDate();

        LineChart chart = binding.chart;
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, 1));
        entries.add(new Entry(2, 2));
        entries.add(new Entry(3, 0));
        entries.add(new Entry(4, 4));
        entries.add(new Entry(5, 3));

        LineDataSet dataSet = new LineDataSet(entries, "Label");
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();

        return rootView;
    }

    private void setDate() {
        binding.startBtn.setOnClickListener(onClickListener);
        binding.endBtn.setOnClickListener(onClickListener);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        mStartY = calendar.get(Calendar.YEAR);
        mStartM = calendar.get(Calendar.MONTH);
        mStartD = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, 3);
        mEndY = calendar.get(Calendar.YEAR);
        mEndM = calendar.get(Calendar.MONTH);
        mEndD = calendar.get(Calendar.DAY_OF_MONTH);
        if (TextUtils.isEmpty(mStartDate)) {
            String startTime = String.format("%d/%d/%d", mStartY, mStartM + 1, mStartD);
            mStartDate = startTime;
            binding.startBtn.setText(startTime.replace("/", "."));
        } else {
            binding.startBtn.setText(mStartDate.replace("/", "."));
        }
        if (TextUtils.isEmpty(mEndDate)) {
            String endTime = String.format("%d/%d/%d", mEndY, mEndM + 1, mEndD);
            mEndDate = endTime;
            binding.endBtn.setText(endTime.replace("/", "."));
        } else {
            binding.endBtn.setText(mEndDate.replace("/", "."));
        }
    }

    private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getContext(), "position [" + parent.getItemAtPosition(position).toString() + "]/id[" + id + "]", Toast.LENGTH_SHORT).show();
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
                            String startTime = String.format("%d/%d/%d", year, monthOfYear + 1,
                                    dayOfMonth);
                            mStartDate = startTime;

                            binding.startBtn.setText(startTime.replace("/", "."));

//                            // endBtn text를 설정한 startDate+3달로 변경
//                            GregorianCalendar gCalendar = getCalendar(startTime);
//                            gCalendar.add(Calendar.MONTH, 3);
//                            mEndY = gCalendar.get(Calendar.YEAR);
//                            mEndM = gCalendar.get(Calendar.MONTH);
//                            mEndD = gCalendar.get(Calendar.DAY_OF_MONTH);
//                            String endTime = String.format("%d/%d/%d", mEndY, mEndM + 1, mEndD);
//                            mPlanItem.endTime = endTime;

//                            binding.endBtn.setText(endTime.replace("/", "."));

                        }
                    }, mStartY, mStartM, mStartD).show();
                    break;

                case R.id.endBtn:
                    new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        // DatePickerDialog로 가져온 날짜를 planItem에 String 형태로 저장한다.
                        // 나중에 DB에서 가져올때 /로 파싱해서 가져올예정
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            String endTime = String.format("%d/%d/%d", year, monthOfYear + 1,
                                    dayOfMonth);
                            GregorianCalendar startCalendar = getCalendar(mStartDate);
                            GregorianCalendar endCalendar = getCalendar(endTime);
                            if (endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis() < 0) {
                                Toast.makeText(getContext(), "시작 이후의 날짜를 선택해주세요.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            mEndDate = endTime;
                            binding.endBtn.setText(endTime.replace("/", "."));
                        }
                    }, mEndY, mEndM, mEndD).show();
                    break;
                default:
                    break;
            }
        }
    };

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
}
