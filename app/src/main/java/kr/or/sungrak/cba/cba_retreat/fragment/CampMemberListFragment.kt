package kr.or.sungrak.cba.cba_retreat.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.camp_member_list.*
import kotlinx.android.synthetic.main.sr_camp_member_listitem.view.*
import kr.or.sungrak.cba.cba_retreat.R
import kr.or.sungrak.cba.cba_retreat.network.ApiService
import kr.or.sungrak.cba.cba_retreat.network.ServiceGenerator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


data class CampMemList(
        @SerializedName("data")
        @Expose
        var srCampMemberList: List<SRCampMember>? = null
)

class CampMemberListFragment : Fragment() {

    private val adapter: CampMemberAdapter by lazy {
        CampMemberAdapter()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
                R.layout.camp_member_list,
                container,
                false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val service = ServiceGenerator.createService(ApiService::class.java)

        val request = service.getRegiCampMember()
        request.enqueue(object : Callback<CampMemList> {
            override fun onResponse(call: Call<CampMemList>, response: Response<CampMemList>) {
                if (response.code() / 100 == 4) {
                } else {
                    val member = response.body()
                    if (member != null) {
                        adapter.srMembers = member.srCampMemberList!!
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<CampMemList>, t: Throwable) {
                Log.e("OKHttp_ERR", t.message)
            }
        })

        activity?.let {
            campMemberRecyclerView.layoutManager = LinearLayoutManager(activity)
            campMemberRecyclerView.adapter = adapter
        }

    }
}

private class CampMemberAdapter : RecyclerView.Adapter<CampMemberAdapter.Holder>() {
    var srMembers: List<SRCampMember> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(parent)

    override fun getItemCount() = srMembers.count()
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val srMember = srMembers[position]

        with(holder.itemView) {
            srItemName.text = srMember.name
            srItemMobile.text = srMember.mobile
            srItemBelongTo.text = srMember.belongTo
            srItemCarNumber.text = srMember.carNumber
//            helloButton.setOnClickListener {
////                context.toast("Hello ${user.name}")
////
////                /*
////                Toast.makeText(
////                    context, "Hello, ${user.name}!", Toast.LENGTH_SHORT
////                ).show()
////                */
////            }
        }

        /*
        holder.itemView.textView.text = user.name
        holder.itemView.helloButton.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                "Hello, ${user.name}!", Toast.LENGTH_SHORT
            ).show()
        }
        */

    }

    class Holder(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.sr_camp_member_listitem, parent, false)
    )


}