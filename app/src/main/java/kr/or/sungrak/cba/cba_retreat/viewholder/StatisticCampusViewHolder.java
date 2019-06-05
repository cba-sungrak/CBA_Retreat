package kr.or.sungrak.cba.cba_retreat.viewholder;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import kr.or.sungrak.cba.cba_retreat.databinding.CampusStatisticItemBinding;

public class StatisticCampusViewHolder extends RecyclerView.ViewHolder {
    public CampusStatisticItemBinding binding;

    public StatisticCampusViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }
}
