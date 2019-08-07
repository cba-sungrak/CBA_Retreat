package kr.or.sungrak.cba.cba_camp.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.info_layout2.*
import kotlinx.android.synthetic.main.info_layout2.view.*
import kr.or.sungrak.cba.cba_camp.MainActivity
import kr.or.sungrak.cba.cba_camp.R
import kr.or.sungrak.cba.cba_camp.common.CBAUtil
import kr.or.sungrak.cba.cba_camp.common.Tag
import kr.or.sungrak.cba.cba_camp.models.Post
class InfoFragment : Fragment() {
    companion object {
        private val TAG = "CBA/InfoFragment"
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
                R.layout.info_layout2,
                container,
                false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val database = FirebaseDatabase.getInstance().reference
        lateinit var myRef: DatabaseReference
        when (CBAUtil.getRetreat(this.activity)) {
            Tag.RETREAT_CBA -> {
                backGroundImage.setImageResource(R.drawable.backgroundtext)
                cbaInfoLayout.visibleOrGone(true)
                srInfoLayout.visibleOrGone(false)
                myRef = database.child(Tag.RETREAT_CBA)

            }
            Tag.RETREAT_SUNGRAK -> {
                backGroundImage.setImageResource(R.drawable.sr_background)
                cbaInfoLayout.visibleOrGone(false)
                srInfoLayout.visibleOrGone(true)
                myRef = database.child(Tag.RETREAT_SUNGRAK)
            }
        }


        myRef.child(Tag.NOTI).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val post = dataSnapshot.getValue(Post::class.java)  // chatDat
                view.notiTextView.text = post!!.message
                view.notiTextView.isSelected = true
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        notiTextView.setOnClickListener {
            (activity as MainActivity).replaceFragment(PostListFragment())
        }

        //cba
        QABtn.setOnClickListener {
            (activity as MainActivity).replaceFragment(QAListFragment())
        }

        callBtn.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("tel:01032253652")))
        }
        timeBtn.setOnClickListener {
            (activity as MainActivity).replaceFragment(SwipeImageFragment("timetable"))
        }
        youtubeBtn.setOnClickListener {
            (activity as MainActivity).replaceFragment(VideoViewFragment())
//            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCW6bF9L0ZK__Tlwl19B0FYQ")))
        }
        gbsBtn.setOnClickListener {
            (activity as MainActivity).replaceFragment(GBSFragment())
        }
        //cba

        srRegi.setOnClickListener {
            if (ContextCompat.checkSelfPermission(activity!!,
                            Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.READ_PHONE_STATE), 20)
            } else {
                (activity as MainActivity).replaceFragment(CampRegistFragment())
            }

        }
        srCallBtn.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("tel:07073006239")))
        }
        srTimeBtn.setOnClickListener {
            (activity as MainActivity).replaceFragment(SwipeImageFragment("m3"))
        }
        srQABtn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(activity!!,
                            Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.READ_PHONE_STATE), 10)
            } else {
                (activity as MainActivity).replaceFragment(QAListFragment())
            }
        }
        srMapBtn.setOnClickListener {
            (activity as MainActivity).replaceFragment(SwipeImageFragment("m5"))
        }
    }

    private fun View.visibleOrGone(visible: Boolean) {
        visibility = if (visible) View.VISIBLE else View.GONE
    }
}

