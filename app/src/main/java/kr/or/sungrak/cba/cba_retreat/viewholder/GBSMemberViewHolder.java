package kr.or.sungrak.cba.cba_retreat.viewholder;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import kr.or.sungrak.cba.cba_retreat.databinding.GbsMemberLayoutBinding;

public class GBSMemberViewHolder extends RecyclerView.ViewHolder {
    public GbsMemberLayoutBinding binding;

    public GBSMemberViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }
}
