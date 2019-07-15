package kr.or.sungrak.cba.cba_retreat.dialog

import android.content.Context
import android.os.Bundle
import kotlinx.android.synthetic.main.select_dialog.*
import kr.or.sungrak.cba.cba_retreat.R
import kr.or.sungrak.cba.cba_retreat.common.CBAUtil
import kr.or.sungrak.cba.cba_retreat.common.Tag

class SelectDialog(mContext: Context) : MyProgessDialog(mContext) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_dialog)

        selectSungrak.setOnClickListener {
            CBAUtil.setRetreat(context, Tag.RETREAT_SUNGRAK)
            CBAUtil.removeAllPreferences(context)
            dismiss()
        }

        selectCBA.setOnClickListener {
            CBAUtil.setRetreat(context, Tag.RETREAT_CBA)
            CBAUtil.removeAllPreferences(context)
            dismiss()
        }
    }


}