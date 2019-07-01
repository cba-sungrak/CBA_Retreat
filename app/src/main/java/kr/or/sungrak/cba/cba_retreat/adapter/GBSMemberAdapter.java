package kr.or.sungrak.cba.cba_retreat.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.or.sungrak.cba.cba_retreat.R;
import kr.or.sungrak.cba.cba_retreat.models.MyInfo;
import kr.or.sungrak.cba.cba_retreat.viewholder.GBSMemberViewHolder;

public class GBSMemberAdapter extends RecyclerView.Adapter<GBSMemberViewHolder> {
    private List<MyInfo> mMemberList;

    @NonNull
    @Override
    public GBSMemberViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gbs_member_layout, viewGroup, false);
        return new GBSMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GBSMemberViewHolder GBSMemberViewHolder, int i) {
        MyInfo myInfo = mMemberList.get(i);
        GBSMemberViewHolder.binding.setMember(myInfo);
    }


    @Override
    public int getItemCount() {
        if (mMemberList == null) {
            return 0;
        }
        return mMemberList.size();
    }

    public void updateItems(List<MyInfo> items) {
        if (this.mMemberList == null) {
            mMemberList = new ArrayList<>();
        }
        this.mMemberList.clear();
        this.mMemberList.addAll(items);

        notifyDataSetChanged();
    }
}
