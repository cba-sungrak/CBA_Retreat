package kr.or.sungrak.cba.cba_retreat.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import kr.or.sungrak.cba.cba_retreat.MainActivity;
import kr.or.sungrak.cba.cba_retreat.R;

@SuppressLint("ValidFragment")
public class SwipeImageFragment extends Fragment {
    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    String mImage;
    ArrayList<String> mFragmentNames;
    ArrayList<ImageViewFragment> mFragments;

    @SuppressLint("ValidFragment")
    public SwipeImageFragment(String image) {
        mImage = image;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.ui_swipe_layout, container, false);

        HashMap<String, String> imageMap = ((MainActivity) getActivity()).loadImage(mImage);
        mFragments = new ArrayList<>();
        mFragmentNames = new ArrayList<>();
        for (String key : imageMap.keySet()) {
            mFragmentNames.add(key);
            mFragments.add(new ImageViewFragment(imageMap.get(key)));
        }
        Collections.reverse(mFragmentNames);
        Collections.reverse(mFragments);

        mPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames.get(position);
            }


        };

        mViewPager = rootView.findViewById(R.id.swipe_view);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        if (imageMap.size() == 1) {
            tabLayout.setVisibility(View.GONE);
        } else {
            tabLayout.setVisibility(View.VISIBLE);
        }

        return rootView;
//
//
//
//
//        if (mImage.equalsIgnoreCase("room")) {
//            mPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
//                private final ImageViewFragment[] mFragments = new ImageViewFragment[]{
//                        new ImageViewFragment(mImage + "1.png"),
//                };
//
//                @Override
//                public Fragment getItem(int position) {
//                    return mFragments[position];
//                }
//
//                @Override
//                public int getCount() {
//                    return mFragments.length;
//                }
//
//                @Override
//                public CharSequence getPageTitle(int position) {
//                    return mFragmentNames[position];
//                }
//            };
//        } else if (mImage.equalsIgnoreCase("gbs_place")) {
//            mPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
//                private final ImageViewFragment[] mFragments = new ImageViewFragment[]{
//                        new ImageViewFragment(mImage + "1.png"),
//                        new ImageViewFragment(mImage + "2.png"),
//                        new ImageViewFragment(mImage + "3.png"),
//                        new ImageViewFragment(mImage + "4.png"),
//                };
//
//                @Override
//                public Fragment getItem(int position) {
//                    return mFragments[position];
//                }
//
//                @Override
//                public int getCount() {
//                    return mFragments.length;
//                }
//
//                @Override
//                public CharSequence getPageTitle(int position) {
//                    return mFragmentNames[position];
//                }
//            };
//        } else if (mImage.equalsIgnoreCase("campus_place")) {
//            mPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
//                private final ImageViewFragment[] mFragments = new ImageViewFragment[]{
//                        new ImageViewFragment(mImage + "1.png"),
//                        new ImageViewFragment(mImage + "2.png"),
//                        new ImageViewFragment(mImage + "3.png"),
//                        new ImageViewFragment(mImage + "4.png"),
//                };
//
//                @Override
//                public Fragment getItem(int position) {
//                    return mFragments[position];
//                }
//
//                @Override
//                public int getCount() {
//                    return mFragments.length;
//                }
//
//                @Override
//                public CharSequence getPageTitle(int position) {
//                    return mFragmentNames[position];
//                }
//            };
//        }
        // Set up the ViewPager with the sections adapter.

    }
}
