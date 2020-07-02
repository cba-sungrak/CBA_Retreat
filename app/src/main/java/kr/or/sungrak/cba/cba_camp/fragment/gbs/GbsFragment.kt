package kr.or.sungrak.cba.cba_camp.fragment.gbs

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.attend_layout.*
import kr.or.sungrak.cba.cba_camp.R
import kr.or.sungrak.cba.cba_camp.adapter.AttendMemeberAdapter
import kr.or.sungrak.cba.cba_camp.common.CBAUtil
import kr.or.sungrak.cba.cba_camp.databinding.AttendLayoutBinding
import kr.or.sungrak.cba.cba_camp.fragment.attend.AttendEditFragment
import kr.or.sungrak.cba.cba_camp.models.AttendList
import kr.or.sungrak.cba.cba_camp.network.ApiService
import kr.or.sungrak.cba.cba_camp.network.ServiceGenerator
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat

class GbsFragment(val mGbsName:String, val mleaderMemId: String, var mSelectedDate: String) : Fragment() {
    private val TAG = "GBSFragment"
    private val NAVI_PREV = "PREV"
    private val NAVI_NEXT = "NEXT"
    private val NAVI_CURRENT = "CURRENT"
    var mAttendMemberList: AttendList? = null
    private val mAttendMemberAdapter: AttendMemeberAdapter by lazy {
        AttendMemeberAdapter()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.gbs_attend_layout, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        attend_date.text = mSelectedDate
        attend_member_list.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        attend_member_list.adapter = mAttendMemberAdapter

        getAttendInfo(mSelectedDate, mleaderMemId, NAVI_CURRENT)

        mAttendMemberAdapter!!.setCustomOnItemClickListener { v: View ->
            val cb = v as CheckBox
            if (cb.isChecked) {
                mAttendMemberList!!.attended = mAttendMemberList!!.attended + 1
            } else {
                mAttendMemberList!!.attended = mAttendMemberList!!.attended - 1
            }
            attend_total.text = getString(mAttendMemberList)
        }


        attend_prev_date.setOnClickListener { getAttendInfo(mSelectedDate, mleaderMemId, NAVI_PREV) }
        attend_next_date.setOnClickListener { getAttendInfo(mSelectedDate, mleaderMemId, NAVI_NEXT) }

        attend_date.setOnClickListener {
            DatePickerDialog(context, OnDateSetListener { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                try {
                    val selectedTime = String.format("%d-%d-%d", year, monthOfYear + 1,
                            dayOfMonth)
                    val sdf = SimpleDateFormat("yyyy-mm-dd")
                    val date = sdf.format(sdf.parse(selectedTime))
                    mSelectedDate = date
                    attend_date.text = date
                    getAttendInfo(date, mleaderMemId, NAVI_CURRENT)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }, mSelectedDate!!.split("-").toTypedArray()[0].toInt(), mSelectedDate!!.split("-").toTypedArray()[1].toInt() - 1, mSelectedDate!!.split("-").toTypedArray()[2].toInt()).show()
        }

        edit_attend.visibility = View.GONE;
        create_attend.setOnClickListener { createAttendList(mSelectedDate, mleaderMemId) }
        confirm_attend.setOnClickListener { postAttendList() }
        delete_attend.setOnClickListener {
            AlertDialog.Builder(activity!!)
                    .setTitle("출석부 삭제")
                    .setMessage(mSelectedDate + "날짜의 출석부를 삭제 하시겠습니까?")
                    .setPositiveButton(android.R.string.yes) { dialog: DialogInterface?, whichButton: Int -> deleteAtteand() }
                    .setNegativeButton(android.R.string.no) { dialog: DialogInterface?, whichButton: Int ->
                        // 취소시 처리 로직
                        Toast.makeText(activity, "취소하였습니다.", Toast.LENGTH_SHORT).show()
                    }.show()
        }

    }


    private fun getAttendInfo(date: String?, leaderMemId: String?, navi: String) {
        val service = ServiceGenerator.createService.getGBSAttendList(date, leaderMemId, navi)
        service.enqueue(object : Callback<AttendList?> {
            override fun onResponse(call: Call<AttendList?>, response: Response<AttendList?>) {
                if (response.code() / 100 == 4) {
                    if (navi.equals(NAVI_CURRENT, ignoreCase = true)) {
                        create_attend.visibility = View.VISIBLE
                        attend_member_list.visibility = View.GONE
                        confirm_attend.visibility = View.GONE
                        delete_attend.visibility = View.GONE
                    } else {
                        Toast.makeText(context, "출석 내역이 존재 하지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    create_attend.visibility = View.GONE
                    attend_member_list.visibility = View.VISIBLE
                    confirm_attend.visibility = View.VISIBLE
                    delete_attend.visibility = View.VISIBLE

                    mAttendMemberList = response.body()
                    attend_total.text = getString(mAttendMemberList)
                    mAttendMemberAdapter!!.updateItems(mAttendMemberList!!.attendInfos)
                    mSelectedDate = mAttendMemberList!!.attendInfos[0].date
                    attend_date.text = mSelectedDate
                }
            }

            override fun onFailure(call: Call<AttendList?>, t: Throwable) {
                Log.e("OKHttp_ERR", t.message)
            }
        })
    }

    private fun getString(`as`: AttendList?): String {
        val percent = (`as`!!.attended.toDouble() / `as`.registered.toDouble() * 100.0).toInt()
        return "[" + mGbsName + "] 출석 " + `as`.attended + " / 전체 " + `as`.registered + " / " + percent + "%"
    }

    private fun createAttendList(date: String?, leaderMemberId: String?) {
        val obj = JSONObject()
        try {
            obj.put("date", date)
            obj.put("leaderMemberId", leaderMemberId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val body = RequestBody.create(MediaType.parse("application/json"), obj.toString())

        val service = ServiceGenerator.createService.createGBSAttend(body)
        service.enqueue(object : Callback<AttendList?> {
            override fun onResponse(call: Call<AttendList?>, response: Response<AttendList?>) {
                if (response.code() / 100 == 4) {
                    Toast.makeText(context,
                                    "failed make attend ", Toast.LENGTH_SHORT)
                            .show()
                } else {
                    create_attend.visibility = View.GONE
                    attend_member_list.visibility = View.VISIBLE
                    confirm_attend.visibility = View.VISIBLE
                    mAttendMemberList = response.body()
                    attend_total.text = getString(mAttendMemberList)
                    mAttendMemberAdapter!!.updateItems(mAttendMemberList!!.attendInfos)
                    mSelectedDate = mAttendMemberList!!.attendInfos[0].date
                    attend_date.text = mSelectedDate
                    Toast.makeText(context, "출석부가 생성 되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AttendList?>, t: Throwable) {
                Log.e("OKHttp_ERR", t.message)
            }
        })
    }

    private fun postAttendList() {
        val attendInfoList = mAttendMemberAdapter!!.attendInfoList
        val jsonArray = JSONArray()
        val obj = JSONObject()
        try {
            for (attendInfo in attendInfoList) {
                val jsonObject = JSONObject()
                jsonObject.put("id", attendInfo.id)
                jsonObject.put("status", attendInfo.status)
                jsonObject.put("note", attendInfo.note)
                jsonArray.put(jsonObject)
            }
            obj.put("checkList", jsonArray)
            obj.put("leaderUid", FirebaseAuth.getInstance().uid)
            Log.d(TAG, obj.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val body = RequestBody.create(MediaType.parse("application/json"), obj.toString())

        val service = ServiceGenerator.createService.postGBSAttend(body)
        service.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.code() / 100 == 4) {
                    Toast.makeText(context, "post attend failed ", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "post attend succes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Log.e("OKHttp_ERR", t.message)
            }
        })
    }

    private fun deleteAtteand() {
        val jsonObject = JSONObject()
        //  {date: "2019-05-05", campus: "천안", "leaderUid": "9999"}
        try {
            jsonObject.put("date", mSelectedDate)
            jsonObject.put("leaderUid", FirebaseAuth.getInstance().uid)
            jsonObject.put("leaderMemberId", mleaderMemId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Log.d(TAG, jsonObject.toString())
        val body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())

        val service = ServiceGenerator.createService.deleteGBSAttend(body)
        service.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.code() / 100 == 4) {
                    Toast.makeText(context,
                            "Delete attend failed ", Toast.LENGTH_SHORT)
                            .show()
                } else {
                    Toast.makeText(context,
                                    "Delete attend success", Toast.LENGTH_SHORT)
                            .show()
                }
                getAttendInfo(mSelectedDate, mleaderMemId, NAVI_CURRENT)
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Log.e("OKHttp_ERR", t.message)
            }
        })
    }

}