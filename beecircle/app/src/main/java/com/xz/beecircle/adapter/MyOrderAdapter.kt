package com.xz.beecircle.adapter

import com.xz.beecircle.MyBuyListData
import com.xz.beecircle.base.ModuleAdapter
import com.xz.beecircle.base.VBViewHolder
import com.xz.beecircle.databinding.ItemMyOrderBinding

class MyOrderAdapter(private var entryType: Int?, private var myOrderType: Int?) : ModuleAdapter<MyBuyListData, ItemMyOrderBinding>() {

    override fun convert(holder: VBViewHolder<ItemMyOrderBinding>, item: MyBuyListData) {
        val adapterPosition = holder.absoluteAdapterPosition

        holder.binding.apply {

            llItem.setOnClickListener {
                if (listenerMy != null) {
                    listenerMy?.onItemClick(adapterPosition, it)
                }
            }

            item.apply {
                when (myOrderType) {
                    0 -> {
                        tvType.text = "卖出蜂蜜"
                        when (state) {
                            0 -> tvStatus.text = "待收款"//我的卖出未完成-待收款
                            1 -> tvStatus.text = "待确认"//我的卖出未完成-待确认
                            3 ->  tvStatus.text = "申诉中"//我的卖出申诉中
                            2 -> tvStatus.text = "已完成"//我的卖出已完成
                        }
                    }
                    1 -> {
                        tvType.text = "求购蜂蜜"
                        when (entryType) {
                            0 -> tvStatus.text = "待付款"//我的发布待付款
                            1 -> tvStatus.text = "待收货"//我的发布待收货
                            2 -> tvStatus.text = "申诉中"//我的发布申诉中
                            3 -> tvStatus.text = "已完成"
                        }
                    }
                }


                tvNum.text = num
                tvUnitPrice.text = "$unitprice CNY"
                tvTotalPrice.text = "$totalprice CNY"
            }
        }
    }
}