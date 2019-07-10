package kr.or.sungrak.cba.cba_retreat.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.sr_noti_layout.*
import kr.or.sungrak.cba.cba_retreat.MainActivity
import kr.or.sungrak.cba.cba_retreat.R

class SRNotiFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
                R.layout.sr_noti_layout,
                container,
                false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val map = (activity as MainActivity).loadImage("c2")
        for (key in map!!.keys) {
            val btn = Button(activity)
            btn.text = key
            buttonLayout.addView(btn)
            btn.setOnClickListener {
//                (activity as MainActivity).toast(map[key].toString())
                (activity as MainActivity).replaceFragment(ImageViewFragment((map[key].toString())))
            }
        }

    }

}

fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()