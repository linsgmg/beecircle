package com.xz.beecircle.adapter

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import com.lin.baselib.utils.DensityUtil
import com.xz.beecircle.R
import com.xz.beecircle.TradeCenterListData
import com.xz.beecircle.base.ModuleAdapter
import com.xz.beecircle.base.VBViewHolder
import com.xz.beecircle.databinding.*

class ChangeCenterAdapter(var currentSelect: Int) : ModuleAdapter<TradeCenterListData, ItemChangeCenterBinding>() {

    override fun convert(holder: VBViewHolder<ItemChangeCenterBinding>, item: TradeCenterListData) {
        val adapterPosition = holder.absoluteAdapterPosition

        holder.binding.apply {

            llItem.setOnClickListener {
                if (listenerMy != null) {
                    listenerMy?.onItemClick(adapterPosition, it)
                }
            }
            tvOperate.setOnClickListener {
                if (listenerMy != null) {
                    listenerMy?.onItemClick(adapterPosition, it)
                }
            }

            item.apply {
                if (adapterPosition%2==0){
                    llItem.setBackgroundColor(Color.WHITE)
                }else{
                    llItem.setBackgroundColor(Color.parseColor("#F2F8FC"))
                }

                when(currentSelect){
                    0-> {
                        tvHonorValue.visibility = View.VISIBLE
                        tvType.visibility = View.GONE
                        tvFee.visibility = View.GONE
                        tvStatus.visibility = View.GONE
                        tvOperate.background = ContextCompat.getDrawable(context,R.drawable.bg_red_40)
                        tvOperate.text = "卖出"
                        tvOperate.visibility = View.VISIBLE
                    }
                    1-> {
                        tvHonorValue.visibility = View.VISIBLE
                        tvType.visibility = View.GONE
                        tvFee.visibility = View.GONE
                        tvStatus.visibility = View.GONE
                        tvOperate.background = ContextCompat.getDrawable(context,R.drawable.bg_red_40)
                        tvOperate.text = "卖出"
                        tvOperate.visibility = View.VISIBLE
                    }
                    2-> {
                        tvHonorValue.visibility = View.GONE
                        tvType.visibility = View.VISIBLE
                        tvFee.visibility = View.VISIBLE
                        tvStatus.visibility = View.VISIBLE
                        tvOperate.background = ContextCompat.getDrawable(context,R.drawable.bg_gray_40)
                        tvOperate.text = "取消"
                        when(state){
                            0->{
                                tvStatus.text = "订单刚发布 不可取消"
                                tvOperate.visibility = View.GONE
                            }
                            1->{
                                tvStatus.text = "订单发布超过1小时 可取消"
                                tvOperate.visibility = View.VISIBLE
                            }
                            2->{
                                tvStatus.text = "订单已取消"
                                tvOperate.visibility = View.GONE
                            }
                            3->{
                                tvStatus.text = "交易中"
                                tvOperate.visibility = View.GONE
                            }
                            4->{
                                tvStatus.text = "交易完成"
                                tvOperate.visibility = View.GONE
                            }
                        }
                    }
                }

                tvHonorValue.text = sincerity
                tvNum.text = num
                when(type){
                    0->tvType.text = "小单"
                    1->tvType.text = "大单"
                }
                tvUnit.text = unitprice
                tvTotal.text = totalprice
                tvFee.text = charge
            }
        }
    }
}