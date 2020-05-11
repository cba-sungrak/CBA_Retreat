package kr.or.sungrak.cba.cba_camp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kr.or.sungrak.cba.cba_camp.R
import kr.or.sungrak.cba.cba_camp.databinding.StatisticTotalGbsItemBinding
import kr.or.sungrak.cba.cba_camp.models.GBSTotalStatisticData

class GBSTotalStatisticAdapter : RecyclerView.Adapter<GBSTotalStatisticAdapter.StatisticGBSTotalViewHolder>() {
    companion object {
        private val TAG = "GBSTotalStatisticAdapter"
    }

    private val items = mutableListOf<GBSTotalStatisticData>()

    inner class StatisticGBSTotalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: StatisticTotalGbsItemBinding = DataBindingUtil.bind(itemView)!!
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): StatisticGBSTotalViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.statistic_total_gbs_item, viewGroup, false)
        return StatisticGBSTotalViewHolder(view)
    }

    override fun onBindViewHolder(memberViewHolder: StatisticGBSTotalViewHolder, i: Int) {

        memberViewHolder.binding.statistic = items[i]

        memberViewHolder.itemView.setOnClickListener { v: View? ->
        }
    }


    override fun getItemCount() = items!!.count()

    fun updateItems(_items: List<GBSTotalStatisticData>) {
        items.clear()
        items.addAll(_items)
        notifyDataSetChanged()
    }

}