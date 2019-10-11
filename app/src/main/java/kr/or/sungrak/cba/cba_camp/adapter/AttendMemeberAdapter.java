package kr.or.sungrak.cba.cba_camp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kr.or.sungrak.cba.cba_camp.R;
import kr.or.sungrak.cba.cba_camp.models.AttendList;
import kr.or.sungrak.cba.cba_camp.viewholder.AttendViewHolder;

public class AttendMemeberAdapter extends RecyclerView.Adapter<AttendViewHolder> {
    private List<AttendList.AttendInfo> mAttendInfoList;

    public interface OnItemClickListener {
        void onItemClick(View v);
    }

    private OnItemClickListener mListener = null ;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setCustomOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }

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

        memberViewHolder.binding.attendStatus.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(v);
            }
        });

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
