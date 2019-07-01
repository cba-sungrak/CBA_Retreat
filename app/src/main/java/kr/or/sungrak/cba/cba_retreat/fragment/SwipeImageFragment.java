package kr.or.sungrak.cba.cba_retreat.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.or.sungrak.cba.cba_retreat.R;

@SuppressLint("ValidFragment")
public class SwipeImageFragment extends Fragment {
    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    String mImage;
    String[] mFragmentNames;

    @SuppressLint("ValidFragment")
    public SwipeImageFragment(String image, String[] FragmentNames) {
        mImage = image;
        mFragmentNames = FragmentNames;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.ui_swipe_layout, container, false);
        if (mImage.equalsIgnoreCase("room")) {
            mPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
                private final ImageViewFragment[] mFragments = new ImageViewFragment[]{
                        new ImageViewFragment(mImage + "1.png"),
                        new ImageViewFragment(mImage + "2.png"),
                        new ImageViewFragment(mImage + "3.png"),
                        new ImageViewFragment(mImage + "4.png"),
                };

                @Override
                public Fragment getItem(int position) {
                    return mFragments[position];
                }

                @Override
                public int getCount() {
                    return mFragments.length;
                }

                @Override
                public CharSequence getPageTitle(int position) {
                    return mFragmentNames[position];
                }
            };
        } else if (mImage.equalsIgnoreCase("gbs_place")) {
            mPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
                private final ImageViewFragment[] mFragments = new ImageViewFragment[]{
                        new ImageViewFragment(mImage + "1.png"),
                        new ImageViewFragment(mImage + "2.png"),
                        new ImageViewFragment(mImage + "3.png"),
                        new ImageViewFragment(mImage + "4.png"),
                };

                @Override
                public Fragment getItem(int position) {
                    return mFragments[position];
                }

                @Override
                public int getCount() {
                    return mFragments.length;
                }

                @Override
                public CharSequence getPageTitle(int position) {
                    return mFragmentNames[position];
                }
            };
        } else if (mImage.equalsIgnoreCase("campus_place")) {
            mPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
                private final ImageViewFragment[] mFragments = new ImageViewFragment[]{
                        new ImageViewFragment(mImage + "1.png"),
                        new ImageViewFragment(mImage + "2.png"),
                        new ImageViewFragment(mImage + "3.png"),
                        new ImageViewFragment(mImage + "4.png"),
                };

                @Override
                public Fragment getItem(int position) {
                    return mFragments[position];
                }

                @Override
                public int getCount() {
                    return mFragments.length;
                }

                @Override
                public CharSequence getPageTitle(int position) {
                    return mFragmentNames[position];
                }
            };
        }
        // Set up the ViewPager with the sections adapter.
        mViewPager = rootView.findViewById(R.id.swipe_view);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return rootView;
    }
}
