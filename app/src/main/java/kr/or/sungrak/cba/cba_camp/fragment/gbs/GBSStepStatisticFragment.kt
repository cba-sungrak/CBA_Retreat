package kr.or.sungrak.cba.cba_camp.fragment.gbs

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.statistic_gbs_total_layout.view.*
import kr.or.sungrak.cba.cba_camp.R
import kr.or.sungrak.cba.cba_camp.adapter.GBSStepStatisticAdapter
import kr.or.sungrak.cba.cba_camp.common.CBAUtil
import kr.or.sungrak.cba.cba_camp.common.Tag
import kr.or.sungrak.cba.cba_camp.databinding.StatisticGbsStepLayoutBinding
import kr.or.sungrak.cba.cba_camp.models.GBSStepStatisticData
import kr.or.sungrak.cba.cba_camp.models.GBSStepStatisticDatas
import kr.or.sungrak.cba.cba_camp.network.ApiService
import kr.or.sungrak.cba.cba_camp.network.ServiceGenerator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat

class GBSStepStatisticFragment(var mSelectedDate: String, val mGbsId: Int, val mGbsName: String) : Fragment() {
    private val TAG = "GBSStepStatisticFragment"
    lateinit var mBinding: StatisticGbsStepLayoutBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var mGBSStepSatisticAdapter: GBSStepStatisticAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("CBA", "GBSTotalStatisticFragment create")
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.statistic_gbs_step_layout, container, false)
        mBinding.fragment = this
        mGBSStepSatisticAdapter = GBSStepStatisticAdapter(context) { item ->
            if (item.leaderGbsMemberId != 0)
                fragmentManager!!.beginTransaction().replace(R.id.fragment_container, GbsFragment(mGbsName, item.leaderMemberId, mSelectedDate)).addToBackStack(null).commit()
        }
        if (TextUtils.isEmpty(mSelectedDate)) mSelectedDate = CBAUtil.getCurrentDate()
        mBinding.statisticDate.text = mSelectedDate

        recyclerView = mBinding.gbsStatisticRecylerview
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        getGBSStepStatistic(mSelectedDate, Tag.NAVI_CURRENT, mGbsId)

        recyclerView.adapter = mGBSStepSatisticAdapter

        return mBinding.root
    }

    private fun getGBSStepStatistic(date: String, navi: String, gbsId: Int) {
        val service = ServiceGenerator.createService.getGBSStepStatistic(date, navi, gbsId)

        service.enqueue(object : Callback<GBSStepStatisticDatas?> {
            override fun onResponse(call: Call<GBSStepStatisticDatas?>, response: Response<GBSStepStatisticDatas?>) {
                if (response.code() / 100 == 4) {
                    Log.e("CBA", "fail")
                } else {
                    var gbsTSData = response.body()

                    gbsTSData?.data?.find { it.leaderName == "전체" }?.leaderName = mGbsName

                    mGBSStepSatisticAdapter!!.updateItems(gbsTSData!!.data)
                    mSelectedDate = gbsTSData!!.data[0].date
                    mBinding.statisticDate.text = mSelectedDate
                }
            }

            override fun onFailure(call: Call<GBSStepStatisticDatas?>, t: Throwable) {
                Log.e("OKHttp_ERR", t.message)
            }
        })
    }

    fun onButtonClick(v: View) {
        when (v.id) {
//            R.id.statistic_prev_date -> getGBSStepStatistic(mSelectedDate, Tag.NAVI_PREV, mGbsId)
//            R.id.statistic_next_date -> getGBSStepStatistic(mSelectedDate, Tag.NAVI_NEXT, mGbsId)
            R.id.statistic_date -> DatePickerDialog(requireContext(), OnDateSetListener { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                try {
                    val selectedTime = String.format("%d-%d-%d", year, monthOfYear + 1,
                            dayOfMonth)
                    val sdf = SimpleDateFormat("yyyy-mm-dd")
                    val date = sdf.format(sdf.parse(selectedTime))
                    mSelectedDate = date
                    v.statistic_date.text = date
                    getGBSStepStatistic(date, Tag.NAVI_CURRENT, mGbsId)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }, mSelectedDate.split("-").toTypedArray()[0].toInt(), mSelectedDate.split("-").toTypedArray()[1].toInt() - 1, mSelectedDate.split("-").toTypedArray()[2].toInt()).show()
        }
    }
}