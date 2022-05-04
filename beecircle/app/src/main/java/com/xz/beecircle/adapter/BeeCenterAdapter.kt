package com.xz.beecircle.adapter

import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.lin.baselib.net.API
import com.lin.baselib.utils.Arith
import com.lin.baselib.utils.PicUtils
import com.lin.baselib.utils.TimeUtils
import com.xz.beecircle.BeeCenterData
import com.xz.beecircle.R
import com.xz.beecircle.base.ModuleAdapter
import com.xz.beecircle.base.VBViewHolder
import com.xz.beecircle.databinding.ItemBeeCenterBinding

class BeeCenterAdapter(private var entryType: Int) : ModuleAdapter<BeeCenterData, ItemBeeCenterBinding>() {

    override fun convert(holder: VBViewHolder<ItemBeeCenterBinding>, item: BeeCenterData) {
        val adapterPosition = holder.absoluteAdapterPosition

        holder.binding.apply {

            val drawable = ContextCompat.getDrawable(context, R.drawable.huoyudu)
            drawable?.setBounds(0, 0, 45, 60);
            tvActivity.setCompoundDrawables(drawable, null, null, null)
            tvActivity2.setCompoundDrawables(drawable, null, null, null)

            llItem.setOnClickListener {
                if (listenerMy != null) {
                    listenerMy?.onItemClick(adapterPosition, it)
                }
            }
            tvBuy.setOnClickListener {
                if (listenerMy != null) {
                    listenerMy?.onItemClick(adapterPosition, it)
                }
            }

            when (entryType) {
                0 -> {
                    tvAlreadyOutput.visibility = View.GONE
                    tvLimit.visibility = View.GONE
                }
                1 -> {
                    tvBuyTime.visibility = View.VISIBLE
                }
                2 -> {
                    tvStatus.visibility = View.VISIBLE
                    tvBuyTime.visibility = View.VISIBLE
                    tvLastTime.visibility = View.GONE
                }
            }

            item.apply {
                PicUtils.uploadPic(context,API.URL_HOST_USER_IMAGE+img,ivAvatar)
                tvTitle.text = level
                tvTotalOutput.text = "总产：${total}"
                tvDayOutput.text = "日产：${dayoutput}"
                tvCycle.text = "周期：${cycle}天"
                tvActivity.text = "活跃度：${activity}"
                tvActivity2.text = "活跃度：${activity}"
                tvLimit.text = "限量：${limited}个"
                tvLimit2.text = "限量：${limited}个"
                tvAlreadyOutput.text = "已生产：${overcreat}"
                when (entryType) {
                    0 -> {
                        if (limited == 0) {
                            tvLastTime.visibility = View.GONE
                            tvLimit2.visibility = View.GONE
                            tvActivity2.visibility = View.VISIBLE
                            tvActivity.visibility = View.GONE
                            tvBuy.visibility = View.GONE
                        } else {
                            tvLastTime.visibility = View.VISIBLE
                            tvLastTime.text = "${price}蜂蜜/分享值"
                            tvLimit2.visibility = View.VISIBLE
                            tvActivity2.visibility = View.GONE
                            tvActivity.visibility = View.VISIBLE
                            tvBuy.visibility = View.VISIBLE
                        }
                    }
                    1 -> {
                        tvBuyTime.text = "购买时间：${starttime}"
                        if (gettype == "0") {//获取方式（0赠送/活动 （1购买
                            tvLimit.visibility = View.GONE
                            tvSend.visibility = View.VISIBLE
                        } else {
                            tvLimit.visibility = View.VISIBLE
                            tvSend.visibility = View.GONE
                        }
                        val time: Long = Arith.sub(TimeUtils.dateToStamp(endtime), TimeUtils.dateToStamp(nowtime)).toLong()
                        if (time > 0) {
                            if (!item.isStartTime) {//防止开启倒计时了再次开启
                                if (tvLastTime.tag != null) {//防止刷新时间空间复用
                                    val tag: CountDownTimer = tvLastTime.tag as CountDownTimer
                                    tag.cancel()
                                }
                                item.isStartTime = true
                                setTime(time, tvLastTime)
                            }
                        }
                    }
                    2 -> {
                        tvBuyTime.text = "购买时间：${starttime}"
                        if (gettype == "0") {//获取方式（0赠送/活动 （1购买
                            tvLimit.visibility = View.GONE
                            tvSend.visibility = View.VISIBLE
                        } else {
                            tvLimit.visibility = View.VISIBLE
                            tvSend.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private var timer: CountDownTimer? = null

    private fun setTime(time: Long, view: TextView) {
        /**
         * CountDownTimer timer = new CountDownTimer(3000, 1000)中，
         * 第一个参数表示总时间，第二个参数表示间隔时间。
         * 意思就是每隔一秒会回调一次方法onTick，然后1秒之后会回调onFinish方法。
         */
        timer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                view.text = TimeUtils.secondToTime(millisUntilFinished/1000)
            }

            override fun onFinish() {

            }
        }
        //调用 CountDownTimer 对象的 start() 方法开始倒计时，也不涉及到线程处理
        view.tag = timer
        timer!!.start()
    }
}