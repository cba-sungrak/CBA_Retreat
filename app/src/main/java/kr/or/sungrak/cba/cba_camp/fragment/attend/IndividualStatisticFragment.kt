package kr.or.sungrak.cba.cba_camp.fragment.attend

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.or.sungrak.cba.cba_camp.models.AttendList
import kr.or.sungrak.cba.cba_camp.network.ApiService
import kr.or.sungrak.cba.cba_camp.network.ServiceGenerator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IndividualStatisticFragment : Fragment() {


    private fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, function: () -> Unit) {

    }

    private fun callIndividualAttend(id: Integer) {
        val service = ServiceGenerator.createService(ApiService::class.java)

        val request = service.getindividualAttend(id.toString())

        request.enqueue(object : Callback<AttendList> {
            override fun onResponse(call: Call<AttendList>, response: Response<AttendList>) {
                if (response.code() / 100 == 4) {
                    Log.e("CBA", "fail")
                } else {
                    val statisticInfoList = response.body()
                }
            }

            override fun onFailure(call: Call<AttendList>, t: Throwable) {
                Log.e("OKHttp_ERR", t.message)
            }
        })
    }

    fun onButtonClick(v: View) {
        when (v.id) {

        }
    }

    companion object {
        private val NAVI_PREV = "PREV"
        private val NAVI_NEXT = "NEXT"
        private val NAVI_CURRENT = "CURRENT"
    }
}

