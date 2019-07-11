package kr.or.sungrak.cba.cba_retreat.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.tabs.TabLayout
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import kotlinx.android.synthetic.main.video_layout.*
import kotlinx.android.synthetic.main.video_layout.view.*
import kr.or.sungrak.cba.cba_retreat.MainActivity
import kr.or.sungrak.cba.cba_retreat.R
import java.util.*




@SuppressLint("ValidFragment")
class VideoViewFragment @SuppressLint("ValidFragment")
constructor(private var mImage: String) : Fragment() {
    private val YUTUEBE = "AIzaSyAAnuBDpQaicuio3meFzq2ALwD9mfdL9R4"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.video_layout, container, false)

        val youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance()
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.youtube_layout, youTubePlayerFragment as Fragment).commit()
        rootView.youtube_layout.visibility = View.GONE

        youTubePlayerFragment.initialize(YUTUEBE, object : YouTubePlayer.OnInitializedListener {

            override fun onInitializationSuccess(provider: YouTubePlayer.Provider, player: YouTubePlayer, wasRestored: Boolean) {
                if (!wasRestored) {
                    player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                    player.loadVideo("1tmc9Z9MmNc")
                    player.play()
                }
            }

            override fun onInitializationFailure(provider: YouTubePlayer.Provider, error: YouTubeInitializationResult) {
                // YouTube error
            }
        })



        val imageMap = (activity as MainActivity).loadImage(mImage)
        var mFragments = ArrayList<ImageViewFragment>()
        var mFragmentNames = ArrayList<String>()
        for (key in imageMap!!.keys) {
            mFragmentNames.add(key)
            mFragments.add(ImageViewFragment(imageMap[key]!!))
        }


        var mViewPager = rootView.swipe_view
        mViewPager.adapter = object : FragmentPagerAdapter(childFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return mFragments.get(position)
            }

            override fun getCount(): Int {
                return mFragments.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return mFragmentNames.get(position)
            }
        }

        mViewPager.setOnPageChangeListener(object : OnPageChangeListener {

            override fun onPageScrollStateChanged(arg0: Int) {


            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {


            }

            override fun onPageSelected(position: Int) {
                if(position==0){
                    youtube_layout.visibility = View.GONE
                }else{
                    youtube_layout.visibility = View.VISIBLE
                }
                Log.e("video", "position - $position")
            }

        })


        val tabLayout = rootView.findViewById<TabLayout>(R.id.videoTabs)
        tabLayout.setupWithViewPager(mViewPager)

        if (imageMap.size == 1) {
            tabLayout.visibility = View.GONE
        } else {
            tabLayout.visibility = View.VISIBLE
        }
        return rootView
    }
}

