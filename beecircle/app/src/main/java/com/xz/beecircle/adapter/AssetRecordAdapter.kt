package com.xz.beecircle.adapter

import android.graphics.Color
import com.lin.baselib.utils.Arith
import com.xz.beecircle.BtfRecord
import com.xz.beecircle.HoneyListData
import com.xz.beecircle.base.ModuleAdapter
import com.xz.beecircle.base.VBViewHolder
import com.xz.beecircle.databinding.ItemAssetRecordBinding

class AssetRecordAdapter: ModuleAdapter<HoneyListData, ItemAssetRecordBinding>() {

    override fun convert(holder: VBViewHolder<ItemAssetRecordBinding>, item: HoneyListData) {
        val adapterPosition = holder.absoluteAdapterPosition

        holder.binding.apply {

            llItem.setOnClickListener {
                if (listenerMy != null) {
                    listenerMy?.onItemClick(adapterPosition, it)
                }
            }

            item.apply {
                when(type){
                    0-> tvTitle.text = "挖矿所得"
                    1-> tvTitle.text = "上级转入"
                    2-> tvTitle.text = "下级转出"
                }
                if (Arith.sub(num,"0").toFloat()>0){
                    tvMoney.text = "+$num"
                    tvMoney.setTextColor(Color.parseColor("#ff86e152"))
                }else{
                    tvMoney.text = num
                    tvMoney.setTextColor(Color.parseColor("#FF6060"))
                }
                tvValue.text = creattime
            }
        }
    }
}