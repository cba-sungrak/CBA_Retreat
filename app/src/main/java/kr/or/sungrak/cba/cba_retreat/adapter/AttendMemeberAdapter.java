package kr.or.sungrak.cba.cba_retreat.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import kr.or.sungrak.cba.cba_retreat.R;
import kr.or.sungrak.cba.cba_retreat.models.AttendInfo;
import kr.or.sungrak.cba.cba_retreat.viewholder.AttendViewHolder;

public class AttendMemeberAdapter extends RecyclerView.Adapter<AttendViewHolder> {
    private List<AttendInfo> mMemberList;

    @NonNull
    @Override
    public AttendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.attend_member_layout, viewGroup, false);
        return new AttendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendViewHolder memberViewHolder, int i) {
        AttendInfo myInfo = mMemberList.get(i);
        memberViewHolder.binding.setAttend(myInfo);
    }


    @Override
    public int getItemCount() {
        if (mMemberList == null) {
            return 0;
        }
        return mMemberList.size();
    }

//    public void updateItems(List<MyInfo> items) {
//        if (this.mMemberList == null) {
//            mMemberList = new ArrayList<>();
//        }
//        this.mMemberList.clear();
//        this.mMemberList.addAll(items);
//
//        notifyDataSetChanged();
//    }
}
