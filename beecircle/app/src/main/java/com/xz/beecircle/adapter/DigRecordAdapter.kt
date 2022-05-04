package com.xz.beecircle.adapter

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import com.xz.beecircle.MineRecord
import com.xz.beecircle.MineRecordListDate
import com.xz.beecircle.R
import com.xz.beecircle.base.ModuleAdapter
import com.xz.beecircle.base.VBViewHolder
import com.xz.beecircle.databinding.*

class DigRecordAdapter : ModuleAdapter<MineRecordListDate, ItemDigRecordBinding>() {

    override fun convert(holder: VBViewHolder<ItemDigRecordBinding>, item: MineRecordListDate) {
        val adapterPosition = holder.absoluteAdapterPosition

        holder.binding.apply {

            llItem.setOnClickListener {
                if (listenerMy != null) {
                    listenerMy?.onItemClick(adapterPosition, it)
                }
            }

            item.apply {
                tvPhone.text = height
                tvNum.text = creatsize
                tvHash.text = hashcode
                tvStatus.text = creatnum
                tvTime.text = creattime
            }
        }
    }
}