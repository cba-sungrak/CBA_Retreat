package kr.or.sungrak.cba.cba_retreat.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.or.sungrak.cba.cba_retreat.R;

public class SwipeImageFragment extends Fragment {
    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    public SwipeImageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.ui_swipe_layout, container, false);

        mPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {

            private final ImageViewFragment[] mFragments = new ImageViewFragment[] {
                    new ImageViewFragment("menu.png"),
                    new ImageViewFragment("mealwork.png"),
                    new ImageViewFragment("lecture.png"),
            };
            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }
            @Override
            public int getCount() {
                return mFragments.length;
            }
        };
        // Set up the ViewPager with the sections adapter.
        mViewPager = rootView.findViewById(R.id.swipe_view);
        mViewPager.setAdapter(mPagerAdapter);


        return rootView;
    }
}
