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
                tvTotalOutput.text = "?????????${total}"
                tvDayOutput.text = "?????????${dayoutput}"
                tvCycle.text = "?????????${cycle}???"
                tvActivity.text = "????????????${activity}"
                tvActivity2.text = "????????????${activity}"
                tvLimit.text = "?????????${limited}???"
                tvLimit2.text = "?????????${limited}???"
                tvAlreadyOutput.text = "????????????${overcreat}"
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
                            tvLastTime.text = "${price}??????/?????????"
                            tvLimit2.visibility = View.VISIBLE
                            tvActivity2.visibility = View.GONE
                            tvActivity.visibility = View.VISIBLE
                            tvBuy.visibility = View.VISIBLE
                        }
                    }
                    1 -> {
                        tvBuyTime.text = "???????????????${starttime}"
                        if (gettype == "0") {//???????????????0??????/?????? ???1??????
                            tvLimit.visibility = View.GONE
                            tvSend.visibility = View.VISIBLE
                        } else {
                            tvLimit.visibility = View.VISIBLE
                            tvSend.visibility = View.GONE
                        }
                        val time: Long = Arith.sub(TimeUtils.dateToStamp(endtime), TimeUtils.dateToStamp(nowtime)).toLong()
                        if (time > 0) {
                            if (!item.isStartTime) {//????????????????????????????????????
                                if (tvLastTime.tag != null) {//??????????????????????????????
                                    val tag: CountDownTimer = tvLastTime.tag as CountDownTimer
                                    tag.cancel()
                                }
                                item.isStartTime = true
                                setTime(time, tvLastTime)
                            }
                        }
                    }
                    2 -> {
                        tvBuyTime.text = "???????????????${starttime}"
                        if (gettype == "0") {//???????????????0??????/?????? ???1??????
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
         * CountDownTimer timer = new CountDownTimer(3000, 1000)??????
         * ?????????????????????????????????????????????????????????????????????
         * ?????????????????????????????????????????????onTick?????????1??????????????????onFinish?????????
         */
        timer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                view.text = TimeUtils.secondToTime(millisUntilFinished/1000)
            }

            override fun onFinish() {

            }
        }
        //?????? CountDownTimer ????????? start() ???????????????????????????????????????????????????
        view.tag = timer
        timer!!.start()
    }
}