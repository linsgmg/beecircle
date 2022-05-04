package com.xz.beecircle.adapter

import com.xz.beecircle.NoticeDetailData
import com.xz.beecircle.NoticeListData
import com.xz.beecircle.base.ModuleAdapter
import com.xz.beecircle.base.VBViewHolder
import com.xz.beecircle.databinding.ItemNoticeBinding

class NoticeAdapter : ModuleAdapter<NoticeListData, ItemNoticeBinding>() {

    override fun convert(holder: VBViewHolder<ItemNoticeBinding>, item: NoticeListData) {
        val adapterPosition = holder.absoluteAdapterPosition

        holder.binding.apply {
            llItem.setOnClickListener {
                if (listenerMy != null) {
                    listenerMy?.onItemClick(adapterPosition, it)
                }
            }
            item.apply {
                tvTitle.text = title
                tvTime.text = createtime
            }
        }
    }
}