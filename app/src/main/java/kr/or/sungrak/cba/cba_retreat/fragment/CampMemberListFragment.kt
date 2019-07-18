package kr.or.sungrak.cba.cba_retreat.fragment

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.camp_member_list.*
import kotlinx.android.synthetic.main.camp_member_list.view.*
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

        activity?.let {
            campMemberRecyclerView.layoutManager = LinearLayoutManager(activity)
            campMemberRecyclerView.adapter = adapter
        }

        val service = ServiceGenerator.createService(ApiService::class.java)

        val request = service.regiCampMember
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

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        view.memberSearchView.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))
        view.memberSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                adapter.notifyDataSetChanged()
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                adapter.filter.filter(query)
                adapter.notifyDataSetChanged()
                return true
            }

        })

    }
}

private class CampMemberAdapter : RecyclerView.Adapter<CampMemberAdapter.Holder>(), Filterable {
    var srMembers: List<SRCampMember> = emptyList()
        set(value) {
            field = value
            memberSearchList = srMembers
            notifyDataSetChanged()
        }
    var memberSearchList: List<SRCampMember> = emptyList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(parent)

    override fun getItemCount() = memberSearchList.count()
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val srMember = memberSearchList[position]

        with(holder.itemView) {
            srItemName.text = srMember.name
            srItemMobile.text = srMember.mobile
            srItemBelongTo.text = srMember.belongTo
            srItemCarNumber.text = srMember.carNumber
        }


    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                var filteredList = mutableListOf<SRCampMember>()
                if (charString.isEmpty()) {
                    filteredList = srMembers as MutableList<SRCampMember>
                } else {
                    for (row in srMembers) {
                        if (row.name.toLowerCase().contains(charString.toLowerCase())
                                || row.mobile.contains(charSequence)
                                || row.carNumber.toLowerCase().contains(charString.toLowerCase())
                                || row.belongTo.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
//                    memberSearchList = filteredList
                }
                val filterResults = FilterResults()
                Log.e("1", filteredList.toString())
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                memberSearchList = filterResults.values as List<SRCampMember>
                Log.e("2", memberSearchList.toString())
                notifyDataSetChanged()
            }
        }
    }


    class Holder(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.sr_camp_member_listitem, parent, false)
    )


}