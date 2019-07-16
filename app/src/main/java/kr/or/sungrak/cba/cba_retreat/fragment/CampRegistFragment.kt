package kr.or.sungrak.cba.cba_retreat.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.regist_layout.view.*
import kr.or.sungrak.cba.cba_retreat.MainActivity
import kr.or.sungrak.cba.cba_retreat.R
import kr.or.sungrak.cba.cba_retreat.network.ApiService
import kr.or.sungrak.cba.cba_retreat.network.ServiceGenerator
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class SRCampMember(
        val name: String,
        val mobile: String,
        val belongTo: String,
        val carNumber: String
)

class CampRegistFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
                R.layout.regist_layout,
                container,
                false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.campRegiBtn.setOnClickListener {
            val service = ServiceGenerator.createService(ApiService::class.java)

            val regiMem = SRCampMember(view.regiName.text.toString(), view.regiPhone.text.toString(), view.regiChurch.text.toString(), view.carModel.text.toString() + "/" + view.carNo.text.toString())

            val obj = Gson().toJson(regiMem)

//            val obj = JSONObject()
//            try {
//                obj.put("name", )
//                obj.put("mobile", )
//                obj.put("belongTo", )
//                obj.put("carNumber", )
//            } catch (e: JSONException) {
//                e.printStackTrace()
//            }

            val body = RequestBody.create(MediaType.parse("application/json"), obj.toString())
            val request = service.regiCampMember(body)
            request.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.code() / 100 == 4) {
                        Toast.makeText(context,
                                "등록에 실패하였습니다. ", Toast.LENGTH_LONG)
                                .show()
                    } else {
                        Toast.makeText(context,
                                "등록 되었습니다. ", Toast.LENGTH_LONG)
                                .show()
                    }
                    (activity as MainActivity).replaceFragment(InfoFragment())
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("OKHttp_ERR", t.message)
                }
            })

        }
    }
}