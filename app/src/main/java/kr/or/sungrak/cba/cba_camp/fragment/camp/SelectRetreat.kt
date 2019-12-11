package kr.or.sungrak.cba.cba_camp.fragment.camp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.select_dialog.*
import kr.or.sungrak.cba.cba_camp.R

class SelectRetreat : Fragment(){
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
                R.layout.select_dialog,
                container,
                false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        selectCBA.setOnClickListener {
            Toast.makeText(context, "cba", Toast.LENGTH_SHORT).show();
        }
        selectSungrak.setOnClickListener {
            Toast.makeText(context, "sungrak", Toast.LENGTH_SHORT).show();
        }
    }
}