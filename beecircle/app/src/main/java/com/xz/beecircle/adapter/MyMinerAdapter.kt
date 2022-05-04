package com.xz.beecircle.adapter

import com.xz.beecircle.Minerlevel1
import com.xz.beecircle.base.ModuleAdapter
import com.xz.beecircle.base.VBViewHolder
import com.xz.beecircle.databinding.ItemDigRecordBinding

class MyMinerAdapter(private val entryType: Int) : ModuleAdapter<Minerlevel1, ItemDigRecordBinding>() {

    override fun convert(holder: VBViewHolder<ItemDigRecordBinding>, item: Minerlevel1) {
        val adapterPosition = holder.absoluteAdapterPosition

        holder.binding.apply {

            llItem.setOnClickListener {
                if (listenerMy != null) {
                    listenerMy?.onItemClick(adapterPosition, it)
                }
            }

            item.apply {
                tvPhone.text = phone
                tvNum.text = power
                tvHash.text = upPower
                when (state) {
                    "0" -> tvStatus.text = "未报名"
                    "1" -> tvStatus.text = "已报名"
                    "2" -> tvStatus.text = "待领取"

                }
                tvTime.text = creatTime
            }
        }
    }
}