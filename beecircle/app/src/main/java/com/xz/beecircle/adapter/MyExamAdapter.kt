package com.xz.beecircle.adapter

import android.graphics.Color
import androidx.core.content.ContextCompat
import com.lin.baselib.utils.Arith
import com.xz.beecircle.MyPowerDetailsDate
import com.xz.beecircle.base.ModuleAdapter
import com.xz.beecircle.base.VBViewHolder
import com.xz.beecircle.databinding.ItemMyExamBinding
import com.xz.beecircle.databinding.ItemMyTeamBinding
import com.xz.beecircle.databinding.ItemNoticeBinding

class MyExamAdapter : ModuleAdapter<MyPowerDetailsDate, ItemMyExamBinding>() {

    override fun convert(holder: VBViewHolder<ItemMyExamBinding>, item: MyPowerDetailsDate) {
        val adapterPosition = holder.absoluteAdapterPosition

        holder.binding.apply {
            llItem.setOnClickListener {
                if (listenerMy != null) {
                    listenerMy?.onItemClick(adapterPosition, it)
                }
            }
            item.apply {
                when (counttype) {
                    "0" -> tvTitle.text = "蜂蜜兑换永久算力"
                    "1" -> tvTitle.text = "看广告临时算力"
                    "2" -> tvTitle.text = "看广告临时算力到期"
                }
                if (Arith.sub(power.substring(0, power.length - 1), "0").toFloat() > 0) {
                    tvMoney.text = "+$power"
                    tvMoney.setTextColor(Color.parseColor("#ff86e152"))
                } else {
                    tvMoney.text = power
                    tvMoney.setTextColor(Color.parseColor("#FF6060"))
                }
                tvValue.text = starttime
            }
        }
    }
}