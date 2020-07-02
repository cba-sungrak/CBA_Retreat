package kr.or.sungrak.cba.cba_camp.adapter

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kr.or.sungrak.cba.cba_camp.R
import kr.or.sungrak.cba.cba_camp.databinding.StatisticStepGbsItemBinding
import kr.or.sungrak.cba.cba_camp.databinding.StatisticTotalGbsItemBinding
import kr.or.sungrak.cba.cba_camp.models.GBSStepStatisticData
import kr.or.sungrak.cba.cba_camp.models.GBSStepStatisticDatas
import kr.or.sungrak.cba.cba_camp.models.GBSTotalStatisticData

class GBSStepStatisticAdapter(val context:Context?, val itemClick: (GBSStepStatisticData) -> Unit) : RecyclerView.Adapter<GBSStepStatisticAdapter.ViewHolder>() {
    companion object {
        private val TAG = "GBSStepStatisticAdapter"
        private const val CHECKED = 1
        private const val NOTCHECKED = 0
    }

    private val items = mutableListOf<GBSStepStatisticData>()

    inner class ViewHolder(itemView: View, val itemClick: (GBSStepStatisticData) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val binding: StatisticStepGbsItemBinding = DataBindingUtil.bind(itemView)!!
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.statistic_step_gbs_item, viewGroup, false)
        return ViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(memberViewHolder: ViewHolder, i: Int) {
        memberViewHolder.binding.statistic = items[i]
        val grey = ContextCompat.getColor(context!!, R.color.grey_500)

        when (getItemViewType(i)) {
            CHECKED -> {
                memberViewHolder.binding.statisticGbs.setTextColor(Color.BLACK)
                memberViewHolder.binding.statisticCount.setTextColor(Color.BLACK)
                memberViewHolder.binding.statisticPercent.setTextColor(Color.BLACK)
            }
            NOTCHECKED -> {
                memberViewHolder.binding.statisticGbs.setTextColor(grey)
                memberViewHolder.binding.statisticCount.setTextColor(grey)
                memberViewHolder.binding.statisticPercent.setTextColor(grey)
            }
        }

        memberViewHolder.itemView.setOnClickListener { itemClick(items[i]) }
    }

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        return if (TextUtils.isEmpty(item.date)) {
            NOTCHECKED
        } else {
            CHECKED
        }
    }


    override fun getItemCount() = items!!.count()

    fun updateItems(_items: List<GBSStepStatisticData>) {
        items.clear()
        items.addAll(_items)
        notifyDataSetChanged()
    }

}