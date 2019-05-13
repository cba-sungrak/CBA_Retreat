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
    private List<AttendList.AttendInfo> mAttendInfoList;

    @NonNull
    @Override
    public AttendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.attend_member_layout, viewGroup, false);
        return new AttendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendViewHolder memberViewHolder, int i) {
        AttendList.AttendInfo attendInfo = mAttendInfoList.get(i);
        memberViewHolder.binding.setAttend(attendInfo);
    }


    @Override
    public int getItemCount() {
        if (mAttendInfoList == null) {
            return 0;
        }
        return mAttendInfoList.size();
    }

    public void updateItems(List<AttendList.AttendInfo> items) {
        if (this.mAttendInfoList == null) {
            mAttendInfoList = new ArrayList<>();
        }
        this.mAttendInfoList.clear();
        this.mAttendInfoList.addAll(items);

        notifyDataSetChanged();
    }

    public List<AttendList.AttendInfo> getAttendInfoList() {
        return mAttendInfoList;
    }
}
