package com.xz.beecircle.adapter

import com.xz.beecircle.MyTeam
import com.xz.beecircle.base.ModuleAdapter
import com.xz.beecircle.base.VBViewHolder
import com.xz.beecircle.databinding.ItemMyTeamBinding

class MyTeamAdapter : ModuleAdapter<MyTeam, ItemMyTeamBinding>() {

    override fun convert(holder: VBViewHolder<ItemMyTeamBinding>, item: MyTeam) {
        val adapterPosition = holder.absoluteAdapterPosition

        holder.binding.apply {
            llItem.setOnClickListener {
                if (listenerMy != null) {
                    listenerMy?.onItemClick(adapterPosition, it)
                }
            }
            item.apply {
                tvName.text = name
                tvNum.text = peopleNum+"人"
                tvTeamValue.text = "团队活跃度：${temaActive}"
                tvPersonValue.text = "个人活跃度：${greaterOneSize}"
            }
        }
    }
}