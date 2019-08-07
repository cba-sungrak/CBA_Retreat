package kr.or.sungrak.cba.cba_camp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kr.or.sungrak.cba.cba_camp.R;
import kr.or.sungrak.cba.cba_camp.models.MemberInfo;
import kr.or.sungrak.cba.cba_camp.viewholder.GBSMemberViewHolder;

public class GBSMemberAdapter extends RecyclerView.Adapter<GBSMemberViewHolder> {
    private List<MemberInfo> mMemberList;

    @NonNull
    @Override
    public GBSMemberViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gbs_member_layout, viewGroup, false);
        return new GBSMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GBSMemberViewHolder GBSMemberViewHolder, int i) {
        MemberInfo memberInfo = mMemberList.get(i);
        GBSMemberViewHolder.binding.setMember(memberInfo);
        if (getItemCount() == i) {
            GBSMemberViewHolder.binding.gbsItemLine.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        if (mMemberList == null) {
            return 0;
        }
        return mMemberList.size();
    }

    public void updateItems(List<MemberInfo> items) {
        if (this.mMemberList == null) {
            mMemberList = new ArrayList<>();
        }
        this.mMemberList.clear();
        this.mMemberList.addAll(items);

        notifyDataSetChanged();
    }
}
