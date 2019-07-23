package kr.or.sungrak.cba.cba_camp.fragment

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.sr_noti_layout.*
import kr.or.sungrak.cba.cba_camp.MainActivity
import kr.or.sungrak.cba.cba_camp.R


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

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val newContext = ContextThemeWrapper(context, R.style.ThemeOverlay_MyWhiteButton)
        val map = (activity as MainActivity).loadImage("c3")
        for (key in map!!.keys) {
            val btn = Button(newContext)
            btn.text = key
            btn.textSize = 17F
            buttonLayout.addView(btn)
            btn.setOnClickListener {
                (activity as MainActivity).addFragment(ImageViewFragment((map[key].toString())))
            }
        }

    }

}

fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()