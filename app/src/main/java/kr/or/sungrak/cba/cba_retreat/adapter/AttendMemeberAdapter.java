package kr.or.sungrak.cba.cba_retreat.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.or.sungrak.cba.cba_retreat.R;
import kr.or.sungrak.cba.cba_retreat.models.AttendList;
import kr.or.sungrak.cba.cba_retreat.viewholder.AttendViewHolder;

public class AttendMemeberAdapter extends RecyclerView.Adapter<AttendViewHolder> {
    private List<AttendList.AttendInfo> attendInfoList;

    @NonNull
    @Override
    public AttendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.attend_member_layout, viewGroup, false);
        return new AttendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendViewHolder memberViewHolder, int i) {
        AttendList.AttendInfo attendInfo = attendInfoList.get(i);
        memberViewHolder.binding.setAttend(attendInfo);
    }


    @Override
    public int getItemCount() {
        if (attendInfoList == null) {
            return 0;
        }
        return attendInfoList.size();
    }

    public void updateItems(List<AttendList.AttendInfo> items) {
        if (this.attendInfoList == null) {
            attendInfoList = new ArrayList<>();
        }
        this.attendInfoList.clear();
        this.attendInfoList.addAll(items);

        notifyDataSetChanged();
    }
}
