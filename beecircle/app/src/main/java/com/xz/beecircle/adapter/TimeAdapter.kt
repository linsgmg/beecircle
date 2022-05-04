package com.xz.beecircle.adapter

import android.graphics.Color
import androidx.core.content.ContextCompat
import com.xz.beecircle.BigOrderSelect
import com.xz.beecircle.R
import com.xz.beecircle.TransferPageData
import com.xz.beecircle.base.ModuleAdapter
import com.xz.beecircle.base.VBViewHolder
import com.xz.beecircle.databinding.ItemTimeBinding

class TimeAdapter(private var index: Int) : ModuleAdapter<BigOrderSelect, ItemTimeBinding>() {
    override fun convert(holder: VBViewHolder<ItemTimeBinding>, item: BigOrderSelect) {
        val adapterPosition = holder.absoluteAdapterPosition

        holder.binding.apply {

            llItem.setOnClickListener {
                if (listenerMy != null) {
                    listenerMy?.onItemClick(adapterPosition, it)
                }
            }
            tvAssetType.text = item.buynum

            if (index == adapterPosition) {
                tvAssetType.setBackgroundColor(Color.parseColor("#FDD604"))
                tvAssetType.setTextColor(Color.WHITE)
            } else {
                tvAssetType.setBackgroundColor(Color.WHITE)
                tvAssetType.setTextColor(Color.parseColor("#211000"))
            }
        }
    }
}