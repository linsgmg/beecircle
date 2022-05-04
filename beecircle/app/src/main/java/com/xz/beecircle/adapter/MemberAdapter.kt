package com.xz.beecircle.adapter

import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import com.lin.baselib.utils.PicUtils
import com.xz.beecircle.MyPushYesRealName
import com.xz.beecircle.R
import com.xz.beecircle.base.ModuleAdapter
import com.xz.beecircle.base.VBViewHolder
import com.xz.beecircle.databinding.*

class MemberAdapter(private var entryType: Int) : ModuleAdapter<MyPushYesRealName, ItemMemberBinding>() {

    override fun convert(holder: VBViewHolder<ItemMemberBinding>, item: MyPushYesRealName) {
        val adapterPosition = holder.absoluteAdapterPosition

        holder.binding.apply {

            llItem.setOnClickListener {
                if (listenerMy != null) {
                    listenerMy?.onItemClick(adapterPosition, it)
                }
            }

            when (entryType) {
                0 -> {

                }
                1 -> {

                }
            }

            item.apply {
                item.apply {
                    PicUtils.uploadPic(context, headerpic, ivAvatar)
                    tvUid.text = "UID:${uid}"
                    tvPhone.text = if (phone.length > 7) "${phone.substring(0, 3)}+****+${phone.substring(phone.length - 4)}" else phone
                    tvTime.text = creattime
                    tvTeamActivity.text = temaActive
                    tvPersonActivity.text = myActive
                    tvTeamNum.text = teamsize
                }
            }
        }
    }
}