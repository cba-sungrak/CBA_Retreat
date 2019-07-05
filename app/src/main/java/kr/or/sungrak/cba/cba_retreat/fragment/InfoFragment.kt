package kr.or.sungrak.cba.cba_retreat.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.info_layout2.*
import kr.or.sungrak.cba.cba_retreat.MainActivity
import kr.or.sungrak.cba.cba_retreat.R
import kr.or.sungrak.cba.cba_retreat.common.Tag
import kr.or.sungrak.cba.cba_retreat.models.Post

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
        val myRef = database.child(Tag.CBA_DB).child(Tag.MESSAGE)

        myRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val post = dataSnapshot.getValue(Post::class.java)  // chatDat
                if (post!!.isStaff.contentEquals("공지")) {
                    notiTextView.text = post.message
                    notiTextView.isSelected = true
                }
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

        QABtn.setOnClickListener {
            (activity as MainActivity).replaceFragment(PostListFragment())
        }

        callBtn.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("tel:01050254375")))
        }
        timeBtn.setOnClickListener {
            (activity as MainActivity).replaceFragment(ImageViewFragment("timetable"))
        }
        youtubeBtn.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCW6bF9L0ZK__Tlwl19B0FYQ")))
        }
        gbsBtn.setOnClickListener {
            (activity as MainActivity).replaceFragment(GBSFragment())
        }
    }
}