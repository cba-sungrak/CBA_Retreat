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
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.statistic_gbs_total_layout.view.*
import kr.or.sungrak.cba.cba_camp.R
import kr.or.sungrak.cba.cba_camp.adapter.GBSTotalStatisticAdapter
import kr.or.sungrak.cba.cba_camp.common.CBAUtil
import kr.or.sungrak.cba.cba_camp.common.Tag
import kr.or.sungrak.cba.cba_camp.databinding.StatisticGbsTotalLayoutBinding
import kr.or.sungrak.cba.cba_camp.models.GBSTotalStatisticDatas
import kr.or.sungrak.cba.cba_camp.network.ApiService
import kr.or.sungrak.cba.cba_camp.network.ServiceGenerator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat

class GBSTotalStatisticFragment(var mSelectedDate: String) : Fragment() {
    private val TAG = "GBSTotalStatisticFragment"
    lateinit var mBinding: StatisticGbsTotalLayoutBinding
    private lateinit var recyclerView: RecyclerView
    lateinit var mGBSTotalSatisticAdapter: GBSTotalStatisticAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("CBA", "GBSTotalStatisticFragment create")
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.statistic_gbs_total_layout, container, false)
        mBinding.fragment = this
        mGBSTotalSatisticAdapter = GBSTotalStatisticAdapter(context) { item ->
            if (item.gbsId != 0)
            fragmentManager!!.beginTransaction().replace(R.id.fragment_container, GBSStepStatisticFragment(mSelectedDate, item.gbsId)).addToBackStack(null).commit()
        }
        if (TextUtils.isEmpty(mSelectedDate)) mSelectedDate = CBAUtil.getCurrentDate()
        mBinding.statisticDate.text = mSelectedDate

        recyclerView = mBinding.gbsStatisticRecylerview
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        getGBSTotalStatistic(mSelectedDate, Tag.NAVI_CURRENT)

        recyclerView.adapter = mGBSTotalSatisticAdapter



        return mBinding.root
    }

    private fun getGBSTotalStatistic(date: String, navi: String) {
        val service = ServiceGenerator.createService(ApiService::class.java)

        val request = service.getGBSTotalStatistic(date, navi)

        request.enqueue(object : Callback<GBSTotalStatisticDatas?> {
            override fun onResponse(call: Call<GBSTotalStatisticDatas?>, response: Response<GBSTotalStatisticDatas?>) {
                if (response.code() / 100 == 4) {
                    Log.e("CBA", "fail")
                } else {
                    val gbsTSData = response.body()
                    mGBSTotalSatisticAdapter!!.updateItems(gbsTSData!!.data)
                    mSelectedDate = gbsTSData!!.data[0].date
                    mBinding.statisticDate.text = mSelectedDate
                }
            }

            override fun onFailure(call: Call<GBSTotalStatisticDatas?>, t: Throwable) {
                Log.e("OKHttp_ERR", t.message)
            }
        })
    }

    fun onButtonClick(v: View) {
        when (v.id) {
            R.id.statistic_prev_date -> getGBSTotalStatistic(mSelectedDate, Tag.NAVI_PREV)
            R.id.statistic_next_date -> getGBSTotalStatistic(mSelectedDate, Tag.NAVI_NEXT)
            R.id.statistic_date -> DatePickerDialog(context, OnDateSetListener { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                try {
                    val selectedTime = String.format("%d-%d-%d", year, monthOfYear + 1,
                            dayOfMonth)
                    val sdf = SimpleDateFormat("yyyy-mm-dd")
                    val date = sdf.format(sdf.parse(selectedTime))
                    mSelectedDate = date
                    v.statistic_date.text = date
                    getGBSTotalStatistic(date, Tag.NAVI_CURRENT)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }, mSelectedDate.split("-").toTypedArray()[0].toInt(), mSelectedDate.split("-").toTypedArray()[1].toInt() - 1, mSelectedDate.split("-").toTypedArray()[2].toInt()).show()
        }
    }
}