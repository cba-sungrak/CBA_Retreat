package kr.or.sungrak.cba.cba_camp.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import kr.or.sungrak.cba.cba_camp.databinding.StatisticCampusItemBinding;

public class StatisticCampusViewHolder extends RecyclerView.ViewHolder {
    public StatisticCampusItemBinding binding;

    public StatisticCampusViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }
}
