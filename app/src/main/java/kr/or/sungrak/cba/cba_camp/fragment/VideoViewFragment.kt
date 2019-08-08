package kr.or.sungrak.cba.cba_camp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.video_layout.view.*
import kr.or.sungrak.cba.cba_camp.R
import kr.or.sungrak.cba.cba_camp.common.CBAUtil
import kr.or.sungrak.cba.cba_camp.common.Tag


@SuppressLint("ValidFragment")
class VideoViewFragment : Fragment() {
    private val YUTUEBE = "AIzaSyAAnuBDpQaicuio3meFzq2ALwD9mfdL9R4"
    private lateinit var database: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.video_layout, container, false)


        val youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance()
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.youtube_layout, youTubePlayerFragment as Fragment).commit()
        when (CBAUtil.getRetreat(activity)) {
            Tag.RETREAT_CBA -> {
                rootView.videoViewLayout.background = ContextCompat.getDrawable(requireContext(), R.drawable.cba_bg)
            }
            Tag.RETREAT_SUNGRAK -> {
                rootView.videoViewLayout.background = ContextCompat.getDrawable(requireContext(), R.drawable.sub_bg)
            }
        }

        youTubePlayerFragment.initialize(YUTUEBE, object : YouTubePlayer.OnInitializedListener {

            override fun onInitializationSuccess(provider: YouTubePlayer.Provider, player: YouTubePlayer, wasRestored: Boolean) {
                if (!wasRestored) {
                    player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                    player.loadVideo(CBAUtil.getPref(context, Tag.YOUTUBE))
                    player.play()
                }
            }

            override fun onInitializationFailure(provider: YouTubePlayer.Provider, error: YouTubeInitializationResult) {
                // YouTube error
            }
        })

        return rootView
    }
}

