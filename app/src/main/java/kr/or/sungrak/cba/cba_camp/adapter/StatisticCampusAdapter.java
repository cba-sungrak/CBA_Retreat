package kr.or.sungrak.cba.cba_camp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kr.or.sungrak.cba.cba_camp.R;
import kr.or.sungrak.cba.cba_camp.models.CampusStatisticList;
import kr.or.sungrak.cba.cba_camp.viewholder.StatisticCampusViewHolder;

public class StatisticCampusAdapter extends RecyclerView.Adapter<StatisticCampusViewHolder> {
    private List<CampusStatisticList.CampusStatisticInfo> mStatisticCampusList;

    public interface OnItemClickListener {
        void onItemClick(View v, String date, String campusName);
    }

    private OnItemClickListener mListener = null;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setCustomOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public StatisticCampusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.statistic_campus_item, viewGroup, false);
        return new StatisticCampusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatisticCampusViewHolder memberViewHolder, int i) {
        CampusStatisticList.CampusStatisticInfo campusStatisticInfo = mStatisticCampusList.get(i);
        memberViewHolder.binding.setStatistic(campusStatisticInfo);

        memberViewHolder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(v, campusStatisticInfo.getDate(), campusStatisticInfo.getCampus());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mStatisticCampusList == null) {
            return 0;
        }
        return mStatisticCampusList.size();
    }

    public void updateItems(List<CampusStatisticList.CampusStatisticInfo> items) {
        if (this.mStatisticCampusList == null) {
            mStatisticCampusList = new ArrayList<>();
        }
        this.mStatisticCampusList.clear();
        this.mStatisticCampusList.addAll(items);

        notifyDataSetChanged();
    }

    public List<CampusStatisticList.CampusStatisticInfo> getAttendInfoList() {
        return mStatisticCampusList;
    }
}
