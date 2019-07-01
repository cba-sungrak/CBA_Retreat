package kr.or.sungrak.cba.cba_retreat.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.or.sungrak.cba.cba_retreat.R;
import kr.or.sungrak.cba.cba_retreat.models.CampusStatisticList;
import kr.or.sungrak.cba.cba_retreat.viewholder.StatisticCampusViewHolder;

public class StatisticCampusAdapter extends RecyclerView.Adapter<StatisticCampusViewHolder> {
    private List<CampusStatisticList.CampusStatisticInfo> mStatisticCampusList;

    @NonNull
    @Override
    public StatisticCampusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.campus_statistic_item, viewGroup, false);
        return new StatisticCampusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatisticCampusViewHolder memberViewHolder, int i) {
        CampusStatisticList.CampusStatisticInfo campusStatisticInfo = mStatisticCampusList.get(i);
        memberViewHolder.binding.setStatistic(campusStatisticInfo);
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
