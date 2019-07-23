package kr.or.sungrak.cba.cba_camp.viewholder;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import kr.or.sungrak.cba.cba_camp.databinding.AttendMemberLayoutBinding;

public class AttendViewHolder extends RecyclerView.ViewHolder {
    public AttendMemberLayoutBinding binding;

    public AttendViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }
}
