package com.xz.beecircle.adapter

import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import com.xz.beecircle.R
import com.xz.beecircle.RechargeListData
import com.xz.beecircle.base.ModuleAdapter
import com.xz.beecircle.base.VBViewHolder
import com.xz.beecircle.databinding.*

class ChargeAdapter(private var entryType: String) : ModuleAdapter<RechargeListData, ItemChargeBinding>() {

    override fun convert(holder: VBViewHolder<ItemChargeBinding>, item: RechargeListData) {
        val adapterPosition = holder.absoluteAdapterPosition

        holder.binding.apply {
            llItem.setOnClickListener {
                if (listenerMy != null) {
                    listenerMy?.onItemClick(adapterPosition, it)
                }
            }
            when (entryType) {
                "charge" -> {
                    tvTypeKey.visibility = View.GONE
                    tvTypeValue.visibility = View.GONE
                }
                "member" -> {

                }
            }

            item.apply {
                when (state) {//状态（0待审核（1审核通过（2审核未通过
                    0 -> tvStatus.text = "待审核"
                    1 -> tvStatus.text = "审核通过"
                    2 -> tvStatus.text = "审核未通过"
                }
                when (vipid) {//vip类型（0腾讯（1爱奇艺（2优酷
                    0 -> tvTypeValue.text = "腾讯"
                    1 -> tvTypeValue.text = "爱奇艺"
                    2 -> tvTypeValue.text = "优酷"
                }
                tvAccount.text = rechargeid
                tvMoney.text = rechargenum.toString()
                tvTime.text = "充值时间：${creattime}"
            }
        }
    }
}