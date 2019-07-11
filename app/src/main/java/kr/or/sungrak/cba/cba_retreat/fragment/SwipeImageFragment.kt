package kr.or.sungrak.cba.cba_retreat.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.ui_swipe_layout.view.*
import kr.or.sungrak.cba.cba_retreat.MainActivity
import kr.or.sungrak.cba.cba_retreat.R

class SwipeImageFragment @SuppressLint("ValidFragment")
constructor(private var mImage: String) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.ui_swipe_layout, container, false)

        val imageMap = (activity as MainActivity).loadImage(mImage)
        var mFragments = mutableListOf<ImageViewFragment>()
        var mFragmentNames = mutableListOf<String>()

        for (key in imageMap!!.keys) {
            mFragmentNames.add(key)
            mFragments.add(ImageViewFragment(imageMap[key]!!))
        }


        var mViewPager = rootView.swipe_view
        mViewPager.adapter = object : FragmentPagerAdapter(childFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return mFragments[position]
            }

            override fun getCount(): Int {
                return mFragments.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return mFragmentNames[position]
            }
        }
        val tabLayout = rootView.findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(mViewPager)
        if (imageMap.size == 1) {
            tabLayout.visibility = View.GONE
        } else {
            tabLayout.visibility = View.VISIBLE
        }

        return rootView
    }
}