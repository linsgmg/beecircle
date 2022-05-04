package com.xz.beecircle.adapter

import androidx.core.content.ContextCompat
import com.xz.beecircle.MyPayWayData
import com.xz.beecircle.R
import com.xz.beecircle.base.ModuleAdapter
import com.xz.beecircle.base.VBViewHolder
import com.xz.beecircle.databinding.ItemReceiveInfoBinding

class ReceiveInfoAdapter : ModuleAdapter<MyPayWayData, ItemReceiveInfoBinding>() {

    override fun convert(holder: VBViewHolder<ItemReceiveInfoBinding>, item: MyPayWayData) {
        val adapterPosition = holder.absoluteAdapterPosition

        holder.binding.apply {

            llItem.setOnClickListener {
                if (listenerMy != null) {
                    listenerMy?.onItemClick(adapterPosition, it)
                }
            }

            item.apply {
                tvName.text = payeenme
                val sb = StringBuffer()
                for (i in account.split("").indices){
                    sb.append(account.split("")[i])
                    if (i%4==0)  sb.append(" ")
                }
                tvPhone.text = sb.toString().trim()
                when(paytype){
                    0->{
                        ivIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.zhifubbao_shou))
                        tvType.text = "支付宝"
                    }
                    1->{
                        ivIcon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.weixin_shou))
                        tvType.text = "微信"
                    }
                    2->{
                        ivIcon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.yinhangka_shou))
                        tvType.text = bankname
                    }
                }
            }
        }
    }
}