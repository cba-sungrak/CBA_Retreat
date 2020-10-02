package kr.or.sungrak.cba.cba_camp.dialog

import android.content.Context
import android.os.Bundle
import kotlinx.android.synthetic.main.select_dialog.*
import kr.or.sungrak.cba.cba_camp.R
import kr.or.sungrak.cba.cba_camp.common.CBAUtil
import kr.or.sungrak.cba.cba_camp.common.Tag

class SelectDialog(mContext: Context) : MyProgessDialog(mContext) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_dialog)

        selectSungrak.setOnClickListener {
            CBAUtil.setRetreat(context, Tag.RETREAT_SUNGRAK)
            CBAUtil.signOut(context)
            CBAUtil.setCurrentDepartment(context, Tag.SUNGRAK)
            dismiss()
        }

        selectCBA.setOnClickListener {
            CBAUtil.setRetreat(context, Tag.RETREAT_CBA)
            CBAUtil.signOut(context)
            CBAUtil.setCurrentDepartment(context, Tag.CBA)
            dismiss()
        }

        selectBWM.setOnClickListener {
            CBAUtil.setRetreat(context, Tag.RETREAT_BWM)
            CBAUtil.signOut(context)
            CBAUtil.setCurrentDepartment(context, Tag.BWM)
            dismiss()
        }

    }


}