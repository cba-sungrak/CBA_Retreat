package kr.or.sungrak.cba.cba_camp.adapter

import android.app.PendingIntent.getActivity
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
import kr.or.sungrak.cba.cba_camp.databinding.StatisticTotalGbsItemBinding
import kr.or.sungrak.cba.cba_camp.models.GBSTotalStatisticData

class GBSTotalStatisticAdapter(val context:Context?) : RecyclerView.Adapter<GBSTotalStatisticAdapter.StatisticGBSTotalViewHolder>() {
    companion object {
        private val TAG = "GBSTotalStatisticAdapter"
        private const val CHECKED = 1
        private const val NOTCHECKED = 0
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


        memberViewHolder.itemView.setOnClickListener { v: View? ->
        }
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

    fun updateItems(_items: List<GBSTotalStatisticData>) {
        items.clear()
        items.addAll(_items)
        notifyDataSetChanged()
    }

}