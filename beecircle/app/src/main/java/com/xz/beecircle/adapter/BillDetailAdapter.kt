package com.xz.beecircle.adapter

import android.graphics.Color
import android.view.View
import com.lin.baselib.utils.Arith
import com.xz.beecircle.HoneyListData
import com.xz.beecircle.base.ModuleAdapter
import com.xz.beecircle.base.VBViewHolder
import com.xz.beecircle.databinding.*

class BillDetailAdapter(private val entryType: String, var activityType: String) :
    ModuleAdapter<HoneyListData, ItemAssetRecordBinding>() {

    override fun convert(holder: VBViewHolder<ItemAssetRecordBinding>, item: HoneyListData) {
        val adapterPosition = holder.absoluteAdapterPosition

        holder.binding.apply {

            llItem.setOnClickListener {
                if (listenerMy != null) {
                    listenerMy?.onItemClick(adapterPosition, it)
                }
            }

            item.apply {
                when (entryType) {
                    "bee" -> {
                        when (type) {
                            0 -> tvTitle.text = "蜜源产出"
                            1 -> tvTitle.text = "买蜜源支出"
                            2 -> tvTitle.text = "卖蜂蜜"
                            3 -> tvTitle.text = "买蜂蜜"
                            4 -> tvTitle.text = "卖蜂蜜订单取消"
                            5 -> tvTitle.text = "转给下级"
                            6 -> tvTitle.text = "上级专入"
                            7 -> tvTitle.text = "充值话费"
                            8 -> tvTitle.text = "充值vip"
                            9 -> tvTitle.text = "管理员操作"
                            10 -> tvTitle.text = "兑换算力"
                        }
                    }
                    "activity" -> {
                        when (activityType) {
                            "0" -> {
                                when (type) {
                                    0 -> tvTitle.text = "购买蜜源"
                                }
                            }
                        }
                    }
                    "share" -> {
                        when (type) {
                            0 -> tvTitle.text = "买蜜源"
                            1 -> tvTitle.text = "下级买蜜源"
                            2 -> tvTitle.text = "团队交易分红"
                            3 -> tvTitle.text = "全球交易分红"
                        }
                    }
                    "honest" -> {
                        when (type) {
                            0 -> tvTitle.text = "买蜂蜜"
                            1 -> tvTitle.text = "卖蜂蜜"
                            2 -> tvTitle.text = "买蜂蜜付款超时"
                            3 -> tvTitle.text = "违规"
                            4 -> tvTitle.text = "管理员操作"
                        }
                    }
                    "btf" -> {
                        when (type) {
                            0 -> tvTitle.text = "挖矿产出"
                            1 -> tvTitle.text = "上级转入"
                            2 -> tvTitle.text = "下级转出"
                            9 -> tvTitle.text = "管理员操作"
                        }
                    }
                }

                if (activityType == "1") {
                    tvTitle.text = name
                    tvMoney.text = temaActive
                    tvValue.visibility = View.GONE
                }
                if (activityType == "0") {
                    tvValue.text = creattime
                    if (Arith.sub(num, "0").toDouble() >= 0) {
                        tvMoney.setTextColor(Color.parseColor("#FF6060"))
                        tvMoney.text = "+$num"
                    } else {
                        tvMoney.setTextColor(Color.parseColor("#86E152"))
                        tvMoney.text = num
                    }
                }
            }
        }
    }
}