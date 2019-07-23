package kr.or.sungrak.cba.cba_camp.fragment;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.or.sungrak.cba.cba_camp.R;
import kr.or.sungrak.cba.cba_camp.databinding.AttendEditLayoutBinding;

public class AttendEditFragment extends Fragment {

    AttendEditLayoutBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.attend_edit_layout, container, false);
        binding.setFragment(this);
        View rootView = binding.getRoot();



        return rootView;
    }
    public void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.attend_prev_date:
        }
    }

}
