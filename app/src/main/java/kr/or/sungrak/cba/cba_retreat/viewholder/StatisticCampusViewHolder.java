package kr.or.sungrak.cba.cba_retreat.viewholder;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import kr.or.sungrak.cba.cba_retreat.databinding.CampusStatisticItemBinding;

public class StatisticCampusViewHolder extends RecyclerView.ViewHolder {
    public CampusStatisticItemBinding binding;

    public StatisticCampusViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }
}
